package dk.apaq.shopsystem.ui.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
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
import dk.apaq.shopsystem.entity.Organisation;

import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.ui.common.Spacer;
import dk.apaq.shopsystem.util.CurrencyUtil;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.util.Currency;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class CurrencyAndTaxPanel extends CustomComponent {

    private final VerticalLayout outerLayout = new VerticalLayout();
    private final HorizontalLayout innerLayout = new HorizontalLayout();
    private final HorizontalLayout bottomLayout = new HorizontalLayout();
    private final VerticalLayout settingsWrapper = new VerticalLayout();
    private final GridLayout defaultSettingsLayout = new GridLayout(2, 2);
    private final Spacer settingsLayoutSpacer = new Spacer();
    private final Spacer defaultSettingsLayoutSpacer = new Spacer();
    private final ListSelect taxList = new ListSelect();
    private final Label lblDefaultTax = new Label("Default tax");
    private final Label lblDefaultCurrency = new Label("Default currency");
    private final TaxForm taxForm = new TaxForm();
    private final Button btnNewTax = new Button("New tax");
    private final Button btnRemoveTax = new Button("Remove tax");
    private final Select defaultTaxSelect = new Select();
    private final Select defaultCurrencySelect = new Select();
    private Item organisationItem;
    private Item lastDefaultTax = null;
    private final CurrencyChangeListener currencyChangeListener = new CurrencyChangeListener();
    private final DefaultTaxChangeListener defaultTaxChangeListener = new DefaultTaxChangeListener();
    private Container taxContainer;
    private OrganisationService service;

    private class TaxListSelectionHandler implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            Object id = event.getProperty().getValue();
            if (id == null) {
                taxForm.setEnabled(false);
            } else {
                taxForm.setEnabled(true);
                Item item = taxList.getItem(id);
                taxForm.setItemDataSource(item);
            }
        }
    }

    private class DefaultTaxChangeListener implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            Object id = event.getProperty().getValue();
            Item nextDefaultTax = id == null ? null : taxContainer.getItem(id);

            if (lastDefaultTax != null) {
                lastDefaultTax.getItemProperty("defaultEnabled").setValue(false);

                if (lastDefaultTax instanceof Buffered) {
                    ((Buffered) lastDefaultTax).commit();
                }
            }

            if (nextDefaultTax != null) {
                nextDefaultTax.getItemProperty("defaultEnabled").setValue(true);

                if (nextDefaultTax instanceof Buffered) {
                    ((Buffered) nextDefaultTax).commit();
                }
            }

            lastDefaultTax = nextDefaultTax;
        }
    }

    private class CurrencyChangeListener implements ValueChangeListener {

        public void valueChange(ValueChangeEvent event) {
            Organisation org = service.readOrganisation();
            org.setCurrency((String) event.getProperty().getValue());
            service.updateOrganisation(org);
        }
    }

    public CurrencyAndTaxPanel() {

        setStyleName("v-currencyandtaxpanel");
        settingsWrapper.setStyleName("v-currencyandtaxpanel-settings-wrapper");

        innerLayout.addComponent(taxList);
        innerLayout.addComponent(settingsWrapper);
        innerLayout.setExpandRatio(settingsWrapper, 1.0F);

        bottomLayout.addComponent(btnNewTax);
        bottomLayout.addComponent(btnRemoveTax);
        bottomLayout.addComponent(defaultSettingsLayoutSpacer);
        bottomLayout.addComponent(defaultSettingsLayout);
        bottomLayout.setExpandRatio(defaultSettingsLayoutSpacer, 1.0F);
        bottomLayout.setSpacing(true);
        bottomLayout.setWidth(100, UNITS_PERCENTAGE);

        taxForm.setEnabled(false);
        taxForm.setItemDataSource(new BeanItem(new Tax()));

        settingsWrapper.addComponent(taxForm);
        settingsWrapper.addComponent(settingsLayoutSpacer);
        settingsWrapper.setExpandRatio(settingsLayoutSpacer, 1.0F);

        outerLayout.addComponent(innerLayout);
        outerLayout.addComponent(bottomLayout);
        outerLayout.setExpandRatio(innerLayout, 1.0F);

        defaultSettingsLayout.addComponent(lblDefaultTax, 0, 0);
        defaultSettingsLayout.setComponentAlignment(lblDefaultTax, Alignment.MIDDLE_RIGHT);
        defaultSettingsLayout.addComponent(defaultTaxSelect, 1, 0);
        defaultSettingsLayout.addComponent(lblDefaultCurrency, 0, 1);
        defaultSettingsLayout.setComponentAlignment(lblDefaultCurrency, Alignment.MIDDLE_RIGHT);
        defaultSettingsLayout.addComponent(defaultCurrencySelect, 1, 1);
        defaultSettingsLayout.setSpacing(true);

        outerLayout.setMargin(true);
        outerLayout.setSpacing(true);
        innerLayout.setSpacing(true);

        lblDefaultTax.setSizeUndefined();

        taxList.setNewItemsAllowed(false);
        taxList.setNullSelectionAllowed(false);
        taxList.setItemCaptionPropertyId("name");
        taxList.setImmediate(true);
        taxList.setContainerDataSource(taxContainer);

        setCompositionRoot(outerLayout);

        taxList.setWidth(200, UNITS_PIXELS);
        taxList.setHeight(100, UNITS_PERCENTAGE);
        settingsWrapper.setSizeFull();
        innerLayout.setSizeFull();
        outerLayout.setSizeFull();
        setSizeFull();
        
        taxList.addListener(new TaxListSelectionHandler());

        defaultTaxSelect.setNewItemsAllowed(false);
        defaultTaxSelect.setImmediate(true);
        defaultTaxSelect.setItemCaptionPropertyId("name");
        defaultTaxSelect.addListener(defaultTaxChangeListener);

        defaultCurrencySelect.setNewItemsAllowed(false);
        defaultCurrencySelect.setImmediate(true);
        defaultCurrencySelect.setNullSelectionAllowed(false);
        defaultCurrencySelect.addListener(currencyChangeListener);

        List<Currency> currencies = CurrencyUtil.getCountries();
        for (Currency currency : currencies) {
            defaultCurrencySelect.addItem(currency.getCurrencyCode());
        }

        btnNewTax.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = taxContainer.addItem();
                Item item = taxContainer.getItem(id);
                item.getItemProperty("name").setValue("Unnamed tax");
                if (item instanceof Buffered) {
                    ((Buffered) item).commit();
                }
                taxList.setValue(id);
            }
        });

        btnRemoveTax.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = taxList.getValue();
                if (id == null) {
                    return;
                }
                taxContainer.removeItem(id);
            }
        });

    }

    public void setService(OrganisationService service) {
        this.service=service;
    }

    /*
    @Deprecated
    public void setContainerDataSource(Container newDataSource) {
        defaultTaxSelect.removeListener(defaultTaxChangeListener);

        this.taxContainer = newDataSource;
        taxList.setContainerDataSource(newDataSource);
        defaultTaxSelect.setContainerDataSource(newDataSource);
        lastDefaultTax = null;

        defaultTaxSelect.setValue(getDefaultTaxId());

        for (Object id : newDataSource.getItemIds()) {
            Item item = newDataSource.getItem(id);
            if (Boolean.TRUE.equals(item.getItemProperty("defaultEnabled").getValue())) {
                defaultTaxSelect.setValue(id);
            }
        }
        defaultTaxSelect.addListener(defaultTaxChangeListener);

    }

    @Deprecated
    public Container getContainerDataSource() {
        return taxContainer;
    }

    @Deprecated
    public void setOrganisationDataSource(Item shopItem) {
        this.organisationItem = shopItem;

        defaultCurrencySelect.removeListener(currencyChangeListener);
        defaultCurrencySelect.setPropertyDataSource(shopItem.getItemProperty("currency"));
        defaultCurrencySelect.addListener(currencyChangeListener);
    }*/

    @Override
    public void attach() {
        defaultTaxSelect.removeListener(defaultTaxChangeListener);
        defaultCurrencySelect.removeListener(currencyChangeListener);

        this.taxContainer = new CrudContainer(service.getTaxes(), Tax.class);
        taxList.setContainerDataSource(this.taxContainer);
        defaultTaxSelect.setContainerDataSource(this.taxContainer);
        lastDefaultTax = null;

        defaultTaxSelect.setValue(getDefaultTaxId());

        for (Object id : this.taxContainer.getItemIds()) {
            Item item = this.taxContainer.getItem(id);
            if (Boolean.TRUE.equals(item.getItemProperty("defaultEnabled").getValue())) {
                defaultTaxSelect.setValue(id);
            }
        }
        defaultTaxSelect.addListener(defaultTaxChangeListener);
        defaultCurrencySelect.addListener(currencyChangeListener);

        Organisation org = service.readOrganisation();
        defaultCurrencySelect.setValue(org.getCurrency());
    }


    private Object getDefaultTaxId() {
        Container c = this.defaultTaxSelect.getContainerDataSource();
        for (Object id : c.getItemIds()) {
            Property prop = c.getContainerProperty(id, "defaultEnabled");
            if (Boolean.TRUE.equals(prop.getValue())) {
                return id;
            }
        }
        return null;
    }
}
