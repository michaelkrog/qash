package dk.apaq.shopsystem.webrenderer;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import org.xhtmlrenderer.css.style.CalculatedStyle;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextReplacedElement;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.PageBox;
import org.xhtmlrenderer.render.RenderingContext;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.swing.SwingReplacedElement;
import org.xhtmlrenderer.swing.SwingReplacedElementFactory;

/**
 *
 * @author michael
 */
public class FlyingSaucerRenderer extends AbstractImageRenderer implements PdfRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(FlyingSaucerRenderer.class);

    private static boolean isImageTag(Element elem) {
        return "img".equals(elem.getNodeName());
    }
    
    private static void paintSvg(Graphics2D graphics2d, SVGDiagram diagram, float widthInPoints, float heightInPoints) {
        try {
            graphics2d.scale(widthInPoints / diagram.getWidth(), heightInPoints / diagram.getHeight());
            diagram.render(graphics2d);
        } catch (SVGException e) {
            e.printStackTrace();
            return;
        } finally {
            graphics2d.dispose();
        }

    }

    private static SVGDiagram loadDiagram(URI svgFileUrl) throws IOException {
        SVGUniverse svgUniverse = new SVGUniverse();
        URI svgFileUri = svgUniverse.loadSVG(svgFileUrl.toURL());
        if (svgFileUri == null) {
            throw new IOException("The svg file is not a supported svg file.");
        }
        SVGDiagram diagram = svgUniverse.getDiagram(svgFileUri);
        diagram.setIgnoringClipHeuristic(true);
        return diagram;
    }

    public class SVGPanel extends JPanel {

        public static final long serialVersionUID = 1;
        private final URI svgURI;
        private final boolean scaleToFit;
        private final boolean antiAlias;
        private final SVGUniverse svgUniverse = SVGCache.getSVGUniverse();
        private final AffineTransform scaleXform = new AffineTransform();
        private final SVGDiagram diagram;

        public SVGPanel(URI svgURI, boolean scaleToFit, boolean antiAlias) throws IOException {
            this.setBackground(Color.white);
            this.svgURI = svgURI;
            this.scaleToFit = scaleToFit;
            this.antiAlias = antiAlias;
            initComponents();
            this.diagram = loadDiagram(svgURI);
        }

        public int getSVGHeight() {
            return (int) diagram.getHeight();
        }

        public int getSVGWidth() {
            return (int) diagram.getWidth();
        }

        public void paintComponent(Graphics gg) {
            super.paintComponent(gg);

            Graphics2D g = (Graphics2D) gg;

            Object oldAliasHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);


            if (!scaleToFit) {
                try {
                    diagram.render(g);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
                } catch (SVGException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            Dimension dim = getSize();
            final int width = dim.width;
            final int height = dim.height;

            final Rectangle2D.Double rect = new Rectangle2D.Double();
            diagram.getViewRect(rect);

            scaleXform.setToScale(width / rect.width, height / rect.height);

            AffineTransform oldXform = g.getTransform();
            g.transform(scaleXform);

            try {
                diagram.render(g);
            } catch (SVGException e) {
                throw new RuntimeException(e);
            }

            g.setTransform(oldXform);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
        }

        public SVGUniverse getSvgUniverse() {
            return svgUniverse;
        }

        public URI getSvgURI() {
            return svgURI;
        }

        public boolean isScaleToFit() {
            return scaleToFit;
        }

        /**
         * @return true if antiAliasing is turned on.
         */
        public boolean getAntiAlias() {
            return antiAlias;
        }

        private void initComponents() {
            setLayout(new java.awt.BorderLayout());
        }
    }

    private class ChainedReplacedElementFactory implements ReplacedElementFactory {

        private List factoryList;

        public ChainedReplacedElementFactory() {
            this.factoryList = new ArrayList();
        }

        public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
            ReplacedElement re = null;
            for (Iterator it = factoryList.iterator(); it.hasNext();) {
                ReplacedElementFactory ref = (ReplacedElementFactory) it.next();
                re = ref.createReplacedElement(c, box, uac, cssWidth, cssHeight);
                if (re != null) {
                    break;
                }
            }
            return re;
        }

        public void addFactory(ReplacedElementFactory ref) {
            this.factoryList.add(ref);
        }

        public void reset() {
            for (Iterator i = this.factoryList.iterator(); i.hasNext();) {
                ReplacedElementFactory factory = (ReplacedElementFactory) i.next();
                factory.reset();
            }
        }

        public void remove(Element e) {
            for (Iterator i = this.factoryList.iterator(); i.hasNext();) {
                ReplacedElementFactory factory = (ReplacedElementFactory) i.next();
                factory.remove(e);
            }
        }

        public void setFormSubmissionListener(FormSubmissionListener listener) {
            // nothing to do ?
        }
    }

    public class SVGSwingReplacedElement extends SwingReplacedElement {

        private final JPanel panel;
        private SVGDiagram diagram;
        private int cssWidth;
        private int cssHeight;

        public SVGSwingReplacedElement(URI svgFileUrl, int cssWidth, int cssHeight) throws Exception {
            super(new JPanel());
            panel = (JPanel) getJComponent();
            this.cssWidth = cssWidth;
            this.cssHeight = cssHeight;
            this.diagram = loadDiagram(svgFileUrl);
        }

        public void paint(LayoutContext c, BlockBox blockBox) {
            float width = diagram.getWidth();
            float height = diagram.getHeight();
            float ratio = height / width;

            if (cssWidth > 0) {
                width = cssWidth;
                height = (int) (cssWidth * ratio);
            }

            if (cssHeight > 0) {
                height = cssHeight;
            }

            /*String val = elem.getAttribute("width");
            if (val != null && val.length() > 0) {
            width = Integer.valueOf(val).intValue();
            }
            val = elem.getAttribute("height");
            if (val != null && val.length() > 0) {
            height = Integer.valueOf(val).intValue();
            }
             */


            panel.setSize((int) width, (int) height);
            Graphics2D graphics2d = (Graphics2D) panel.getGraphics();

            FlyingSaucerRenderer.paintSvg(graphics2d, diagram, width, height);


        }
    }

        public class SVGITextReplacedElement implements ITextReplacedElement {

        private Point location = new Point(0, 0);
        private SVGDiagram diagram;
        private int cssWidth;
        private int cssHeight;
        private float ratio;

        public SVGITextReplacedElement(URI svgFileUrl, int cssWidth, int cssHeight) throws Exception {
            this.cssWidth = cssWidth;
            this.cssHeight = cssHeight;
            this.diagram = loadDiagram(svgFileUrl);
            this.ratio = diagram.getHeight() / diagram.getWidth();
        }

        private SVGDiagram loadDiagram(URI svgFileUrl) throws Exception {
            SVGUniverse svgUniverse = new SVGUniverse();
            URI svgFileUri = svgUniverse.loadSVG(svgFileUrl.toURL());
            SVGDiagram diagram = svgUniverse.getDiagram(svgFileUri);
            diagram.setIgnoringClipHeuristic(true);
            return diagram;
        }

        @Override
        public void detach(LayoutContext c) {
        }

        @Override
        public int getBaseline() {
            return 0;
        }

        @Override
        public int getIntrinsicWidth() {
            return cssWidth;// (int) diagram.getWidth();
        }

        @Override
        public int getIntrinsicHeight() {
            return cssHeight>0?cssHeight: (int)(cssWidth * ratio); //(int) diagram.getHeight();
        }

        @Override
        public boolean hasBaseline() {
            return false;
        }

        @Override
        public boolean isRequiresInteractivePaint() {
            return false;
        }

        @Override
        public Point getLocation() {
            return location;
        }

        @Override
        public void setLocation(int x, int y) {
            this.location.x = x;
            this.location.y = y;
        }

        @Override
        public void paint(RenderingContext renderingContext,
                ITextOutputDevice outputDevice, BlockBox blockBox) {
            PdfContentByte pdfContentByte = outputDevice.getWriter().getDirectContent();
            
            float width = diagram.getWidth();
            float height = diagram.getHeight();
            float ratio = height / width;
            
            float widthInPoints = (float) (cssWidth / outputDevice.getDotsPerPoint());
            float heightInPoints = (float) (cssHeight / outputDevice.getDotsPerPoint());

            if(heightInPoints<=0) {
                heightInPoints = widthInPoints * ratio;
                cssHeight = (int)(cssWidth * ratio);
            }
            
            PdfTemplate pdfTemplate =  pdfContentByte.createTemplate(widthInPoints, heightInPoints);
            Graphics2D graphics2d =  pdfTemplate.createGraphics(pdfTemplate.getWidth(), pdfTemplate.getHeight());
            try {
                graphics2d.scale(widthInPoints / diagram.getWidth(), heightInPoints / diagram.getHeight());
                diagram.render(graphics2d);
            } catch (SVGException e) {
                e.printStackTrace();
                return;
            } finally {
                graphics2d.dispose();
            }
            PageBox page = renderingContext.getPage();
            float x = blockBox.getAbsX() + page.getMarginBorderPadding(renderingContext, CalculatedStyle.LEFT);
            float y = (page.getBottom() - (blockBox.getAbsY()  + cssHeight)) + page.getMarginBorderPadding(renderingContext, CalculatedStyle.BOTTOM);
            x /= outputDevice.getDotsPerPoint();
            y /= outputDevice.getDotsPerPoint();
            pdfContentByte.addTemplate(pdfTemplate, x, y);
        }
    }

    private class SvgSalamanderITextReplacedElementFactory implements ReplacedElementFactory {

        @Override
        public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
            try {
                Element elem = box.getElement();
                if (elem == null || !isImageTag(elem)) {
                    return null;
                }

                String path = elem.getAttribute("src");

                return new SVGITextReplacedElement(new URI(uac.resolveURI(path)), cssWidth, cssHeight);
            } catch (Exception ex) {
                LOG.warn("Error replacing SVG for PDF.", ex);
                return null;
            }
        }

        @Override
        public void reset() {
        }

        @Override
        public void remove(Element e) {
        }

        @Override
        public void setFormSubmissionListener(FormSubmissionListener listener) {
        }
    }

    private class SVGSalamanderSwingReplacedElementFactory implements ReplacedElementFactory {

        @Override
        public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
            try {
                Element elem = box.getElement();
                if (elem == null || !isImageTag(elem)) {
                    return null;
                }

                String path = elem.getAttribute("src");
                SVGPanel panel = new SVGPanel(new URI(uac.resolveURI(path)), true, true);
                SwingReplacedElement replacedElement = new SwingReplacedElement(panel);

                int width = panel.getSVGWidth();
                int height = panel.getSVGHeight();
                float ratio = (float) height / (float) width;

                if (cssWidth > 0) {
                    width = cssWidth;
                    height = (int) (cssWidth * ratio);
                }

                if (cssHeight > 0) {
                    height = cssHeight;
                }

                String val = elem.getAttribute("width");
                if (val != null && val.length() > 0) {
                    width = Integer.valueOf(val).intValue();
                }
                val = elem.getAttribute("height");
                if (val != null && val.length() > 0) {
                    height = Integer.valueOf(val).intValue();
                }

                panel.setPreferredSize(new Dimension(width, height));
                panel.setSize(panel.getPreferredSize());

                if (c.isInteractive()) {
                    c.getCanvas().add(panel);
                }
                return replacedElement;
            } catch (Exception e) {
                LOG.debug("Could not replace SVG element; rendering failed"
                        + " in SVG renderer. Skipping and using blank JPanel.", e);
                return null;
            }

        }

        public void reset() {
        }

        public void remove(Element e) {
        }

        public void setFormSubmissionListener(FormSubmissionListener listener) {
            // nothing to do ?
        }
    }

    public FlyingSaucerRenderer() {
    }

    /**
     * {@inheritDoc}
     * This methods is syncronized because SvgSalamanders SvgPanel is not thread safe.
     */
    @Override
    public synchronized BufferedImage renderWebpageToImage(Device device, String url, boolean useCache) {
        LOG.debug("Rendering image from url [device={}; url={}; useCache= {}]", new Object[]{device.getScreenWidth() + "x" + device.getScreenHeight(), url, useCache});
        Java2DRenderer renderer = new Java2DRenderer(url, device.getScreenWidth());

        ChainedReplacedElementFactory chainedReplacedElementFactoryForImage = new ChainedReplacedElementFactory();
        chainedReplacedElementFactoryForImage.addFactory(new SVGSalamanderSwingReplacedElementFactory());
        chainedReplacedElementFactoryForImage.addFactory(new SwingReplacedElementFactory());

        renderer.getSharedContext().setReplacedElementFactory(chainedReplacedElementFactoryForImage);
        return renderer.getImage();
    }

    @Override
    public void renderWebpageToPdf(OutputStream os, String... url) {

        if (url.length == 0) {
            return;
        }

        try {

            if (url.length == 1) {
                renderWebpageToPdf(os, url[0]);
                return;
            }

            List<File> pdfFiles = new ArrayList<File>();
            for (String currentUrl : url) {

                File tmp = File.createTempFile("renderedWebpage", ".pdf");
                pdfFiles.add(tmp);
                tmp.deleteOnExit();
                OutputStream fos = new FileOutputStream(tmp);
                renderWebpageToPdf(fos, currentUrl);
            }

            concatPDFs(pdfFiles, os);

        } catch (DocumentException ex) {
            throw new RuntimeException("Unable to render PDF.", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to render PDF.", ex);
        }

    }

    private void renderWebpageToPdf(OutputStream os, String url) throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        ChainedReplacedElementFactory cref = new ChainedReplacedElementFactory();
        cref.addFactory(new ITextReplacedElementFactory(renderer.getOutputDevice()));
        cref.addFactory(new SvgSalamanderITextReplacedElementFactory());
        
        renderer.getSharedContext().setReplacedElementFactory(cref);
        renderer.setDocument(url);
        renderer.layout();
        renderer.createPDF(os);

        os.close();
    }

    private void concatPDFs(List<File> files, OutputStream outputStream) throws IOException, DocumentException {

        Document document = new Document();
        try {
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            Iterator<File> iteratorPDFs = files.iterator();

            // Create Readers for the pdfs.
            while (iteratorPDFs.hasNext()) {
                InputStream pdf = new FileInputStream(iteratorPDFs.next());
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages += pdfReader.getNumberOfPages();
            }
            // Create a writer for the outputstream
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            //BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
            // data

            PdfImportedPage page;
            int currentPageNumber = 0;
            int pageOfCurrentReaderPDF = 0;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();

            // Loop through the PDF files and add to the output.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                // Create a new page in the target for each source page.
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pageOfCurrentReaderPDF++;
                    currentPageNumber++;
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
                    cb.addTemplate(page, 0, 0);
                }
                pageOfCurrentReaderPDF = 0;
            }
            outputStream.flush();
            document.close();
            outputStream.close();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
