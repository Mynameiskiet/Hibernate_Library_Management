package com.tuankiet.dto.common;

import java.util.List;

public class PageRequest {
    private int page = 0; // 0-based
    private int size = 10;
    private List<Sort> sorts;
    
    // Constructors
    public PageRequest() {}
    
    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }
    
    public PageRequest(int page, int size, List<Sort> sorts) {
        this.page = page;
        this.size = size;
        this.sorts = sorts;
    }
    
    // Getters and Setters
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public List<Sort> getSorts() { return sorts; }
    public void setSorts(List<Sort> sorts) { this.sorts = sorts; }
    
    public int getOffset() {
        return page * size;
    }
}
