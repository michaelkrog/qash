package dk.apaq.qash.app.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import dk.apaq.qash.share.model.Tax;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;

/**
 *
 * @author michaelzachariassenkrog
 */
public class NonStockItemCreatePanel extends CustomComponent {

    private final HorizontalLayout layout = new HorizontalLayout();
    private final TextField textTitle = new TextField("Title");
    private final ComboBox comboTax = new ComboBox("Tax");

    public NonStockItemCreatePanel(Container taxContainer) {
        comboTax.setContainerDataSource(taxContainer);
        comboTax.setValue(getDefaultTaxId());
        setStyleName("v-nonstockitemcreatewindow");

        textTitle.setInputPrompt("Type title");

        comboTax.setItemCaptionPropertyId("name");
        comboTax.setNullSelectionAllowed(false);
        comboTax.setNullSelectionItemId("dummy");

        layout.addComponent(textTitle);
        layout.addComponent(comboTax);
        layout.setWidth(100, Component.UNITS_PERCENTAGE);
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        setCompositionRoot(layout);
    }

    public String getTitle() {
        return (String) textTitle.getValue();
    }

    public Tax getTax() {
        String id = (String) comboTax.getValue();
        if (id != null) {
            Item item = comboTax.getContainerDataSource().getItem(id);
            if (item instanceof BeanItem) {
                return (Tax) ((BeanItem) item).getBean();
            }
            if (item instanceof HasBean) {
                return (Tax) ((HasBean) item).getBean();
            }
        }
        return null;

    }

    private Object getDefaultTaxId() {
        Container c = this.comboTax.getContainerDataSource();
        for(Object id : c.getItemIds()) {
            Property prop = c.getContainerProperty(id, "defaultEnabled");
            if(Boolean.TRUE.equals(prop.getValue())) {
                return id;
            }
        }
        return null;
    }

}
