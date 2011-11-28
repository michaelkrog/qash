package dk.apaq.shopsystem.qash;

import com.vaadin.Application;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author michaelzachariassenkrog
 */
public class RegisterApplication extends Application {

    private RegisterPanel registerPanel = new RegisterPanel();
    private VerticalLayout outerLayout = new VerticalLayout();

    @Override
    public void init() {
        Window window = new Window();
        setMainWindow(window);

        window.setContent(outerLayout);
        window.addComponent(registerPanel);
    }

}
