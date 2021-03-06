package dk.apaq.shopsystem.qash.settings;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.qash.common.CommonDialog;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.qash.validators.AvailableUsernameValidator;
import dk.apaq.shopsystem.qash.validators.RepeatFieldValidator;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemServiceHolder;
import dk.apaq.shopsystem.site.form.AccountFormBean;
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
    private OrganisationService organsiationService;

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
            layout.setMargin(true);
            layout.addComponent(lblDisplayName, 0, 0);
            layout.addComponent(lblName, 0, 1);
            layout.addComponent(lblPassword, 0, 2);
            layout.addComponent(lblPasswordRepeat, 0, 3);
            layout.addComponent(txtDisplayName, 1, 0);
            layout.addComponent(txtName, 1, 1);
            layout.addComponent(txtPassword, 1, 2);
            layout.addComponent(txtPasswordRepeat, 1, 3);
            
            txtDisplayName.addValidator(new StringLengthValidator("Must be at least 3 characters and no longer than 30.", 3, 30, false));
            
            txtName.addValidator(new StringLengthValidator("Must be at least 3 characters long and no longer than 30.", 3, 30, false));
            txtName.addValidator(new AvailableUsernameValidator("Username not available."));
            
            txtPassword.addValidator(new StringLengthValidator("Must be at least 5 characters long and no longer than 16.", 3, 30, false));
            txtPasswordRepeat.addValidator(new RepeatFieldValidator(txtPassword, "Password fields are not equal."));
            
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
                final Form form = new Form();
                final UserBean userBean = new UserBean();
                BeanItem accountItem = new BeanItem(userBean);
                form.setItemDataSource(accountItem);
                form.getField("displayName").addValidator(new StringLengthValidator("Must be at least 3 characters and no longer than 30.", 3, 30, false));
                form.getField("name").addValidator(new StringLengthValidator("Must be at least 3 characters long and no longer than 30.", 3, 30, false));
                form.getField("name").addValidator(new AvailableUsernameValidator("Username not available."));
                form.getField("password").addValidator(new StringLengthValidator("Must be at least 5 characters long and no longer than 16.", 3, 30, false));
                form.getField("repeatedPassword").addValidator(new RepeatFieldValidator(form.getField("password"), "Password fields are not equal."));

                //final CreateUserWindow cuw = new CreateUserWindow();
                final CommonDialog dialog = new CommonDialog("Opret ny bruger", form);
                dialog.setModal(true);
                dialog.setWidth(300, UNITS_PIXELS);
                dialog.setButtonCaption(CommonDialog.ButtonType.Ok, "Opret bruger");
                dialog.setButtonCaption(CommonDialog.ButtonType.Cancel, "Annuller");
                getApplication().getMainWindow().addWindow(dialog);
                
                
                dialog.addListener(new Window.CloseListener() {

                    
                    @Override
                    public void windowClose(CloseEvent e) {
                        if(dialog.getResult() == CommonDialog.ButtonType.Ok) {
                            try{
                                form.commit();
                            } catch(InvalidValueException ex) {
                                getApplication().getMainWindow().addWindow(dialog);
                                return;
                            }
                            
                            SystemUser user = new SystemUser();
                            user.setName(userBean.getName());
                            user.setDisplayName(userBean.getDisplayName());
                            user.setPassword(userBean.getPassword());
                            String id = SystemServiceHolder.getSystemService().getSystemUserCrud().create(user);
                            user = SystemServiceHolder.getSystemService().getSystemUserCrud().read(id);
                            
                            OrganisationUserReference reference = new OrganisationUserReference(user);
                            Crud.Complete<String, OrganisationUserReference> users = organsiationService.getUsers();
                            users.create(reference);
                            
                        }
                    }
                });
            }
        });

        btnRemoveUser.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                String id = (String) userList.getValue();
                organsiationService.getUsers().delete(id);
            }
        });

    }

    public void setOrganisationService(OrganisationService organsiationService) {
        this.organsiationService = organsiationService;
    
        this.userList.setContainerDataSource(new CrudContainer(organsiationService.getUsers(), OrganisationUserReference.class));
    }


}
