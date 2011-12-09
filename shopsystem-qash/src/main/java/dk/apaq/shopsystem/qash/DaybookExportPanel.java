package dk.apaq.shopsystem.qash;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window.Notification;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class DaybookExportPanel extends CustomComponent {
    
    private final Form form = new Form();
    private PostingsData data;
    private Button btnDownload = new Button("Download");
    
    public static class PostingsData {
        private Date dateFrom;
        private Date dateTo;
        private String account;
        private String offsetAccount;

        public String getAccount() {
            return account;
        }

        public Date getDateFrom() {
            return dateFrom;
        }

        public Date getDateTo() {
            return dateTo;
        }

        public String getOffsetAccount() {
            return offsetAccount;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setDateFrom(Date dateFrom) {
            this.dateFrom = dateFrom;
        }

        public void setDateTo(Date dateTo) {
            this.dateTo = dateTo;
        }

        public void setOffsetAccount(String offsetAccount) {
            this.offsetAccount = offsetAccount;
        }
        
        
    }

    public DaybookExportPanel() {
        form.getFooter().addComponent(btnDownload);
        setCompositionRoot(form);
        
        btnDownload.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                PipedInputStream inForReturn = null;
                try {
                    PipedOutputStream out = new PipedOutputStream();
                    inForReturn = new PipedInputStream(out);
                    DownloadStream downloadStream = new DownloadStream(inForReturn, "text/csv", "daybook.csv");
                    getWindow().open(null);
                } catch (IOException ex) {
                    getWindow().showNotification(ex.getMessage(), Notification.TYPE_ERROR_MESSAGE);
                } finally {
                    try {
                        inForReturn.close();
                    } catch (IOException ex) {}
                }
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
    
    
    
}
