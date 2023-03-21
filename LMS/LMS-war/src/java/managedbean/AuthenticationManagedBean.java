/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Staff;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import session.stateless.StaffSessionLocal;
import util.Exception.InvalidLoginException;

/**
 *
 * @author dorothyyuan
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StaffSessionLocal staffSessionLocal;

    private String username = null;
    private String password = null;
    private Long userId = new Long(-1);

    public AuthenticationManagedBean() {
    }

    public String login() {
        System.out.println("logging in...");
        try {
            Staff staff = staffSessionLocal.login(username, password);
            userId = staff.getStaffId();
            System.out.println("Attempted login");
            System.out.println("Successful login");
            return "/access/home.xhtml?faces-redirect=true";
        } catch (InvalidLoginException ex) {
            username = null;
            password = null;
            userId = new Long(-1);
            return "login.xhtml";
        }
    }

    public String logout() {
        username = null;
        password = null;
        userId = new Long(-1);

        return "/index.xhtml?faces-redirect=true";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return Character.toUpperCase(username.charAt(0)) + username.substring(1);
    }
}
