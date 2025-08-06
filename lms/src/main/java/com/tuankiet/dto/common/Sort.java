package com.tuankiet.dto.common;

public class Sort {
    private String field;
    private Direction direction = Direction.ASC;
    
    public enum Direction {
        ASC, DESC
    }
    
    // Constructors
    public Sort() {}
    
    public Sort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }
    
    // Getters and Setters
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
    
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
}
