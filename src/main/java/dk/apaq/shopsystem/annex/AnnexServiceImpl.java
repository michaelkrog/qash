package dk.apaq.shopsystem.annex;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.print.PrintService;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import dk.apaq.shopsystem.OutputType;
import dk.apaq.shopsystem.i18n.LocaleUtil;
import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.OrderLine;
import dk.apaq.shopsystem.entity.Payment;
import dk.apaq.shopsystem.entity.Store;
import dk.apaq.shopsystem.entity.Tax;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.XHTMLPrintable;
import org.xhtmlrenderer.swing.Java2DRenderer;

public class AnnexServiceImpl implements AnnexService {

    private static final Logger LOG = LoggerFactory.getLogger(AnnexServiceImpl.class);

    public AnnexServiceImpl() {
        try {
            LOG.info("Initializing AnnexService.");
            usNumberFormat.setMinimumFractionDigits(2);
            usNumberFormat.setMaximumFractionDigits(2);

            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/META-INF/resource.loader"));
            ve = new VelocityEngine(properties);
            ve.init();

            LOG.info("Properties for velocitoy loaded.");

            receiptTemplate = ve.getTemplate("/dk/apaq/shopsystem/annex/templates/receipt_xhtml.vm");
            postingsTemplate = ve.getTemplate("/dk/apaq/shopsystem/annex/templates/postings_csv.vm");
            invoiceTemplate = ve.getTemplate("/dk/apaq/shopsystem/annex/templates/invoice_xhtml.vm", "utf-8");

            LOG.info("Templates loaded.");



        } catch (Exception e) {
            LOG.error("Error while initializing AnnexServlet.", e);
        }
    }

    private VelocityEngine ve;
    private Template receiptTemplate, invoiceTemplate, postingsTemplate;
    private DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private NumberFormat usNumberFormat = NumberFormat.getNumberInstance(Locale.US);

    public void printInvoice(AnnexContext<CommercialDocumentContent, PrintService> context) throws Exception {
        printCommercialDocument(context, invoiceTemplate);
    }

    public void printReceipt(AnnexContext<CommercialDocumentContent, PrintService> context) throws Exception {
        printCommercialDocument(context, receiptTemplate);
    }


    public void generateReceipt(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType) throws Exception {
        generateCommercialDocument(context, outputType, receiptTemplate);
    }

    public void generateInvoice(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType) throws Exception {
        generateCommercialDocument(context, outputType, invoiceTemplate);
    }

    /**
     * @deprecated
     */
    public void writeReceipt(Store shop, Order order, OutputType outputType, dk.apaq.shopsystem.annex.PageSize pagesize,
            OutputStream out, Locale locale) throws Exception {

        Page page = new Page(pagesize, 5, 5, 5, 5);
        CommercialDocumentContent content = new CommercialDocumentContent(shop, order, null);
        AnnexContext<CommercialDocumentContent, OutputStream> context = new AnnexContext<CommercialDocumentContent, OutputStream>(content, out, page, 300, locale);
        generateReceipt(context, outputType);
    }

    /**
     * @deprecated
     */
    public void writeInvoice(Store shop, Order order, OutputType outputType,
            PageSize pageSize, OutputStream out, Locale locale
            ) throws Exception {

        Page page = new Page(pageSize);
        CommercialDocumentContent content = new CommercialDocumentContent(shop, order, null);
        AnnexContext<CommercialDocumentContent, OutputStream> context = new AnnexContext<CommercialDocumentContent, OutputStream>(content, out, page, 300, locale);
        generateCommercialDocument(context, outputType, invoiceTemplate);
    }

    /**
     * @deprecated
     */
    public void writeOrder(Store shop, Order order, OutputStream out,
            OutputType outputType, Locale locale) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     */
    public void writeOrderList(Store shop, List<Order> orderlist, OutputStream out,
            OutputType outputType, Locale locale) throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     */
    public void writePostings(List<Order> orderlist, List<Tax> taxlist, int account,
            int offsetaccount, OutputStream out, OutputType outputType, Locale locale) throws Exception {
        if (outputType != OutputType.Csv) {
            throw new IllegalArgumentException("Only Outputtype.Csv is supported.");
        }

        if(LOG.isDebugEnabled()) {
            LOG.debug("Creating postings [format="+outputType+"]");
        }

        /*  create a context and add data */
        VelocityContext context = new VelocityContext();

        context.put("dateformatter", isoDateFormat);
        context.put("decimalformatter", usNumberFormat);
        context.put("orderlist", orderlist);
        context.put("account", account);
        context.put("offsetaccount", offsetaccount);
        context.put("taxlist", taxlist);

        Writer writer = new OutputStreamWriter(out);
        postingsTemplate.merge(context, writer);
        writer.close();

        if(LOG.isDebugEnabled()) {
            LOG.debug("Postings created.");
        }
    }

    private void generateCommercialDocument(AnnexContext<CommercialDocumentContent, OutputStream> context, OutputType outputType, Template template) throws Exception {
        Store shop = context.getInput().getSeller();
        Order order = context.getInput().getOrder();
        Page page = context.getPage();

        if(LOG.isDebugEnabled()) {
            LOG.debug("Creating document [shop="+shop.getId()+"; order="+order.getNumber()+"; format="+outputType+"]");
        }

        OutputStream orgOut = context.getOutput();
        
        switch (outputType) {
            case Html:
                generateXhtmlOrderAnnex(context, template);
                break;

            case Pdf:

                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                context = new AnnexContext<CommercialDocumentContent, OutputStream>(context, baos2);
                generateXhtmlOrderAnnex(context, template);

                Document dom = XMLResource.load(new ByteArrayInputStream(baos2.toByteArray())).getDocument();
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocument(dom, "http://localhost");
                renderer.layout();
                renderer.createPDF(orgOut);
                orgOut.flush();
                break;
            case Png:
                ByteArrayOutputStream baos4 = new ByteArrayOutputStream();
                context = new AnnexContext<CommercialDocumentContent, OutputStream>(context, baos4);
                generateXhtmlOrderAnnex(context, template);

                Document dom2 = XMLResource.load(new ByteArrayInputStream(baos4.toByteArray())).getDocument();
                Java2DRenderer renderer2 = new Java2DRenderer(dom2, page.getSize().getPixelWidth(), -1);

                BufferedImage image = renderer2.getImage();
                ImageIO.write(image, "PNG", orgOut);
                orgOut.flush();
                break;
            case SvgBundle:
            case PngBundle:
            case PostScript:
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                context = new AnnexContext<CommercialDocumentContent, OutputStream>(context, baos);
                generateXhtmlOrderAnnex(context, template);

                XHTMLPanel panel = new XHTMLPanel();
                panel.setSize(page.getSize().getPixelWidth(), page.getSize().getPixelHeight());
                panel.setDocument(new ByteArrayInputStream(baos.toByteArray()), "");

                XHTMLPrintable printable = new ExtendedXHtmlPrintable(panel);
                getPrinter(outputType).print(printable, page.getSize(), orgOut);
                break;
            case Svg:
                ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                context = new AnnexContext<CommercialDocumentContent, OutputStream>(context, baos3);
                generateXhtmlOrderAnnex(context, template);

                XHTMLPanel panel2 = new XHTMLPanel();
                panel2.setSize(page.getSize().getPixelWidth(), page.getSize().getPixelHeight());
                panel2.setDocument(new ByteArrayInputStream(baos3.toByteArray()), "");
                getComposer(outputType).compose(panel2, orgOut);
                break;

        }

        if(LOG.isDebugEnabled()) {
            LOG.debug("Document created. [shop="+shop.getId()+"]");
        }
    }
    private void printCommercialDocument(AnnexContext<CommercialDocumentContent, PrintService> context, Template template) throws Exception {
        Order order = context.getInput().getOrder();
        Store shop = context.getInput().getSeller();

        if(LOG.isDebugEnabled()) {
            LOG.debug("Printing document [shop="+shop.getId()+"; order="+order.getNumber()+"]");
        }

        Page page = context.getPage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        generateXhtmlOrderAnnex(new AnnexContext<CommercialDocumentContent, OutputStream>(context, baos), template);

        XHTMLPanel panel = new XHTMLPanel();
        panel.setSize(page.getSize().getPixelWidth(), page.getSize().getPixelHeight());
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        panel.setDocument(new ByteArrayInputStream(baos.toByteArray()), "");

        XHTMLPrintable printable = new ExtendedXHtmlPrintable(panel);

        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(printable);
        printerJob.setPrintService(context.getOutput());
        printerJob.print();


        if(LOG.isDebugEnabled()) {
            LOG.debug("Document created. [shop="+shop.getId()+"]");
        }

    }

    private void generateXhtmlOrderAnnex(AnnexContext<CommercialDocumentContent, OutputStream> annexcontext, Template template) throws Exception {

        Order order = annexcontext.getInput().getOrder();
        List<Payment> payments = annexcontext.getInput().getPaymentList();
        Store shop = annexcontext.getInput().getSeller();
        Page page = annexcontext.getPage();

        Locale locale = annexcontext.getLocale();

        ResourceBundle rb = LocaleUtil.getResourceBundle(LocaleUtil.SYSTEM_I18N_BASE_NAME, locale);

        /*  create a context and add data */
        VelocityContext context = new VelocityContext();

        List<OrderLine> orderlineList = new ArrayList<OrderLine>();
        for (int i = 0; i < order.getOrderLineCount(); i++) {
            orderlineList.add(order.getOrderLine(i));
        }

        NumberFormat cf = NumberFormat.getCurrencyInstance(locale);
        NumberFormat cf2 = NumberFormat.getNumberInstance(locale);
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        NumberFormat pf = NumberFormat.getPercentInstance(locale);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        cf.setCurrency(Currency.getInstance(order.getCurrency()));
        cf2.setMinimumFractionDigits(2);
        cf2.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(1);

        context.put("order", order);
        context.put("orderlines", orderlineList);
        context.put("payment", payments);

        context.put("shop", shop);
        context.put("nf", nf);
        context.put("cf", cf);
        context.put("cf2", cf2);
        context.put("df", df);
        context.put("pf", pf);
        context.put("resource", rb);
        context.put("locale", locale);
        context.put("tool", new VelocityTool());
        context.put("pageWidth", annexcontext.getPage().getSize().getWidth());
        context.put("pageHeight", annexcontext.getPage().getSize().getHeight());
        context.put("pixelWidth", page.getSize().getPixelWidth());
        context.put("pixelHeight", annexcontext.getPage().getSize().getPixelHeight());
        context.put("pixelMarginTop", page.getTopMargin());
        context.put("pixelMarginRight", page.getRightMargin());
        context.put("pixelMarginBottom", page.getBottomMargin());
        context.put("pixelMarginLeft", page.getLeftMargin());

        Writer writer = new OutputStreamWriter(annexcontext.getOutput(), "utf-8");

        template.merge(context, writer);
        writer.flush();

    }

    private AnnexComposer getComposer(OutputType type){
        switch(type){
            case Png:
                return new PngAnnexComposer();
            case Svg:
                return new SvgAnnexComposer();
            default:
                return null;
        }

    }

    private AnnexPrinter getPrinter(OutputType outputType){
        switch(outputType){
            case PngBundle:
                return new PngBundleAnnexPrinter();
            case PostScript:
                return new PostscriptAnnexPrinter();
            case SvgBundle:
                return new SvgAnnexBundlePrinter();
            default:
                return null;
        }
    }
}
