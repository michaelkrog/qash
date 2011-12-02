package dk.apaq.shopsystem.qash.settings;

import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import java.util.Arrays;

/**
 *
 * @author krog
 */
public class ReceiptForm extends Form {

    private GridLayout ourLayout;
    private Label lblNote = new Label("Note");
    private Button btnSave = new Button("Save");

    private class ReceiptFormFieldFactory extends DefaultFieldFactory {

        @Override
        public Field createField(Item item, Object propertyId, Component uiContext) {
            Field f;

            if ("annexNote".equals(propertyId)) {
                f = new TextArea();
                ((TextArea) f).setInputPrompt("Note at bottom of receipts");
                ((TextArea) f).setColumns(25);
            } else {
                f = super.createField(item, propertyId, uiContext);
            }

            if (f instanceof TextArea) {
                ((TextArea) f).setNullRepresentation("");
            }

            return f;
        }
    }

    public ReceiptForm() {
        // Create our layout (3x3 GridLayout)
        ourLayout = new GridLayout(2, 2);

        // Use top-left margin and spacing
        ourLayout.setMargin(true, false, false, true);
        ourLayout.setSpacing(true);
        ourLayout.setWidth(400, Component.UNITS_PIXELS);

        setLayout(ourLayout);

        setFormFieldFactory(new ReceiptFormFieldFactory());

        // Set up buffering
        setWriteThrough(false); // we want explicit 'apply'
        setInvalidCommitted(false); // no invalid values in datamodel



        lblNote.setSizeUndefined();
        ourLayout.addComponent(lblNote, 0, 0);
        ourLayout.setComponentAlignment(lblNote, Alignment.TOP_RIGHT);
        ourLayout.addComponent(btnSave, 1, 1);

        btnSave.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                commit();
                if (getItemDataSource() instanceof Buffered) {
                    ((Buffered) getItemDataSource()).commit();
                }
            }
        });
    }

    /*
     * Override to get control over where fields are placed.
     */
    @Override
    protected void attachField(Object propertyId, Field field) {
        field.setCaption(null);
        if (propertyId.equals("annexNote")) {
            field.setWidth(100, UNITS_PERCENTAGE);
            ourLayout.addComponent(field, 1, 0);
            ourLayout.setColumnExpandRatio(1, 1.0F);
        }
    }

    @Override
    public void setItemDataSource(Item newDataSource) {
        super.setItemDataSource(newDataSource, Arrays.asList(new String[]{"annexNote"}));
    }
}