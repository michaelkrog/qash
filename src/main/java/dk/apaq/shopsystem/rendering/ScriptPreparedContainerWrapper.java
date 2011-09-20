package dk.apaq.shopsystem.rendering;

import org.apache.wicket.MarkupContainer;

/**
 *
 * @author michael
 */
public class ScriptPreparedContainerWrapper {
    
    private final MarkupContainer container;

    public ScriptPreparedContainerWrapper(MarkupContainer container) {
        this.container = container;
    }
    
    public void addComponent(org.apache.wicket.Component component) {
        this.container.add(component);
    }
}
