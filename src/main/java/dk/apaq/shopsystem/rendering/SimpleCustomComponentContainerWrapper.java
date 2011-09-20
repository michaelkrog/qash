package dk.apaq.shopsystem.rendering;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

/**
 *
 */
public class SimpleCustomComponentContainerWrapper {

    private final MarkupContainer container;

    public SimpleCustomComponentContainerWrapper(MarkupContainer container) {
        this.container = container;
    }

    public void addComponent(Component component) {
        container.add(component);
    }
}
