/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Book;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.Exception.BookExistsException;
import util.Exception.BookNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Stateless
public class BookSession implements BookSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createBook(String title, String isbn, String author) throws BookExistsException, UnknownPersistenceException {
        try {
            Book book = new Book(title, isbn, author, new ArrayList());
            em.persist(book);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new BookExistsException("Book already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public void createBook(Book book) throws BookExistsException, UnknownPersistenceException {
        try {
            em.persist(book);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new BookExistsException("Book already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Book retrieveBookById(Long bookId) throws BookNotFoundException {
        Book book = em.find(Book.class, bookId);

        if (book != null) {
            return book;
        } else {
            throw new BookNotFoundException("Book ID " + bookId + " does not exist!");
        }
    }

    @Override
    public Book retrieveBookByISBN(String isbn) throws BookNotFoundException {

        Query query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :inIsbn");
        query.setParameter("inIsbn", isbn);

        try {
            return (Book) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BookNotFoundException("Book with ISBN " + isbn + " does not exist!");
        }
    }

    @Override
    public List<Book> retrieveAllBooks() {
        List<Book> books = em.createQuery("SELECT b FROM Book b ").getResultList();
        for (Book b : books) {
            b.getLending();
        }
        return books;
    }
}
