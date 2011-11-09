package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.ProductCategory;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;



public class ProductCategoryCrudImpl extends ContentCrud<ProductCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCategoryCrudImpl.class);

    
    
    public ProductCategoryCrudImpl(EntityManager em, Organisation organisation, Crud.Editable<String, ProductCategory> productCategory) {
        super(em, organisation, ProductCategory.class);
    }

    
    /**
    * Returns all childrens of a category.

    @param      productCategoryId  Id of the product category.
    *  
    @return     A list of children id's.
    */
    private List getChildren(String productCategoryId) {
        Filter filter = new CompareFilter<String>("productCategoryParentId", productCategoryId, CompareFilter.CompareType.Equals);
        List<String> idList = listIds(filter, null, null);

        return idList;
    }
    
    
    /**
    * Returns the complete hierachy of a category.

    @param      productCategoryId  Id of the product category.
    *  
    @return     A hierachical list of id's, from root to this category.
    */
    private List getHierachy(String productCategoryId) {
        List<String> idList = null;
        ProductCategory productCategoryParent;
        Boolean go = true;
        
        idList.add(productCategoryId);
        
        while(go == true) {
            productCategoryParent = super.read(productCategoryId).getProductCategoryParent();

            if(productCategoryParent == null) {
                go = false;
            }
            else {
                productCategoryId = productCategoryParent.getId();
                idList.add(productCategoryId);
            }
            
        }
                
        return idList;
    }
    
    
    /**
    * Creates a product category attached to a parent.
    * If productCategoryParentId is null, the new category is created as a root with no parent.

    @param      productCategoryParentId  Id of the parent product category.
    *  
    @return     Id of the new product category.
    */
    @Transactional
    public String create(String productCategoryParentId) {

        String productCategoryId = super.create();
        ProductCategory productCategory = read(productCategoryId);
        
        if (productCategoryParentId != null) {
            ProductCategory productCategoryParent = read(productCategoryParentId);
            productCategory.setProductCategoryParent(productCategoryParent);
            
            update(productCategory);
        }

        return productCategoryId;
    }
        
}
