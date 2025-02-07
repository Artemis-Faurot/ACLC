package com.artemis.aclc.utils;

public class Position {
    private int line;
    private int column;

    public Position() {}
    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public void setLine(int line) { this.line = line; }
    public void setColumn(int column) { this.column = column; }

    public int getLine() { return line; }
    public int getColumn() { return column; }
}
