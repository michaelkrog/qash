/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui.util;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.Field.ValueChangeEvent;

/**
 *
 * @author michaelzachariassenkrog
 */
public class FieldEnabler {

    private final CheckBox checkBox;
    private final Field fields[];

    public FieldEnabler(CheckBox checkBox, Field... fields) {
        this.checkBox = checkBox;
        this.fields = fields;

        this.checkBox.addListener(new Field.ValueChangeListener() {

            public void valueChange(Property.ValueChangeEvent event) {
                for (Field field : FieldEnabler.this.fields) {
                    field.setEnabled(FieldEnabler.this.checkBox.booleanValue());
                }
            }
        });
    }
}
