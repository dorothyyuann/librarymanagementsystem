/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Book;
import entity.LendAndReturn;
import java.util.List;
import javax.ejb.Local;
import util.Exception.BookExistsException;
import util.Exception.BookNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Local
public interface BookSessionLocal {

    public void createBook(String title, String isbn, String author) throws BookExistsException, UnknownPersistenceException;

    public Book retrieveBookById(Long bookId) throws BookNotFoundException;

    public Book retrieveBookByISBN(String isbn) throws BookNotFoundException;

    public void createBook(Book book) throws BookExistsException, UnknownPersistenceException;

    public List<Book> retrieveAllBooks();
}

