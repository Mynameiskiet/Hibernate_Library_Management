package com.tuankiet.dto.common;

/**
 * Represents sorting criteria for queries.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
public class SortCriteria {
    
    private String field;
    private SortDirection direction;
    private String property; // Alternative field name for compatibility

    /**
     * Default constructor.
     */
    public SortCriteria() {
        this.direction = SortDirection.ASC;
    }

    /**
     * Constructor with field and direction.
     * 
     * @param field the field to sort by
     * @param direction the sort direction
     */
    public SortCriteria(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction != null ? direction : SortDirection.ASC;
        this.property = field; // Set property same as field for compatibility
    }

    /**
     * Create ascending sort criteria.
     * 
     * @param field the field to sort by
     * @return new SortCriteria instance
     */
    public static SortCriteria asc(String field) {
        return new SortCriteria(field, SortDirection.ASC);
    }

    /**
     * Create descending sort criteria.
     * 
     * @param field the field to sort by
     * @return new SortCriteria instance
     */
    public static SortCriteria desc(String field) {
        return new SortCriteria(field, SortDirection.DESC);
    }

    // Getters and Setters
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
        if (this.property == null) {
            this.property = field;
        }
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction != null ? direction : SortDirection.ASC;
    }

    /**
     * Get property name for sorting.
     * Returns property if set, otherwise returns field.
     * 
     * @return the property name
     */
    public String getProperty() {
        return property != null ? property : field;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "SortCriteria{" +
                "field='" + field + '\'' +
                ", direction=" + direction +
                ", property='" + property + '\'' +
                '}';
    }
}
