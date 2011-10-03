package dk.apaq.shopsystem.ui.qash;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 */
public class WebsiteEditor extends CustomComponent {

    private final VerticalLayout layout = new VerticalLayout();
    private final TabSheet tabSheet = new TabSheet();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final Label title = new Label("Website name");
    private final GeneralEditor generalEditor = new GeneralEditor();
    private final ProductCategoryEditor categoryEditor = new ProductCategoryEditor();
    private final PagesEditor pagesEditor = new PagesEditor();
    private final NavigationEditor navigationEditor = new NavigationEditor();

    private class GeneralEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        public GeneralEditor() {
            setCompositionRoot(layout);
        }


    }

    private class ProductCategoryEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        public ProductCategoryEditor() {
            setCompositionRoot(layout);
        }

    }

    private class PagesEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        public PagesEditor() {
            setCompositionRoot(layout);
        }

    }

    private class NavigationEditor extends CustomComponent {

        private final VerticalLayout layout = new VerticalLayout();

        public NavigationEditor() {
            setCompositionRoot(layout);
        }

    }


    public WebsiteEditor() {
        setStyleName("v-WebsiteEditor");

        title.setStyleName(Reindeer.LABEL_H1);
        
        tabSheet.setSizeFull();
        tabSheet.addTab(generalEditor, "General", null);
        tabSheet.addTab(categoryEditor, "Products", null);
        tabSheet.addTab(pagesEditor, "Pages", null);
        tabSheet.addTab(navigationEditor, "Navigation", null);

        layout.setSizeFull();
        layout.addComponent(topLayout);
        layout.addComponent(tabSheet);
        layout.setExpandRatio(tabSheet, 1.0F);

        setCompositionRoot(layout);

    }


}
