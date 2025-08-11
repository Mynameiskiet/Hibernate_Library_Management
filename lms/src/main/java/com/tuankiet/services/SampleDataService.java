package com.tuankiet.services;

/**
 * Service interface for generating sample data for the Library Management System.
 * 
 * @author congdinh2008
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SampleDataService {

    /**
     * Generates a predefined set of sample authors, books, members, and borrowing records.
     */
    void generateSampleData();

    /**
     * Clears all existing data from the database.
     */
    void clearAllData();
}
