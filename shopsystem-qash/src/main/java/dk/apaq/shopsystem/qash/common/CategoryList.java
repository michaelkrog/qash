package dk.apaq.shopsystem.qash.common;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Method;

/**
 * A list of categorized items. This can be made to imitate the look of the iTunes navigation bar.
 * @author michael
 */
public class CategoryList extends CustomComponent {

    private VerticalLayout layout = new VerticalLayout();
    private ButtonListener buttonListener = new ButtonListener();
    private Button lastSelected = null;

    /**
     * Listener for button clicks.
     */
    private class ButtonListener implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
            changeSelection(event.getButton());
        }
    }

    /**
     * Creates a new instance of CategoryList.
     */
    public CategoryList() {
        setCompositionRoot(layout);

        setStyleName("v-categorylist");
    }

    /**
     * Adds a new category.
     * @param name The name of the category.
     */
    public void addCategory(String name) {
        Label label = new Label(name);
        layout.addComponent(label);
    }

    /**
     * Adds a new item to the last added category.
     * @param name The caption of the new item. The caption will also be the data of the item.
     */
    public void addItem(String name) {
        this.addItem(name, name);
    }

    /**
     * Adds a new item to the last added category.
     * @param name The caption of the new item.
     * @param data The data of the new item.
     */
    public void addItem(String name, String data) {
        Button button = new NativeButton(name);
        button.setData(data);
        button.addListener(buttonListener);
        button.setWidth("100%");
        layout.addComponent(button);
    }

    /**
     * Selects the item which has the specified data.
     */
    public void select(String data) {
        for (int i = 0; i < layout.getComponentCount(); i++) {
            Component c = layout.getComponent(i);
            if (c instanceof Button) {
                Button b = (Button) c;
                if (data.equals(b.getData())) {
                    changeSelection(b);
                    return;
                }
            }
        }
    }

    /**
     * Adds a new <code>SelectListener</code> to the list.
     */
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
    /**
     * The listener used for the <code>CategoryList</code> events. 
     */
    public interface SelectListener {

        public void onSelect(SelectEvent event);
    }

    public class SelectEvent extends Component.Event {

        private final String selection;

        public SelectEvent(Component source, String selection) {
            super(source);
            this.selection = selection;
        }

        /**
         * Returns the selection performed in the CategoryList. This string returned is the data
         * specified for the item in the CategoryList.
         * @return
         */
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
