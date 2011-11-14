package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container.Hierarchical;
import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.CompareFilter.CompareType;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.util.List;
import java.util.Collection;


public class HierarchicalCrudContainer<IDT, BT> extends CrudContainer<IDT, BT> implements Hierarchical {

    private String parentPropertyId = "";
    private String childrenAllowedPropertyId = "";
            

    public HierarchicalCrudContainer(Crud<IDT, BT> crud, Class beanClass) {
        super(crud, beanClass);
    }

   
    public void setParentPropertyId(String parentPropertyId) {
        this.parentPropertyId = parentPropertyId;
    }
    
    public void setChildrenAllowedPropertyId(String childrenAllowedPropertyId) {
        this.childrenAllowedPropertyId = childrenAllowedPropertyId;
    }
    
    @Override
    public Collection<?> getChildren(Object itemId) {
        CompareFilter compareFilter = new CompareFilter(this.parentPropertyId, getItem(itemId).getItemProperty(this.parentPropertyId), CompareType.Equals);
        List idList = getItemIdList(compareFilter, null);
        
        return idList;
    }

    @Override
    public Object getParent(Object itemId) {
        return getItem(itemId).getItemProperty(this.parentPropertyId);
    }

    @Override
    public Collection<?> rootItemIds() {
        CompareFilter compareFilter = new CompareFilter(this.parentPropertyId, null, CompareType.Equals);
        List idList = getItemIdList(compareFilter, null);
        
        return idList;
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        getItem(itemId).getItemProperty(this.parentPropertyId).setValue(newParentId);
                
        return true;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        //return getItem(itemId).getItemProperty(this.childrenAllowedPropertyId).getValue();
        //@todo: how to get the boolean value from this item? Always true for now.
        return true;
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        getItem(itemId).getItemProperty(this.childrenAllowedPropertyId).setValue(areChildrenAllowed);
        
        return true;
    }

    @Override
    public boolean isRoot(Object itemId) {
        if(getItem(itemId).getItemProperty(this.parentPropertyId).getValue() == null) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean hasChildren(Object itemId) {
        if(getChildren(itemId).isEmpty()) {
            return false;
        }
        else {
            return true;
        }
            
    }
    
}
