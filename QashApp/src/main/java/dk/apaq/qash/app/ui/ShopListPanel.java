/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.ui;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ShopListPanel extends CustomComponent {

    private VerticalLayout layout = new VerticalLayout();
    private HorizontalLayout headerLayout = new HorizontalLayout();
    private VerticalLayout bottomLayout = new VerticalLayout();
    private Label lblHeader = new Label("Select shop");
    private Table table = new Table();

    public ShopListPanel() {
        
        lblHeader.setStyleName(Reindeer.LABEL_H1);

        table.setSizeFull();
        table.setStyleName(Reindeer.TABLE_BORDERLESS);
        
        bottomLayout.addComponent(table);
        bottomLayout.setSizeFull();
        
        headerLayout.addComponent(lblHeader);
        headerLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        headerLayout.setWidth(100, UNITS_PERCENTAGE);
        headerLayout.setMargin(true);

        layout.addComponent(headerLayout);
        layout.addComponent(bottomLayout);
        layout.setSizeFull();
        layout.setExpandRatio(bottomLayout, 1.0F);

        setCompositionRoot(layout);

    }


}
