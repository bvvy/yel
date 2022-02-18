package org.bvvy.yel.parser;

import org.bvvy.yel.exp.YelExpression;
import org.bvvy.yel.exp.ast.*;
import org.bvvy.yel.exp.token.ITokenKind;
import org.bvvy.yel.exp.token.Token;
import org.bvvy.yel.exp.token.TokenKind;
import org.bvvy.yel.exp.token.Tokenizer;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author bvvy
 * @date 2021/11/22
 */
public class YelParser implements Parser {

    protected List<Token> tokenStream;
    protected int tokenStreamPointer;
    protected int tokenStreamLength;
    protected Deque<Node> constructedNodes = new ArrayDeque<>();
    protected YelParserConfig configuration;

    public YelParser() {
        this.configuration = new YelParserConfig();
    }

    public YelParser(YelParserConfig configuration) {
        this.configuration = configuration;
    }

    @Override
    public YelExpression parse(String expression) {
        Tokenizer tokenizer = new Tokenizer(expression);
        List<Token> tokens = tokenizer.process();
        this.tokenStream = tokens;
        this.tokenStreamLength = tokens.size();
        this.constructedNodes.clear();
        this.tokenStreamPointer = 0;
        Node ast = this.eatExpression();
        return new YelExpression(ast, this.configuration);
    }

    protected Node eatExpression() {
        Node expr = eatLogicalOrExpression();
        Token t = peekToken();
        if (t != null) {
            if (t.getKind() == TokenKind.ASSIGN) {
                if (expr == null) {
                    expr = new NullLiteral(t.getStartPos() - 1, t.getEndPos() - 1);
                }
                nextToken();
                Node assignedValue = eatLogicalOrExpression();
                return new Assign(t.getStartPos(), t.getEndPos(), expr, assignedValue);
            }
            if (t.getKind() == TokenKind.ELVIS) {
                if (expr == null) {
                    expr = new NullLiteral(t.getStartPos() - 1, t.getEndPos() - 2);
                }
                nextToken();
                Node valueIfNull = eatExpression();
                if (valueIfNull == null) {
                    valueIfNull = new NullLiteral(t.getStartPos() - 1, t.getEndPos() - 1);
                }
                return new Elvis(t.getStartPos(), t.getEndPos(), expr, valueIfNull);
            }
            if (t.getKind() == TokenKind.QMARK) {
                if (expr == null) {
                    expr = new NullLiteral(t.getStartPos() - 1, t.getEndPos() - 1);
                }
                nextToken();
                Node ifTrueExprValue = eatExpression();
                eatToken(TokenKind.COLON);
                Node ifFalseExprValue = eatExpression();
                return new Ternary(t.getStartPos(), t.getEndPos(), expr, ifTrueExprValue, ifFalseExprValue);
            }

        }
        return expr;
    }

    protected Node eatLogicalOrExpression() {
        Node expr = eatLogicalAndExpression();
        while (peekToken(TokenKind.SYMBOLIC_OR)) {
            Token t = takeToken();
            Node rhExpr = eatLogicalAndExpression();
            expr = new OpOr(t.getStartPos(), t.getEndPos(), expr, rhExpr);
        }
        return expr;
    }

    protected Node eatLogicalAndExpression() {
        Node expr = eatBitXorExpression();
        while (peekToken(TokenKind.SYMBOLIC_AND)) {
            Token t = takeToken();
            Node rhExpr = eatBitXorExpression();
            expr = new OpAnd(t.getStartPos(), t.getEndPos(), expr, rhExpr);
        }
        return expr;
    }

    protected Node eatBitXorExpression() {
        Node expr = eatBitOrExpression();
        while (peekToken(TokenKind.BIT_XOR)) {
            Token t = takeToken();
            Node rhExpr = eatBitOrExpression();
            expr = new OpBitXor(t.getStartPos(), t.getEndPos(), expr, rhExpr);
        }
        return expr;
    }

    protected Node eatBitOrExpression() {
        Node expr = eatBitAndExpression();
        while (peekToken(TokenKind.BIT_OR)) {
            Token t = takeToken();
            Node rhExpr = eatBitAndExpression();
            expr = new OpBitOr(t.getStartPos(), t.getEndPos(), expr, rhExpr);
        }
        return expr;
    }

    protected Node eatBitAndExpression() {
        Node expr = eatRelationalExpression();
        while (peekToken(TokenKind.BIT_AND)) {
            Token t = takeToken();
            Node rhExpr = eatRelationalExpression();
            expr = new OpBitAnd(t.getStartPos(), t.getEndPos(), expr, rhExpr);
        }
        return expr;
    }

    protected Node eatRelationalExpression() {
        Node expr = eatSumExpression();
        if (peekToken(TokenKind.GT, TokenKind.LT, TokenKind.LE)
                || peekToken(TokenKind.GE, TokenKind.EQ, TokenKind.NE)) {
            Token t = takeToken();
            Node rhExpr = eatSumExpression();
            ITokenKind tk = t.getKind();

            if (tk == TokenKind.GT) {
                return new OpGT(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else if (tk == TokenKind.LT) {
                return new OpLT(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else if (tk == TokenKind.LE) {
                return new OpLE(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else if (tk == TokenKind.GE) {
                return new OpGE(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else if (tk == TokenKind.EQ) {
                return new OpEQ(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else {
                return new OpNE(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            }
        }
        return expr;

    }

    protected Node eatSumExpression() {
        Node expr = eatProductExpression();
        while (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.INC)) {
            Token t = takeToken();
            Node rhExpr = eatProductExpression();
            //
            if (t.getKind() == TokenKind.PLUS) {
                expr = new OpPlus(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else if (t.getKind() == TokenKind.MINUS) {
                expr = new OpMinus(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            }
        }
        return expr;
    }

    protected Node eatProductExpression() {
        Node expr = eatIncDecExpression();
        while (peekToken(TokenKind.STAR, TokenKind.DIV, TokenKind.MOD)) {
            Token t = takeToken();
            Node rhExpr = eatIncDecExpression();
            if (t.getKind() == TokenKind.STAR) {
                expr = new OpMultiply(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else if (t.getKind() == TokenKind.DIV) {
                expr = new OpDivide(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            } else {
                expr = new OpModulus(t.getStartPos(), t.getEndPos(), expr, rhExpr);
            }
        }
        return expr;
    }

    protected Node eatIncDecExpression() {
        Node expr = eatUnaryExpression();
        if (expr != null && peekToken(TokenKind.INC, TokenKind.DEC)) {
            Token t = takeToken();
            if (t.getKind() == TokenKind.INC) {
                return new OpInc(t.getStartPos(), t.getEndPos(), true, expr);
            }
            return new OpDec(t.getStartPos(), t.getEndPos(), true, expr);
        }
        return expr;
    }

    protected Node eatUnaryExpression() {
        if (peekToken(TokenKind.PLUS, TokenKind.MINUS, TokenKind.NOT)) {
            Token t = takeToken();
            Node expr = eatUnaryExpression();
            if (t.getKind() == TokenKind.NOT) {
                return new OperatorNot(t.getStartPos(), t.getEndPos(), expr);
            } else if (t.getKind() == TokenKind.PLUS) {
                return new OpPlus(t.getStartPos(), t.getEndPos(), expr);
            }
            return new OpMinus(t.getStartPos(), t.getEndPos(), expr);
        }
        if (peekToken(TokenKind.INC, TokenKind.DEC)) {
            Token t = takeToken();
            Node expr = eatUnaryExpression();
            if (t.getKind() == TokenKind.INC) {
                return new OpInc(t.getStartPos(), t.getEndPos(), false, expr);
            }
            return new OpDec(t.getStartPos(), t.getEndPos(), false, expr);
        }
        return eatPrimaryExpression();
    }

    protected Node eatPrimaryExpression() {
        Node start = eatStartNode();
        List<Node> nodes = null;
        Node node = eatNode();
        while (node != null) {
            if (nodes == null) {
                nodes = new ArrayList<>(4);
                nodes.add(start);
            }
            nodes.add(node);
            node = eatNode();
        }
        if (start == null || nodes == null) {
            return start;
        }
        return new CompoundExpression(start.getStartPosition(),
                nodes.get(nodes.size() - 1).getEndPosition(),
                nodes.toArray(new Node[]{})
        );
    }

    protected Node eatNode() {
        return peekToken(TokenKind.DOT, TokenKind.SAFE_NAVI) ? eatDottedNode() : eatNonDottedNode();
    }

    protected Node eatNonDottedNode() {
        if (peekToken(TokenKind.LSQUARE)) {
            if (maybeEatIndexer()) {
                return pop();
            }
        }
        return null;
    }

    protected boolean maybeEatIndexer() {
        Token t = peekToken();
        if (!peekToken(TokenKind.LSQUARE, true)) {
            return false;
        }
        Node expr = eatExpression();
        eatToken(TokenKind.RSQUARE);
        this.constructedNodes.push(new Indexer(t.getStartPos(), t.getEndPos(), expr));
        return true;
    }

    protected Node eatDottedNode() {
        Token t = takeToken();
        boolean nullSafeNavigation = t.getKind() == TokenKind.SAFE_NAVI;
        if (maybeEatMethodProperty(nullSafeNavigation)) {
            return pop();
        }

        return null;
    }

    protected boolean maybeEatMethodProperty(boolean nullSafeNavigation) {
        if (peekToken(TokenKind.IDENTIFIER)) {
            Token methodOrPropertyName = takeToken();
            Node[] args = maybeEatMethodArgs();
            if (args == null) {
                push(new PropertyOrFieldReference(nullSafeNavigation, methodOrPropertyName.stringValue(),
                        methodOrPropertyName.getStartPos(), methodOrPropertyName.getEndPos()));
                return true;
            }
            push(new MethodReference(nullSafeNavigation, methodOrPropertyName.stringValue(),
                    methodOrPropertyName.getStartPos(), methodOrPropertyName.getEndPos(), args));
            return true;
        }
        return false;
    }

    protected Node[] maybeEatMethodArgs() {

        if (!peekToken(TokenKind.LPAREN)) {
            return null;
        }
        List<Node> args = new ArrayList<>();
        consumeArguments(args);
        eatToken(TokenKind.RPAREN);
        return args.toArray(new Node[0]);

    }

    protected void consumeArguments(List<Node> accumulatedArguments) {
        Token t = peekToken();
        int pos = t.getStartPos();
        Token next;
        do {
            nextToken();
            t = peekToken();
            if (t == null) {

            }
            if (t.getKind() != TokenKind.RPAREN) {
                accumulatedArguments.add(eatExpression());
            }
            next = peekToken();
        }
        while (next != null && next.getKind() == TokenKind.COMMA);
        if (next == null) {

        }
    }

    protected Node eatStartNode() {
        if (maybeEatLiteral()) {
            return pop();
        } else if (maybeEatParenExpression()) {
            return pop();
        } else if (maybeEatNullReference() || maybeEatMethodProperty(false)) {
            return pop();
        }

        return null;
    }

    private boolean maybeEatNullReference() {
        if (peekToken(TokenKind.IDENTIFIER)) {
            Token nullToken = peekToken();
            if (!"null".equalsIgnoreCase(nullToken.stringValue())) {
                return false;
            }
            nextToken();
            this.constructedNodes.push(new NullLiteral(nullToken.getStartPos(), nullToken.getEndPos()));
            return true;
        }
        return false;
    }

    protected boolean maybeEatParenExpression() {
        if (peekToken(TokenKind.LPAREN)) {
            nextToken();
            Node expr = eatExpression();
            eatToken(TokenKind.RPAREN);
            push(expr);
            return true;
        } else {
            return false;
        }
    }

    protected Token eatToken(TokenKind expectedKind) {
        Token t = nextToken();
        if (t == null) {
        }
        if (t.getKind() != expectedKind) {

        }
        return t;

    }

    protected boolean maybeEatLiteral() {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        if (t.getKind() == TokenKind.LITERAL_INT) {
            push(new IntLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), Integer.parseInt(t.stringValue(), 10)));
        } else if (t.getKind() == TokenKind.LITERAL_LONG) {
            push(new LongLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), Long.parseLong(t.stringValue(), 10)));
        } else if (t.getKind() == TokenKind.LITERAL_HEXINT) {
            push(new IntLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), Integer.parseInt(t.stringValue(), 16)));
        } else if (t.getKind() == TokenKind.LITERAL_HEXLONG) {
            push(new LongLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), Long.parseLong(t.stringValue(), 16)));
        } else if (t.getKind() == TokenKind.LITERAL_REAL) {
            if (configuration.isUseBigDecimalForFloat()) {
                push(new DecimalLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), new BigDecimal(t.stringValue())));
            } else {
                push(new RealLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), Double.parseDouble(t.stringValue())));
            }
        } else if (t.getKind() == TokenKind.LITERAL_REAL_FLOAT) {
            if (configuration.isUseBigDecimalForFloat()) {
                push(new DecimalLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), new BigDecimal(t.stringValue())));
            } else {
                push(new FloatLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), Float.parseFloat(t.stringValue())));
            }
        } else if (peekIdentifierToken("true")) {
            push(new BooleanLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), true));
        } else if (peekIdentifierToken("false")) {
            push(new BooleanLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), false));
        } else if (t.getKind() == TokenKind.LITERAL_STRING) {
            push(new StringLiteral(t.stringValue(), t.getStartPos(), t.getEndPos(), t.stringValue()));
        } else {
            return false;
        }
        nextToken();
        return true;
    }

    protected boolean peekIdentifierToken(String identifierString) {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        return t.getKind() == TokenKind.IDENTIFIER
                && identifierString.equalsIgnoreCase(t.stringValue());
    }

    protected void push(Node node) {
        this.constructedNodes.push(node);
    }

    protected Node pop() {
        return this.constructedNodes.pop();
    }

    protected Token nextToken() {
        if (this.tokenStreamPointer >= this.tokenStreamLength) {
            return null;
        }
        return this.tokenStream.get(this.tokenStreamPointer++);
    }

    protected Token takeToken() {
        if (this.tokenStreamPointer >= this.tokenStreamLength) {
            throw new IllegalStateException("No token");
        }
        return this.tokenStream.get(this.tokenStreamPointer++);
    }

    protected Token peekToken() {
        if (this.tokenStreamPointer >= this.tokenStreamLength) {
            return null;
        }
        return tokenStream.get(this.tokenStreamPointer);
    }

    protected boolean peekToken(ITokenKind token) {

        Token t = peekToken();
        if (t == null) {
            return false;
        }
        return t.getKind() == token;
    }

    protected boolean peekToken(ITokenKind desiredTokenKind, boolean consumeIfMatched) {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        if (t.getKind() == desiredTokenKind) {
            if (consumeIfMatched) {
                this.tokenStreamPointer++;
            }
            return true;
        }
        return false;
    }

    protected boolean peekToken(ITokenKind possible1, ITokenKind possible2) {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        return t.getKind() == possible1 || t.getKind() == possible2;

    }


    protected boolean peekToken(ITokenKind possible1, ITokenKind possible2, ITokenKind possible3) {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        return (t.getKind() == possible1 || t.getKind() == possible2 || t.getKind() == possible3);
    }
}
