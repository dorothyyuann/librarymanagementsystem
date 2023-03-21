/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Book;
import entity.LendAndReturn;
import entity.LibMember;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.DateComparator;
import util.Exception.BookNotFoundException;
import util.Exception.FineNotPaidException;
import util.Exception.MemberExistsException;
import util.Exception.MemberNotFoundException;
import util.Exception.UnknownPersistenceException;
import util.TimeComparator;

/**
 *
 * @author dorothyyuan
 */
@Stateless
public class MemberSession implements MemberSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private BookSessionLocal bookSession;
    DateComparator dateComparator = new DateComparator();
    TimeComparator timeComparator = new TimeComparator();

    @Override
    public LibMember createMember(LibMember mem) throws MemberExistsException, UnknownPersistenceException {
        try {
            LibMember exists = retrieveMemberByIdentityNo(mem.getIdentityNo());
            if (exists != null) {
                throw new MemberExistsException("Member already exists!");
            }
        } catch (MemberNotFoundException notExistsEx) {
            try {
                em.persist(mem);
                return mem;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new MemberExistsException("Member already exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public void createMember(String firstName, String lastName, char gender, int age, String identityNo, String phone, String address) throws MemberExistsException, UnknownPersistenceException {
        try {
            LibMember mem = new LibMember(firstName, lastName, new Character(gender), new Integer(age), identityNo, phone, address, new ArrayList());
            em.persist(mem);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new MemberExistsException("Member already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public LibMember retrieveMemberById(Long memId) throws MemberNotFoundException {

        LibMember mem = em.find(LibMember.class,
                memId);

        if (mem != null) {
            return mem;
        } else {
            throw new MemberNotFoundException("Member ID " + memId + " does not exist!");
        }
    }

    @Override
    public LibMember retrieveMemberByIdentityNo(String idNo) throws MemberNotFoundException {
        Query query = em.createQuery("SELECT m FROM LibMember m WHERE m.identityNo = :inIdNo");
        query.setParameter("inIdNo", idNo);
        System.out.println("Retrieving Member...");
        try {
            return (LibMember) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new MemberNotFoundException("Member with Identity Number " + idNo + " does not exist!");
        }
    }

    @Override
    // fine amount can only be viewed when member tries to return book
    public BigDecimal viewFineAmount(String isbn) throws MemberNotFoundException {
        try {
            Book book = bookSession.retrieveBookByISBN(isbn);
            LendAndReturn loan = book.getLending().get(book.getLending().size() - 1);

            if (loan.getReturnDate() == null) { // book still unreturned use current date as temporary return date
                Calendar lendDate = Calendar.getInstance(); // loan date
                lendDate.setTime(loan.getLendDate());
                System.out.println("Book was loaned on: " + loan.getLendDate());
                Calendar dueDate = lendDate;
                Calendar today = Calendar.getInstance(); // present date

                // Using 1 MINUTE for Testing
                dueDate.add(Calendar.MINUTE, 1); // 1 MINUTE after loan
                // *** UNCOMMENT FOR 14 DAYS ***
                // dueDate.add(Calendar.DATE, 14); // 14 days ago       

                System.out.printf("Due date is: %d-%02d-%02d %02d:%02d", dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH) + 1, dueDate.get(Calendar.DAY_OF_MONTH), dueDate.get(Calendar.HOUR_OF_DAY), dueDate.get(Calendar.MINUTE));
                System.out.printf("Book is being returned at: %d-%02d-%02d %02d:%02d", today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.HOUR_OF_DAY), today.get(Calendar.MINUTE));

                // *** UNCOMMENT FOR 14 DAYS ***
                // if (dateComparator.compare(dueDate, today) > 0) { // fine
                if (timeComparator.compare(today, dueDate) > 0) { // positive integer returned, today is greater than dueDate (overdue)
                    System.out.println("Book is overdue!");
                    long todayInMs = today.getTimeInMillis();
                    long dueDateInMs = dueDate.getTimeInMillis();
                    long timeDiff = Math.abs(todayInMs - dueDateInMs);

                    // Using 1 MINUTE for Testing
                    long daysDiff = TimeUnit.MINUTES.convert(timeDiff, TimeUnit.MILLISECONDS);

                    // *** UNCOMMENT FOR DAYS ***
                    //long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
                    System.out.println("Number of days overdue: " + daysDiff);
                    BigDecimal lateFine = new BigDecimal(daysDiff);
                    lateFine = lateFine.multiply(new BigDecimal(0.5));
                    loan.setFineAmount(lateFine);
                }
                System.out.println("Member " + loan.getMember().getFirstName() + " has a fine of $" + loan.getFineAmount() + " for book " + loan.getBook().getTitle());
            }
            return loan.getFineAmount();
        } catch (BookNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return BigDecimal.ZERO;
    }

    // return -> has fine -> fail -> view fine -> pay fine -> set fine value as 0 -> return
    @Override
    public void returnBook(Long bookId) throws FineNotPaidException {
        try {
            Book book = bookSession.retrieveBookById(bookId);
            LendAndReturn loan = book.getLending().get(book.getLending().size() - 1); // most recent loan

            // check for fine
            if (loan.getFineAmount().compareTo(BigDecimal.ZERO) > 0) { // fineAmount is positive
                throw new FineNotPaidException("Unpaid fine. Unable to complete book return.");
            } else { // no unpaid fine
                loan.setReturnDate(new Date()); // book is returned
            }
        } catch (BookNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void lendBook(String idNo, String isbn) {
        Date lendDate = new Date();
        try {
            LibMember member = retrieveMemberByIdentityNo(idNo);
            Long memId = member.getMemberId();
            Book book = bookSession.retrieveBookByISBN(isbn);
            System.out.println("Member " + member.getIdentityNo() + " is attempting to borrow " + book.getTitle());

            if (book.getLending().size() > 0 && book.getLending().get(book.getLending().size() - 1).getReturnDate() == null) { // book is unreturned, i.e., unavailable
                throw new BookNotFoundException("Book is unavailable for loan.");
            }

            LendAndReturn loan = new LendAndReturn(memId, book.getBookId(), lendDate);
            loan.setBook(book);
            loan.setMember(member);
            em.persist(loan);
            book.getLending().add(loan);
            member.getLending().add(loan);
        } catch (BookNotFoundException | MemberNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // fine can only be paid when book is returned
    // there should not be a situation where the fine is paid but the book is unreturned
    // if book is returned, reverse the payment
    @Override
    public void payFine(Long bookId) {
        try {
            Book book = bookSession.retrieveBookById(bookId);
            LendAndReturn loan = book.getLending().get(book.getLending().size() - 1); // most recent loan
            loan.setFineAmount(BigDecimal.ZERO);
        } catch (BookNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Book> retrieveBorrowed(String idNo) {
        LibMember member = (LibMember) em.createQuery("SELECT m FROM LibMember m WHERE m.identityNo = :inIdNo")
                .setParameter("inIdNo", idNo)
                .getSingleResult();
        List<Book> borrowed = new ArrayList();
        List<LendAndReturn> lending = member.getLending();

        for (LendAndReturn l : lending) {
            if (l.getReturnDate() == null) {
                borrowed.add(l.getBook());
            }
        }

        return borrowed;
    }
}
