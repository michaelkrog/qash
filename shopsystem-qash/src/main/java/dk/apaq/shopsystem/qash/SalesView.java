package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.annex.AnnexService;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Outlet;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.vaadin.addon.crudcontainer.SortableCrudContainer;

/**
 *
 */
public class SalesView extends CustomComponent {

    private final TabSheet tabSheet = new TabSheet();
    private final OrderList orderList = new OrderList();
    private SortableCrudContainer orderContainer;
    private Crud.Complete<String, Product> productCrud;
    private Crud.Editable<String, Payment> paymentCrud;
    private OrderEditor chosenEditor = null;
    private OrganisationService organisationService;
    private AnnexService annexService;
    private Outlet outlet;
    private boolean autoOpenPaymentDialog = false;
    
    private class TabCloser implements ClickListener {
        private TabSheet tabSheet;
        private Tab tab;

        public TabCloser(TabSheet tabSheet, Tab tab) {
            this.tabSheet = tabSheet;
            this.tab = tab;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            tabSheet.removeTab(tab);
        }
        
        
    }
    
    public SalesView() {
        
        orderList.setSizeFull();

        tabSheet.setSizeFull();
        tabSheet.setStyleName(Reindeer.TABSHEET_BORDERLESS);
        
        initOrderListView();

        setCompositionRoot(tabSheet);

        tabSheet.addListener(new TabSheet.SelectedTabChangeListener() {

            public void selectedTabChange(SelectedTabChangeEvent event) {
                if (chosenEditor != null) {
                    chosenEditor.commit();
                }

                if (event.getTabSheet().getSelectedTab() instanceof OrderEditor) {
                    chosenEditor = (OrderEditor) event.getTabSheet().getSelectedTab();
                } else {
                    chosenEditor = null;
                }
            }
        });



        orderList.addListener(new ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                Object id = event.getProperty().getValue();

                if (id == null) {
                    return;
                }

                Item item = orderContainer.getItem(id);

                showEditor(item);

                orderList.setValue(null);
            }
        });

        
        orderList.addListener(new ItemClickListener() {

            public void itemClick(ItemClickEvent event) {
                Item item = event.getItem();

                if (item == null) {
                    return;
                }

                showEditor(item);
            }
        });



    }

    private OrderEditor createEditor(Item item) {
        OrderEditor editor = new OrderEditor();
        editor.setSizeFull();
        editor.setOrganisationService(organisationService);
        editor.setAnnexService(annexService);
        editor.setItemDataSource(item);
        editor.setAutoOpenPaymentDialog(autoOpenPaymentDialog);
        return editor;
    }

    private int getTabIndexWithOrder(long orderNumber) {
        for (int i = 1; i < tabSheet.getComponentCount(); i++) {
            String caption = tabSheet.getTab(i).getCaption();
            long numberFromCaption = getOrderNumberFromCaption(caption);
            if (numberFromCaption == orderNumber) {
                return i;
            }
        }
        return -1;
    }

    private int getSuitableIndexForNewOrderTab(long orderNumber) {
        for (int i = 1; i < tabSheet.getComponentCount(); i++) {
            String caption = tabSheet.getTab(i).getCaption();
            long numberFromCaption = getOrderNumberFromCaption(caption);
            if (numberFromCaption > orderNumber) {
                return i;
            }
        }
        return tabSheet.getComponentCount();
    }

    private long getOrderNumberFromCaption(String caption) {
        if (!caption.contains("#")) {
            return -1;
        }

        int hashIndex = caption.indexOf("#");

        return Long.parseLong(caption.substring(hashIndex+1));
    }

    private void initOrderListView() {
        tabSheet.removeAllComponents();
        tabSheet.addTab(orderList, "Overview", null);
    }

    private void showEditor(Item item) {
        long orderNumber = (Long) item.getItemProperty("number").getValue();
        int existingTabIndex = getTabIndexWithOrder(orderNumber);
        if (existingTabIndex > 0) {
            tabSheet.setSelectedTab(tabSheet.getTab(existingTabIndex).getComponent());
        } else {
            int suitableIndex = getSuitableIndexForNewOrderTab(orderNumber);
            OrderEditor editor = createEditor(item);
            TabSheet.Tab tab = tabSheet.addTab(editor, "Order #" + orderNumber, null, suitableIndex);
            editor.getCloseButton().addListener(new TabCloser(tabSheet, tab));
            tabSheet.setSelectedTab(tab.getComponent());
        }
    }

    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
        
        initOrderListView();
        this.orderContainer = new SortableCrudContainer(organisationService.getOrders(), Order.class);
        orderList.setContainerDataSource(orderContainer);
        orderList.setOrganisationService(organisationService);
        
        this.paymentCrud = organisationService.getPayments();
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
        orderList.setOutlet(outlet);
    }
    
    public void setAnnexService(AnnexService annexService) {
        this.annexService = annexService;
        orderList.setAnnexService(annexService);
    }
    
    public void setAutoOpenPaymentDialog(boolean value) {
        this.autoOpenPaymentDialog = value;
    }
}
