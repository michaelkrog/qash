package dk.apaq.shopsystem.qash;

import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import dk.apaq.shopsystem.util.Country;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class StoreForm  extends Form {

    private static List<String> PROPERTIES = Arrays.asList(new String[]{"name", "street", "postalCode", "city",
                                                        "countryCode", "telephone", "email", "faxNo"});
    private GridLayout ourLayout;
    private Label lblName = new Label("Name");
    private Label lblAddress = new Label("Address");
    private Label lblEmail = new Label("Email");
    private Label lblPhone = new Label("Phone");
    private Label lblFax = new Label("VatNo.");
    private Button btnSave = new Button("Save");
    
    private class FormFieldFactory extends DefaultFieldFactory {

        final ComboBox countries = new ComboBox("Country");

        public FormFieldFactory() {
            countries.setWidth("100%");
            BeanContainer countryContainer = new BeanContainer(Country.class);
            countryContainer.setBeanIdProperty("code");
            countries.setItemCaptionPropertyId("name");
            for(Country country : Country.getCountries()) {
                countryContainer.addBean(country);
            }
            countries.setContainerDataSource(countryContainer);
            countries.setFilteringMode(ComboBox.FILTERINGMODE_STARTSWITH);
        }

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f;
            if ("countryCode".equals(propertyId)) {
                return countries;
            } else {
                f = super.createField(item, propertyId, uiContext);
            }

            if("name".equals(propertyId)) {
                f.addValidator(new StringLengthValidator("Name should be at least 3 characters.", 3, 30, true));
                ((TextField)f).setInputPrompt("Name of Store");
            } else if("street".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Street and houseno.");
            } else if("postalCode".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Postal Code");
            } else if("city".equals(propertyId)) {
                ((TextField)f).setInputPrompt("City");
            } else if("email".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Fx. my@address.net");
            }

            if(f instanceof TextField) {
                ((TextField)f).setNullRepresentation("");
            }

            return f;
        }
    }

    public StoreForm() {
        // Create our layout (3x3 GridLayout)
        ourLayout = new GridLayout(3, 8);

        // Use top-left margin and spacing
        ourLayout.setMargin(true, false, false, true);
        ourLayout.setSpacing(true);

        setLayout(ourLayout);

        setFormFieldFactory(new FormFieldFactory());

        // Set up buffering
        setWriteThrough(false); // we want explicit 'apply'
        setInvalidCommitted(false); // no invalid values in datamodel


        lblName.setSizeUndefined();
        ourLayout.addComponent(lblName, 0, 0);
        ourLayout.setComponentAlignment(lblName, Alignment.MIDDLE_RIGHT);
        lblAddress.setSizeUndefined();
        ourLayout.addComponent(lblAddress, 0, 1);
        ourLayout.setComponentAlignment(lblAddress, Alignment.MIDDLE_RIGHT);
        lblPhone.setSizeUndefined();
        ourLayout.addComponent(lblPhone, 0, 4);
        ourLayout.setComponentAlignment(lblPhone, Alignment.MIDDLE_RIGHT);
        lblEmail.setSizeUndefined();
        ourLayout.addComponent(lblEmail, 0, 5);
        ourLayout.setComponentAlignment(lblEmail, Alignment.MIDDLE_RIGHT);
        lblFax.setSizeUndefined();
        ourLayout.addComponent(lblFax, 0, 6);
        ourLayout.setComponentAlignment(lblFax, Alignment.MIDDLE_RIGHT);
        ourLayout.addComponent(btnSave, 1, 7);

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
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 0, 2, 0);
        } else if (propertyId.equals("street")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 1, 2, 1);
        } else if (propertyId.equals("postalCode")) {
            ourLayout.addComponent(field, 1, 2);
        } else if (propertyId.equals("city")) {
            ourLayout.addComponent(field, 2, 2);
        } else if (propertyId.equals("countryCode")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 3, 2, 3);
        } else if (propertyId.equals("telephone")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 4, 2, 4);
        } else if (propertyId.equals("email")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 5, 2, 5);
        } else if (propertyId.equals("faxNo")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 6, 2, 6);
        } else {
            return;
        }

        field.setTabIndex(PROPERTIES.indexOf(propertyId) + 1);
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        super.setItemDataSource(newDataSource, PROPERTIES);
    }

}

