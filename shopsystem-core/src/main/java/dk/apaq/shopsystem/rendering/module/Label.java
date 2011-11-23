package dk.apaq.shopsystem.rendering.module;

/**
 *
 * @author michael
 */
public class Label extends CmsModule {

    public Label(String id) {
        super(id);
    }

    @Override
    public void compose() {
        add(new org.apache.wicket.markup.html.basic.Label("text", parameters.get("text").getString()));
    }
    
    
    
}
