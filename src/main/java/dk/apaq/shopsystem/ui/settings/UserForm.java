package dk.apaq.shopsystem.ui.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.util.Arrays;

/**
 *
 * @author krog
 */
public class UserForm extends Form {

    private GridLayout ourLayout;
    private Label lblDisplayName = new Label("Full Name");
    private Label lblName = new Label("Login Name");
    private Label lblEmail = new Label("Email");
    private Button btnChangePassword = new Button("Change Password");
    
    private class FormFieldFactory extends DefaultFieldFactory {

        
        public FormFieldFactory() {
        }

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            
            
            Field f = super.createField(item, propertyId, uiContext);
            

            if("name".equals(propertyId)) {
                f.setReadOnly(true);
            } else if("displayname".equals(propertyId)) {
                f.addValidator(new StringLengthValidator("Name should be at least one character.", 1, 30, true));
                ((TextField)f).setInputPrompt("Fx. 'John Doe'");
            } else if("email".equals(propertyId)) {
                f.addValidator(new EmailValidator("Email must be correct email address."));
                ((TextField)f).setInputPrompt("Fx. 'john@doe.com'");
            }

            if(f instanceof TextField) {
                ((TextField)f).setNullRepresentation("");
            }

            return f;
        }
    }

    public UserForm() {
        ourLayout = new GridLayout(2, 4);

        // Use top-left margin and spacing
        ourLayout.setMargin(true);
        ourLayout.setSpacing(true);

        setLayout(ourLayout);

        setFormFieldFactory(new FormFieldFactory());
        
        // Set up buffering
        setWriteThrough(true); // we want explicit 'apply'
        setInvalidCommitted(false); // no invalid values in datamodel

        // Determines which properties are shown, and in which order:
  

        lblDisplayName.setSizeUndefined();
        ourLayout.addComponent(lblDisplayName, 0, 0);
        ourLayout.setComponentAlignment(lblDisplayName, Alignment.MIDDLE_RIGHT);
        lblName.setSizeUndefined();
        ourLayout.addComponent(lblName, 0, 1);
        ourLayout.setComponentAlignment(lblName, Alignment.MIDDLE_RIGHT);
        lblEmail.setSizeUndefined();
        ourLayout.addComponent(lblEmail, 0, 2);
        ourLayout.setComponentAlignment(lblEmail, Alignment.MIDDLE_RIGHT);
        
        ourLayout.addComponent(btnChangePassword, 1, 3);

        btnChangePassword.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                //getApplication().getMainWindow().showNotification("This should change password when done");
                NewPasswordPanel newPasswordForm = new NewPasswordPanel();
                Window window = new Window("Change password");
                window.setModal(true);
                getApplication().getMainWindow().addWindow(window);
                window.addComponent(newPasswordForm);
            }
        });
    }

    /*
     * Override to get control over where fields are placed.
     */
    @Override
    protected void attachField(Object propertyId, Field field) {
        field.setCaption(null);
        if (propertyId.equals("displayname")) {
            ourLayout.addComponent(field, 1,0);
        } else if (propertyId.equals("name")) {
            ourLayout.addComponent(field, 1,1);
        } else if (propertyId.equals("email")) {
            ourLayout.addComponent(field, 1,2);
        } 
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        if(newDataSource instanceof Buffered) {
            ((Buffered)newDataSource).setWriteThrough(true);
        }
        super.setItemDataSource(newDataSource, Arrays.asList(new String[]{
                    "displayname", "name", "email"}));
    }


}
