/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.ui.util;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author michaelzachariassenkrog
 */
public class Spacer extends CustomComponent {

    private final VerticalLayout layout = new VerticalLayout();

    public Spacer() {
        layout.setSizeFull();
        setCompositionRoot(layout);
    }


}
