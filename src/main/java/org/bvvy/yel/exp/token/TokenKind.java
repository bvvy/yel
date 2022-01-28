package org.bvvy.yel.exp.token;

public enum TokenKind {
    IDENTIFIER,
    LITERAL_HEXLONG,
    LITERAL_HEXINT,
    LITERAL_LONG,
    LITERAL_INT,
    LITERAL_REAL_FLOAT,
    LITERAL_REAL,
    LITERAL_STRING,
    BIT_AND("&"),
    BIT_OR("|"),
    BIT_XOR("^"),

    SYMBOLIC_AND("&&"),
    SYMBOLIC_OR("||"),
    ELVIS("?:"),
    SAFE_NAVI("?."),
    QMARK("?"),
    PLUS("+"),
    MINUS("-"),
    COLON(":"),
    INC("++"),
    DEC("--"),
    DOT("."),
    COMMA(","),
    DIV("/"),
    STAR("*"),
    MOD("%"),
    LPAREN("("),
    RPAREN(")"),
    LSQUARE("["),
    RSQUARE("]"),
    HASH("#"),
    LCURLY("{"),
    RCURLY("}"),
    NOT("!"),
    NE("!="),
    EQ("=="),
    GE(">="),
    GT(">"),
    LE("<="),
    LT("<"),
    ASSIGN("=");

    final char[] tokenChars;
    private final boolean hasPayload;

    TokenKind(String tokenString) {
        this.tokenChars = tokenString.toCharArray();
        this.hasPayload = this.tokenChars.length == 0;
    }

    TokenKind() {
        this("");
    }

    public int getLength() {
        return tokenChars.length;
    }
}
