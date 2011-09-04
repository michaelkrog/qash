package dk.apaq.shopsystem.ui.settings;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Editor;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author michael
 */
public class UsersPanel extends CustomComponent implements Editor {

    private final VerticalLayout outerLayout = new VerticalLayout();

    public UsersPanel() {
        setCompositionRoot(outerLayout);
    }
    
    
    @Override
    public void setContainerDataSource(Container newDataSource) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Container getContainerDataSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
