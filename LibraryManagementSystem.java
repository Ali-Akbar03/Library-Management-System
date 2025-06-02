import java.util.*;
import java.text.SimpleDateFormat;

// Book class
class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") - " + (isAvailable ? "Available" : "Borrowed");
    }
}

// Member class
class Member {
    private String name;
    private int memberId;

    public Member(String name, int memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    public int getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return name + " (ID: " + memberId + ")";
    }
}

// Borrow class
class Borrow {
    private Book book;
    private Member member;
    private Date borrowDate;
    private Date returnDate;

    public Borrow(Book book, Member member, Date borrowDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.returnDate = null;
        book.setAvailable(false);
    }

    public void returnBook(Date returnDate) {
        this.returnDate = returnDate;
        book.setAvailable(true);
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String retDate = (returnDate == null) ? "Not returned" : sdf.format(returnDate);
        return book + " borrowed by " + member + " on " + sdf.format(borrowDate) + " - " + retDate;
    }
}

// Library class
class Library {
    private Map<String, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private List<Borrow> borrowRecords = new ArrayList<>();

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    public Book findBook(String isbn) {
        return books.get(isbn);
    }

    public Member findMember(int memberId) {
        return members.get(memberId);
    }

    public boolean borrowBook(String isbn, int memberId) {
        Book book = findBook(isbn);
        Member member = findMember(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        if (member == null) {
            System.out.println("Member not found.");
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is currently borrowed.");
            return false;
        }

        Borrow borrow = new Borrow(book, member, new Date());
        borrowRecords.add(borrow);
        System.out.println("Book borrowed successfully.");
        return true;
    }

    public boolean returnBook(String isbn, int memberId) {
        for (Borrow b : borrowRecords) {
            if (b.getBook().getIsbn().equals(isbn) && b.getMember().getMemberId() == memberId && !b.isReturned()) {
                b.returnBook(new Date());
                System.out.println("Book returned successfully.");
                return true;
            }
        }
        System.out.println("Borrow record not found.");
        return false;
    }

    public void showBooks() {
        System.out.println("Books in Library:");
        for (Book b : books.values()) {
            System.out.println(b);
        }
    }

    public void showMembers() {
        System.out.println("Library Members:");
        for (Member m : members.values()) {
            System.out.println(m);
        }
    }

    public void showBorrowRecords() {
        System.out.println("Borrow Records:");
        for (Borrow b : borrowRecords) {
            System.out.println(b);
        }
    }
}

// Main class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        // Adding some sample books and members
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565"));
        library.addBook(new Book("1984", "George Orwell", "9780451524935"));
        library.addMember(new Member("Alice", 1));
        library.addMember(new Member("Bob", 2));

        System.out.println("Welcome to Library Management System");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Show all books");
            System.out.println("2. Show all members");
            System.out.println("3. Borrow a book");
            System.out.println("4. Return a book");
            System.out.println("5. Show borrow records");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    library.showBooks();
                    break;
                case 2:
                    library.showMembers();
                    break;
                case 3:
                    System.out.print("Enter member ID: ");
                    int memIdBorrow = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbnBorrow = scanner.nextLine();
                    library.borrowBook(isbnBorrow, memIdBorrow);
                    break;
                case 4:
                    System.out.print("Enter member ID: ");
                    int memIdReturn = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter book ISBN: ");
                    String isbnReturn = scanner.nextLine();
                    library.returnBook(isbnReturn, memIdReturn);
                    break;
                case 5:
                    library.showBorrowRecords();
                    break;
                case 6:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
