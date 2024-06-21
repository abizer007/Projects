import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagementSystem extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Library library = new Library();
        LibraryGUI libraryGUI = new LibraryGUI(library);

        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(new Scene(libraryGUI.getMainPane(), 800, 600));
        primaryStage.show();
    }
}

class Library {
    private List<Book> books;
    private List<Member> members;
    private List<Transaction> transactions;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.transactions = new ArrayList<>();
        // Initialize with sample data or load from a database
        books.add(new Book("Java Programming", "John Doe", "978-0134685991"));
        books.add(new Book("Database Management", "Jane Smith", "978-0133544012"));
        members.add(new Member("Alice", 101));
        members.add(new Member("Bob", 102));
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}

class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = true;
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
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ")";
    }
}

class Member {
    private String name;
    private int memberId;

    public Member(String name, int memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public int getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return name + " (ID: " + memberId + ")";
    }
}

class Transaction {
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate returnDate;

    public Transaction(Book book, Member member, LocalDate issueDate) {
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.returnDate = null;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}

class LibraryGUI {
    private Library library;
    private BorderPane mainPane;
    private TextField searchField;
    private ListView<Book> booksListView;
    private ListView<Member> membersListView;
    private Button borrowButton;
    private Button returnButton;

    public LibraryGUI(Library library) {
        this.library = library;
        this.mainPane = new BorderPane();
        initializeGUI();
    }

    private void initializeGUI() {
        // Top: Search and search button
        searchField = new TextField();
        searchField.setPromptText("Search books...");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchBooks());

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));

        // Center: Books and members list views
        booksListView = new ListView<>();
        membersListView = new ListView<>();
        refreshBooksListView();
        refreshMembersListView();

        VBox listsBox = new VBox(10, new Label("Books"), booksListView, new Label("Members"), membersListView);
        listsBox.setPadding(new Insets(10));

        // Bottom: Borrow and return buttons
        borrowButton = new Button("Borrow");
        returnButton = new Button("Return");
        borrowButton.setOnAction(e -> handleBorrow());
        returnButton.setOnAction(e -> handleReturn());
        HBox buttonsBox = new HBox(10, borrowButton, returnButton);
        buttonsBox.setPadding(new Insets(10));

        mainPane.setTop(searchBox);
        mainPane.setCenter(listsBox);
        mainPane.setBottom(buttonsBox);
    }

    private void searchBooks() {
        String searchText = searchField.getText().toLowerCase();
        List<Book> matchedBooks = new ArrayList<>();

        for (Book book : library.getBooks()) {
            if (book.getTitle().toLowerCase().contains(searchText) ||
                book.getAuthor().toLowerCase().contains(searchText) ||
                book.getIsbn().toLowerCase().contains(searchText)) {
                matchedBooks.add(book);
            }
        }

        booksListView.getItems().clear();
        booksListView.getItems().addAll(matchedBooks);
    }

    private void refreshBooksListView() {
        booksListView.getItems().clear();
        booksListView.getItems().addAll(library.getBooks());
    }

    private void refreshMembersListView() {
        membersListView.getItems().clear();
        membersListView.getItems().addAll(library.getMembers());
    }

    private void handleBorrow() {
        Book selectedBook = booksListView.getSelectionModel().getSelectedItem();
        Member selectedMember = membersListView.getSelectionModel().getSelectedItem();

        if (selectedBook == null || selectedMember == null) {
            showAlert("Please select a book and a member.");
            return;
        }

        if (!selectedBook.isAvailable()) {
            showAlert("Selected book is already borrowed.");
            return;
        }

        LocalDate issueDate = LocalDate.now();
        Transaction transaction = new Transaction(selectedBook, selectedMember, issueDate);
        library.addTransaction(transaction);
        selectedBook.setAvailable(false);

        refreshBooksListView();
    }

    private void handleReturn() {
        Book selectedBook = booksListView.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            showAlert("Please select a book to return.");
            return;
        }

        if (selectedBook.isAvailable()) {
            showAlert("Selected book is already returned.");
            return;
        }

        Transaction transaction = findTransaction(selectedBook);
        if (transaction != null) {
            transaction.setReturnDate(LocalDate.now());
            selectedBook.setAvailable(true);
            refreshBooksListView();
        } else {
            showAlert("Could not find transaction for the selected book.");
        }
    }

    private Transaction findTransaction(Book book) {
        for (Transaction transaction : library.getTransactions()) {
            if (transaction.getBook().equals(book) && transaction.getReturnDate() == null) {
                return transaction;
            }
        }
        return null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getMainPane() {
        return mainPane;
    }
}
