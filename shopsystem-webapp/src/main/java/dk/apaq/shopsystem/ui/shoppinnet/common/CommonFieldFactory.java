/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui.shoppinnet.common;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;

/**
 *
 * @author Martin Christensen
 */
public class CommonFieldFactory extends DefaultFieldFactory {
    
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        
        // Just removing captions from all fields, after getting field from super class
        Field f = super.createField(item, propertyId, uiContext);
        f.setCaption(null);
        f.setWidth("150px");
        
        return f;
    }
            
}
