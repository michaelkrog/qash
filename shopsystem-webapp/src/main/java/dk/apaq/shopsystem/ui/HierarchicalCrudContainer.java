package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container.Hierarchical;
import dk.apaq.crud.Crud;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.CompareFilter.CompareType;
import dk.apaq.filter.core.IsNullFilter;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.entity.ProductCategory;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.util.List;
import java.util.Collection;


public class HierarchicalCrudContainer<IDT, BT> extends CrudContainer<IDT, BT> implements Hierarchical {

    private String parentPropertyId;
    private String childrenAllowedPropertyId;
            

    public HierarchicalCrudContainer(Crud<IDT, BT> crud, Class beanClass) {
        super(crud, beanClass);
    }

   
    private void validateSettings() {
        if (parentPropertyId == null) {
            throw new NullPointerException("parentPropertyId has not been set.");
        }
        if (childrenAllowedPropertyId == null) {
            throw new NullPointerException("childrenAllowedPropertyId has not been set.");
        }
    }
    
    public void setParentPropertyId(String parentPropertyId) {
        this.parentPropertyId = parentPropertyId;
    }
    
    public void setChildrenAllowedPropertyId(String childrenAllowedPropertyId) {
        this.childrenAllowedPropertyId = childrenAllowedPropertyId;
    }
    
    @Override
    public Collection<?> getChildren(Object itemId) {
        validateSettings();
        CompareFilter compareFilter = new CompareFilter(this.parentPropertyId, crud.read((IDT) itemId), CompareType.Equals);
        Collection idList = getItemIdList(compareFilter, null);

        return idList;
    }

    @Override
    public Object getParent(Object itemId) {
        validateSettings();
        return getItem(itemId).getItemProperty(this.parentPropertyId);
    }

    @Override
    public Collection<?> rootItemIds() {
        validateSettings();
        IsNullFilter filter = new IsNullFilter(this.parentPropertyId); 
        Collection idList = getItemIdList(filter, null);

        return idList;
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        validateSettings();
        getItem(itemId).getItemProperty(this.parentPropertyId).setValue(newParentId);
                
        return true;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        System.out.println("areChildrenAllowed");
        validateSettings();
        if (getItem(itemId).getItemProperty(this.childrenAllowedPropertyId).getValue() == null) {
           System.out.println("Nope...");
            return false; 
        }
        else if ("true".equals(getItem(itemId).getItemProperty(this.childrenAllowedPropertyId).getValue().toString())) {
            System.out.println("Yeps...");
            return true;
        }
        else {
            System.out.println("Nope...");
            return false;
        }
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        validateSettings();
        getItem(itemId).getItemProperty(this.childrenAllowedPropertyId).setValue(areChildrenAllowed);
        
        return true;
    }

    @Override
    public boolean isRoot(Object itemId) {
        validateSettings();
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
