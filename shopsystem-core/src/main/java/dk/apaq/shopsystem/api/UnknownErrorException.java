package dk.apaq.shopsystem.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception that tells spring mvc that the server encountered an internal error and that it 
 * should report so via standard Http codes(fx. 501)
 * @author krog
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownErrorException extends RuntimeException {

    public UnknownErrorException(String message) {
        super(message);
    }

    public UnknownErrorException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
