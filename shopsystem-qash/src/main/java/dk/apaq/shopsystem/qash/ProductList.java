package dk.apaq.shopsystem.qash;

import com.vaadin.data.Buffered;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.themes.Reindeer;

import dk.apaq.crud.Crud;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.qash.common.CommonDialog;
import dk.apaq.shopsystem.qash.common.ProductFilterGenerator;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.qash.data.ProductContainer;
import dk.apaq.shopsystem.qash.data.util.NumberColumnGenerator;
import dk.apaq.shopsystem.qash.listeners.RemoveSelectedOnClickListener;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;

/**
 * A List for products including filtering and editing.
 */
public class ProductList extends CustomComponent {

    private static final String[] COLUMNS = {"name", "itemNo", "barcode", "price", "priceWithTax", "quantityInStock"};
    private final Action editAction = new Action("Edit");
    private final Action deleteAction = new Action("Delete");
    private Label titleLabel = new Label("Stock Management");
    private VerticalLayout layout = new VerticalLayout();
    private VerticalLayout topVLayout = new VerticalLayout();
    private HorizontalLayout topHLayout = new HorizontalLayout();
    private Button addButton = new Button("Add new item");
    private Button removeButton = new Button("Remove item");
    private Button openButton = new Button("Open item");
    private Button adjustButton = new Button("Fast quantity adjustment");
    private TextField searchField = new TextField();
    private Spacer spacer = new Spacer();
    private Table table = new Table();
    private Crud<String, Product> productCrud;
    private Crud<String, Tax> taxCrud;
    private CrudContainer productContainer;
    private CrudContainer taxContainer;
    private ThemeResource resourceDelete = new ThemeResource("img/clear.png");
    private Sorter productSorter = new Sorter("name");
    private final ProductFilterGenerator filterGenerator = new ProductFilterGenerator();

    private class SearchFieldHandler implements TextChangeListener {

        public void textChange(TextChangeEvent event) {
            if (productContainer instanceof FilterableContainer) {
                ((FilterableContainer) productContainer).setFilter(filterGenerator.generateFilter(event.getText()));
            }
        }
    }

    public ProductList() {

        setStyleName("v-itemlist");
        setCompositionRoot(layout);
        layout.addComponent(topVLayout);
        layout.addComponent(table);

        titleLabel.setStyleName(Reindeer.LABEL_H1);

        searchField.setInputPrompt("title, barcode, itemno");
        searchField.setImmediate(true);
        searchField.addListener(new SearchFieldHandler());


        topVLayout.addComponent(titleLabel);
        topVLayout.addComponent(topHLayout);
        topVLayout.setMargin(true);

        topHLayout.setSpacing(true);
        topHLayout.setMargin(true, false, false, false);
        topHLayout.addComponent(searchField);
        topHLayout.addComponent(spacer);
        topHLayout.addComponent(addButton);
        topHLayout.addComponent(openButton);
        topHLayout.addComponent(removeButton);
        //topHLayout.addComponent(adjustButton);
        topHLayout.setComponentAlignment(searchField, Alignment.BOTTOM_RIGHT);
        topHLayout.setExpandRatio(spacer, 1.0F);
        topHLayout.setWidth(100, Component.UNITS_PERCENTAGE);

        table.addGeneratedColumn("price", new NumberColumnGenerator(2, 2));
        table.addGeneratedColumn("priceWithTax", new NumberColumnGenerator(2, 2));
        table.addGeneratedColumn("quantityInStock", new NumberColumnGenerator(0, 3));

        table.setSelectable(true);
        table.addActionHandler(new Handler() {

            public Action[] getActions(Object target, Object sender) {
                return new Action[]{editAction, deleteAction};
            }

            public void handleAction(Action action, Object sender, Object target) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });


        table.setSizeFull();

        addButton.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = table.addItem();
                Item item = table.getItem(id);
                item.getItemProperty("name").setValue("Unnamed item");
                Tax tax = getDefaultTax();
                item.getItemProperty("tax").setValue(tax);
                editItem(item);
            }
        });

        openButton.setStyleName(Reindeer.BUTTON_DEFAULT);
        openButton.setClickShortcut(KeyCode.ENTER, null);
        openButton.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                if (table.getValue() == null) {
                    return;
                }
                String productId = (String) table.getValue();
                Item item = table.getItem(productId);

                editItem(item);

            }
        });

        removeButton.addListener(new RemoveSelectedOnClickListener(table));


        table.setColumnHeader("quantityInStock", "quantity");
        table.setColumnAlignment("price", Table.ALIGN_RIGHT);
        table.setColumnAlignment("priceWithTax", Table.ALIGN_RIGHT);
        table.setColumnAlignment("quantityInStock", Table.ALIGN_RIGHT);
        table.setColumnWidth("price", 90);
        table.setColumnWidth("quantityInStock", 80);

        layout.setMargin(false);
        layout.setExpandRatio(table, 1.0F);
        layout.setSizeFull();
    }

    public void setTaxCrud(Crud<String, Tax> taxCrud) {
        this.taxCrud = taxCrud;
        refreshTaxContainer();
    }

    public void setProductCrud(Crud<String, Product> productCrud) {
        this.productCrud = productCrud;
        refreshProductContainer();
    }

    @Override
    public void attach() {
        super.attach();
        refreshProductContainer();
    }

    private void editItem(final Item item) {
        final ProductEditor editor = new ProductEditor();
        //editor.setWriteThrough(false);

        final CommonDialog dialog = new CommonDialog("Edit product", editor, CommonDialog.ButtonType.Close);
        dialog.setWidth(400, Component.UNITS_PIXELS);
        dialog.setHeight(285, Component.UNITS_PIXELS);
        dialog.center();
        dialog.setDefaultButtonType(CommonDialog.ButtonType.Close);
        getApplication().getMainWindow().addWindow(dialog);

        if(taxContainer!=null) {
            editor.setTaxContainerDatasource(taxContainer);
        }

        editor.setItemDataSource(item);
        

        dialog.addListener(new CloseListener() {

            @Override
            public void windowClose(CloseEvent e) {
                if (dialog.getResult() == CommonDialog.ButtonType.Close) {
                    editor.commit();
                    if (item instanceof Buffered) {
                        ((Buffered) item).commit();
                    }
                } else {
                    if (item instanceof Buffered) {
                        ((Buffered) item).discard();
                    }
                }
            }
        });

    }

    private void refreshProductContainer() {
        if (productCrud != null) {
            this.productContainer = new ProductContainer(productCrud);
            this.productContainer.setSorter(productSorter);
            table.setContainerDataSource(this.productContainer);
            table.setVisibleColumns(COLUMNS);
        }
    }

    private void refreshTaxContainer() {
        if (taxCrud != null) {
            this.taxContainer = new CrudContainer(taxCrud, Tax.class);
        }
    }

    private Tax getDefaultTax() {
        Container c = this.taxContainer;
        for(Object id : c.getItemIds()) {
            Item item = c.getItem(id);
            Property prop = item.getItemProperty("defaultEnabled");
            if(Boolean.TRUE.equals(prop.getValue())) {
                return ((HasBean<Tax>)item).getBean();
            }
        }
        return null;
    }
}
