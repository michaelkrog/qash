package dk.apaq.shopsystem.rendering.simplescript;

import org.apache.wicket.MarkupContainer;

/**
 *
 * @author michael
 */
public class SimpleScriptContainerWrapper {
    
    private final MarkupContainer container;

    public SimpleScriptContainerWrapper(MarkupContainer container) {
        this.container = container;
    }
    
    public void addComponent(org.apache.wicket.Component component) {
        this.container.add(component);
    }
}
