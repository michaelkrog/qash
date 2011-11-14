package dk.apaq.shopsystem.qash.data.util;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Property.ValueChangeNotifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class PropertyDependencyManager {

    private final Item item;
    private Map<Object, DependentPropertyWrapper> dependencyMap = new HashMap<Object, DependentPropertyWrapper>();

    public class DependentPropertyValueChangeEvent implements Property.ValueChangeEvent {

        private final Property property;

        public DependentPropertyValueChangeEvent(Property property) {
            this.property = property;
        }

        public Property getProperty() {
            return property;
        }
    }

    private class DependencyChangeListener implements Property.ValueChangeListener {

        private final DependentPropertyWrapper propertyWrapper;

        public DependencyChangeListener(DependentPropertyWrapper propertyWrapper) {
            this.propertyWrapper = propertyWrapper;
        }

        
        public void valueChange(ValueChangeEvent event) {
            if (!(event instanceof DependentPropertyValueChangeEvent)) {
                propertyWrapper.fireValueChangeEvent();
            }
        }
    }

    private class DependentPropertyWrapper implements Property, ValueChangeNotifier {

        private final Property wrapped;
        private final List<ValueChangeListener> listeners = new ArrayList<ValueChangeListener>();
        private final DependencyChangeListener changeListener = new DependencyChangeListener(this);

        public DependentPropertyWrapper(Property wrapped, Property[] dependencies) {
            this.wrapped = wrapped;

            for(Property p : dependencies) {
                if(p instanceof ValueChangeNotifier) {
                    ((ValueChangeNotifier)p).addListener(changeListener);
                }
            }
        }

        public Object getValue() {
            return wrapped.getValue();
        }

        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            wrapped.setValue(newValue);
        }

        public Class<?> getType() {
            return wrapped.getType();
        }

        public boolean isReadOnly() {
            return wrapped.isReadOnly();
        }

        public void setReadOnly(boolean newStatus) {
            wrapped.setReadOnly(newStatus);
        }

        public void addListener(ValueChangeListener listener) {
            if(wrapped instanceof ValueChangeNotifier) {
                ((ValueChangeNotifier)wrapped).addListener(listener);
            }
            listeners.add(listener);
        }

        public void removeListener(ValueChangeListener listener) {
            if(wrapped instanceof ValueChangeNotifier) {
                ((ValueChangeNotifier)wrapped).removeListener(listener);
            }
            listeners.remove(listener);
        }

        @Override
        public String toString() {
            return wrapped.toString();
        }


        private void fireValueChangeEvent() {
            ValueChangeEvent e = new DependentPropertyValueChangeEvent(this);
            for(ValueChangeListener l : listeners) {
                l.valueChange(e);
            }
        }

    }

    public PropertyDependencyManager(Item item) {
        this.item = item;
    }

    public Property addDependency(Object propertyId, String[] dependentPropertyIds) {
        Property prop = item.getItemProperty(propertyId);
        List<Property> propList = new ArrayList<Property>();
        for(String id : dependentPropertyIds) {
            Property dep = item.getItemProperty(id);
            propList.add(dep);
        }
        DependentPropertyWrapper wrapper = new DependentPropertyWrapper(prop, propList.toArray(new Property[0]));
        dependencyMap.put(propertyId, wrapper);
        return wrapper;
    }

    public void removeDependency(Object propertyId) {
        dependencyMap.remove(propertyId);
    }

    public Property getDependentProperty(Object propertyId) {
        return dependencyMap.get(propertyId);
    }

    public boolean isManagerFor(Object propertyId) {
        return dependencyMap.containsKey(propertyId);
    }

    
}
