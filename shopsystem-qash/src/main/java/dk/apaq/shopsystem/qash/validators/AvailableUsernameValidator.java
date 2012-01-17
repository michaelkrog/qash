package dk.apaq.shopsystem.qash.validators;

import com.vaadin.data.Validator;
import dk.apaq.shopsystem.service.SystemServiceHolder;

/**
 *
 * @author krog
 */
public class AvailableUsernameValidator implements Validator {

    private String errorMessage = "Username is not available.";

    public AvailableUsernameValidator() {
    }
    
    public AvailableUsernameValidator(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public void validate(Object value) throws InvalidValueException {
        if(!isValid(value)) {
            throw new InvalidValueException(errorMessage);
        }
    }

    @Override
    public boolean isValid(Object value) {
        if(!(value instanceof String)) return false;
        return SystemServiceHolder.getSystemService().isUsernameInUse((String)value);
    }
    
}
