package managedbean;

import entity.Book;
import entity.LibMember;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import session.stateless.MemberSessionLocal;
import util.Exception.FineNotPaidException;
import util.Exception.MemberNotFoundException;

/**
 *
 * @author dorothyyuan
 */
@Named(value = "memberManagedBean")
@SessionScoped
public class MemberManagedBean implements Serializable {

    @EJB
    private MemberSessionLocal memberSessionLocal;

    private String firstName;
    private String lastName;
    private Character gender;
    private Integer age;
    private String idNo;
    private String phone;
    private String address;

    private BigDecimal fine;

    private String isbn;
    private String title;
    private String author;

    private List<Book> borrowed;
    private List<Book> selected;

    public MemberManagedBean() {
    }

    public void retrieveMemberByIdentityNo() {
        System.out.println("Searching for Member " + idNo);
        try {
            LibMember m = memberSessionLocal.retrieveMemberByIdentityNo(idNo);
            firstName = m.getFirstName();
            lastName = m.getLastName();
            gender = m.getGender();
            age = m.getAge();
            phone = m.getAddress();
            retrieveBorrowing();
        } catch (MemberNotFoundException ex) {
            firstName = null;
            lastName = null;
            gender = null;
            age = null;
            phone = null;
            System.out.println(ex.getMessage());
            FacesContext currentInstance = FacesContext.getCurrentInstance();
            currentInstance.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Member Found", "Incorrect ID Number entered."));
        }
    }

    public void lendBook() {
        memberSessionLocal.lendBook(idNo, isbn);
        System.out.println("Book ISBN " + isbn + " has been loaned to Member ID " + idNo);
    }

    public String returnBook() {
        System.out.println("Attempting to return: " + selected.toString());
        try {
            for (Book book : selected) {
                System.out.println("Returning book: " + book.getTitle());
                memberSessionLocal.returnBook(book.getBookId());
                fine = BigDecimal.ZERO;
                System.out.println("Book " + book.getTitle() + " has been returned.");
            }
            return "/access/home.xhtml?faces-redirect=true";

        } catch (FineNotPaidException ex) {
            System.out.println(ex.getMessage());
            PrimeFaces.current().dialog().openDynamic("/access/function/payment");
        }
        return null;
    }
    
    public void close() {
        PrimeFaces.current().dialog().closeDynamic("/access/function/payment");
    }

    public void payFine() {
        for (Book book : selected) {
            memberSessionLocal.payFine(book.getBookId());
        }
        System.out.println("Fines paid.");
        returnBook();
    }

    public void retrieveBorrowing() {
        borrowed = memberSessionLocal.retrieveBorrowed(idNo);
        System.out.println("Member " + idNo + " has borrowed: " + borrowed);
    }

    public String viewSelected() {
        String selectedString = "";
        if (selected == null || selected.isEmpty()) {
            selectedString = "No books selected.";
        } else {
            System.out.print("Selected books include: ");
            for (Book b : selected) {
                System.out.print(b.getTitle() + " ");
                selectedString = selectedString + b.getTitle() + ", ";
            }
            selectedString = selectedString.substring(0, selectedString.length() - 2);
        }
        return selectedString;
    }

    public void calculateFine() {
        BigDecimal calcFine = BigDecimal.ZERO;
        try {
            for (Book book : selected) {
                calcFine = calcFine.add(memberSessionLocal.viewFineAmount(book.getIsbn()));
            }
            System.out.println("Total fine amount for member " + idNo + " is $" + calcFine);
            if (!calcFine.equals(BigDecimal.ZERO)) {
                fine = calcFine;
            }
        } catch (MemberNotFoundException ex) {
            ex.getMessage();
        }
    }

    public String nextLend() {
        retrieveMemberByIdentityNo();
        if (firstName != null) {
            return "/access/function/lend.xhtml?faces-redirect=true";
        }
        return null;
    }

    public String nextReturn() {
        retrieveMemberByIdentityNo();
        if (firstName != null) {
            return "/access/function/return.xhtml?faces-redirect=true";
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Book> getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(List<Book> borrowed) {
        this.borrowed = borrowed;
    }

    public List<Book> getSelected() {
        return selected;
    }

    public void setSelected(List<Book> selected) {
        this.selected = selected;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

}
