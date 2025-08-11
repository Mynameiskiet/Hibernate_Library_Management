package com.tuankiet.cli.menus;

import com.tuankiet.cli.helpers.InputHelper;
import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.Sort;
import com.tuankiet.dto.common.SortCriteria;
import com.tuankiet.dto.common.SortDirection;
import com.tuankiet.dto.request.CreateBookRequest;
import com.tuankiet.dto.request.UpdateBookRequest;
import com.tuankiet.dto.response.AuthorResponse;
import com.tuankiet.dto.response.BookResponse;
import com.tuankiet.dto.search.BookSearchCriteria;
import com.tuankiet.enums.BookCategory;
import com.tuankiet.exceptions.CLIExceptionHandler;
import com.tuankiet.services.AuthorService;
import com.tuankiet.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * CLI menu for managing books.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class BookMenu {

    private static final Logger logger = LoggerFactory.getLogger(BookMenu.class);

    private final InputHelper inputHelper;
    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookMenu(InputHelper inputHelper, BookService bookService, AuthorService authorService) {
        this.inputHelper = inputHelper;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Book Management ---");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Books");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = inputHelper.readInt();

            try {
                switch (choice) {
                    case 1:
                        addNewBook();
                        break;
                    case 2:
                        viewAllBooks();
                        break;
                    case 3:
                        searchBooks();
                        break;
                    case 4:
                        updateBook();
                        break;
                    case 5:
                        deleteBook();
                        break;
                    case 0:
                        logger.info("Returning to main menu from Book Management.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                CLIExceptionHandler.handle(e);
            }
        } while (choice != 0);
    }

    private void addNewBook() {
        System.out.println("\n--- Add New Book ---");
        String title = inputHelper.readString("Enter title: ", false);
        String isbn = inputHelper.readString("Enter ISBN (e.g., 978-3-16-148410-0): ", false);
        Integer publicationYear = inputHelper.readInt("Enter publication year (optional, 0 to skip): ");
        if (publicationYear == 0) publicationYear = null;
        BookCategory category = inputHelper.readBookCategory("Select category:");
        Integer totalCopies = inputHelper.readInt("Enter total copies: ");

        System.out.println("\n--- Select Author ---");
        List<AuthorResponse> authors = authorService.getAll();
        if (authors.isEmpty()) {
            System.out.println("No authors found. Please add an author first.");
            return;
        }
        for (int i = 0; i < authors.size(); i++) {
            System.out.println((i + 1) + ". " + authors.get(i).getFirstName() + " " + 
                             authors.get(i).getLastName() + " (ID: " + authors.get(i).getId() + ")");
        }
        int authorChoice = inputHelper.readInt("Select author by number: ");
        if (authorChoice < 1 || authorChoice > authors.size()) {
            System.out.println("Invalid author selection.");
            return;
        }
        UUID authorId = authors.get(authorChoice - 1).getId();

        CreateBookRequest request = new CreateBookRequest(title, isbn, publicationYear, category, totalCopies, authorId);
        bookService.create(request);
        System.out.println("✅ Book added successfully!");
    }

    private void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        int page = 0;
        int size = 5; // Display 5 books per page
        Sort sort = Sort.by(new SortCriteria("title", SortDirection.ASC));
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<BookResponse> bookPage;

        do {
            bookPage = bookService.search(new BookSearchCriteria(), pageRequest);
            displayBooks(bookPage.getContent());

            if (bookPage.hasContent()) {
                System.out.println("\nPage " + (bookPage.getCurrentPage() + 1) + " of " + bookPage.getTotalPages());
                if (bookPage.hasPrevious()) System.out.print(" (P)revious");
                if (bookPage.hasNext()) System.out.print(" (N)ext");
                System.out.print(" (E)xit to menu: ");
                String nav = inputHelper.readLine().trim().toLowerCase();
                if (nav.equals("n") && bookPage.hasNext()) {
                    pageRequest = new PageRequest(bookPage.getCurrentPage() + 1, size, sort);
                } else if (nav.equals("p") && bookPage.hasPrevious()) {
                    pageRequest = new PageRequest(bookPage.getCurrentPage() - 1, size, sort);
                } else if (nav.equals("e")) {
                    break;
                } else {
                    System.out.println("Invalid navigation choice. Staying on current page.");
                }
            } else {
                System.out.println("No books found.");
                break;
            }
        } while (true);
    }

    private void searchBooks() {
        System.out.println("\n--- Search Books ---");
        String title = inputHelper.readString("Enter title (leave empty for any): ", true);
        String isbn = inputHelper.readString("Enter ISBN (leave empty for any): ", true);
        Integer publicationYear = inputHelper.readInt("Enter publication year (optional, 0 to skip): ");
        if (publicationYear == 0) publicationYear = null;
        BookCategory category = null;
        if (inputHelper.readBoolean("Filter by category?")) {
            category = inputHelper.readBookCategory("Select category:");
        }
        UUID authorId = null;
        if (inputHelper.readBoolean("Filter by author?")) {
            List<AuthorResponse> authors = authorService.getAll();
            if (authors.isEmpty()) {
                System.out.println("No authors found to filter by.");
            } else {
                for (int i = 0; i < authors.size(); i++) {
                    System.out.println((i + 1) + ". " + authors.get(i).getFirstName() + " " + 
                                     authors.get(i).getLastName() + " (ID: " + authors.get(i).getId() + ")");
                }
                int authorChoice = inputHelper.readInt("Select author by number (0 to skip): ");
                if (authorChoice > 0 && authorChoice <= authors.size()) {
                    authorId = authors.get(authorChoice - 1).getId();
                }
            }
        }

        BookSearchCriteria criteria = new BookSearchCriteria(
                title.isEmpty() ? null : title,
                isbn.isEmpty() ? null : isbn,
                publicationYear,
                category,
                authorId
        );

        int page = 0;
        int size = 5;
        Sort sort = Sort.by(new SortCriteria("title", SortDirection.ASC));
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<BookResponse> bookPage;

        do {
            bookPage = bookService.search(criteria, pageRequest);
            displayBooks(bookPage.getContent());

            if (bookPage.hasContent()) {
                System.out.println("\nPage " + (bookPage.getCurrentPage() + 1) + " of " + bookPage.getTotalPages());
                if (bookPage.hasPrevious()) System.out.print(" (P)revious");
                if (bookPage.hasNext()) System.out.print(" (N)ext");
                System.out.print(" (E)xit to menu: ");
                String nav = inputHelper.readLine().trim().toLowerCase();
                if (nav.equals("n") && bookPage.hasNext()) {
                    pageRequest = new PageRequest(bookPage.getCurrentPage() + 1, size, sort);
                } else if (nav.equals("p") && bookPage.hasPrevious()) {
                    pageRequest = new PageRequest(bookPage.getCurrentPage() - 1, size, sort);
                } else if (nav.equals("e")) {
                    break;
                } else {
                    System.out.println("Invalid navigation choice. Staying on current page.");
                }
            } else {
                System.out.println("No books found matching your criteria.");
                break;
            }
        } while (true);
    }

    private void updateBook() {
        System.out.println("\n--- Update Book ---");
        UUID id = inputHelper.readUuid("Enter Book ID to update: ");
        if (id == null) {
            System.out.println("Book ID cannot be empty.");
            return;
        }

        BookResponse existingBook = bookService.getById(id);
        System.out.println("Current Book Details: " + existingBook);

        String title = inputHelper.readString("Enter new title (current: " + existingBook.getTitle() + 
                                            ", leave empty to keep): ", true);
        String isbn = inputHelper.readString("Enter new ISBN (current: " + existingBook.getIsbn() + 
                                           ", leave empty to keep): ", true);
        Integer publicationYear = inputHelper.readInt("Enter new publication year (current: " + 
                                                    (existingBook.getPublicationYear() != null ? 
                                                     existingBook.getPublicationYear() : "N/A") + 
                                                    ", 0 to skip/keep): ");
        BookCategory category = null;
        if (inputHelper.readBoolean("Change category? (current: " + existingBook.getCategory() + ")")) {
            category = inputHelper.readBookCategory("Select new category:");
        }
        Integer totalCopies = inputHelper.readInt("Enter new total copies (current: " + 
                                                existingBook.getTotalCopies() + ", 0 to skip/keep): ");
        Integer availableCopies = inputHelper.readInt("Enter new available copies (current: " + 
                                                    existingBook.getAvailableCopies() + ", 0 to skip/keep): ");

        UUID authorId = existingBook.getAuthor() != null ? existingBook.getAuthor().getId() : null;
        if (inputHelper.readBoolean("Change author? (current: " + 
                                   (existingBook.getAuthor() != null ? 
                                    existingBook.getAuthor().getFirstName() + " " + 
                                    existingBook.getAuthor().getLastName() : "N/A") + ")")) {
            List<AuthorResponse> authors = authorService.getAll();
            if (authors.isEmpty()) {
                System.out.println("No authors found. Cannot change author.");
            } else {
                for (int i = 0; i < authors.size(); i++) {
                    System.out.println((i + 1) + ". " + authors.get(i).getFirstName() + " " + 
                                     authors.get(i).getLastName() + " (ID: " + authors.get(i).getId() + ")");
                }
                int authorChoice = inputHelper.readInt("Select new author by number: ");
                if (authorChoice >= 1 && authorChoice <= authors.size()) {
                    authorId = authors.get(authorChoice - 1).getId();
                } else {
                    System.out.println("Invalid author selection. Keeping current author.");
                }
            }
        }

        UpdateBookRequest request = new UpdateBookRequest(
                id,
                title.isEmpty() ? existingBook.getTitle() : title,
                isbn.isEmpty() ? existingBook.getIsbn() : isbn,
                publicationYear == 0 ? existingBook.getPublicationYear() : publicationYear,
                category == null ? existingBook.getCategory() : category,
                availableCopies == 0 ? existingBook.getAvailableCopies() : availableCopies,
                totalCopies == 0 ? existingBook.getTotalCopies() : totalCopies,
                authorId
        );

        bookService.update(request);
        System.out.println("✅ Book updated successfully!");
    }

    private void deleteBook() {
        System.out.println("\n--- Delete Book ---");
        UUID id = inputHelper.readUuid("Enter Book ID to delete: ");
        if (id == null) {
            System.out.println("Book ID cannot be empty.");
            return;
        }

        System.out.print("Are you sure you want to delete book with ID " + id + "? (yes/no): ");
        String confirm = inputHelper.readLine();
        if ("yes".equalsIgnoreCase(confirm)) {
            boolean deleted = bookService.delete(id);
            if (deleted) {
                System.out.println("✅ Book deleted successfully!");
            } else {
                System.out.println("❌ Book not found or could not be deleted.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private void displayBooks(List<BookResponse> books) {
        if (books.isEmpty()) {
            System.out.println("No books to display.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(120));
        System.out.printf("%-36s %-30s %-20s %-15s %-10s %-10s%n", 
                         "ID", "Title", "Author", "Category", "Available", "Total");
        System.out.println("=".repeat(120));
        
        for (BookResponse book : books) {
            String authorName = book.getAuthor() != null ? 
                               book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() : 
                               "Unknown";
            
            System.out.printf("%-36s %-30s %-20s %-15s %-10d %-10d%n",
                book.getId().toString(),
                truncate(book.getTitle(), 30),
                truncate(authorName, 20),
                book.getCategory().toString(),
                book.getAvailableCopies(),
                book.getTotalCopies());
        }
        System.out.println("=".repeat(120));
    }
    
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        return str.length() > maxLength ? str.substring(0, maxLength - 3) + "..." : str;
    }
}
