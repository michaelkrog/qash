package dk.apaq.shopsystem.ui.qash.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.validator.DoubleValidator;
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
import java.util.Arrays;

/**
 *
 * @author krog
 */
public class TaxForm extends Form {

    private GridLayout ourLayout;
    private Label lblName = new Label("Name");
    private Label lblRate = new Label("Rate");
    private Button btnSave = new Button("Save");



    private class TaxFormFieldFactory extends DefaultFieldFactory {

        
        public TaxFormFieldFactory() {
        }

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f = super.createField(item, propertyId, uiContext);
            

            if("name".equals(propertyId)) {
                f.addValidator(new StringLengthValidator("Name should be at least one character.", 1, 30, true));
                ((TextField)f).setInputPrompt("Name of tax");
            } else if("rate".equals(propertyId)) {
                f.addValidator(new DoubleValidator("Input must be a number."));
                ((TextField)f).setInputPrompt("Rate of tax fx. 10.3");
            }

            if(f instanceof TextField) {
                ((TextField)f).setNullRepresentation("");
            }

            return f;
        }
    }

    public TaxForm() {
        ourLayout = new GridLayout(2, 3);

        // Use top-left margin and spacing
        ourLayout.setMargin(true);
        ourLayout.setSpacing(true);

        setLayout(ourLayout);

        setFormFieldFactory(new TaxFormFieldFactory());
        
        // Set up buffering
        setWriteThrough(false); // we want explicit 'apply'
        setInvalidCommitted(false); // no invalid values in datamodel

        // Determines which properties are shown, and in which order:
  

        lblName.setSizeUndefined();
        ourLayout.addComponent(lblName, 0, 0);
        ourLayout.setComponentAlignment(lblName, Alignment.MIDDLE_RIGHT);
        lblRate.setSizeUndefined();
        ourLayout.addComponent(lblRate, 0, 1);
        ourLayout.setComponentAlignment(lblRate, Alignment.MIDDLE_RIGHT);
        
        ourLayout.addComponent(btnSave, 1, 2);

        btnSave.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                commit();
                if(getItemDataSource() instanceof Buffered) {
                    ((Buffered)getItemDataSource()).commit();
                }
            }
        });
    }

    /*
     * Override to get control over where fields are placed.
     */
    @Override
    protected void attachField(Object propertyId, Field field) {
        field.setCaption(null);
        if (propertyId.equals("name")) {
            ourLayout.addComponent(field, 1,0);
        } else if (propertyId.equals("rate")) {
            ourLayout.addComponent(field, 1,1);
        } 
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        super.setItemDataSource(newDataSource, Arrays.asList(new String[]{
                    "name", "rate"}));
    }


}
