/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Staff;
import javax.ejb.Local;
import util.Exception.InvalidLoginException;
import util.Exception.StaffExistsException;
import util.Exception.StaffNotFoundException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Local
public interface StaffSessionLocal {

    public void createStaff(String firstName, String lastName, String userName, String password) throws StaffExistsException, UnknownPersistenceException;

    public Staff retrieveStaffById(Long staffId) throws StaffNotFoundException;

    public Staff retrieveStaffByUsername(String un) throws StaffNotFoundException;

    public Staff login(String username, String password) throws InvalidLoginException;
}
