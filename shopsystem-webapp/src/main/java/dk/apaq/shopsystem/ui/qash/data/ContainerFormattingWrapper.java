package dk.apaq.shopsystem.ui.qash.data;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ContainerFormattingWrapper implements Container,
    Container.ItemSetChangeNotifier, Container.PropertySetChangeNotifier {

    private final Container wrapped;
    private final Map<Object, PropertyWrapper> wrapperMap = new HashMap<Object, PropertyWrapper>();
    private final Map<Property, Property> propertyMap = new HashMap<Property, Property>();

    public static class WithIndexed extends ContainerFormattingWrapper implements Container.Indexed {

        private Container.Indexed indexedWrapper;

        public WithIndexed(Indexed indexedWrapper) {
            super(indexedWrapper);
            this.indexedWrapper = indexedWrapper;
        }


        public int indexOfId(Object itemId) {
            return indexedWrapper.indexOfId(itemId);
        }

        public Object getIdByIndex(int index) {
            return indexedWrapper.getIdByIndex(index);
        }

        public Object addItemAt(int index) throws UnsupportedOperationException {
            return indexedWrapper.addItemAt(index);
        }

        public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
            return indexedWrapper.addItemAt(index, newItemId);
        }

        public Object nextItemId(Object itemId) {
            return indexedWrapper.nextItemId(itemId);
        }

        public Object prevItemId(Object itemId) {
            return indexedWrapper.prevItemId(itemId);
        }

        public Object firstItemId() {
            return indexedWrapper.firstItemId();
        }

        public Object lastItemId() {
            return indexedWrapper.lastItemId();
        }

        public boolean isFirstId(Object itemId) {
            return indexedWrapper.isFirstId(itemId);
        }

        public boolean isLastId(Object itemId) {
            return indexedWrapper.isLastId(itemId);
        }

        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            return indexedWrapper.addItemAfter(previousItemId);
        }

        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            return indexedWrapper.addItemAfter(previousItemId, newItemId);
        }


    }

    public class WithOrdered extends ContainerFormattingWrapper implements Container.Ordered {

        private Container.Ordered orderedWrapped;

        public WithOrdered(Container.Ordered wrapped) {
            super(wrapped);
            this.orderedWrapped = wrapped;
        }

        public Object nextItemId(Object itemId) {
            return orderedWrapped.nextItemId(itemId);
        }

        public Object prevItemId(Object itemId) {
            return orderedWrapped.prevItemId(itemId);
        }

        public Object firstItemId() {
            return orderedWrapped.firstItemId();
        }

        public Object lastItemId() {
            return orderedWrapped.lastItemId();
        }

        public boolean isFirstId(Object itemId) {
            return orderedWrapped.isFirstId(itemId);
        }

        public boolean isLastId(Object itemId) {
            return orderedWrapped.isLastId(itemId);
        }

        public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
            return orderedWrapped.addItemAfter(previousItemId);
        }

        public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
            return orderedWrapped.addItemAfter(previousItemId, newItemId);
        }


    }

    /*
     * Registers a new Item set change listener for this Container. Don't add a
     * JavaDoc comment here, we use the default documentation from implemented
     * interface.
     */
    public void addListener(Container.ItemSetChangeListener listener) {
        if (wrapped instanceof Container.ItemSetChangeNotifier) {
            ((Container.ItemSetChangeNotifier) wrapped).addListener(listener);
        }
    }

    /*
     * Removes a Item set change listener from the object. Don't add a JavaDoc
     * comment here, we use the default documentation from implemented
     * interface.
     */
    public void removeListener(Container.ItemSetChangeListener listener) {
        if (wrapped instanceof Container.ItemSetChangeNotifier) {
            ((Container.ItemSetChangeNotifier) wrapped).removeListener(listener);
        }
    }

    /*
     * Registers a new Property set change listener for this Container. Don't
     * add a JavaDoc comment here, we use the default documentation from
     * implemented interface.
     */
    public void addListener(Container.PropertySetChangeListener listener) {
        if (wrapped instanceof Container.PropertySetChangeNotifier) {
            ((Container.PropertySetChangeNotifier) wrapped).addListener(listener);
        }
    }

    /*
     * Removes a Property set change listener from the object. Don't add a
     * JavaDoc comment here, we use the default documentation from implemented
     * interface.
     */
    public void removeListener(Container.PropertySetChangeListener listener) {
        if (wrapped instanceof Container.PropertySetChangeNotifier) {
            ((Container.PropertySetChangeNotifier) wrapped).removeListener(listener);
        }
    }

    public interface PropertyWrapper {
        Property createWrappedProperty(Object propertyId, Property wrapped);
    }

    public ContainerFormattingWrapper(Container wrapped) {
        this.wrapped = wrapped;

        if(wrapped instanceof Container.ItemSetChangeNotifier) {
            ((ItemSetChangeNotifier)wrapped).addListener(new ItemSetChangeListener() {

                public void containerItemSetChange(ItemSetChangeEvent event) {
                    propertyMap.clear();
                }
            });
        }
    }

    public void addWrapper(Object propertyId, PropertyWrapper wrapper) {
        wrapperMap.put(propertyId, wrapper);
    }

    public Item getItem(Object itemId) {
        return wrapped.getItem(itemId);
    }

    public Collection<?> getContainerPropertyIds() {
        return wrapped.getContainerPropertyIds();
    }

    public Collection<?> getItemIds() {
        return wrapped.getItemIds();
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        Property property = wrapped.getContainerProperty(itemId, propertyId);
        if(property==null) {
            return null;
        }

        Property wrappedProperty = propertyMap.get(property);
        if(wrappedProperty!=null) {
            return wrappedProperty;
        } else {
            PropertyWrapper wrapper = wrapperMap.get(propertyId);
            if(wrapper!=null) {
            wrappedProperty = wrapper.createWrappedProperty(propertyId, property);
                if(wrappedProperty!=null) {
                    propertyMap.put(property, wrappedProperty);
                    property = wrappedProperty;
                }
            }
            return property;
        }
    }

    public Class<?> getType(Object propertyId) {
        return wrapped.getType(propertyId);
    }

    public int size() {
        return wrapped.size();
    }

    public boolean containsId(Object itemId) {
        return wrapped.containsId(itemId);
    }

    public Item addItem(Object itemId) throws UnsupportedOperationException {
        return wrapped.addItem(itemId);
    }

    public Object addItem() throws UnsupportedOperationException {
        return wrapped.addItem();
    }

    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        return wrapped.removeItem(itemId);
    }

    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        return wrapped.addContainerProperty(propertyId, type, defaultValue);
    }

    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        return wrapped.removeContainerProperty(propertyId);
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        return removeAllItems();
    }



}
