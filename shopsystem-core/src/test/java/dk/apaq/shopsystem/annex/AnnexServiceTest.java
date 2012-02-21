package dk.apaq.shopsystem.annex;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import dk.apaq.shopsystem.entity.ContactInformation;
import java.util.ArrayList;
import java.util.List;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderStatus;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.PaymentType;
import dk.apaq.shopsystem.entity.Tax;
import dk.apaq.shopsystem.util.StreamUtils;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import junit.framework.TestCase;

public class AnnexServiceTest extends TestCase {

    private AnnexService annexService = new AnnexServiceImpl();

    private ContactInformation getContactInformation() {
        ContactInformation ci = new ContactInformation();
        ci.setCompanyName("Apple");
        ci.setContactName("Steve Jobs");
        ci.setStreet("Infinite loop 1");
        ci.setPostalCode("12345");
        ci.setCity("Somewhere");
        return ci;
    }
    private Organisation getOrganisation(){
        Organisation org = new Organisation();
        org.setCompanyName("Apaq");
        org.setTelephone("51923192");
        org.setBankAccount("1231-2312313123");
        org.setEmail("mail@gmail.com");
        org.setStreet("Stovringparken 10");
        org.setPostalCode("9530");
        org.setCity("Stovring");
        org.setCountryCode("DK");
        org.setAnnexNote("Dette er en note\nPas på den");
        return org;
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
        order.setBuyer(getContactInformation());

        for(int i=0;i<orderlinecount;i++){
            order.addOrderLine("Coffee & cup and much much more #"+i, 1, 10.23*(i+1), i%2==0?tax:tax2);
        }
        return order;
    }

    private List<Order> getOrderList(int count){
        Tax tax = getTax();
        
        List<Order> orderlist = new ArrayList<Order>();

        for (int i = 0; i < count; i++) {
            Order order = new Order();
            order.setCurrency("DKK");
            order.setNumber(i + 1);
            order.setStatus(OrderStatus.Completed);
            order.addOrderLine("Coffee Cup", i + 1, 10, "ID", "COFFEECUP", tax);
            orderlist.add(order);

        }
        return orderlist;
    }
    
    private List<Payment> getPaymentList(int count) {
        List<Payment> payments = new ArrayList();
        
        for(int i=0;i<count;i++) {
            Payment p = new Payment();
            if(i % 4 == 0) {
                p.setAmount(100);
                p.setPaymentType(PaymentType.Cash);
            } else {
                p.setAmount(-10);
                p.setPaymentType(PaymentType.Change); 
            }
            payments.add(p);
        }
        return payments;
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
    
    public void testGenerateReceiptHtml() throws Exception {
        Organisation org = getOrganisation();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        OrderDocumentContent content = new OrderDocumentContent(org, order, null);
        Page page = new Page(PageSize.Receipt);
        AnnexContext<OrderDocumentContent, OutputStream> context = new AnnexContext<OrderDocumentContent, OutputStream>(content, out, page, Locale.getDefault());
        annexService.generatePurchaseDocument(context, AnnexType.Receipt, OutputType.Html);
        
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
        assertTrue(value.contains("Stovringparken"));
        assertTrue(value.contains("Apaq"));
        assertTrue(value.contains("Dette er en note<br/>Pas p&aring; den"));
    }
    
    public void testGenerateInvoiceHtml() throws Exception {
        Organisation org = getOrganisation();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("invoice.html");
        
        OrderDocumentContent content = new OrderDocumentContent(org, order, null);
        Page page = new Page(PageSize.A4, 7, 7, 7, 7);
        AnnexContext<OrderDocumentContent, OutputStream> context = new AnnexContext<OrderDocumentContent, OutputStream>(content, out, page, Locale.getDefault());
        annexService.generatePurchaseDocument(context, AnnexType.Invoice, OutputType.Html);
        
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
        assertTrue(value.contains("Stovringparken 10"));
        assertTrue(value.contains("9530 Stovring"));
        assertTrue(value.contains("Apaq"));
        
        assertTrue(value.contains("Apple"));
        assertTrue(value.contains("Steve Jobs"));
        assertTrue(value.contains("Infinite loop"));
        
        assertTrue(value.contains(page.getLeftMargin().getMillimetres() + "mm"));
        assertTrue(value.contains(page.getSize().getWidth().getMillimetres() + "mm"));
        
    }
    
    public void testGenerateInvoicePdf() throws Exception {
        Organisation org = getOrganisation();
        Order order = getOrder(1);

        //FileOutputStream out = new FileOutputStream("invoice.pdf");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        OrderDocumentContent content = new OrderDocumentContent(org, order, null);
        Page page = new Page(PageSize.A4, 15, 15, 15, 15);
        AnnexContext<OrderDocumentContent, OutputStream> context = new AnnexContext<OrderDocumentContent, OutputStream>(content, out, page, Locale.getDefault());
        annexService.generatePurchaseDocument(context, AnnexType.Invoice, OutputType.Pdf);
        
        byte[] data = out.toByteArray();
        assertNotSame(0, data.length);
        
        PdfReader reader = new PdfReader(data);
        List<String> fonts = new ArrayList(listFonts(reader));
        assertTrue(fonts.contains("Courier"));
        
        FileOutputStream fout = new FileOutputStream("invoice.pdf");
        StreamUtils.copyThenClose(new ByteArrayInputStream(data), fout);
    }
    
    public void testGenerateInvoicePrintable() throws Exception {
        Organisation org = getOrganisation();
        Order order = getOrder(1);

        //FileOutputStream out = new FileOutputStream("invoice.pdf");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        OrderDocumentContent content = new OrderDocumentContent(org, order, null);
        Page page = new Page(PageSize.A4, 5, 5, 5, 5);
        
        AnnexContext<OrderDocumentContent, Void> context = new AnnexContext<OrderDocumentContent, Void>(content, null, page, Locale.getDefault());
        Printable p = annexService.generatePurchaseDocumentPrintable(context, AnnexType.Invoice);
        
        int width = page.getSize().getWidth().getPixels();
        int height = page.getSize().getHeight().getPixels();
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PageFormat pf = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pf.setPaper(paper);
        p.print(img.createGraphics(), pf, 0);
        
        ImageIO.write(img, "png", out);
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
    }
    
    
    public void testGenerateInvoicePostScript() throws Exception{
        Organisation org = getOrganisation();
        Order order = getOrder(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("invoice.ps");

        OrderDocumentContent content = new OrderDocumentContent(org, order, null);
        Page page = new Page(PageSize.A4);
        AnnexContext<OrderDocumentContent, OutputStream> context = new AnnexContext<OrderDocumentContent, OutputStream>(content, out, page, Locale.getDefault());
        annexService.generatePurchaseDocument(context, AnnexType.Invoice, OutputType.PostScript);


        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        //TODO Extract the images and ensure they are correct in size.
        out.close();
    }
    
     public void testGenerateInvoicePngBundle() throws Exception{
        Organisation org = getOrganisation();
        Order order = getOrder(30);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("invoice_png.zip");

        OrderDocumentContent content = new OrderDocumentContent(org, order, null);
        Page page = new Page(PageSize.A4, 7);
        AnnexContext<OrderDocumentContent, OutputStream> context = new AnnexContext<OrderDocumentContent, OutputStream>(content, out, page, Locale.getDefault());
        annexService.generatePurchaseDocument(context, AnnexType.Invoice, OutputType.PngBundle);


        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        //TODO Extract the images and ensure they are correct in size.
        out.close();
    }

     public void testGenerateAuditReportHtml() throws Exception{
        Organisation org = getOrganisation();
        List<Order> orders = getOrderList(30);
        List<Payment> payments = getPaymentList(40);
        
        Date from = new Date();
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        Date to = new Date();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        AuditReportContent content = new AuditReportContent(org, from, to, orders, payments);
        Page page = new Page(PageSize.A4, 7);
        AnnexContext<AuditReportContent, OutputStream> context = new AnnexContext<AuditReportContent, OutputStream>(content, out, page, Locale.getDefault());
        annexService.generateAuditReport(context, AnnexType.Receipt, OutputType.Html);

        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());

        out.close();
    }
     
     public void testGenerateAuditReportPrintable() throws Exception {
        Organisation org = getOrganisation();
        List<Order> orders = getOrderList(30);
        List<Payment> payments = getPaymentList(40);
        
        Date from = new Date();
        from.setHours(0);
        from.setMinutes(0);
        from.setSeconds(0);
        Date to = new Date();
        
        //FileOutputStream out = new FileOutputStream("invoice.pdf");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        AuditReportContent content = new AuditReportContent(org, from, to, orders, payments);
        Page page = new Page(PageSize.A4, 5, 5, 5, 5);
        
        AnnexContext<AuditReportContent, Void> context = new AnnexContext<AuditReportContent, Void>(content, null, page, Locale.getDefault());
        Printable p = annexService.generateAuditReportPrintable(context, AnnexType.Receipt);
        
        int width = page.getSize().getWidth().getPixels();
        int height = page.getSize().getHeight().getPixels();
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PageFormat pf = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pf.setPaper(paper);
        p.print(img.createGraphics(), pf, 0);
        
        ImageIO.write(img, "png", out);
        String value = new String(out.toByteArray());
        assertNotSame(0, value.length());
    }
     
         /**
     * Creates a Set containing information about the fonts in the src PDF file.
     * @param src the path to a PDF file
     * @throws IOException
     */
    public Set<String> listFonts(PdfReader reader) throws IOException {
        Set<String> set = new TreeSet<String>();
        PdfDictionary resources;
        for (int k = 1; k <= reader.getNumberOfPages(); ++k) {
            resources = reader.getPageN(k).getAsDict(PdfName.RESOURCES);
            processResource(set, resources);
        }
        return set;
    }
 
    /**
     * Extracts the font names from page or XObject resources.
     * @param set the set with the font names
     * @param resources the resources dictionary
     */
    public static void processResource(Set<String> set, PdfDictionary resource) {
        if (resource == null)
            return;
        PdfDictionary xobjects = resource.getAsDict(PdfName.XOBJECT);
        if (xobjects != null) {
            for (PdfName key : (Set<PdfName>)xobjects.getKeys()) {
                processResource(set, xobjects.getAsDict(key));
            }
        }
        PdfDictionary fonts = resource.getAsDict(PdfName.FONT);
        if (fonts == null)
            return;
        PdfDictionary font;
        for (PdfName key : (Set<PdfName>)fonts.getKeys()) {
            font = fonts.getAsDict(key);
            String name = font.getAsName(PdfName.BASEFONT).toString();
            if (name.length() > 8 && name.charAt(7) == '+') {
                name = String.format("%s subset (%s)", name.substring(8), name.substring(1, 7));
            }
            else {
                name = name.substring(1);
                /*PdfDictionary desc = font.getAsDict(PdfName.FONTDESCRIPTOR);
                if (desc == null)
                    name += " nofontdescriptor";
                else if (desc.get(PdfName.FONTFILE) != null)
                    name += " (Type 1) embedded";
                else if (desc.get(PdfName.FONTFILE2) != null)
                    name += " (TrueType) embedded";
                else if (desc.get(PdfName.FONTFILE3) != null)
                    name += " (" + font.getAsName(PdfName.SUBTYPE).toString().substring(1) + ") embedded";*/
            }
            set.add(name);
        }
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
    
    /*

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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("receipt.html");

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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("receipt.png");
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("receipt.zip");
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //FileOutputStream out = new FileOutputStream("receipt.pdf");
        annexService.writeReceipt(shop, order, OutputType.Pdf, PageSize.A7,  out, Locale.ENGLISH);

        //String value = new String(out.toByteArray());
        //assertNotSame(0, value.length());
        //TODO Test that it is a valid PDF
        out.close();

    }
*/



}

