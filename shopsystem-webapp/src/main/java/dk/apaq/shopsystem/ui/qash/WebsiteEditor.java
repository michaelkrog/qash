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
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final Label title = new Label("Website name");
    private final ProductCategoryEditor categoryEditor = new ProductCategoryEditor();
    private final PagesEditor pagesEditor = new PagesEditor();
    private final NavigationEditor navigationEditor = new NavigationEditor();
    private final Panel rightPanel = new Panel("General");
    private final NativeButton changeTitleButton = new NativeButton("Change title");
    private final VerticalLayout headerLayout = new VerticalLayout();
    private final HorizontalLayout titleLayout = new HorizontalLayout();
    private final GridLayout generalLayout = new GridLayout(2, 2);
    private final Select selectTheme = new Select("Theme");
    private final ListSelect listDomains = new ListSelect("Domains");
    private final Button btnDomains = new Button("Edit domains");



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
        
        tabSheet.setSizeFull();
        tabSheet.addTab(categoryEditor, "Products", null);
        tabSheet.addTab(pagesEditor, "Pages", null);
        tabSheet.addTab(navigationEditor, "Navigation", null);
        tabSheet.setStyleName(Reindeer.TABSHEET_BORDERLESS);

        layout.setSizeFull();
        layout.addComponent(topLayout);
        layout.addComponent(tabSheet);
        layout.setExpandRatio(tabSheet, 1.0F);

        topLayout.addComponent(headerLayout);
        topLayout.addComponent(rightPanel);
        topLayout.setMargin(true, true, true, true);
        topLayout.setWidth(100, Component.UNITS_PERCENTAGE);
        topLayout.setComponentAlignment(rightPanel, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(headerLayout, 1.0F);

        headerLayout.addComponent(titleLayout);
        headerLayout.setMargin(false, false, true, false);

        titleLayout.addComponent(title);
        titleLayout.addComponent(changeTitleButton);
        titleLayout.setSpacing(true);
        titleLayout.setStyleName("title");
        titleLayout.setWidth(100, Component.UNITS_PERCENTAGE);
        titleLayout.setExpandRatio(title, 1.0F);

        changeTitleButton.setStyleName(Reindeer.BUTTON_LINK);

        //rightPanel.addComponent(generalLayout);
        rightPanel.addComponent(selectTheme);
        //rightPanel.addComponent(listDomains);
        rightPanel.addComponent(btnDomains);
        rightPanel.addStyleName(Reindeer.PANEL_LIGHT);
        rightPanel.addStyleName(ShopSystemTheme.PANEL_BUBBLE);
        rightPanel.setWidth(319, Component.UNITS_PIXELS);
        rightPanel.setHeight(180, Component.UNITS_PIXELS);

        setCompositionRoot(layout);

    }


}

