import java.util.*;

class Book {
    String title;
    String author;
    String isbn;
    boolean isAvailable;

    public Book(String title, String author, String isbn, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

class BookInventorySystem {
    private Map<String, Book> booksByIsbn = new HashMap<>();

    public void addBook(Book book) {
        booksByIsbn.put(book.getIsbn(), book);
    }

    public Book findBookByTitle(String title) {
        return booksByIsbn.values().stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public List<Book> findBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        for (Book book : booksByIsbn.values()) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                books.add(book);
            }
        }
        return books;
    }

    public boolean checkAvailability(String isbn) {
        if (booksByIsbn.containsKey(isbn)) {
            return booksByIsbn.get(isbn).isAvailable();
        }
        return false;
    }

    public void updateBookAvailability(String isbn, boolean isAvailable) {
        if (booksByIsbn.containsKey(isbn)) {
            booksByIsbn.get(isbn).setAvailable(isAvailable);
        }
    }
}

class UserManagementSystem {
    private Map<String, List<String>> userBorrowingHistory = new HashMap<>();

    public void borrowBook(String userId, String isbn) {
        userBorrowingHistory.putIfAbsent(userId, new ArrayList<>());
        userBorrowingHistory.get(userId).add(isbn);
    }

    public void returnBook(String userId, String isbn) {
        if (userBorrowingHistory.containsKey(userId)) {
            userBorrowingHistory.get(userId).remove(isbn);
        }
    }

    public List<String> getBorrowingHistory(String userId) {
        return userBorrowingHistory.getOrDefault(userId, Collections.emptyList());
    }
}

class LibraryManagementFacade {
    private BookInventorySystem bookInventory;
    private UserManagementSystem userManager;

    public LibraryManagementFacade() {
        this.bookInventory = new BookInventorySystem();
        this.userManager = new UserManagementSystem();
    }
    public void addBook(Book book) {
        bookInventory.addBook(book);
    }


    public boolean borrowBook(String userId, String title) {
        Book book = bookInventory.findBookByTitle(title);
        if (book != null && book.isAvailable()) {
            userManager.borrowBook(userId, book.getIsbn());
            bookInventory.updateBookAvailability(book.getIsbn(), false);
            return true;
        }
        return false;
    }

    public void returnBook(String userId, String title) {
        Book book = bookInventory.findBookByTitle(title);
        if (book != null) {
            userManager.returnBook(userId, book.getIsbn());
            bookInventory.updateBookAvailability(book.getIsbn(), true);
        }
    }

    public List<Book> searchBooksByAuthor(String author) {
        return bookInventory.findBooksByAuthor(author);
    }

    public Book searchBookByTitle(String title) {
        return bookInventory.findBookByTitle(title);
    }

    public boolean checkBookAvailability(String isbn) {
        return bookInventory.checkAvailability(isbn);
    }
}

public class LibraryManagementFacadeTest {
    public static void main(String[] args) {
        LibraryManagementFacade facade = new LibraryManagementFacade();

        Book book1 = new Book("The Hobbit", "J.R.R. Tolkien", "123456789", true);
        facade.addBook(book1);
        boolean result = facade.borrowBook("user1", "The Hobbit");
        assert result : "Borrowing failed when it should have succeeded.";

        facade.returnBook("user1", "The Hobbit");
        assert facade.checkBookAvailability("123456789") : "Book should be available after return.";

        Book foundBook = facade.searchBookByTitle("The Hobbit");
        assert foundBook != null : "Book should be found.";

        System.out.println("All tests passed.");
    }
}
