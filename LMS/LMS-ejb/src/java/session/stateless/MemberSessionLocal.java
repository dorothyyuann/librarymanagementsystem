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
import java.util.List;
import javax.ejb.Local;
import util.Exception.FineNotPaidException;
import util.Exception.MemberExistsException;
import util.Exception.MemberNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Local
public interface MemberSessionLocal {

    public LibMember retrieveMemberById(Long memId) throws MemberNotFoundException;

    public void payFine(Long bookId);

    public LibMember createMember(LibMember mem) throws MemberExistsException, UnknownPersistenceException;

    public void createMember(String firstName, String lastName, char gender, int age, String identityNo, String phone, String address) throws MemberExistsException, UnknownPersistenceException;

    public LibMember retrieveMemberByIdentityNo(String idNo) throws MemberNotFoundException;

    public void lendBook(String idNo, String isbn);

    public List<Book> retrieveBorrowed(String idNo);

    public BigDecimal viewFineAmount(String idNo) throws MemberNotFoundException;

    public void returnBook(Long bookId) throws FineNotPaidException;
}
