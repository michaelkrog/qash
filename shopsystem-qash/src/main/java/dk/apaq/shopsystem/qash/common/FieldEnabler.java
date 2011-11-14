package dk.apaq.shopsystem.qash.common;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;

/**
 * A Utilty class that enabled a list of fields depending on the state of a CheckBox.
 * @author michaelzachariassenkrog
 */
public class FieldEnabler {

    private final CheckBox checkBox;
    private final Field fields[];

    public FieldEnabler(CheckBox checkBox, Field... fields) {
        this.checkBox = checkBox;
        this.fields = fields;

        this.checkBox.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                for (Field field : FieldEnabler.this.fields) {
                    field.setEnabled(FieldEnabler.this.checkBox.booleanValue());
                }
            }
        });
    }
}
