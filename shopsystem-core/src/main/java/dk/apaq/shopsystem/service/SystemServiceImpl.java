package dk.apaq.shopsystem.service;

import dk.apaq.shopsystem.file.FileSystemPopulator;
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
import dk.apaq.filter.jpa.FilterTranslatorForJPA;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.OrganisationUserReference;
import dk.apaq.shopsystem.service.crud.OrganisationCrud;
import dk.apaq.vfs.FileSystem;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
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

    public SystemServiceImpl() {
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
            ((CrudNotifier)orgCrud).addListener(new SecurityHandler.OrganisationSecurity(this));
        }
        return orgCrud;
    }

    @Override
    public Complete<String, SystemUser> getSystemUserCrud() {
        LOG.debug("Retrieving AccountCrud");
        if(systemUserCrud==null) {
            systemUserCrud = (Crud.Complete<String, SystemUser>) context.getBean("crud", em, SystemUser.class);
            ((CrudNotifier)systemUserCrud).addListener(new SecurityHandler.SystemUserSecurity());
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
        if(user.getId()!=null) {
            throw new IllegalArgumentException("User has already been persisted.");
        }

        if(organisation.getId()!=null) {
            throw new IllegalArgumentException("Organisation has already been persisted.");
        }

        String orgId = getOrganisationCrud().create(organisation);
        organisation = getOrganisationCrud().read(orgId);
        
        String userId = getSystemUserCrud().create(user);
        user = getSystemUserCrud().read(userId);

        OrganisationService organisationService = getOrganisationService(organisation);
        OrganisationUserReference orgUser = new OrganisationUserReference();
        orgUser.setOrganisation(organisation);
        orgUser.setUser(user);
        organisationService.getUsers().create(orgUser);

        if(mailSender!=null) { 
            SimpleMailMessage msg = this.templateMessage == null ? new SimpleMailMessage() : new SimpleMailMessage(this.templateMessage);
            msg.setSubject("New account");
            msg.setTo(user.getEmail());
            msg.setText(
                "Dear " + user.getDisplayName()
                    + ", thank you for creating a new account. \n\nYour credentials are:\n"
                    + "username: " + user.getName() + "\n"
                    + "password: " + user.getPassword() + "\n\n"
                    + "Best Regards\n"
                    + "The Qash team.");
            try{
                this.mailSender.send(msg);
            }
            catch(MailException ex) {
                // simply log it and go on...
                LOG.error("Unable to send mail.", ex);      
            }
        }
        return organisation;
    }


   
}
