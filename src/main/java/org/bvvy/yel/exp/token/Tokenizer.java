package org.bvvy.yel.exp.token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class Tokenizer {

    public static final byte IS_DIGIT = 0x01;
    public static final byte IS_HEXDIGIT = 0x02;
    private static final byte[] FLAGS = new byte[256];

    static {
        for (int ch = '0'; ch <= '9'; ch++) {
            FLAGS[ch] |= IS_DIGIT | IS_HEXDIGIT;
        }
        for (int ch = 'A'; ch <= 'Z'; ch++) {
            FLAGS[ch] |= IS_HEXDIGIT;
        }
        for (int ch = 'a'; ch <= 'z'; ch++) {
            FLAGS[ch] |= IS_HEXDIGIT;
        }
    }

    private String expressionString;
    private char[] charsToProcess;
    private int max;
    private int pos;
    private List<Token> tokens = new ArrayList<>();

    public Tokenizer(String expression) {
        this.expressionString = expression;
        this.charsToProcess = (expression + '\0').toCharArray();
        this.max = this.charsToProcess.length;
        this.pos = 0;
    }

    public List<Token> process() {
        while (this.pos < this.max) {
            char ch = this.charsToProcess[this.pos];
            if (isIdentifierStart(ch)) {
                lexIdentifier();
            } else {
                switch (ch) {
                    case '+':
                        if (isTwoCharToken(TokenKind.INC)) {
                            pushPairToken(TokenKind.INC);
                        } else {
                            pushCharToken(TokenKind.PLUS);
                        }
                        break;
                    case '_':
                        lexIdentifier();
                        break;
                    case '-':
                        if (isTwoCharToken(TokenKind.DEC)) {
                            pushPairToken(TokenKind.DEC);
                        } else {
                            pushCharToken(TokenKind.MINUS);
                        }
                        break;
                    case ':':
                        pushCharToken(TokenKind.COLON);
                        break;
                    case '.':
                        pushCharToken(TokenKind.DOT);
                        break;
                    case ',':
                        pushCharToken(TokenKind.COMMA);
                        break;
                    case '*':
                        pushCharToken(TokenKind.STAR);
                        break;
                    case '/':
                        pushCharToken(TokenKind.DIV);
                        break;
                    case '%':
                        pushCharToken(TokenKind.MOD);
                        break;
                    case '(':
                        pushCharToken(TokenKind.LPAREN);
                        break;
                    case ')':
                        pushCharToken(TokenKind.RPAREN);
                        break;
                    case '[':
                        pushCharToken(TokenKind.LSQUARE);
                        break;
                    case ']':
                        pushCharToken(TokenKind.RSQUARE);
                        break;
                    case '#':
                        pushCharToken(TokenKind.HASH);
                        break;
                    case '{':
                        pushCharToken(TokenKind.LCURLY);
                        break;
                    case '}':
                        pushCharToken(TokenKind.RCURLY);
                        break;
                    case '^':
                        pushCharToken(TokenKind.BIT_XOR);
                        break;
                    case '!':
                        if (isTwoCharToken(TokenKind.NE)) {
                            pushPairToken(TokenKind.NE);
                        } else {
                            pushCharToken(TokenKind.NOT);
                        }
                        break;
                    case '=':
                        if (isTwoCharToken(TokenKind.EQ)) {
                            pushPairToken(TokenKind.EQ);
                        } else {
                            pushCharToken(TokenKind.ASSIGN);
                        }
                        break;
                    case '&':
                        if (isTwoCharToken(TokenKind.SYMBOLIC_AND)) {
                            pushPairToken(TokenKind.SYMBOLIC_AND);
                        } else {
                            pushCharToken(TokenKind.BIT_AND);
                        }
                        break;
                    case '|':
                        if (!isTwoCharToken(TokenKind.SYMBOLIC_OR)) {
                            raiseParseException(this.pos);
                        }
                        pushPairToken(TokenKind.BIT_OR);
                        break;
                    case '?':
                        if (isTwoCharToken(TokenKind.ELVIS)) {
                            pushPairToken(TokenKind.ELVIS);
                        } else if (isTwoCharToken(TokenKind.SAFE_NAVI)) {
                            pushPairToken(TokenKind.SAFE_NAVI);
                        } else {
                            pushCharToken(TokenKind.QMARK);
                        }
                        break;
                    case '>':
                        if (isTwoCharToken(TokenKind.GE)) {
                            pushPairToken(TokenKind.GE);
                        } else {
                            pushCharToken(TokenKind.GT);
                        }
                        break;
                    case '<':
                        if (isTwoCharToken(TokenKind.LE)) {
                            pushPairToken(TokenKind.LE);
                        } else {
                            pushPairToken(TokenKind.LT);
                        }
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        lexNumberLiteral(ch == '0');
                        break;
                    case ' ':
                    case '\t':
                    case '\r':
                    case '\n':
                        this.pos++;
                        break;
                    case '\'':
                        lexQuotedStringLiteral();
                        break;
                    case '"':
                        lexDoubleQuotedStringLiteral();
                        break;
                    case 0:
                        //
                        this.pos++;
                        break;
                }
            }
        }
        return this.tokens;

    }

    private void lexDoubleQuotedStringLiteral() {
        int start = this.pos;
        boolean terminated = false;
        while (!terminated) {
            this.pos++;
            char ch = this.charsToProcess[this.pos];
            if (ch == '"') {
                // may not be the end if the char after is also a "
                if (this.charsToProcess[this.pos + 1] == '"') {
                    this.pos++;  // skip over that too, and continue
                } else {
                    terminated = true;
                }
            }
            if (isExhausted()) {
                raiseParseException(start);
            }
        }
        this.pos++;
        this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
    }

    private void lexQuotedStringLiteral() {
        int start = this.pos;
        boolean terminated = false;
        while (!terminated) {
            this.pos++;
            char ch = this.charsToProcess[this.pos];
            if (ch == '\'') {
                if (this.charsToProcess[this.pos + 1] == '\'') {
                    this.pos++;
                } else {
                    terminated = true;
                }
            }
            if (isExhausted()) {
                raiseParseException(start);
            }

        }
        this.pos++;
        this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
    }

    private void raiseParseException(int start) {
        throw new IllegalStateException("语法错误在: " + start);
    }

    private boolean isExhausted() {
        return (this.pos == this.max - 1);
    }

    private void lexNumberLiteral(boolean firstCharIsZero) {
        boolean isReal = false;
        int start = this.pos;
        char ch = this.charsToProcess[this.pos + 1];
        boolean isHex = ch == 'x' || ch == 'X';

        //16进制
        if (firstCharIsZero && isHex) {
            this.pos = this.pos + 1;
            do {
                this.pos++;
            } while (isHexadecimalDigit(this.charsToProcess[this.pos]));
            if (isChar('L', 'l')) {
                pushHexIntToken(subarray(start + 2, this.pos), true, start, this.pos);
                this.pos++;
            } else {
                pushHexIntToken(subarray(start + 2, this.pos), false, start, this.pos);
            }
            return;
        }
        do {
            this.pos++;
        } while (isDigit(this.charsToProcess[this.pos]));

        // 处理 '.' 的情况
        ch = this.charsToProcess[this.pos];
        if (ch == '.') {
            isReal = true;
            int dotPos = this.pos;
            do {
                this.pos++;
            } while (isDigit(this.charsToProcess[this.pos]));
            if (this.pos == dotPos + 1) {
                this.pos = dotPos;
                pushIntToken(subarray(start, this.pos), false, start, this.pos);
                return;
            }
        }

        int endOfNumber = this.pos;
        if (isChar('L', 'l')) {
            if (isReal) {

            }
            pushIntToken(subarray(start, endOfNumber), true, start, endOfNumber);
            this.pos++;
        } else if (isExponentChar(this.charsToProcess[this.pos])) {
            isReal = true;
            this.pos++;
            char possibleSign = this.charsToProcess[this.pos];
            if (isSign(possibleSign)) {
                this.pos++;
            }

            do {
                this.pos++;
            } while (isDigit(this.charsToProcess[this.pos]));
            boolean isFloat = false;
            if (isFloatSuffix(this.charsToProcess[this.pos])) {
                isFloat = true;
                endOfNumber = ++this.pos;
            } else if (isDoubleSuffix(this.charsToProcess[this.pos])) {
                endOfNumber = ++this.pos;
            }
            pushRealToken(subarray(start, this.pos), isFloat, start, endOfNumber);
        } else {
            ch = this.charsToProcess[this.pos];
            boolean isFloat = false;
            if (isFloatSuffix(ch)) {
                isReal = true;
                isFloat = true;
                endOfNumber = ++this.pos;
            } else if (isDoubleSuffix(ch)) {
                isReal = true;
                endOfNumber = ++this.pos;
            }
            if (isReal) {
                pushRealToken(subarray(start, endOfNumber), isFloat, start, endOfNumber);
            } else {
                pushIntToken(subarray(start, endOfNumber), false, start, endOfNumber);
            }
        }


    }

    private boolean isSign(char ch) {
        return ch == '+' || ch == '-';
    }

    private boolean isExponentChar(char ch) {
        return ch == 'e' || ch == 'E';
    }

    private boolean isDoubleSuffix(char ch) {
        return ch == 'd' || ch == 'D';
    }

    private boolean isFloatSuffix(char ch) {
        return ch == 'f' || ch == 'F';
    }

    private void pushRealToken(char[] data, boolean isFloat, int start, int end) {
        if (isFloat) {
            this.tokens.add(new Token(TokenKind.LITERAL_REAL_FLOAT, data, start, end));
        } else {
            this.tokens.add(new Token(TokenKind.LITERAL_REAL, data, start, end));
        }
    }

    private void pushIntToken(char[] data, boolean isLong, int start, int end) {
        if (isLong) {
            this.tokens.add(new Token(TokenKind.LITERAL_LONG, data, start, end));
        } else {
            this.tokens.add(new Token(TokenKind.LITERAL_INT, data, start, end));
        }
    }

    private void pushHexIntToken(char[] data, boolean isLong, int start, int end) {
        if (isLong) {
            this.tokens.add(new Token(TokenKind.LITERAL_HEXLONG, data, start, end));
        } else {
            this.tokens.add(new Token(TokenKind.LITERAL_HEXINT, data, start, end));
        }
    }

    private boolean isChar(char a, char b) {
        char ch = this.charsToProcess[this.pos];
        return ch == a || ch == b;
    }

    private boolean isDigit(char ch) {
        if (ch > 255) {
            return false;
        }
        return (FLAGS[ch] & IS_DIGIT) != 0;
    }

    private boolean isHexadecimalDigit(char ch) {
        if (ch > 255) {
            return false;
        }
        return (FLAGS[ch] & IS_HEXDIGIT) != 0;
    }

    private boolean isIdentifierStart(char ch) {
        return '\0' != ch && Character.isJavaIdentifierStart(ch);
    }

    private boolean isIdentifier(char ch) {
        return '\0' != ch && Character.isJavaIdentifierPart(ch);
    }

    private void pushCharToken(TokenKind kind) {
        this.tokens.add(new Token(kind, this.pos, this.pos + 1));
        this.pos++;
    }

    private void pushPairToken(TokenKind kind) {
        this.tokens.add(new Token(kind, this.pos, this.pos + 2));
        this.pos += 2;

    }

    private boolean isTwoCharToken(TokenKind kind) {
        return (kind.getLength() == 2)
                && this.charsToProcess[this.pos] == kind.tokenChars[0]
                && this.charsToProcess[this.pos + 1] == kind.tokenChars[1];

    }

    private void lexIdentifier() {
        int start = this.pos;
        do {
            this.pos++;
        } while (isIdentifier(charsToProcess[this.pos]));
        char[] subarray = subarray(start, this.pos);

        this.tokens.add(new Token(TokenKind.IDENTIFIER, subarray, start, this.pos));
    }

    private char[] subarray(int start, int end) {
        return Arrays.copyOfRange(this.charsToProcess, start, end);
    }

}
