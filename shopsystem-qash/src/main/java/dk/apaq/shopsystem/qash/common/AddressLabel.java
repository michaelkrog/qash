package dk.apaq.shopsystem.qash.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Address;
import dk.apaq.shopsystem.entity.ContactInformation;
import dk.apaq.shopsystem.util.AddressUtil;
import java.util.Locale;

/**
 *
 * @author krog
 */
public class AddressLabel extends CustomComponent {
    
    private final VerticalLayout layout = new VerticalLayout();
    private final Label[] lbls = new Label[] {new Label(),new Label(),new Label(),new Label(),new Label()};
    private ContactInformation contactInformation;
    private String nullRepresentation = "";
    
    public AddressLabel() {
        setStyleName("v-addresslabel");
    
        for(Label lbl : lbls) {
            layout.addComponent(lbl);
        }
        
        setCompositionRoot(layout);
        update();
    }
    
    public void setAddress(ContactInformation ci) {
        this.contactInformation = ci;
        update();
    }
    
    private void update() {
        if(getApplication()==null) return;
        
        for(Label lbl : lbls) {
            lbl.setValue(null);
        }
        
        if(this.contactInformation == null) {
            lbls[0].setValue(this.nullRepresentation);
        } else {
            lbls[0].setValue(this.contactInformation.getCompanyName());

            String addressFormatted = AddressUtil.formatAddress(this.contactInformation, getLocale(), "\n");
            String[] addressSplit = addressFormatted.split("\n");
            for(int i = 0;i<addressSplit.length && i<lbls.length-1; i++) {
                lbls[i+1].setValue(addressSplit[i]);
            }
        }
    }

    @Override
    public void attach() {
        super.attach();
        update();
    }
    
    public void setNullRepresentation(String text) {
        this.nullRepresentation = text;
    }
    
    
}
