package dk.apaq.shopsystem.qash.data;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.data.util.BeanItem;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderLine;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import dk.apaq.vaadin.addon.crudcontainer.HasBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
public class OrderLineContainer implements Container, Container.ItemSetChangeNotifier,
        Container.Ordered, Container.Indexed {

    private final static Map<String, String[]> dependencyMap = new HashMap<String, String[]>();
    private static LinkedHashMap<String, Class> map;

    private final Map<Integer, BeanItem<OrderLine>> indexToItem = new HashMap<Integer, BeanItem<OrderLine>>();
    private final Map<Property, Property> propertyToProperty = new HashMap<Property, Property>();

    private Order order;
    private final List<ItemSetChangeListener> setChangeListeners = new ArrayList<ItemSetChangeListener>();

    static {
        map = CrudContainer.getPropertyDescriptors(OrderLine.class);

        dependencyMap.put("price", new String[]{"priceWithTax"});
        dependencyMap.put("priceWithTax", new String[]{"price"});
        dependencyMap.put("totalWithTax", new String[]{"price", "priceWithTax", "discountPercentage", "quantity"});


    }




    
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

        private final DependentPropertyWrapper dependentPropertyWrapper;

        public DependencyChangeListener(DependentPropertyWrapper dependentPropertyWrapper) {
            this.dependentPropertyWrapper = dependentPropertyWrapper;
        }

        public void valueChange(ValueChangeEvent event) {
            if(!(event instanceof DependentPropertyValueChangeEvent)) {
                dependentPropertyWrapper.fireValueChangeEvent();
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

        private void fireValueChangeEvent() {
            ValueChangeEvent e = new DependentPropertyValueChangeEvent(this);
            for(ValueChangeListener l : listeners) {
                l.valueChange(e);
            }
        }

    }

    private class OrderLineSetChangeEvent implements ItemSetChangeEvent {

        private final Container parent;

        public OrderLineSetChangeEvent(Container parent) {
            this.parent = parent;
        }

        public Container getContainer() {
            return parent;
        }

    }

    public OrderLineContainer() {
    }



    public OrderLineContainer(Item item) {
        setDatasource(item);
    }


    public OrderLineContainer(HasBean<Order> item) {
        setDatasource(item);
    }

    public OrderLineContainer(Order order) {
        this.setDatasource(order);
    }

    public Order getDatasource() {
        return this.order;
    }

    public void setDatasource(Order order) {
        this.order = order;
        fireSetChange();
    }

    public void setDatasource(HasBean<Order> item) {
        this.setDatasource((Order)item.getBean());
    }

    public void setDatasource(Item item) {
        if(item instanceof HasBean) {
            Object o = ((HasBean)item).getBean();
            if(!(o instanceof Order)) {
                throw new IllegalArgumentException("Item has bean, but type was not " + Order.class.getCanonicalName());
            }
            this.setDatasource((Order) o);
        } else {
            throw new IllegalArgumentException("Item does not have a bean.");
        }
    }

    public int indexOfId(Object itemId) {
        return (Integer) itemId;
    }

    public Object getIdByIndex(int index) {
        return index;
    }

    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object nextItemId(Object itemId) {
        int index = (Integer) itemId;
        if(index<order.getOrderLineCount()-1) {
            return index+1;
        } else {
            return null;
        }
    }

    public Object prevItemId(Object itemId) {
        int index = (Integer) itemId;
        if(index>0) {
            return index-1;
        } else {
            return null;
        }
    }

    public Object firstItemId() {
        return order.getOrderLineCount()>0 ? 0 : null;
    }

    public Object lastItemId() {
        return order.getOrderLineCount()>0 ? order.getOrderLineCount()-1 : null;
    }

    public boolean isFirstId(Object itemId) {
        return ((Integer)itemId) == 0;
    }

    public boolean isLastId(Object itemId) {
        return ((Integer)itemId) == order.getOrderLineCount()-1;
    }

    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Item getItem(Object itemId) {
        if(order==null) {
            return null;
        }
        
        int index = (Integer) itemId;
        if(index>=0 && index < order.getOrderLineCount()) {
            BeanItem item = indexToItem.get(itemId);
            if(item==null) {
                item = new BeanItem(order.getOrderLine(index));
                indexToItem.put(index, item);
            }
            return item;
        } else {
            return null;
        }
        
    }

    public Collection<?> getContainerPropertyIds() {
        return map.keySet();
    }

    public Collection<?> getItemIds() {
        List<Integer> ids = new ArrayList<Integer>();
        if(order!=null) {
            for(int i=0;i<order.getOrderLineCount();i++) {
                ids.add(i);
            }
        }
        return ids;
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        Item item = getItem(itemId);
        Property prop = item.getItemProperty(propertyId);


        //Check if we should add changelistener
        Property wrappedProp = propertyToProperty.get(prop);
        if(wrappedProp==null) {
            //find dependencies
            String[] depsIds = dependencyMap.get(propertyId);
            if(depsIds!=null) {
                List<Property> depsList = new ArrayList<Property>();
                for(String id : depsIds) {
                    depsList.add(item.getItemProperty(id));
                }
                wrappedProp = new DependentPropertyWrapper(prop, depsList.toArray(new Property[0]));
                propertyToProperty.put(prop, wrappedProp);
                prop = wrappedProp;
            }
        } else {
            prop = wrappedProp;
        }
        return prop;
    }

    public Class<?> getType(Object propertyId) {
        return map.get((String)propertyId);
    }

    public int size() {
        return order == null ? 0 : order.getOrderLineCount();
    }

    public boolean containsId(Object itemId) {
        if(order==null) {
            return false;
        }

        int index = (Integer) itemId;
        return index>=0 && index < order.getOrderLineCount();
    }

    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object addItem() throws UnsupportedOperationException {
        if(order==null || !isEditable()) {
            return null;
        }

        int index = order.getOrderLineCount();
        order.addOrderLine("", 0, 0, null);
        fireSetChange();
        return index;
    }

    public Object addItem(Product product) {
        if(order==null || !isEditable()) {
            return null;
        }

        int index = order.getOrderLineCount();
        order.addOrderLine(product);
        fireSetChange();
        return index;
    }

    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        if(order==null || !isEditable()) {
            return false;
        }

        int index = (Integer) itemId;
        OrderLine line =  order.removeOrderLine(index);
        fireSetChange();
        return line != null;
    }

    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        if(order==null || !isEditable()) {
            return false;
        }

        order.clear();
        fireSetChange();
        return true;
    }

    public void addListener(ItemSetChangeListener listener) {
        setChangeListeners.add(listener);
    }

    public void removeListener(ItemSetChangeListener listener) {
        setChangeListeners.remove(listener);
    }

    private void fireSetChange() {
        this.indexToItem.clear();

        OrderLineSetChangeEvent e = new OrderLineSetChangeEvent(this);
        for(ItemSetChangeListener listener : setChangeListeners) {
            listener.containerItemSetChange(e);
        }
    }

    private boolean isEditable() {
        return order.getStatus() == OrderStatus.New || order.getStatus() == OrderStatus.Processing;
    }

}
