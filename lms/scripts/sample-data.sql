-- Sample data for Library Management System
-- MySQL/H2 compatible version

-- Clear existing data (order matters due to foreign key constraints)
DELETE FROM book_author;
DELETE FROM borrowings;
DELETE FROM books;
DELETE FROM authors;
DELETE FROM members;

-- Reset auto-increment values (MySQL syntax)
-- ALTER TABLE authors AUTO_INCREMENT = 1;
-- ALTER TABLE books AUTO_INCREMENT = 1;
-- ALTER TABLE members AUTO_INCREMENT = 1;
-- ALTER TABLE borrowings AUTO_INCREMENT = 1;

-- Insert sample authors
INSERT INTO authors (first_name, last_name, birth_year, biography) VALUES
('J.K.', 'Rowling', 1965, 'British author, best known for the Harry Potter series'),
('George', 'Orwell', 1903, 'English novelist and essayist, known for 1984 and Animal Farm'),
('Jane', 'Austen', 1775, 'English novelist known for her wit and social commentary'),
('Mark', 'Twain', 1835, 'American writer and humorist, author of Tom Sawyer'),
('Charles', 'Dickens', 1812, 'English writer and social critic of the Victorian era'),
('William', 'Shakespeare', 1564, 'English playwright and poet, widely regarded as the greatest writer'),
('Agatha', 'Christie', 1890, 'English writer known for detective novels'),
('Stephen', 'King', 1947, 'American author of horror, supernatural fiction, and fantasy'),
('J.R.R.', 'Tolkien', 1892, 'English author and philologist, creator of Middle-earth'),
('Harper', 'Lee', 1926, 'American novelist known for To Kill a Mockingbird'),
('Isaac', 'Asimov', 1920, 'American writer and professor of biochemistry, known for science fiction'),
('Arthur Conan', 'Doyle', 1859, 'British writer, creator of Sherlock Holmes'),
('Ernest', 'Hemingway', 1899, 'American novelist and journalist, Nobel Prize winner'),
('Virginia', 'Woolf', 1882, 'English writer, considered one of the most important modernist authors'),
('F. Scott', 'Fitzgerald', 1896, 'American novelist, known for The Great Gatsby');

-- Insert sample books
INSERT INTO books (title, category, isbn, description, publication_year, available) VALUES
('Harry Potter and the Philosopher''s Stone', 'FICTION', '978-0747532699', 'First book in the Harry Potter series', 1997, TRUE),
('1984', 'FICTION', '978-0451524935', 'Dystopian social science fiction novel', 1949, TRUE),
('Pride and Prejudice', 'FICTION', '978-0141439518', 'Romantic novel of manners', 1813, TRUE),
('The Adventures of Tom Sawyer', 'FICTION', '978-0486400778', 'Coming-of-age story set in Missouri', 1876, TRUE),
('Great Expectations', 'FICTION', '978-0141439563', 'Bildungsroman depicting the personal growth of Pip', 1861, TRUE),
('Hamlet', 'FICTION', '978-0486272788', 'Tragedy by William Shakespeare', 1603, TRUE),
('Murder on the Orient Express', 'FICTION', '978-0062693662', 'Detective novel featuring Hercule Poirot', 1934, TRUE),
('The Shining', 'FICTION', '978-0307743657', 'Horror novel about the Overlook Hotel', 1977, TRUE),
('The Lord of the Rings', 'FICTION', '978-0544003415', 'Epic high-fantasy novel', 1954, TRUE),
('To Kill a Mockingbird', 'FICTION', '978-0061120084', 'Novel about racial injustice in the American South', 1960, TRUE),
('Foundation', 'SCIENCE_FICTION', '978-0553293357', 'Science fiction novel about psychohistory', 1951, TRUE),
('The Adventures of Sherlock Holmes', 'MYSTERY', '978-0486474915', 'Collection of detective stories', 1892, TRUE),
('The Old Man and the Sea', 'FICTION', '978-0684801223', 'Novella about an aging fisherman', 1952, TRUE),
('Mrs. Dalloway', 'FICTION', '978-0156628709', 'Modernist novel set in post-World War I England', 1925, TRUE),
('The Great Gatsby', 'FICTION', '978-0743273565', 'Novel about the Jazz Age in America', 1925, TRUE),
('Java Programming Guide', 'TECHNOLOGY', '978-1234567890', 'Comprehensive guide to Java programming', 2023, TRUE),
('Database Design Principles', 'TECHNOLOGY', '978-1234567891', 'Fundamentals of database design and management', 2022, TRUE),
('World History: Ancient Civilizations', 'HISTORY', '978-1234567892', 'Comprehensive overview of ancient world civilizations', 2021, TRUE),
('Einstein: His Life and Universe', 'BIOGRAPHY', '978-0743264747', 'Biography of Albert Einstein', 2007, TRUE),
('Children''s Fairy Tales', 'CHILDREN', '978-1234567893', 'Collection of classic fairy tales for children', 2020, TRUE);

-- Link books to authors (Many-to-Many relationship)
INSERT INTO book_author (book_id, author_id) VALUES
(1, 1),   -- Harry Potter - J.K. Rowling
(2, 2),   -- 1984 - George Orwell
(3, 3),   -- Pride and Prejudice - Jane Austen
(4, 4),   -- Tom Sawyer - Mark Twain
(5, 5),   -- Great Expectations - Charles Dickens
(6, 6),   -- Hamlet - Shakespeare
(7, 7),   -- Murder on Orient Express - Agatha Christie
(8, 8),   -- The Shining - Stephen King
(9, 9),   -- LOTR - Tolkien
(10, 10), -- To Kill a Mockingbird - Harper Lee
(11, 11), -- Foundation - Isaac Asimov
(12, 12), -- Sherlock Holmes - Arthur Conan Doyle
(13, 13), -- The Old Man and the Sea - Ernest Hemingway
(14, 14), -- Mrs. Dalloway - Virginia Woolf
(15, 15); -- The Great Gatsby - F. Scott Fitzgerald

-- Insert sample members
INSERT INTO members (name, email, phone) VALUES
('John Smith', 'john.smith@email.com', '+1-555-0101'),
('Jane Doe', 'jane.doe@email.com', '+1-555-0102'),
('Bob Johnson', 'bob.johnson@email.com', '+1-555-0103'),
('Alice Brown', 'alice.brown@email.com', '+1-555-0104'),
('Charlie Wilson', 'charlie.wilson@email.com', '+1-555-0105'),
('Diana Prince', 'diana.prince@email.com', '+1-555-0106'),
('Edward Norton', 'edward.norton@email.com', '+1-555-0107'),
('Fiona Green', 'fiona.green@email.com', '+1-555-0108'),
('George Miller', 'george.miller@email.com', '+1-555-0109'),
('Helen Davis', 'helen.davis@email.com', '+1-555-0110'),
('Ivan Petrov', 'ivan.petrov@email.com', '+1-555-0111'),
('Julia Roberts', 'julia.roberts@email.com', '+1-555-0112');

-- Insert sample borrowings (mix of current and returned)
INSERT INTO borrowings (borrow_date, due_date, return_date, status, member_id, book_id) VALUES
('2024-01-01', '2024-01-15', '2024-01-12', 'RETURNED', 1, 1),
('2024-01-05', '2024-01-19', NULL, 'BORROWED', 2, 2),
('2024-01-10', '2024-01-24', '2024-01-20', 'RETURNED', 3, 3),
('2024-01-15', '2024-01-29', NULL, 'BORROWED', 4, 4),
('2024-01-20', '2024-02-03', NULL, 'BORROWED', 5, 5),
('2024-01-25', '2024-02-08', NULL, 'BORROWED', 1, 6),
('2024-02-01', '2024-02-15', NULL, 'BORROWED', 2, 7),
('2024-02-05', '2024-02-19', '2024-02-18', 'RETURNED', 3, 8),
('2024-02-10', '2024-02-24', NULL, 'BORROWED', 4, 9),
('2024-02-15', '2024-03-01', NULL, 'BORROWED', 5, 10),
('2024-02-20', '2024-03-05', '2024-03-01', 'RETURNED', 6, 11),
('2024-02-25', '2024-03-10', NULL, 'BORROWED', 7, 12),
('2024-03-01', '2024-03-15', NULL, 'BORROWED', 8, 13),
('2024-03-05', '2024-03-19', '2024-03-18', 'RETURNED', 9, 14),
('2024-03-10', '2024-03-24', NULL, 'BORROWED', 10, 15);

-- Update book availability based on current borrowings
UPDATE books SET available = FALSE WHERE id IN (
    SELECT DISTINCT book_id FROM borrowings WHERE status = 'BORROWED'
);

-- Display summary
SELECT 'Sample data inserted successfully!' as message;
SELECT 
    (SELECT COUNT(*) FROM authors) as authors_count,
    (SELECT COUNT(*) FROM books) as books_count,
    (SELECT COUNT(*) FROM members) as members_count,
    (SELECT COUNT(*) FROM borrowings) as borrowings_count,
    (SELECT COUNT(*) FROM borrowings WHERE status = 'BORROWED') as active_borrowings_count,
    (SELECT COUNT(*) FROM books WHERE available = FALSE) as unavailable_books_count;
