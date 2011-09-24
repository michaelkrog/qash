package dk.apaq.shopsystem.ui.qash.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
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
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.util.Country;
import java.util.Arrays;

/**
 *
 * @author krog
 */
public class OrganisationForm extends Form {

    private GridLayout ourLayout;
    private Label lblName = new Label("Organsiation Name");
    private Label lblAddress = new Label("Address");
    private Label lblEmail = new Label("Email");
    private Label lblPhone = new Label("Phone");
    private Label lblVatNo = new Label("VatNo.");
    //private Label lblBankAccount = new Label("Bank");
    private Button btnSave = new Button("Save");
    private OrganisationService service;



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
            if ("country".equals(propertyId)) {
                return countries;
            } else {
                f = super.createField(item, propertyId, uiContext);
            }

            if("name".equals(propertyId)) {
                f.addValidator(new StringLengthValidator("Name should be at least one character.", 1, 30, true));
                ((TextField)f).setInputPrompt("Name of Shop");
            } else if("address".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Street and houseno.");
            } else if("zip".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Zipcode");
            } else if("city".equals(propertyId)) {
                ((TextField)f).setInputPrompt("City");
            } else if("email".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Fx. my@address.net");
            } /*else if("bankAccount".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Your bank account number");
            }*/

            if(f instanceof TextField) {
                ((TextField)f).setNullRepresentation("");
            }

            return f;
        }
    }

    public OrganisationForm() {
        // Create our layout (3x3 GridLayout)
        ourLayout = new GridLayout(3, 9);

        // Use top-left margin and spacing
        ourLayout.setMargin(true, false, false, true);
        ourLayout.setSpacing(true);

        setLayout(ourLayout);

        setFormFieldFactory(new FormFieldFactory());
        
        // Set up buffering
        setWriteThrough(false); // we want explicit 'apply'
        setInvalidCommitted(false); // no invalid values in datamodel

        // Determines which properties are shown, and in which order:
        /*setItem(Arrays.asList(new String[]{
                    "name", "road", "zipCode", "city", "countryCode", "phoneNo", "faxNo",
                    "email", "web", "cvr", "bankAccount"}));
*/

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
        lblVatNo.setSizeUndefined();
        ourLayout.addComponent(lblVatNo, 0, 6);
        ourLayout.setComponentAlignment(lblVatNo, Alignment.MIDDLE_RIGHT);
        //lblBankAccount.setSizeUndefined();
        //ourLayout.addComponent(lblBankAccount, 0, 7);
        //ourLayout.setComponentAlignment(lblBankAccount, Alignment.MIDDLE_RIGHT);
        ourLayout.addComponent(btnSave, 1, 7);

        btnSave.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                commit();
                Organisation org = ((BeanItem<Organisation>)getItemDataSource()).getBean();
                service.updateOrganisation(org);
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
        } else if (propertyId.equals("address")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 1, 2, 1);
        } else if (propertyId.equals("zip")) {
            ourLayout.addComponent(field, 1, 2);
        } else if (propertyId.equals("city")) {
            ourLayout.addComponent(field, 2, 2);
        } else if (propertyId.equals("country")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 3, 2, 3);
        } else if (propertyId.equals("telephone")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 4, 2, 4);
        } else if (propertyId.equals("email")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 5, 2, 5);
        } else if (propertyId.equals("company_reg")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 6, 2, 6);
        }/* else if (propertyId.equals("bankAccount")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 7, 2, 7);
        }*/
    }

    public void setService(OrganisationService service) {
        this.service = service;
        
    }

    @Override
    @Deprecated
    public void setItemDataSource(Item newDataSource) {
        super.setItemDataSource(newDataSource, Arrays.asList(new String[]{
                    "name", "address", "zip", "city", "country", "telephone",
                    "email", "company_reg"}));
    }

    @Override
    public void attach() {
        if(service!=null) {
            BeanItem<Organisation> item = new BeanItem<Organisation>(service.readOrganisation());
            super.setItemDataSource(item, Arrays.asList(new String[]{
                        "name", "address", "zip", "city", "country", "telephone",
                        "email", "company_reg"}));
        }
    }



}
