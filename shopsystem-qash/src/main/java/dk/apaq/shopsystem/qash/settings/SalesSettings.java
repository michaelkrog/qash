package dk.apaq.shopsystem.qash.settings;

import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.sequence.SequenceProcessor;

/**
 *
 * @author michael
 */
public class SalesSettings extends CustomComponent {

    private GridLayout ourLayout = new GridLayout(2, 5);
    private Label lbl_note = new Label("Note on receipt");
    private TextArea txt_note = new TextArea();
    private Label lbl_orderSequence = new Label("Next orderno.");
    private TextField txt_orderSequence = new TextField();
    private Label lbl_invoiceSequence = new Label("Next invoiceno.");
    private TextField txt_invoiceSequence = new TextField();
    private Label lbl_timelyPayment = new Label("Payment period in days");
    private TextField txt_timelyPayment = new TextField();
    private Button btn_save = new Button("Save");
    private OrganisationService organisationService;
    private SequenceValidator orderSequenceValidator;
    private SequenceValidator invoiceSequenceValidator;
    
    private class SequenceValidator implements Validator {
        private SequenceProcessor processor;

        public SequenceValidator(SequenceProcessor processor) {
            this.processor = processor;
        }

        @Override
        public void validate(Object value) throws InvalidValueException {
            try {
                long number = Long.parseLong((String)value);
                long next = processor.getNext();
                if(next>number) {
                    throw new InvalidValueException("New number must be a greater than " + next);
                }
            } catch(NumberFormatException ex) {
                throw new InvalidValueException(ex.getMessage());
            }
        }

        @Override
        public boolean isValid(Object value) {
            try{
                validate(value);
                return true;
            } catch(InvalidValueException ex) {
                return false;
            }
        }
        
        
    }
    
    public SalesSettings() {
        
        ourLayout.addComponent(lbl_orderSequence, 0, 0);
        ourLayout.addComponent(txt_orderSequence, 1, 0);
        ourLayout.addComponent(lbl_invoiceSequence, 0, 1);
        ourLayout.addComponent(txt_invoiceSequence, 1, 1);
        ourLayout.addComponent(lbl_timelyPayment, 0, 2);
        ourLayout.addComponent(txt_timelyPayment, 1, 2);
        ourLayout.addComponent(lbl_note, 0, 3);
        ourLayout.addComponent(txt_note, 1, 3);
        ourLayout.addComponent(btn_save, 0, 4);
        ourLayout.setMargin(true, false, false, true);
        ourLayout.setSpacing(true);
        //ourLayout.setWidth(400, Component.UNITS_PIXELS);
        ourLayout.setColumnExpandRatio(1, 1.0F);
        
        txt_note.setInputPrompt("Note at bottom of receipts");
        txt_note.setColumns(25);
        txt_note.setNullRepresentation("");
        
        btn_save.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(txt_invoiceSequence.isValid()) {
                    Long next = Long.parseLong((String) txt_invoiceSequence.getValue());
                    organisationService.getInvoiceSequence().setNext(next);
                }
                
                if(txt_orderSequence.isValid()) {
                    Long next = Long.parseLong((String) txt_orderSequence.getValue());
                    organisationService.getOrderSequence().setNext(next);
                }
                
                if(txt_timelyPayment.isValid()) {
                    Integer paymentperiod  = Integer.parseInt((String)txt_timelyPayment.getValue());
                    Organisation org = organisationService.readOrganisation();
                    org.setDefaultPaymentPeriodInDays(paymentperiod);
                    organisationService.updateOrganisation(org);
                }
                
                Organisation org = organisationService.readOrganisation();
                org.setAnnexNote((String)txt_note.getValue());
                organisationService.updateOrganisation(org);
            }
        });
        setCompositionRoot(ourLayout);
    }
    
    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
        update();
    }

    @Override
    public void attach() {
        super.attach();
        update();
    }
    
    private void update() {
        if(organisationService!=null) {
            Organisation org = organisationService.readOrganisation();
            txt_invoiceSequence.setValue(Long.toString(organisationService.getInvoiceSequence().getNext()));
            txt_orderSequence.setValue(Long.toString(organisationService.getOrderSequence().getNext()));
            txt_timelyPayment.setValue(Integer.toString(org.getDefaultPaymentPeriodInDays()));
            txt_note.setValue(org.getAnnexNote());
            
            if(invoiceSequenceValidator!=null) {
                txt_invoiceSequence.removeValidator(invoiceSequenceValidator);
            }
            
            if(orderSequenceValidator!=null) {
                txt_orderSequence.removeValidator(orderSequenceValidator);
            }
            
            invoiceSequenceValidator = new SequenceValidator(organisationService.getInvoiceSequence());
            txt_invoiceSequence.addValidator(invoiceSequenceValidator);

            orderSequenceValidator = new SequenceValidator(organisationService.getOrderSequence());
            txt_orderSequence.addValidator(orderSequenceValidator);

        }
    }
    
    
    
    
}
