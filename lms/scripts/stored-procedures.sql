-- Stored procedures for Library Management System
-- SQL Server version

USE LibraryDB;
GO

-- Stored procedure to get overdue books by number of days
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'GetOverdueBooksByDays')
    DROP PROCEDURE GetOverdueBooksByDays;
GO

CREATE PROCEDURE GetOverdueBooksByDays
    @days INT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT DISTINCT 
        b.id,
        b.title,
        b.category,
        b.available,
        b.created_date,
        b.version,
        b.created_at,
        b.updated_at
    FROM books b
    INNER JOIN borrowings br ON b.id = br.book_id
    WHERE br.status = 'BORROWED' 
      AND br.due_date < DATEADD(day, -@days, GETDATE())
    ORDER BY b.title;
END
GO

-- Stored procedure to get top borrowed books
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'GetTopBorrowedBooks')
    DROP PROCEDURE GetTopBorrowedBooks;
GO

CREATE PROCEDURE GetTopBorrowedBooks
    @limit INT = 10
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT TOP (@limit)
        b.id,
        b.title,
        b.category,
        b.available,
        b.created_date,
        b.version,
        b.created_at,
        b.updated_at,
        COUNT(br.id) as borrow_count
    FROM books b
    LEFT JOIN borrowings br ON b.id = br.book_id
    GROUP BY b.id, b.title, b.category, b.available, b.created_date, b.version, b.created_at, b.updated_at
    ORDER BY COUNT(br.id) DESC, b.title;
END
GO

-- Stored procedure to get member borrowing statistics
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'GetMemberBorrowingStats')
    DROP PROCEDURE GetMemberBorrowingStats;
GO

CREATE PROCEDURE GetMemberBorrowingStats
    @member_id BIGINT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT 
        m.id,
        m.name,
        m.email,
        COUNT(br.id) as total_borrowings,
        COUNT(CASE WHEN br.status = 'BORROWED' THEN 1 END) as active_borrowings,
        COUNT(CASE WHEN br.status = 'BORROWED' AND br.due_date < GETDATE() THEN 1 END) as overdue_borrowings
    FROM members m
    LEFT JOIN borrowings br ON m.id = br.member_id
    WHERE m.id = @member_id
    GROUP BY m.id, m.name, m.email;
END
GO

-- Stored procedure to check member borrowing eligibility (max 5 books)
IF EXISTS (SELECT * FROM sys.objects WHERE type = 'P' AND name = 'CheckMemberEligibility')
    DROP PROCEDURE CheckMemberEligibility;
GO

CREATE PROCEDURE CheckMemberEligibility
    @member_id BIGINT,
    @is_eligible BIT OUTPUT,
    @current_count INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT @current_count = COUNT(*)
    FROM borrowings
    WHERE member_id = @member_id AND status = 'BORROWED';
    
    SET @is_eligible = CASE WHEN @current_count < 5 THEN 1 ELSE 0 END;
END
GO

PRINT 'Stored procedures created successfully!';
