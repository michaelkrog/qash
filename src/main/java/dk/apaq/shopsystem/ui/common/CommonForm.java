package dk.apaq.shopsystem.ui.common;

import com.vaadin.data.Container;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a grid combined with common functionality for listing various data
 * 
 * @author Martin Christensen
 */

public class CommonForm extends CustomComponent implements Container.Viewer {
    
    private Container data;
    private List header = new ArrayList();
    private List headerType = new ArrayList();
    private List field = new ArrayList();
    //private Form form = new Form();
    //private VerticalLayout content = new VerticalLayout();
    private Window window = new Window();
    
    
    public void addHeader(String value, String type) {
        this.header.add(value);
        this.headerType.add(type);
    }

    public void addField(String value) {
	this.field.add(value);
    }

    @Override
    public Container getContainerDataSource() {
        return this.data;
    }

    
    @Override
    public void setContainerDataSource(Container data) {
        this.data = data;
        ///form.setContainerDataSource(data);
    }
    
    
    @Override
    public void attach() {
        
        // Create layout
        setStyleName("v-orderlist");
        
        //form.setSizeFull();
        
        //form.addField("test");
        //form.addField("test2");
        
        
        // Create modal window
        this.window.setModal(true);
        getApplication().getMainWindow().addWindow(this.window);
        //this.window.addComponent(form);
        

        
    }
    
    
    public CommonForm() {
        
        // Define layout root
        setCompositionRoot(this.window);
    }
        
}
