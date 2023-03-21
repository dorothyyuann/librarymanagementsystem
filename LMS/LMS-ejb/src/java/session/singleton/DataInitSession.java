/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import session.stateless.BookSessionLocal;
import session.stateless.MemberSessionLocal;
import session.stateless.StaffSessionLocal;
import util.Exception.BookExistsException;
import util.Exception.BookNotFoundException;
import util.Exception.MemberExistsException;
import util.Exception.MemberNotFoundException;
import util.Exception.StaffExistsException;
import util.Exception.StaffNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Singleton
@LocalBean
@Startup
public class DataInitSession implements DataInitSessionLocal {

    @EJB
    private MemberSessionLocal memberSession;

    @EJB
    private BookSessionLocal bookSession;

    @EJB
    private StaffSessionLocal staffSession;

    public DataInitSession() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffSession.retrieveStaffById(1l);
            bookSession.retrieveBookById(1l);
            memberSession.retrieveMemberById(1l);
        } catch (StaffNotFoundException | BookNotFoundException | MemberNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        try {
            staffSession.createStaff("Eric", "Some", "eric", "password");
            staffSession.createStaff("Sarah", "Brightman", "sarah", "password");
            bookSession.createBook("Anna Karenina", "0451528611", "Leo Tolstoy");
            bookSession.createBook("Madame Bovary", "979-8649042031", "Gustave Flaubert");
            bookSession.createBook("Hamlet", "1980625026", "William Shakespeare");
            bookSession.createBook("The Hobbit", "9780007458424", "J R R Tolkien");
            bookSession.createBook("Great Expectations", "1521853592", "Charles Dickens");
            bookSession.createBook("Pride and Prejudice", "979-8653642272", "Jane Austen");
            bookSession.createBook("Wuthering Heights", "3961300224", "Emily Brontë");
            memberSession.createMember("Tony", "Shade", 'M', 31, "S8900678A", "83722773", "13 Jurong East, Ave 3");
            memberSession.createMember("Dewi", "Tan", 'F', 35, "S8581028X", "94602711", "15 Computing Dr");
        } catch (StaffExistsException | BookExistsException | MemberExistsException | UnknownPersistenceException ex) {
            ex.getMessage();
        }
    }
}
