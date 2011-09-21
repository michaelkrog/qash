package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.entity.Category;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Website;
import dk.apaq.crud.Crud;
import dk.apaq.shopsystem.service.crud.InventoryManager;
import dk.apaq.shopsystem.service.crud.SecurityHandler;
import dk.apaq.shopsystem.entity.SystemUser;
import dk.apaq.shopsystem.entity.Product;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Tax;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
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
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.FileSystem;
import dk.apaq.vfs.impl.nativefs.NativeFileSystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
            try {
                //Must create a filesystem for the system using Commons VFS and a local File Folder.
                fileSystem =context.getBean("filesystem", FileSystem.class);

                Directory root = fileSystem.getRoot();
                if(!root.hasDirectory("Organisations")) root.createDirectory("Organisations");

                Directory system = root.getDirectory("System", true);
                
                Directory modules = system.getDirectory("Modules", true);
                if(!modules.hasDirectory("Standard")) modules.createDirectory("Standard");
                if(!modules.hasDirectory("Optional")) modules.createDirectory("Optional");

                Directory templates = system.getDirectory("Themes", true);
                if(!templates.hasDirectory("Standard")) templates.createDirectory("Standard");
                if(!templates.hasDirectory("Optional")) templates.createDirectory("Optional");

                if(filesystemPopulator!=null) {
                    filesystemPopulator.populate(fileSystem);
                }
            } catch (IOException ex) {
                LOG.error("Unable to resolve filesystem.", ex);
                throw new RuntimeException(ex);
            }
        }
        return fileSystem;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

   
}
