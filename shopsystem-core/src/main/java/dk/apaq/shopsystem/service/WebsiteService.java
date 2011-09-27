package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;

/**
 *
 * @author michael
 */
public interface WebsiteService {
    
    public Website readWebsite();
    public void updateWebsite(Website website);

    
    Crud.Editable<String, Domain> getDomains();
    Crud.Complete<String,Page> getPages();
    
}
