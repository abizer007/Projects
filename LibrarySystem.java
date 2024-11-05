import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.*;

public class LibrarySystem extends JFrame {
    // Abstract base class for library items
    abstract static class LibraryItem {
        protected String title;
        protected String itemId;
        protected boolean isAvailable;
        protected String borrowerId;
        protected String dueDate;

        public LibraryItem(String title, String itemId) {
            this.title = title;
            this.itemId = itemId;
            this.isAvailable = true;
            this.borrowerId = "";
            this.dueDate = "";
        }

        public abstract String getItemType();
        
        public String getTitle() { return title; }
        public String getItemId() { return itemId; }
        public boolean isAvailable() { return isAvailable; }
        public void setAvailable(boolean available) { isAvailable = available; }
        public String getBorrowerId() { return borrowerId; }
        public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    }

    // Book class inheriting from LibraryItem
    static class Book extends LibraryItem {
        private String author;
        private int pages;

        public Book(String title, String itemId, String author, int pages) {
            super(title, itemId);
            this.author = author;
            this.pages = pages;
        }

        @Override
        public String getItemType() {
            return "Book";
        }

        public String getDetails() {
            return String.format("Book: %s by %s", title, author);
        }

        public String getDetails(boolean includePages) {
            if (includePages) {
                return String.format("Book: %s by %s, Pages: %d", title, author, pages);
            }
            return getDetails();
        }

        public String getAuthor() { return author; }
    }

    // Member class for managing library members
    static class Member {
        private String memberId;
        private String name;
        private String contact;
        private int itemsBorrowed;
        private static final int MAX_ITEMS = 3;

        public Member(String memberId, String name, String contact) {
            this.memberId = memberId;
            this.name = name;
            this.contact = contact;
            this.itemsBorrowed = 0;
        }

        public String getMemberId() { return memberId; }
        public String getName() { return name; }
        public String getContact() { return contact; }
        public int getItemsBorrowed() { return itemsBorrowed; }
        
        public boolean canBorrow() {
            return itemsBorrowed < MAX_ITEMS;
        }
        
        public void incrementBorrowed() { itemsBorrowed++; }
        public void decrementBorrowed() { itemsBorrowed--; }
        
        @Override
        public String toString() {
            return String.format("Member ID: %s, Name: %s, Items Borrowed: %d", memberId, name, itemsBorrowed);
        }
    }

    // Custom exception class
    static class LibraryException extends Exception {
        public LibraryException(String message) {
            super(message);
        }
    }

    // Main GUI components
    private ArrayList<LibraryItem> items;
    private ArrayList<Member> members;
    private JTextArea displayArea;
    private JTextField titleField, idField, authorField, pagesField;
    private JTextField memberIdField, memberNameField, memberContactField;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;

    public LibrarySystem() {
        items = new ArrayList<>();
        members = new ArrayList<>();
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books", createBookPanel());
        tabbedPane.addTab("Members", createMemberPanel());
        tabbedPane.addTab("Borrowing", createBorrowingPanel());
        
        JPanel searchPanel = createSearchPanel();
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(searchPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        titleField = new JTextField();
        idField = new JTextField();
        authorField = new JTextField();
        pagesField = new JTextField();

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Pages:"));
        panel.add(pagesField);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        panel.add(addButton);

        return panel;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        memberIdField = new JTextField();
        memberNameField = new JTextField();
        memberContactField = new JTextField();

        panel.add(new JLabel("Member ID:"));
        panel.add(memberIdField);
        panel.add(new JLabel("Name:"));
        panel.add(memberNameField);
        panel.add(new JLabel("Contact:"));
        panel.add(memberContactField);

        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(e -> addMember());
        panel.add(addMemberButton);

        return panel;
    }

    private JPanel createBorrowingPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField borrowBookId = new JTextField();
        JTextField borrowMemberId = new JTextField();

        panel.add(new JLabel("Book ID:"));
        panel.add(borrowBookId);
        panel.add(new JLabel("Member ID:"));
        panel.add(borrowMemberId);

        JButton borrowButton = new JButton("Borrow");
        borrowButton.addActionListener(e -> borrowItem(borrowBookId.getText(), borrowMemberId.getText()));
        
        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> returnItem(borrowBookId.getText(), borrowMemberId.getText()));

        panel.add(borrowButton);
        panel.add(returnButton);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchTypeCombo = new JComboBox<>(new String[]{"Title", "Author", "Member"});
        
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> search());

        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(searchTypeCombo);
        panel.add(searchButton);

        return panel;
    }

    private void addBook() {
        try {
            validateBookInput();
            String title = titleField.getText();
            String id = idField.getText();
            String author = authorField.getText();
            int pages = Integer.parseInt(pagesField.getText());

            Book book = new Book(title, id, author, pages);
            items.add(book);
            clearBookFields();
            displayArea.setText("Book added successfully!\n" + book.getDetails(true));
        } catch (LibraryException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMember() {
        try {
            validateMemberInput();
            String id = memberIdField.getText();
            String name = memberNameField.getText();
            String contact = memberContactField.getText();

            Member member = new Member(id, name, contact);
            members.add(member);
            clearMemberFields();
            displayArea.setText("Member added successfully!\n" + member.toString());
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrowItem(String bookId, String memberId) {
        try {
            LibraryItem item = findItem(bookId);
            Member member = findMember(memberId);

            if (item == null || member == null) {
                throw new LibraryException("Book or member not found");
            }

            if (!item.isAvailable()) {
                throw new LibraryException("Book is not available");
            }

            if (!member.canBorrow()) {
                throw new LibraryException("Member has reached maximum borrowing limit");
            }

            item.setAvailable(false);
            item.setBorrowerId(memberId);
            item.setDueDate(LocalDate.now().plusDays(14).toString());
            member.incrementBorrowed();

            displayArea.setText(String.format("Book borrowed successfully!\nDue Date: %s", item.getDueDate()));
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnItem(String bookId, String memberId) {
        try {
            LibraryItem item = findItem(bookId);
            Member member = findMember(memberId);

            if (item == null || member == null) {
                throw new LibraryException("Book or member not found");
            }

            if (item.isAvailable()) {
                throw new LibraryException("Book is already returned");
            }

            if (!item.getBorrowerId().equals(memberId)) {
                throw new LibraryException("Book was not borrowed by this member");
            }

            item.setAvailable(true);
            item.setBorrowerId("");
            item.setDueDate("");
            member.decrementBorrowed();

            displayArea.setText("Book returned successfully!");
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void search() {
        String searchText = searchField.getText().toLowerCase();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        StringBuilder result = new StringBuilder("Search Results:\n\n");

        switch (searchType) {
            case "Title":
                items.stream()
                     .filter(item -> item.getTitle().toLowerCase().contains(searchText))
                     .forEach(item -> result.append(item instanceof Book ? 
                             ((Book) item).getDetails(true) : item.getTitle()).append("\n"));
                break;
            case "Author":
                items.stream()
                     .filter(item -> item instanceof Book)
                     .map(item -> (Book) item)
                     .filter(book -> book.getAuthor().toLowerCase().contains(searchText))
                     .forEach(book -> result.append(book.getDetails(true)).append("\n"));
                break;
            case "Member":
                members.stream()
                      .filter(member -> member.getName().toLowerCase().contains(searchText))
                      .forEach(member -> result.append(member.toString()).append("\n"));
                break;
        }

        displayArea.setText(result.toString());
    }

    private LibraryItem findItem(String itemId) {
        return items.stream()
                   .filter(item -> item.getItemId().equals(itemId))
                   .findFirst()
                   .orElse(null);
    }

    private Member findMember(String memberId) {
        return members.stream()
                     .filter(member -> member.getMemberId().equals(memberId))
                     .findFirst()
                     .orElse(null);
    }

    private void validateBookInput() throws LibraryException {
        if (titleField.getText().trim().isEmpty()) {
            throw new LibraryException("Title cannot be empty");
        }
        if (idField.getText().trim().isEmpty()) {
            throw new LibraryException("ID cannot be empty");
        }
        if (authorField.getText().trim().isEmpty()) {
            throw new LibraryException("Author cannot be empty");
        }
    }

    private void validateMemberInput() throws LibraryException {
        if (memberIdField.getText().trim().isEmpty()) {
            throw new LibraryException("Member ID cannot be empty");
        }
        if (memberNameField.getText().trim().isEmpty()) {
            throw new LibraryException("Name cannot be empty");
        }
        if (memberContactField.getText().trim().isEmpty()) {
            throw new LibraryException("Contact cannot be empty");
        }
    }

    private void clearBookFields() {
        titleField.setText("");
        idField.setText("");
        authorField.setText("");
        pagesField.setText("");
    }

    private void clearMemberFields() {
        memberIdField.setText("");
        memberNameField.setText("");
        memberContactField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibrarySystem().setVisible(true);
        });
    }
}