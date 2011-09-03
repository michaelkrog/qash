package dk.apaq.shopsystem.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Method;

/**
 *
 * @author michael
 */
public class CategoryList extends CustomComponent {

    private VerticalLayout layout = new VerticalLayout();
    private ButtonListener buttonListener = new ButtonListener();
    private Button lastSelected = null;

    private class ButtonListener implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            changeSelection(event.getButton());
        }
    }

    public CategoryList() {
        setCompositionRoot(layout);

        setStyleName("v-categorylist");
    }

    public void addCategory(String name) {
        Label label = new Label(name);
        layout.addComponent(label);
    }

    public void addItem(String name) {
        this.addItem(name, name);
    }

    public void addItem(String name, String data) {
        Button button = new NativeButton(name);
        button.setData(data);
        button.addListener(buttonListener);
        button.setWidth("100%");
        layout.addComponent(button);
    }

    public void select(String name) {
        for (int i = 0; i < layout.getComponentCount(); i++) {
            Component c = layout.getComponent(i);
            if (c instanceof Button) {
                Button b = (Button) c;
                if (name.equals(b.getData())) {
                    changeSelection(b);
                    return;
                }
            }
        }
    }

    public void addListener(SelectListener listener) {
        addListener(SelectEvent.class, listener, SELECT_METHOD);
    }

    private void fireSelection(String selection) {
        fireEvent(new SelectEvent(this, selection));
    }

    private void changeSelection(Button button) {
        if (lastSelected != null) {
            lastSelected.removeStyleName("selected");
        }

        button.addStyleName("selected");
        fireSelection((String) button.getData());
        lastSelected = button;
    }

    /**Event Handling **/
    public interface SelectListener {

        public void onSelect(SelectEvent event);
    }

    public class SelectEvent extends Component.Event {

        private final String selection;

        public SelectEvent(Component source, String selection) {
            super(source);
            this.selection = selection;
        }

        public String getSelection() {
            return selection;
        }
    }
    /* Click event */
    private static final Method SELECT_METHOD;

    static {
        try {
            SELECT_METHOD = SelectListener.class.getDeclaredMethod(
                    "onSelect", new Class[]{SelectEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in CategoryList");
        }
    }
}
