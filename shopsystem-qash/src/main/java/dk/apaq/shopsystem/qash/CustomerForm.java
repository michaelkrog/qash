package dk.apaq.shopsystem.qash;

import dk.apaq.shopsystem.qash.settings.*;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.util.Country;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author krog
 */
public class CustomerForm extends Form {
    
    private static final List<String> PROPERTIES = Arrays.asList(new String[]{
                    "companyName", "contactName", "street", "postalCode", "city", "country", "telephone",
                    "email", "companyRegistration", "bankAccount", "websiteUrl", "birthDay"});

    private TabSheet tabSheet = new TabSheet();
    private VerticalLayout outerLayout = new VerticalLayout();
    private HorizontalLayout buttonLayout = new HorizontalLayout();
    private GridLayout generalLayout;
    private GridLayout miscLayout;
    private Label lblName = new Label("Company Name");
    private Label lblAtt = new Label("Att.");
    private Label lblAddress = new Label("Address");
    private Label lblEmail = new Label("Email");
    private Label lblPhone = new Label("Phone");
    private Label lblVatNo = new Label("VatNo.");
    private Label lblBankAccount = new Label("Bank");
    private Label lblWebsite = new Label("Website");
    private Label lblBirthday = new Label("Birthday");
    private Button btnSave = new Button("Save");
    


    protected class FormFieldFactory extends DefaultFieldFactory {

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

            if("companyName".equals(propertyId)) {
                f.addValidator(new StringLengthValidator("Name should be at least one character.", 1, 30, true));
                ((TextField)f).setInputPrompt("Name of Shop");
            } else if("street".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Street and houseno.");
            } else if("postalCode".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Postal Code");
            } else if("city".equals(propertyId)) {
                ((TextField)f).setInputPrompt("City");
            } else if("email".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Fx. my@address.net");
            } else if("bankAccount".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Your bank account number");
            } else if("websiteUrl".equals(propertyId)) {
                ((TextField)f).setInputPrompt("Your bank account number");
            }

            if(f instanceof TextField) {
                ((TextField)f).setNullRepresentation("");
            }

            return f;
        }
    }

    public CustomerForm() {
        
        // Create generalt layout (3x3 GridLayout)
        generalLayout = new GridLayout(3, 6);
        miscLayout = new GridLayout(2,5);

        // Use top-left margin and spacing
        generalLayout.setMargin(true, false, false, true);
        generalLayout.setSpacing(true);
        miscLayout.setMargin(true, false, false, true);
        miscLayout.setSpacing(true);

        tabSheet.addTab(generalLayout, "General");
        tabSheet.addTab(miscLayout, "Miscellaneous");
        
        tabSheet.setSizeFull();
        tabSheet.setStyleName(Reindeer.TABLE_BORDERLESS);
        
        outerLayout.addComponent(tabSheet);
        outerLayout.addComponent(buttonLayout);
        outerLayout.setSizeFull();
        outerLayout.setExpandRatio(tabSheet, 1.0F);
        outerLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
        
        buttonLayout.addComponent(btnSave);
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(true);
        
        setLayout(outerLayout);
        setSizeFull();

        setFormFieldFactory(new FormFieldFactory());
        
        // Set up buffering
        setWriteThrough(false); // we want explicit 'apply'
        setInvalidCommitted(false); // no invalid values in datamodel

        
        addLabel(generalLayout, lblName, 0, 0);
        addLabel(generalLayout, lblAtt, 0, 1);
        addLabel(generalLayout, lblAddress, 0, 2);
        addLabel(generalLayout, lblVatNo, 0, 5);
        
        addLabel(miscLayout, lblPhone, 0, 0);
        addLabel(miscLayout, lblEmail, 0, 1);
        addLabel(miscLayout, lblBankAccount, 0, 2);
        addLabel(miscLayout, lblWebsite, 0, 3);
        addLabel(miscLayout, lblBirthday, 0, 4);
        
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
        if (propertyId.equals("companyName")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            generalLayout.addComponent(field, 1, 0, 2, 0);
        } else if (propertyId.equals("contactName")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            generalLayout.addComponent(field, 1, 1, 2, 1);
        } else if (propertyId.equals("street")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            generalLayout.addComponent(field, 1, 2, 2, 2);
        } else if (propertyId.equals("postalCode")) {
            generalLayout.addComponent(field, 1, 3);
        } else if (propertyId.equals("city")) {
            generalLayout.addComponent(field, 2, 3);
        } else if (propertyId.equals("country")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            generalLayout.addComponent(field, 1, 4, 2, 4);
        } else if (propertyId.equals("companyRegistration")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            generalLayout.addComponent(field, 1, 5, 2, 5);
        } else if (propertyId.equals("telephone")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            miscLayout.addComponent(field, 1, 0);
        } else if (propertyId.equals("email")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            miscLayout.addComponent(field, 1, 1);
        } else if (propertyId.equals("bankAccount")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            miscLayout.addComponent(field, 1, 2);
        } else if (propertyId.equals("websiteUrl")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            miscLayout.addComponent(field, 1, 3);
        } else if (propertyId.equals("birthDay")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            miscLayout.addComponent(field, 1, 4);
        } else {
            return;
        }
        
        field.setTabIndex(PROPERTIES.indexOf(propertyId) + 1);
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        super.setItemDataSource(newDataSource, PROPERTIES);
    }
    
    private void addLabel(GridLayout layout, Label label, int x, int y) {
        label.setSizeUndefined();
        layout.addComponent(label, x, y);
        layout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
        
    }


}
