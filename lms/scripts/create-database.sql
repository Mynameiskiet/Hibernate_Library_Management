-- Create database and tables for Library Management System
-- SQL Server version

-- Updated database name from library_management to LibraryDB
-- Create database if it doesn't exist (SQL Server syntax)
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'LibraryDB')
BEGIN
    CREATE DATABASE LibraryDB;
END
GO

USE LibraryDB;
GO

-- Create Authors table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='authors' AND xtype='U')
CREATE TABLE authors (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    birth_year INT,
    biography NTEXT,
    version BIGINT NOT NULL DEFAULT 0,
    created_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_date DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

-- Create Books table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='books' AND xtype='U')
CREATE TABLE books (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(300) NOT NULL,
    category NVARCHAR(50) NOT NULL,
    isbn NVARCHAR(20) UNIQUE,
    description NTEXT,
    publication_year INT,
    available BIT NOT NULL DEFAULT 1,
    version BIGINT NOT NULL DEFAULT 0,
    created_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_date DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

-- Create Members table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='members' AND xtype='U')
CREATE TABLE members (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(200) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE,
    phone NVARCHAR(20),
    version BIGINT NOT NULL DEFAULT 0,
    created_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_date DATETIME2 NOT NULL DEFAULT GETDATE()
);
GO

-- Create Borrowings table
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='borrowings' AND xtype='U')
CREATE TABLE borrowings (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status NVARCHAR(20) NOT NULL DEFAULT 'BORROWED',
    member_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
GO

-- Create Book-Author junction table for Many-to-Many relationship
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='book_author' AND xtype='U')
CREATE TABLE book_author (
    book_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);
GO

-- Create indexes for better performance
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_book_title')
    CREATE INDEX idx_book_title ON books(title);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_book_category')
    CREATE INDEX idx_book_category ON books(category);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_book_available')
    CREATE INDEX idx_book_available ON books(available);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_member_email')
    CREATE INDEX idx_member_email ON members(email);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_borrowing_status')
    CREATE INDEX idx_borrowing_status ON borrowings(status);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_borrowing_due_date')
    CREATE INDEX idx_borrowing_due_date ON borrowings(due_date);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_borrowing_member_id')
    CREATE INDEX idx_borrowing_member_id ON borrowings(member_id);
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_borrowing_book_id')
    CREATE INDEX idx_borrowing_book_id ON borrowings(book_id);
GO

-- Add constraints for business rules
IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'chk_borrowing_dates')
    ALTER TABLE borrowings ADD CONSTRAINT chk_borrowing_dates 
        CHECK (due_date >= borrow_date);
GO

IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'chk_return_date')
    ALTER TABLE borrowings ADD CONSTRAINT chk_return_date 
        CHECK (return_date IS NULL OR return_date >= borrow_date);
GO

-- Print success message
PRINT 'Database schema created successfully for SQL Server!';
GO
