package dk.apaq.shopsystem.qash;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import dk.apaq.shopsystem.qash.settings.SettingsDialogForAdmin;

/**
 *
 * @author krog
 */
public interface SiteHeader extends Component {

    //SettingsDialog getSettingsDialog();

    void addLink(Link link);
    void addButton(Button button);
}
