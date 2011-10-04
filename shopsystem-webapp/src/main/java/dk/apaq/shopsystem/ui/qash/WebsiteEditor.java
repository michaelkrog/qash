package dk.apaq.shopsystem.ui.qash;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import dk.apaq.shopsystem.ui.ShopSystemTheme;

/**
 *
 */
public class WebsiteEditor extends CustomComponent {

    private final VerticalLayout layout = new VerticalLayout();
    private final TabSheet tabSheet = new TabSheet();
    private final VerticalLayout topLayout = new VerticalLayout();
    private final Label title = new Label("Website name");
    private final Label lblTheme = new Label("Theme");
    private final ProductCategoryEditor categoryEditor = new ProductCategoryEditor();
    private final PagesEditor pagesEditor = new PagesEditor();
    private final NavigationEditor navigationEditor = new NavigationEditor();
    private final Select selectTheme = new Select();
    private final Button btnDomains = new Button("Edit domains");
    private final HorizontalLayout generalSettingsLayout = new HorizontalLayout();



    private class ProductCategoryEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        //Product categories shown on the site
        public ProductCategoryEditor() {
            setCompositionRoot(layout);
        }

    }

    private class PagesEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        //All pages shown on the site and editor for them
        public PagesEditor() {
            setCompositionRoot(layout);
        }

    }

    private class NavigationEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        //Easy peasy editor for navigation
        public NavigationEditor() {
            setCompositionRoot(layout);
        }

    }


    public WebsiteEditor() {
        setStyleName("v-WebsiteEditor");

        title.setStyleName(Reindeer.LABEL_H1);
        
        btnDomains.setStyleName(Reindeer.BUTTON_SMALL);
        
        selectTheme.setStyleName(Reindeer.TEXTFIELD_SMALL);
        
        tabSheet.setSizeFull();
        tabSheet.addTab(categoryEditor, "Products", null);
        tabSheet.addTab(pagesEditor, "Pages", null);
        tabSheet.addTab(navigationEditor, "Navigation", null);
        tabSheet.setStyleName(Reindeer.TABSHEET_BORDERLESS);

        layout.setSizeFull();
        layout.addComponent(topLayout);
        layout.addComponent(tabSheet);
        layout.setExpandRatio(tabSheet, 1.0F);

        topLayout.addComponent(title);
        topLayout.setSpacing(true);
        topLayout.addComponent(generalSettingsLayout);
        topLayout.setMargin(true, true, true, true);
        topLayout.setWidth(100, Component.UNITS_PERCENTAGE);
        
        generalSettingsLayout.setSpacing(true); 
        generalSettingsLayout.addComponent(lblTheme);
        generalSettingsLayout.addComponent(selectTheme);
        generalSettingsLayout.addComponent(btnDomains);

        setCompositionRoot(layout);

    }


}

