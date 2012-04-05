package dk.apaq.shopsystem.site;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.i18n.LocaleUtil;
import dk.apaq.shopsystem.l10n.Country;
import dk.apaq.shopsystem.pay.PaymentGateway;
import dk.apaq.shopsystem.pay.PaymentGatewayManager;
import dk.apaq.shopsystem.pay.PaymentGatewayType;
import dk.apaq.shopsystem.service.OrganisationService;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * A controller for the pages that shows the user forms and informations about payments
 * @author krog
 */
@Controller
public class PaymentController {
    
    private final NumberFormat nfQuickPayOrderNumber = NumberFormat.getIntegerInstance();
    
    @Autowired
    private SystemService service;
    
    @Autowired
    private PaymentGatewayManager paymentGatewayManager;
    
    public PaymentController() {
        nfQuickPayOrderNumber.setMinimumIntegerDigits(4);
        nfQuickPayOrderNumber.setMaximumIntegerDigits(20);
        nfQuickPayOrderNumber.setGroupingUsed(false);
    }
    
    
    @RequestMapping("/payment/{orgId}/{orderId}/form.htm")
    public ModelAndView handlePaymentForm(HttpServletRequest request, HttpServletResponse response, @PathVariable String orgId, @PathVariable String orderId) throws IOException {
        
        Organisation seller = service.getOrganisationCrud().read(orgId);
        OrganisationService sellerService = service.getOrganisationService(seller);
        Order order = sellerService.getOrders().read(orderId);
        
        Map model = new HashMap();
        
        switch(seller.getPaymentGatewayType()) {
            case MockPay:
                
                model.put("formUrl", "https://secure.quickpay.dk/form/");
                model.put("formElements", buildFormElementsForQuickPay(seller, order, null, null, null));
                return new ModelAndView("payment", model);
            case QuickPay:
                String idPart = "/" + seller.getId() + "/" + order.getId();
                String urlPrefix = "http://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":"+request.getServerPort()) + 
                                               request.getContextPath() + "/payment" + idPart;
                
                String returnUrl = urlPrefix + "/return.htm";
                String cancelUrl = urlPrefix + "/cancel.htm";
                String callbackUrl = urlPrefix + "/callback.htm";
                
                model.put("formUrl", "https://secure.quickpay.dk/form/");
                model.put("formElements", buildFormElementsForQuickPay(seller, order, returnUrl, cancelUrl, callbackUrl));
                return new ModelAndView("payment", model);        
            
            default:
                response.sendError(501);
                return null;
        }

    }
    
    @RequestMapping("/payment/{orgId}/{orderId}/return.htm")
    public ModelAndView handlePaymentReturn(HttpServletRequest request, HttpServletResponse response, @PathVariable String orgId, @PathVariable String orderId) throws IOException {
        //Find organisation and order
        Organisation seller = service.getOrganisationCrud().read(orgId);
        OrganisationService sellerService = service.getOrganisationService(seller);
        Order order = sellerService.getOrders().read(orderId);
        
        //show order and link to pdf
        return null;
    }
    
        @RequestMapping("/payment/{orgId}/{orderId}/cancel.htm")
    public ModelAndView handlePaymentCallback(HttpServletRequest request, HttpServletResponse response, @PathVariable String orgId, @PathVariable String orderId) throws IOException {
        //redirect to front dashboard
        
        return null;
    }
    
    @RequestMapping("/payment/{orgId}/{orderId}/cancel.htm")
    public ModelAndView handlePaymentCancel(HttpServletRequest request, HttpServletResponse response, @PathVariable String orgId, @PathVariable String orderId) throws IOException {
        //redirect to front dashboard
        
        return null;
    }
    
    private Map<String, String> buildFormElementsForQuickPay(Organisation seller, Order order, String returnUrl, String cancelUrl, String callbackUrl) {
        
        String customerLanguage = "en";
        if(order.getBuyer() != null && order.getBuyer().getCountryCode() != null) {
            Locale locale = LocaleUtil.getLocaleFromCountryCode(order.getBuyer().getCountryCode());
            if(locale != null && locale.getLanguage() != null) {
                customerLanguage = locale.getLanguage();
            }
        }
        
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("protocol", "4");
        map.put("msgtype", "subscribe"); //or authorize?
        map.put("merchant", seller.getMerchantId());
        map.put("language", customerLanguage);
        map.put("ordernumber", nfQuickPayOrderNumber.format(order.getNumber()));  //
        map.put("amount", Long.toString(order.getTotalWithTax()));
        map.put("currency", order.getCurrency());
        map.put("continueurl", returnUrl);
        map.put("cancelurl", cancelUrl);
        map.put("callbackurl", callbackUrl);
        map.put("autocapture", "0");
        map.put("cardtypelock", "");
        map.put("description", "Subscription for order #"+order.getNumber());
        map.put("splitpayment", "1");
        
        //md5
        StringBuilder builder = new StringBuilder();
        for(String value : map.values()) {
            builder.append(value);
        }
        builder.append(seller.getMerchantSecret());
        map.put("md5check", DigestUtils.md5Hex(builder.toString()));
        
        return map;
    }
}
