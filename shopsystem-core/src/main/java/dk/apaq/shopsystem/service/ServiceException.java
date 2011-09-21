/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.service;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
