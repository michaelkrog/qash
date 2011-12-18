package dk.apaq.shopsystem.qash.listeners;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 *
 * @author michael
 */
public class RemoveSelectedOnClickListener implements ClickListener {
    
    private final AbstractSelect select;

    public RemoveSelectedOnClickListener(AbstractSelect select) {
        this.select = select;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        Object id = select.getValue();
        if(id!=null) {
            select.removeItem(id);
        }
    }
    
    
    
}
