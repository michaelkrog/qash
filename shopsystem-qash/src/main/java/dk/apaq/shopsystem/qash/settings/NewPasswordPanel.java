package dk.apaq.shopsystem.qash.settings;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;

/**
 *
 * @author michael
 */
public class NewPasswordPanel extends Form {
    
    
    private class PasswordRepeatBean {
    
        private String password1;
        private String password2;

        public String getPassword1() {
            return password1;
        }

        public void setPassword1(String password1) {
            this.password1 = password1;
        }

        public String getPassword2() {
            return password2;
        }

        public void setPassword2(String password2) {
            this.password2 = password2;
        }
        
        
    }
    
    public NewPasswordPanel() {
    
        PasswordRepeatBean bean = new PasswordRepeatBean();
        Item item = new BeanItem(bean);
        setItemDataSource(item);
        
        
    }


    
    
}
