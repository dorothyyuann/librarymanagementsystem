/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Exception;

/**
 *
 * @author dorothyyuan
 */
public class StaffExistsException extends Exception {

    /**
     * Creates a new instance of <code>StaffExistsException</code> without
     * detail message.
     */
    public StaffExistsException() {
    }

    /**
     * Constructs an instance of <code>StaffExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public StaffExistsException(String msg) {
        super(msg);
    }
}
