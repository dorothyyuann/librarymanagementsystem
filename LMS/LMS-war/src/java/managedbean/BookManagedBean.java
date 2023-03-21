package managedbean;

import entity.Book;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import session.stateless.BookSessionLocal;
import util.Exception.BookExistsException;
import util.Exception.BookNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Named(value = "bookManagedBean")
@RequestScoped
public class BookManagedBean {

    @EJB
    private BookSessionLocal bookSessionLocal;

    private String title;
    private String isbn;
    private String author;

    private List<Book> searchResult;
    private String searchString;

    public BookManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (searchString == null || searchString.equals("")) {
            searchResult = bookSessionLocal.retrieveAllBooks();
        } else {
            try {
                searchResult = new ArrayList();
                searchResult.add(bookSessionLocal.retrieveBookByISBN(isbn));
            } catch (BookNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void handleSearch() {
        init();
    }

    public void retrieveBookByISBN(String isbn) {
        try {
            System.out.println("Searching...");
            Book book = bookSessionLocal.retrieveBookByISBN(isbn);
            if (book.getLending().size() > 0 && book.getLending().get(book.getLending().size() - 1).getReturnDate() == null) {
                System.out.println(book.getTitle() + " is currently on loan.");
            } else {
                setIsbn(book.getIsbn());
                setTitle(book.getTitle());
                setAuthor(book.getAuthor());
            }
        } catch (BookNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createBook(ActionEvent evt) {
        Book b = new Book();
        b.setTitle(title);
        b.setIsbn(isbn);
        b.setAuthor(author);

        try {
            bookSessionLocal.createBook(b);
        } catch (BookExistsException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Book> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<Book> searchResult) {
        this.searchResult = searchResult;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

}
