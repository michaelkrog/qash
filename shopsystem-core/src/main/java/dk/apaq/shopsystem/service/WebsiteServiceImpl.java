package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.crud.Crud.Complete;
import dk.apaq.crud.Crud.Editable;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Page;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import java.util.Map;
import java.util.WeakHashMap;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author michael
 */
public class WebsiteServiceImpl implements WebsiteService, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(OrganisationServiceImpl.class);

    private final Map<Class, Complete> crudMap = new WeakHashMap<Class, Complete>();
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private SystemService service;
    
    private final OrganisationService organisationService;
    private final String websiteId;
    private ApplicationContext context;
    

    public WebsiteServiceImpl(OrganisationService organisationService, String websiteId) {
        this.organisationService = organisationService;
        this.websiteId = websiteId;
    }
    
    @Override
    public Website readWebsite() {
        return this.organisationService.getWebsites().read(websiteId);
    }

    @Override
    public void updateWebsite(Website website) {
        this.organisationService.getWebsites().update(website);
    }

    @Override
    public Editable<String, Domain> getDomains() {
        return getGenericWebCrud(Domain.class);
    }

    @Override
    public Complete<String, Page> getPages() {
        return getGenericWebCrud(Page.class);
    }
    
    
    private <T> Complete<String, T> getGenericWebCrud(Class<T> clazz) {
        Website site = readWebsite();
        Complete crud = crudMap.get(clazz);
        if(crud==null) {
            crud = (Crud.Complete<String, T>) context.getBean("contentCrud", em, site, clazz);
            //((CrudNotifier)crud).addListener(new SecurityHandler.ContentSecurity(site));
            crudMap.put(clazz, crud);
        }
        return crud;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
    
}
