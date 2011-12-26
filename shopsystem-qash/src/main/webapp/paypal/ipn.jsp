<%@page import="dk.apaq.shopsystem.entity.OrderStatus"%>
<%@page import="dk.apaq.shopsystem.entity.Plan"%>
<%@page import="java.util.Date"%>
<%@page import="dk.apaq.shopsystem.entity.PaymentType"%>
<%@page import="dk.apaq.shopsystem.entity.Payment"%>
<%@page import="dk.apaq.shopsystem.entity.ContactInformation"%>
<%@page import="dk.apaq.shopsystem.entity.Product"%>
<%@page import="dk.apaq.shopsystem.entity.Order"%>
<%@page import="dk.apaq.shopsystem.service.OrganisationService"%>
<%@page import="dk.apaq.shopsystem.entity.Organisation"%>
<%@page import="dk.apaq.shopsystem.service.SystemService"%>
<%@page import="dk.apaq.shopsystem.service.SystemServiceHolder"%>
<%
String buyingOrgId = "4028a8633464c957013464cb5a200003";
String sellingOrgId = "4028a8633464c957013464c9ca820000";
String sellingProduct = "4028a863347c7df001347c8510df0000";
SystemService service = SystemServiceHolder.getSystemService();

Organisation buyingOrg = service.getOrganisationCrud().read(buyingOrgId);

Organisation sellingOrg = service.getOrganisationCrud().read(sellingOrgId);
OrganisationService organisationService = service.getOrganisationService(sellingOrg);

Product product = organisationService.getProducts().read(sellingProduct);

double payAmount = 79;
int quantity = 1;

Date newExpireDate;
if(buyingOrg.getPlan() == Plan.Free) {
    newExpireDate = new Date();
    newExpireDate.setMonth(newExpireDate.getMonth() + quantity);
} else {
    newExpireDate = buyingOrg.getPlanExpireDate();
    if(newExpireDate == null) {
        newExpireDate = new Date();
    }
    newExpireDate.setMonth(newExpireDate.getMonth() + quantity);
}

buyingOrg.setPlan(Plan.Basic);
buyingOrg.setPlanExpireDate(newExpireDate);
organisationService.updateOrganisation(buyingOrg);

Order order = new Order();
order.addOrderLine(product, quantity);
order.setBuyer(new ContactInformation(buyingOrg));
order.setStatus(OrderStatus.Completed);
String orderId = organisationService.getOrders().create(order);

Payment payment = new Payment();
payment.setAmount(payAmount);
payment.setPaymentType(PaymentType.Card);
payment.setOrderId(orderId);
organisationService.getPayments().create(payment);
%>
