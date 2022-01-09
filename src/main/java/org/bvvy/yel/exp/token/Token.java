package org.bvvy.yel.exp.token;


/**
 * @author bvvy
 * @date 2021/11/22
 */
public class Token {

    private TokenKind kind;
    private final int startPos;
    private final int endPos;

    public TokenKind getKind() {
        return kind;
    }

    private String data;

    public Token(TokenKind kind, int startPos, int endPos) {
        this.kind = kind;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public Token(TokenKind kind, char[] tokenData, int startPos, int endPos) {
        this(kind, startPos, endPos);
        this.data = new String(tokenData);
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public String stringValue() {
        return this.data == null ? "" : this.data;
    }

    public Object getData() {
        return data;
    }
}
