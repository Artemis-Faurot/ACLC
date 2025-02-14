package com.artemis.aclc.utils;

@SuppressWarnings("unused")
public class Token {
    private TokenType type;
    private String value;
    private Location location;
    private final int[] range = new int[2];
    
    public Token() {}
    public Token(TokenType type, String value, Position[] locationPositions) {
        if(locationPositions.length != 2) throw new IllegalArgumentException("Location requires 2 Positions !");
        
        this.type = type;
        this.value = value;
        this.location = new Location(locationPositions[0], locationPositions[1]);
    }
    
    public void setType(TokenType type) { this.type = type; }
    public void setValue(String value) { this.value = value; }
    public void setLocation(Location location) { this.location = location; }
    public void setRange(int start, int end) {
        range[0] = start;
        range[1] = end;
    }
    
    public TokenType getType() { return type; }
    public String getValue() { return value; }
    public Location getLocation() { return location; }
    public int[] getRange() { return range; }
}