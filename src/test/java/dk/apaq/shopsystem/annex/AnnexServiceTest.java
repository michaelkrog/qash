package dk.apaq.shopsystem.annex;

import dk.apaq.shopsystem.annex.AnnexServiceImpl;
import dk.apaq.shopsystem.annex.PageSize;
import dk.apaq.shopsystem.annex.AnnexService;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import dk.apaq.shopsystem.OutputType;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import junit.framework.TestCase;

public class AnnexServiceTest extends TestCase {

    private AnnexService annexService = new AnnexServiceImpl();

    private Store getShop(){
        Store shop = new Store();
        shop.setName("Apaq");
        shop.setPhoneNo("51923192");
        shop.setEmail("mail@gmail.com");
        shop.setRoad("Stoevringparken");
        shop.setZipCode("9530");
        shop.setCity("Stoevring");
        return shop;
    }

    private Order getOrder(int orderlinecount){
        Tax tax = getTax();
        Tax tax2 = getTax2();

        Order order = new Order();
        order.setCurrency("DKK");
        order.setNumber(1);
        order.setStatus(OrderStatus.Completed);
        order.setInvoiceNumber(12312);
        order.setDateInvoiced(new Date());
        order.setDateTimelyPayment(new Date());

        for(int i=0;i<orderlinecount;i++){
            order.addOrderLine("Coffee & cup and much much more #"+i, 1, 10.23*(i+1), i%2==0?tax:tax2);
        }
        return order;
    }

    private List<Order> getOrderList(){
        Tax tax = getTax();
        
        List<Order> orderlist = new ArrayList<Order>();

        for (int i = 0; i < 3000; i++) {
            Order order = new Order();
            order.setCurrency("DKK");
            order.setNumber(i + 1);
            order.setStatus(OrderStatus.Completed);
            order.addOrderLine("Coffee Cup", i + 1, 10, "ID", "COFFEECUP", tax);
            orderlist.add(order);

        }
        return orderlist;
    }

    private Tax getTax(){
        Tax tax = new Tax();
        tax.setId("TAX");
        tax.setName("Moms");
        tax.setRate(25);
        return tax;
    }

    private Tax getTax2(){
        Tax tax = new Tax();
        tax.setId("TAX2");
        tax.setName("Moms 2");
        tax.setRate(12);
        return tax;
    }

    private List<Tax> getTaxList(){
        Tax tax = getTax();

        List<Tax> taxlist = new ArrayList<Tax>();
        taxlist.add(tax);
        return taxlist;

    }

    /*public void testPrintReceiptHtml() throws Exception {
        Shop shop = getShop();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        annexService.writeReceipt(shop, order, out, OutputType.Html, Locale.ENGLISH);
        
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
        System.out.println(value);
    }*/

    public void testPrintPostingsCSV() throws Exception {
        Store shop = getShop();
        List<Order> orderlist = getOrderList();
        List<Tax> taxlist = getTaxList();

        //FileOutputStream out2 = new FileOutputStream("postings.csv");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            annexService.writePostings(orderlist, taxlist, 1000, 58200, out, OutputType.Csv, Locale.ENGLISH);
        } catch (Exception e) {
            fail(e.getMessage());
        }

       // out2.close();
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
        assertTrue(value.contains(orderlist.get(0).getName()));
        assertTrue(value.contains(orderlist.get(0).getOrderLine(0).getItemNo()));
        assertTrue(value.contains(orderlist.get(0).getOrderLine(0).getTitle()));

    }

    public void testGenerateInvoiceHtml(){
        Store shop = getShop();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            annexService.writeInvoice(shop, order, OutputType.Html, PageSize.A4, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        //TODO Ensure the HTML has shop name and order contents.
        assertTrue(value.contains(shop.getName()));
        assertTrue(value.contains("Coffee &amp; cup"));

    }

    public void testGenerateInvoicePdf() throws Exception{
        Store shop = getShop();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out2 = new FileOutputStream("invoice.pdf");
        try {
            annexService.writeInvoice(shop, order, OutputType.Pdf, PageSize.A4, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        //TODO Extract text from PDF and ensure it contains stuff from order and shop
        //out2.close();
    }

    public void testGenerateInvoicePostscript() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out2 = new FileOutputStream("invoice.ps");

        try {
            annexService.writeInvoice(shop, order, OutputType.PostScript, PageSize.A4, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
        assertTrue(value.contains("PageSize [595.2755737304688 841.8897705078125]"));
        assertTrue(value.contains("Page: 2 2"));
        assertFalse(value.contains("Page: 3 3"));
        //out2.close();;

    }


    public void testGenerateInvoicePng() throws Exception{
        Store shop = getShop();
        Order order = getOrder(5);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("invoice.png");

        try {
            annexService.writeInvoice(shop, order, OutputType.Png, PageSize.A4, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        //TODO Extract the images and ensure they are correct in size.
        out.close();
    }

    public void testGenerateInvoicePngBundle() throws Exception{
        Store shop = getShop();
        Order order = getOrder(10);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("invoice_png.zip");

        try {
            annexService.writeInvoice(shop, order, OutputType.PngBundle, PageSize.A4, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        //TODO Extract the images and ensure they are correct in size.
        out.close();
    }

    public void testGenerateInvoiceSvgBundleSinglePage() throws Exception{
        Store shop = getShop();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        annexService.writeInvoice(shop, order, OutputType.SvgBundle, PageSize.A4, out, Locale.ENGLISH);
        
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        ZipInputStream is = new ZipInputStream(new ByteArrayInputStream(out.toByteArray()));
        ZipEntry entry1 = is.getNextEntry();
        
        assertNotNull(entry1);
        is.close();

        //TODO extract the orderpages from the zip and ensure that they contain something from
        //the order and shop
    }

    public void testGenerateInvoiceSvgBundle2Pages() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            annexService.writeInvoice(shop, order, OutputType.SvgBundle, PageSize.A4, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        ZipInputStream is = new ZipInputStream(new ByteArrayInputStream(out.toByteArray()));
        ZipEntry entry1 = is.getNextEntry();
        ZipEntry entry2 = is.getNextEntry();

        assertNotNull(entry1);
        assertNotNull(entry2);
        is.close();

        //TODO extract the orderpages from the zip and ensure that they contain something from
        //the order and shop

    }

    public void testGenerateReceiptHtml() throws Exception{
        Store shop = getShop();
        Order order = getOrder(10);

        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream("receipt.html");

        try {
            annexService.writeReceipt(shop, order, OutputType.Html, PageSize.A7, out, Locale.ENGLISH);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

        out.close();
        //String value = new String(out.toByteArray());
        //assertNotSame(0, value.length());

        //TODO Ensure the HTML has shop name and order contents.
        //assertTrue(value.contains(shop.getName()));
        //assertTrue(value.contains("Coffee &amp; cup"));

    }

    public void testGenerateReceiptPng() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);

        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream("receipt.png");
        annexService.writeReceipt(shop, order, OutputType.Png, PageSize.A7, out, Locale.ENGLISH);
        
        //String value = new String(out.toByteArray());
        //assertNotSame(0, value.length());

        out.close();

        //Ensure that it is a readable image
        //ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
    }

    public void testGenerateReceiptPngBundle() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);

        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream("receipt.zip");
        annexService.writeReceipt(shop, order, OutputType.PngBundle, PageSize.A7, out, Locale.ENGLISH);

        //String value = new String(out.toByteArray());
        //assertNotSame(0, value.length());

        out.close();

        //Ensure that it is a readable image
        //ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
    }

    public void testGenerateReceiptSvg() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);
        PageSize pageSize =  PageSize.Receipt;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out2 = new FileOutputStream("receipt.svg");
        annexService.writeReceipt(shop, order, OutputType.Svg, pageSize,  out, Locale.ENGLISH);

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
        //assertTrue(value.contains("width=\""+(pageSize.getPixelWidth()-10)+"\""));
        //assertTrue(value.contains("height=\""+pageSize.getPixelHeight(72)+"\""));
        out.close();
        //out2.close();
    }

    public void testGenerateReceiptPostscript() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);

        ByteArrayOutputStream outout = new ByteArrayOutputStream();
        annexService.writeReceipt(shop, order, OutputType.PostScript, PageSize.A7,  outout, Locale.ENGLISH);
        
        String value = new String(outout.toByteArray());
        assertNotSame(0, value.length());

        assertTrue(value.contains("PageSize [209.76377868652344 297.6377868652344]"));
        //assertTrue(value.contains("Page: 3 3"));
        //assertFalse(value.contains("Page: 4 4"));
        
    }

        public void testGenerateReceiptPdf() throws Exception{
        Store shop = getShop();
        Order order = getOrder(40);

        //ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream("receipt.pdf");
        annexService.writeReceipt(shop, order, OutputType.Pdf, PageSize.A7,  out, Locale.ENGLISH);

        //String value = new String(out.toByteArray());
        //assertNotSame(0, value.length());
        //TODO Test that it is a valid PDF
        out.close();

    }




}