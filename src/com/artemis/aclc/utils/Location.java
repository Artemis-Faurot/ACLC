package com.artemis.aclc.utils;

public class Location {
    private Position start;
    private Position end;

    public Location() {}
    public Location(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(Position start) { this.start = start; }
    public void setEnd(Position end) { this.end = end; }

    public Position getStart() { return start; }
    public Position getEnd() { return end; }
}
