package dk.apaq.shopsystem.ui.shoppinnet;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeButton;


public class Header extends CustomComponent {

    private CssLayout layout = new CssLayout();
    private CssLayout logoColumn = new CssLayout();
    private CssLayout linkColumn1 = new CssLayout();
    private CssLayout linkColumn2 = new CssLayout();
    private CssLayout linkColumn3 = new CssLayout();
    private Resource homeResource = new ExternalResource("/");
    private Resource helpResource = new ExternalResource("http://help.qashapp.com");
    private Resource logoResource = new ExternalResource("../images/ge-logo.png");
    private Embedded logo = new Embedded(null, logoResource);
    private Link linkAccount = new Link("Your account", new ExternalResource("../account.jsp"));
    private Link linkStores = new Link("Your dashboard", new ExternalResource("../dashboard.jsp"));
    private Button linkImportExport = new Button("Import/Export");
    private Link linkHelp = new Link("Help & Support", helpResource, "Help", 500, 500, Link.TARGET_BORDER_MINIMAL);
    private Link linkLogout = new Link("Log out", new ExternalResource("/logout"));
    private Link linkContact = new Link("info@shoppinnet.com", new ExternalResource("../contact.jsp"));



    public Header() {


        layout.addStyleName("toolbar-invert");
        layout.setWidth("100%");
        
        CssLayout right = new CssLayout();
        right.setSizeUndefined();
        right.addStyleName("right");
        layout.addComponent(right);
        Label text = new Label("Shopping is nothing... without you!");
        right.addComponent(text);
        
        CssLayout left = new CssLayout();
        left.setSizeUndefined();
        left.addStyleName("left");
        layout.addComponent(left);

        Label title = new Label("ShoppinNet");
        title.addStyleName("h1");
        left.addComponent(title);
        
        Button b = new Button("Action");
        b = new NativeButton("Dashboard");
        b.setIcon(new ThemeResource("../runo/icons/16/globe.png"));
        left.addComponent(b);
        
        b = new NativeButton("Support");
        b.setIcon(new ThemeResource("../runo/icons/16/email.png"));
        left.addComponent(b);
        
        b = new NativeButton("QASH Register Edition");
        b.setIcon(new ThemeResource("../runo/icons/16/reload.png"));
        left.addComponent(b);

        setCompositionRoot(layout);

        logo.addListener(new ClickListener() {

            @Override
            public void click(ClickEvent event) {
                getApplication().getMainWindow().open(homeResource);
            }
        });

       

    }


    
}
