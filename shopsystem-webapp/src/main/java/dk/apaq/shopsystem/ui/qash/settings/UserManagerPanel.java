package dk.apaq.shopsystem.ui.qash.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Editor;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import dk.apaq.shopsystem.entity.BaseUser;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.crud.UserCrud;
import dk.apaq.shopsystem.ui.VaadinServiceHolder;
import dk.apaq.shopsystem.ui.common.CommonDialog;
import dk.apaq.shopsystem.ui.common.Spacer;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;

/**
 *
 * @author michael
 */
public class UserManagerPanel extends CustomComponent {

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
    private OrganisationService service;

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
    
    private class CreateUserWindow extends CustomComponent {

        private GridLayout layout = new GridLayout(2, 4);
        private Label lblDisplayName = new Label("Fulde navn:");
        private Label lblName = new Label("Kontonavn");
        private Label lblPassword = new Label("Adgangskode");
        private Label lblPasswordRepeat = new Label("Bekræft");
        private TextField txtDisplayName = new TextField();
        private TextField txtName = new TextField();
        private PasswordField txtPassword = new PasswordField();
        private PasswordField txtPasswordRepeat = new PasswordField();
        
        public CreateUserWindow() {
            layout.addComponent(lblDisplayName, 0, 0);
            layout.addComponent(lblName, 0, 1);
            layout.addComponent(lblPassword, 0, 2);
            layout.addComponent(lblPasswordRepeat, 0, 3);
            layout.addComponent(txtDisplayName, 1, 0);
            layout.addComponent(txtName, 1, 1);
            layout.addComponent(txtPassword, 1, 2);
            layout.addComponent(txtPasswordRepeat, 1, 3);
            
            setCompositionRoot(layout);
        }
        
        public String getDisplayName() {
            return (String) txtDisplayName.getValue();
        }
        
        public String getName() {
            return (String) txtName.getValue();
        }
        
        public String getPassword() {
            return (String) txtPassword.getValue();
        }
        
        public boolean isPasswordsEqual() {
            return txtPassword.getValue().equals(txtPasswordRepeat.getValue());
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
                final CreateUserWindow cuw = new CreateUserWindow();
                final CommonDialog dialog = new CommonDialog("Opret ny bruger", cuw);
                dialog.setModal(true);
                dialog.setWidth(300, UNITS_PIXELS);
                dialog.setButtonCaption(CommonDialog.ButtonType.Ok, "Opret bruger");
                dialog.setButtonCaption(CommonDialog.ButtonType.Cancel, "Annuller");
                getApplication().getMainWindow().addWindow(dialog);
                dialog.addListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(CloseEvent e) {
                        if(dialog.getResult() == CommonDialog.ButtonType.Ok) {
                            UserCrud users = service.getUsers();
                            String id = users.createSystemUser();
                            SystemUser user = (SystemUser) users.read(id);
                            user.setDisplayname(cuw.getDisplayName());
                            user.setName(cuw.getName());
                            user.setPassword(cuw.getPassword());
                            users.update(user);
                        }
                    }
                });
            }
        });

        btnRemoveUser.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                String id = (String) userList.getValue();
                service.getUsers().delete(id);
            }
        });

    }

    @Override
    public void attach() {
        this.service = VaadinServiceHolder.getService(getApplication());
        this.userList.setContainerDataSource(new CrudContainer(service.getUsers(), BaseUser.class));
    }


}
