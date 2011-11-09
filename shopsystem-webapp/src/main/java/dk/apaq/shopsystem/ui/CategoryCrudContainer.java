/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.shopsystem.ui;

import com.vaadin.data.Container.Hierarchical;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Complete;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.ProductCategory;
import dk.apaq.vaadin.addon.crudcontainer.CrudContainer;
import java.util.List;
import java.util.Collection;

/**
 *
 * @author Martin Christensen
 */
public class CategoryCrudContainer extends CrudContainer<String, ProductCategory> implements Hierarchical {

    public CategoryCrudContainer(Complete<String, ProductCategory> complete) {
        super(complete, ProductCategory.class);
    }
   
    @Override
    public Collection<?> getChildren(Object itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getParent(Object itemId) {
        return crud.read(itemId.toString()).getProductCategoryParent().getId();
    }

    @Override
    public Collection<?> rootItemIds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
        ProductCategory productCategoryParent = crud.read(newParentId.toString());
        crud.read(itemId.toString()).setProductCategoryParent(productCategoryParent);
                
        return true;
    }

    @Override
    public boolean areChildrenAllowed(Object itemId) {
        return crud.read(itemId.toString()).getChildrenAllowed();
    }

    @Override
    public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
        crud.read(itemId.toString()).setChildrenAllowed(areChildrenAllowed);
        
        return true;
    }

    @Override
    public boolean isRoot(Object itemId) {
        if(crud.read(itemId.toString()).getProductCategoryParent() == null) {
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
