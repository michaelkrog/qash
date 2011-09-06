package dk.apaq.shopsystem.ui.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Editor;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.ui.util.Spacer;
import dk.apaq.shopsystem.util.CurrencyUtil;
import java.util.Currency;
import java.util.List;

/**
 *
 * @author michael
 */
public class UserManagerPanel extends CustomComponent implements Editor {

    private final VerticalLayout outerLayout = new VerticalLayout();
    private final HorizontalLayout innerLayout = new HorizontalLayout();
    private final HorizontalLayout bottomLayout = new HorizontalLayout();
    private final VerticalLayout settingsWrapper = new VerticalLayout();
    private final Spacer settingsLayoutSpacer = new Spacer();
    private final Spacer defaultSettingsLayoutSpacer = new Spacer();
    private final ListSelect userList = new ListSelect();
    private final UserForm userForm = new UserForm();
    private final Button btnNewUser = new Button("New user");
    private final Button btnRemoveUser = new Button("Remove user");
    private Container userContainer;

    private class ListSelectionHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            Object id = event.getProperty().getValue();
            if (id == null) {
                userForm.setEnabled(false);
            } else {
                userForm.setEnabled(true);
                Item item = userList.getItem(id);
                userForm.setItemDataSource(item);
            }
        }
    }

    public UserManagerPanel() {

        setStyleName("v-usermanagerpanel");
        settingsWrapper.setStyleName("v-usermanagerpanel-settings-wrapper");

        innerLayout.addComponent(userList);
        innerLayout.addComponent(settingsWrapper);
        innerLayout.setExpandRatio(settingsWrapper, 1.0F);

        bottomLayout.addComponent(btnNewUser);
        bottomLayout.addComponent(btnRemoveUser);
        bottomLayout.addComponent(defaultSettingsLayoutSpacer);
        bottomLayout.setExpandRatio(defaultSettingsLayoutSpacer, 1.0F);
        bottomLayout.setSpacing(true);
        bottomLayout.setWidth(100, UNITS_PERCENTAGE);

        userForm.setEnabled(false);
        userForm.setItemDataSource(new BeanItem(new Tax()));

        settingsWrapper.addComponent(userForm);
        settingsWrapper.addComponent(settingsLayoutSpacer);
        settingsWrapper.setExpandRatio(settingsLayoutSpacer, 1.0F);

        outerLayout.addComponent(innerLayout);
        outerLayout.addComponent(bottomLayout);
        outerLayout.setExpandRatio(innerLayout, 1.0F);

        outerLayout.setMargin(true);
        outerLayout.setSpacing(true);
        innerLayout.setSpacing(true);

        userList.setNewItemsAllowed(false);
        userList.setNullSelectionAllowed(false);
        userList.setItemCaptionPropertyId("name");
        userList.setImmediate(true);
        userList.setContainerDataSource(userContainer);

        setCompositionRoot(outerLayout);

        userList.setWidth(200, UNITS_PIXELS);
        userList.setHeight(100, UNITS_PERCENTAGE);
        settingsWrapper.setSizeFull();
        innerLayout.setSizeFull();
        outerLayout.setSizeFull();
        setSizeFull();

        userList.addListener(new ListSelectionHandler());

        btnNewUser.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = userContainer.addItem();
                Item item = userContainer.getItem(id);
                item.getItemProperty("name").setValue("Unnamed user");
                if (item instanceof Buffered) {
                    ((Buffered) item).commit();
                }
                userList.setValue(id);
            }
        });

        btnRemoveUser.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = userList.getValue();
                if (id == null) {
                    return;
                }
                userContainer.removeItem(id);
            }
        });

    }

    public void setContainerDataSource(Container newDataSource) {

        this.userContainer = newDataSource;
        userList.setContainerDataSource(newDataSource);

    }

    public Container getContainerDataSource() {
        return userContainer;
    }


}
