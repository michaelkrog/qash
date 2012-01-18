package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.file.FileSystemPopulator;
import dk.apaq.crud.Crud;
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
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.core.LikeFilter;
import dk.apaq.filter.core.OrFilter;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.security.SecurityRoles;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vfs.FileSystem;
import java.util.List;
import javax.persistence.PersistenceContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author michaelzachariassenkrog
 */
public class SystemServiceImpl implements SystemService, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(SystemServiceImpl.class);

    @PersistenceContext
    private EntityManager em;
    
    private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    private OrganisationCrud orgCrud;
    private Crud.Complete<String, SystemUser> systemUserCrud;
    private Crud.Filterable<String, Domain> domainCrud;
    private final Map<String, OrganisationService> orgServiceMap = new WeakHashMap<String,OrganisationService>();
    private final Map<String, Complete<String, Order>> crudMap = new WeakHashMap<String, Complete<String, Order>>();
    private ApplicationContext context;
    private FileSystem fileSystem;
    private FileSystemPopulator filesystemPopulator;
    private final SecurityHandler.SystemUserSecurity systemUserSecurity;
    private final SecurityHandler.OrganisationSecurity organisationSecurity;

    public SystemServiceImpl() {
        organisationSecurity = new SecurityHandler.OrganisationSecurity(this);
        systemUserSecurity = new SecurityHandler.SystemUserSecurity();
    }

    
    public void setFileSystemPopulator(FileSystemPopulator populator) {
        this.filesystemPopulator = populator;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
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
            ((CrudNotifier)orgCrud).addListener(organisationSecurity);
        }
        return orgCrud;
    }

    @Override
    public Complete<String, SystemUser> getSystemUserCrud() {
        LOG.debug("Retrieving AccountCrud");
        if(systemUserCrud==null) {
            systemUserCrud = (Crud.Complete<String, SystemUser>) context.getBean("crud", em, SystemUser.class);
            ((CrudNotifier)systemUserCrud).addListener(systemUserSecurity);
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
    public Filterable<String, Domain> getDomains() {
        LOG.debug("Retrieving crud for all websites.");
        if(domainCrud==null) {
            domainCrud = (Crud.Filterable<String, Domain>) context.getBean("crud", em, Domain.class);
            //((CrudNotifier)systemUserCrud).addListener(new SecurityHandler.AccountSecurity());
        }
        return domainCrud;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    @Transactional
    public Organisation createOrganisation(SystemUser user, Organisation organisation) {
        if(organisation.getId()!=null) {
            throw new IllegalArgumentException("Organisation has already been persisted.");
        }

        String orgId = getOrganisationCrud().create(organisation);
        organisation = getOrganisationCrud().read(orgId);
        
        if(user.getId() == null) {
            String userId = getSystemUserCrud().create(user) ;
            user = getSystemUserCrud().read(userId);
        }
        
        OrganisationService organisationService = getOrganisationService(organisation);
        OrganisationUserReference orgUser = new OrganisationUserReference();
        orgUser.setOrganisation(organisation);
        orgUser.setUser(user);
        orgUser.getRoles().add(SecurityRoles.ROLE_ORGADMIN.name());
        orgUser.getRoles().add(SecurityRoles.ROLE_ORGPAYER.name());
        organisationService.getUsers().create(orgUser);

        return organisation;
    }

    @Override
    public Organisation getMainOrganisation() {
        List<Organisation> orgList = getOrganisationCrud().list(new CompareFilter("mainOrganisation", true, CompareFilter.CompareType.Equals), null);
        if(orgList.isEmpty()) {
            LOG.error("Main organisation not found");
            throw new RuntimeException("Main organisation not found.");
        }
        return orgList.get(0);
    }

    @Override
    public boolean hasUserVerifiedEmail(String userName) {
        Filter filter = new AndFilter(new LikeFilter("name", userName, false), new CompareFilter("emailVerified", true, CompareFilter.CompareType.Equals));
        return !getSystemUserCrud().list(filter, null).isEmpty();
    }

    @Override
    public boolean isUsernameInUse(String userName) {
        return !getSystemUserCrud().list(new LikeFilter("name", userName, false), null).isEmpty();
    }

    @Override
    public SystemUser getSystemUser(String name) {
        Filter filter = new CompareFilter("name", name, CompareFilter.CompareType.Equals);
        List<SystemUser> userlist = getSystemUserCrud().list(filter, null);
        return userlist.isEmpty() ? null : userlist.get(0);
    }
    
    
    
    
    
}
