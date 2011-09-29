package dk.apaq.shopsystem.service;

import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Order;
import java.util.Map;
import java.util.WeakHashMap;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import dk.apaq.crud.Crud.*;
import dk.apaq.crud.CrudNotifier;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vfs.FileSystem;
import javax.persistence.PersistenceContext;
/**
 *
 * @author michaelzachariassenkrog
 */
public class SystemServiceImpl implements SystemService, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(SystemServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    private OrganisationCrud orgCrud;
    private Crud.Complete<String, SystemUser> systemUserCrud;
    private Crud.Filterable<String, Website> websiteCrud;
    private final Map<String, OrganisationService> orgServiceMap = new WeakHashMap<String,OrganisationService>();
    private final Map<String, Complete<String, Order>> crudMap = new WeakHashMap<String, Complete<String, Order>>();
    private ApplicationContext context;
    private FileSystem fileSystem;
    private FileSystemPopulator filesystemPopulator;
    
    public void setFileSystemPopulator(FileSystemPopulator populator) {
        this.filesystemPopulator = populator;
    }

    @Override
    public OrganisationService getOrganisationService(Organisation org) {
        OrganisationService service = orgServiceMap.get(org.getId());
        if(service==null) {
            service = (OrganisationService) context.getBean("organisationService", org);
            orgServiceMap.put(org.getId(), service);
        }
        return service;
    }


    
    @Override
    public OrganisationCrud getOrganisationCrud() {
        LOG.debug("Retrieving OrganisationCrud");
        if(orgCrud==null) {
            orgCrud = (OrganisationCrud) context.getBean("organisationCrud", em);
            ((CrudNotifier)orgCrud).addListener(new SecurityHandler.OrganisationSecurity());
        }
        return orgCrud;
    }

    @Override
    public Complete<String, SystemUser> getSystemUserCrud() {
        LOG.debug("Retrieving AccountCrud");
        if(systemUserCrud==null) {
            systemUserCrud = (Crud.Complete<String, SystemUser>) context.getBean("crud", em, SystemUser.class);
            ((CrudNotifier)systemUserCrud).addListener(new SecurityHandler.AccountSecurity());
        }
        return systemUserCrud;
    }

    @Override
    public FileSystem getFileSystem() {
        if(fileSystem==null) {
            //Must create a filesystem for the system using Commons VFS and a local File Folder.
            fileSystem =context.getBean("filesystem", FileSystem.class);

            if(filesystemPopulator!=null) {
                filesystemPopulator.populate(fileSystem);
            }
            
        }
        return fileSystem;
    }

    @Override
    public Filterable<String, Website> getWebsites() {
        LOG.debug("Retrieving crud for all websites.");
        if(websiteCrud==null) {
            websiteCrud = (Crud.Filterable<String, Website>) context.getBean("crud", em, Website.class);
            //((CrudNotifier)systemUserCrud).addListener(new SecurityHandler.AccountSecurity());
        }
        return websiteCrud;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

   
}
