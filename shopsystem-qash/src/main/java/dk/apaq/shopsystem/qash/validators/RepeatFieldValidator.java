package dk.apaq.shopsystem.qash.validators;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;

/**
 *
 * @author krog
 */
public class RepeatFieldValidator implements Validator {

    private Field repeatedField;
    private String errorMessage;

    public RepeatFieldValidator(Field repeatedField, String errorMessage) {
        this.repeatedField = repeatedField;
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
        if(repeatedField.getValue() == null && value == null) {
            return true;
        }
        
        if(repeatedField.getValue() != null && repeatedField.getValue().equals(value)) {
            return true;
        }
        
        return false;
    }
    
    
}
