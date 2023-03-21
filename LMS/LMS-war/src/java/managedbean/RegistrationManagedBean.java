/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.LibMember;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.stateless.MemberSessionLocal;
import util.Exception.MemberExistsException;
import util.Exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Named(value = "registrationManagedBean")
@RequestScoped
public class RegistrationManagedBean {

    @EJB
    private MemberSessionLocal memberSessionLocal;

    private String firstName;
    private String lastName;
    private Character gender;
    private Integer age;
    private String idNo;
    private String phone;
    private String address;

    private LibMember member;
    
    public RegistrationManagedBean() {
    }

    public void createMember(ActionEvent evt) {
        LibMember m = new LibMember();
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setGender(gender);
        m.setAge(age);
        m.setIdentityNo(idNo);
        m.setPhone(phone);
        m.setAddress(address);
        m.setLending(new ArrayList());
        try {
            member = memberSessionLocal.createMember(m);
        } catch (MemberExistsException | UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
            FacesContext currentInstance = FacesContext.getCurrentInstance();
            currentInstance.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unsucessful Registration", "Member with this ID number already exists."));
        }
    }

    public String nextPage() {
        if (member != null) {
            return "home.xhtml?faces-redirect=true";
        }
        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LibMember getMember() {
        return member;
    }

    public void setMember(LibMember member) {
        this.member = member;
    }
}
