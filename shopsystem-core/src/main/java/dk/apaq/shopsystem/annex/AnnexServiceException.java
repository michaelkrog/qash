package dk.apaq.shopsystem.annex;

/**
 *
 * @author krog
 */
public class AnnexServiceException extends RuntimeException {

    public AnnexServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnexServiceException(String message) {
        super(message);
    }
    
    
}
