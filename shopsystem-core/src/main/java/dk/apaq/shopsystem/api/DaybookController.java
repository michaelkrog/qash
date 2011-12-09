package dk.apaq.shopsystem.api;

import dk.apaq.filter.Filter;
import dk.apaq.filter.core.AndFilter;
import dk.apaq.filter.core.CompareFilter;
import dk.apaq.filter.sort.Sorter;
import dk.apaq.shopsystem.data.DataExchange;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.beans.PropertyEditorSupport;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author michael
 */
@Controller
public class DaybookController extends AbstractController {

    private static final DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public DaybookController(SystemService service) {
        super(service);
    }
    
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {  
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {

            @Override
            public void setAsText(String value) throws IllegalArgumentException {
                try {
                    setValue(isoDateFormat.parse(value));
                } catch (ParseException e) {
                    setValue(null);
                }
            }

            @Override
            public String getAsText() {
                return isoDateFormat.format((Date) getValue());
            }
        });
    }

    @RequestMapping(value = "/organisations/{orgInfo}/daybook", method = RequestMethod.GET) //List<Order> orderlist, List<Tax> taxlist, int account, int offsetaccount, OutputStream out
    public void getDaybook(@PathVariable String orgInfo, @RequestParam Date from, @RequestParam Date to, @RequestParam int account, @RequestParam int offsetAccount, OutputStream out) {

        OrganisationService orgService = getOrganisationService(orgInfo);
        to.setHours(23);
        to.setMinutes(59);
        to.setSeconds(60);
        
        Filter filterFrom = new CompareFilter("dateInvoiced", from, CompareFilter.CompareType.GreaterThan);
        Filter filterTo = new CompareFilter("dateInvoiced", to, CompareFilter.CompareType.LessThan);
        
        List<Order> orders = orgService.getOrders().list(new AndFilter(filterFrom, filterTo), new Sorter("dateInvoiced"));
        DataExchange.writePostings(orders, account, offsetAccount, out);
        
    }
}
