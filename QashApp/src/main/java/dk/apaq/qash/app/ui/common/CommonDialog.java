/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.ui.common;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CommonDialog extends Window  {

    private Map<ButtonType, Button> buttonMap = new HashMap<ButtonType, Button>();
    private HorizontalLayout buttonLayout = new HorizontalLayout();
    private VerticalLayout layout = new VerticalLayout();
    private Spacer spacer = new Spacer();
    private ButtonType result;
    private final ButtonListener listener = new ButtonListener();
    private ButtonType defaultButtonType;

    public enum ButtonType {
        Cancel, Ok, Close;
    }

    private class ButtonListener implements Button.ClickListener {

        public void buttonClick(ClickEvent event) {
            for(Map.Entry<ButtonType, Button> entry : buttonMap.entrySet()) {
                if(entry.getValue() == event.getButton()) {
                    result = entry.getKey();
                    close();;
                }
            }
        }

    }

    public CommonDialog(String caption, Component component) {
        this(caption, component, ButtonType.Cancel, ButtonType.Ok);
    }

    public CommonDialog(String caption, Component component, ButtonType ... buttons) {
        setCaption(caption);
        setModal(true);

        buttonMap.put(ButtonType.Cancel, new Button("Cancel"));
        buttonMap.put(ButtonType.Ok, new Button("Ok"));
        buttonMap.put(ButtonType.Close, new Button("Close"));

        component.setSizeFull();

        layout.addComponent(component);
        layout.addComponent(buttonLayout);
        layout.setExpandRatio(component, 1.0F);
        layout.setSizeFull();

        buttonLayout.addComponent(spacer);

        for(ButtonType buttonType : buttons) {
            Button button = buttonMap.get(buttonType);
            buttonLayout.addComponent(button);
            buttonLayout.setComponentAlignment(button, Alignment.TOP_RIGHT);
            button.addListener(listener);
        }

        buttonLayout.setMargin(true);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth(100, Component.UNITS_PERCENTAGE);
        buttonLayout.setExpandRatio(spacer, 1.0F);

        setContent(layout);

    }

    public ButtonType getResult() {
        return result;
    }

    public void setButtonCaption(ButtonType type, String caption) {
        Button button = buttonMap.get(type);
        button.setCaption(caption);
    }

    public ButtonType getDefaultButtonType() {
        return defaultButtonType;
    }

    public void setDefaultButtonType(ButtonType defaultButtonType) {
        this.defaultButtonType = defaultButtonType;
        for(Map.Entry<ButtonType, Button> entry: buttonMap.entrySet()) {
            Button btn = entry.getValue();
            if(entry.getKey() == defaultButtonType) {
                btn.setClickShortcut(KeyCode.ENTER, null);
                btn.setStyleName(Reindeer.BUTTON_DEFAULT);
            } else {
                btn.removeClickShortcut();
                btn.removeStyleName(Reindeer.BUTTON_DEFAULT);
            }
        }
    }


}
