package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
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
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import dk.apaq.crud.Crud;
import dk.apaq.filter.FilterGenerator;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.qash.common.Spacer;
import dk.apaq.shopsystem.qash.common.StoreFilterGenerator;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.FilterableContainer;

/**
 * A List for stores including filtering and editing.
 */
public class StoreList extends CustomComponent {

    private static final String[] COLUMNS = {"name", "email", "telephone"};
    private final Action editAction = new Action("Edit");
    private final Action deleteAction = new Action("Delete");
    private Label titleLabel = new Label("Stores");
    private VerticalLayout layout = new VerticalLayout();
    private VerticalLayout topVLayout = new VerticalLayout();
    private HorizontalLayout topHLayout = new HorizontalLayout();
    private Button addButton = new Button("Add new store");
    private Button removeButton = new Button("Remove store");
    private Button openButton = new Button("Edit store");
    private TextField searchField = new TextField();
    private Spacer spacer = new Spacer();
    private Table table = new Table();
    private Crud<String, Store> crud;
    private CrudContainer container;
    private Sorter sorter = new Sorter("name");
    private final FilterGenerator filterGenerator = new StoreFilterGenerator();

    private class SearchFieldHandler implements TextChangeListener {

        public void textChange(TextChangeEvent event) {
            if (container instanceof FilterableContainer) {
                ((FilterableContainer) container).setFilter(filterGenerator.generateFilter(event.getText()));
            }
        }
    }

    public StoreList() {

        setStyleName("v-itemlist");
        setCompositionRoot(layout);
        layout.addComponent(topVLayout);
        layout.addComponent(table);

        titleLabel.setStyleName(Reindeer.LABEL_H1);

        searchField.setInputPrompt("search string");
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
        topHLayout.setComponentAlignment(searchField, Alignment.BOTTOM_RIGHT);
        topHLayout.setExpandRatio(spacer, 1.0F);
        topHLayout.setWidth(100, Component.UNITS_PERCENTAGE);

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
                item.getItemProperty("name").setValue("Unnamed store");
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
                String itemId = (String) table.getValue();
                Item item = table.getItem(itemId);

                editItem(item);

            }
        });

        removeButton.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                Object id = table.getValue();
                table.removeItem(id);
            }
        });

        layout.setMargin(false);
        layout.setExpandRatio(table, 1.0F);
        layout.setSizeFull();
    }

    public void setCrud(Crud<String, Store> crud) {
        this.crud = crud;
        refreshContainer();
    }

    @Override
    public void attach() {
        super.attach();
        refreshContainer();
    }

    private void editItem(final Item item) {
        final StoreForm form = new StoreForm();
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        Window dialog = new Window("Edit Store", layout);
        dialog.addComponent(form);
        dialog.setWidth(445, Component.UNITS_PIXELS);
        dialog.setHeight(370, Component.UNITS_PIXELS);
        dialog.center();
        
        getApplication().getMainWindow().addWindow(dialog);

        
        form.setItemDataSource(item);
        
        
    }

    private void refreshContainer() {
        if (crud != null) {
            this.container = new CrudContainer(crud, Store.class);
            this.container.setSorter(sorter);
            table.setContainerDataSource(this.container);
            table.setVisibleColumns(COLUMNS);
        }
    }

}
