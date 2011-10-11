/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Martin Christensen
 */
public class PageHeader extends CustomComponent {
    
    private VerticalLayout content = new VerticalLayout();
    
    public PageHeader(String headerText) {
        
        Label label = new Label(headerText);
        label.setStyleName("v-pageheader");
        
        this.content.addComponent(label);
        
        setCompositionRoot(this.content);
        
    }
    
}
