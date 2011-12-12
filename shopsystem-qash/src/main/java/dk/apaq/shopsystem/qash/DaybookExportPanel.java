package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window.Notification;
import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.data.DataExchange;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.qash.resource.DaybookSource;
import dk.apaq.shopsystem.service.OrganisationService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class DaybookExportPanel extends CustomComponent {
    
    private static final Logger LOG = LoggerFactory.getLogger(DaybookExportPanel.class);
    
    private final Form form = new Form();
    private PostingsData data;
    private Button btnDownload = new Button("Download");
    private OrganisationService organisationService;
    
    public static class PostingsData {
        private Date dateFrom = new Date();;
        private Date dateTo = new Date();;
        private int account;
        private int offsetAccount;

        public int getAccount() {
            return account;
        }

        public Date getDateFrom() {
            return dateFrom;
        }

        public Date getDateTo() {
            return dateTo;
        }

        public int getOffsetAccount() {
            return offsetAccount;
        }

        public void setAccount(int account) {
            this.account = account;
        }

        public void setDateFrom(Date dateFrom) {
            this.dateFrom = dateFrom;
        }

        public void setDateTo(Date dateTo) {
            this.dateTo = dateTo;
        }

        public void setOffsetAccount(int offsetAccount) {
            this.offsetAccount = offsetAccount;
        }
        
        
    }

    public DaybookExportPanel() {
        form.getLayout().setMargin(true);
        form.getFooter().addComponent(btnDownload);
        form.getFooter().setMargin(true);
        
        setCompositionRoot(form);
        
        btnDownload.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(organisationService == null) {
                    LOG.error("organsiationservice not set.");
                    return;
                }
                Date from = data.dateFrom;
                Date to = data.dateTo;
                to.setHours(23);
                to.setMinutes(59);
                to.setSeconds(60);
                
                Filter filter = new AndFilter(new CompareFilter("dateInvoiced", from, CompareFilter.CompareType.GreaterThan),
                                            new CompareFilter("dateInvoiced", to, CompareFilter.CompareType.LessThan));
                List<Order> orders = organisationService.getOrders().list(filter, null);
                StreamSource source = new DaybookSource(orders, data.account, data.offsetAccount);
                StreamResource resource = new StreamResource(source, "daybook", getApplication());
                resource.setMIMEType("text/csv");
                getWindow().open(resource);
            }
        });
    }

    @Override
    public void attach() {
        super.attach();
        
        data = new PostingsData();
        Item item = new BeanItem(data);
        
        form.setItemDataSource(item);
        form.setVisibleItemProperties(new String[]{"dateFrom", "dateTo", "account", "offsetAccount"});
       
    }

    public void setOrganisationService(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }
    
    
    
}
