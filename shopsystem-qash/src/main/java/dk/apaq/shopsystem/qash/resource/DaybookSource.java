package dk.apaq.shopsystem.qash.resource;

import com.vaadin.terminal.StreamResource.StreamSource;
import dk.apaq.shopsystem.data.DataExchange;
import dk.apaq.shopsystem.entity.Order;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 *
 * @author michael
 */
public class DaybookSource implements StreamSource {
    private final List<Order> orders;
    private final int account;
    private final int offsetAccount;

    public DaybookSource(List<Order> orders, int account, int offsetAccount) {
        this.orders = orders;
        this.account = account;
        this.offsetAccount = offsetAccount;
    }

    @Override
    public InputStream getStream() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataExchange.writePostings(orders, account, offsetAccount, out);
        
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    
}
