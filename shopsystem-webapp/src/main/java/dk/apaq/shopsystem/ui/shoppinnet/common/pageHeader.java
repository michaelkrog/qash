package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;


public class PageHeader extends CustomComponent {
    
    public PageHeader(String name) {
        
        Label label = new Label(name);
        label.setWidth("100%");
        label.setStyleName("v-pageheader");
        
        setCompositionRoot(label);
    }
}
