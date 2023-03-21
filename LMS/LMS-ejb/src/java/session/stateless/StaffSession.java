/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Staff;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.Exception.InvalidLoginException;
import util.Exception.StaffExistsException;
import util.Exception.StaffNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Stateless
public class StaffSession implements StaffSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createStaff(String firstName, String lastName, String userName, String password) throws StaffExistsException, UnknownPersistenceException {
        try {
            Staff staff = new Staff(firstName, lastName, userName, password);
            em.persist(staff);
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new StaffExistsException("Staff already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException {

        Staff staff = em.find(Staff.class,
                staffId);

        if (staff != null) {
            return staff;
        } else {
            throw new StaffNotFoundException("Staff ID " + staffId + " does not exist!");
        }
    }

    @Override
    public Staff retrieveStaffByUsername(String un) throws StaffNotFoundException {

        Query query = em.createQuery("SELECT s FROM Staff s WHERE s.userName = :inUsername");
        query.setParameter("inUsername", un);

        try {
            return (Staff) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new StaffNotFoundException("Staff with username " + un + " does not exist!");
        }
    }
    
    @Override
    public Staff login(String username, String password) throws InvalidLoginException {
        try {
            Staff staff = retrieveStaffByUsername(username);

            if (staff.getPassword().equals(password)) {
                return staff;
            } else {
                throw new InvalidLoginException("Invalid login credentials!");
            }
        } catch (StaffNotFoundException ex) {
            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }

}
