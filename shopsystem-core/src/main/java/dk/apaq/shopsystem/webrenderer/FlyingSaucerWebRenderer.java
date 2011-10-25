package dk.apaq.shopsystem.webrenderer;

import com.kitfox.svg.app.beans.SVGPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.w3c.dom.Element;

import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.swing.SwingReplacedElement;
import org.xhtmlrenderer.swing.SwingReplacedElementFactory;
import org.xhtmlrenderer.util.XRLog;

/**
 *
 * @author michael
 */
public class FlyingSaucerWebRenderer extends AbstractWebRenderer {

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

    private class SVGSalamanderReplacedElementFactory implements ReplacedElementFactory {

        @Override
        public ReplacedElement createReplacedElement(
                LayoutContext c,
                BlockBox box,
                UserAgentCallback uac,
                int cssWidth,
                int cssHeight) {
            SVGPanel panel = null;
            String content = null;
            JComponent cc = null;
            try {
                Element elem = box.getElement();
                if (elem == null || !isSVGEmbedded(elem)) {
                    return null;
                }

                panel = new SVGPanel();
                

                // HACK: the easiest way to integrate with Salamander is to have it read
                // our SVG from a file--so, push the content to a temporary file, yuck!
                content = getSVGElementContent(elem);

                String path = elem.getAttribute("data");
                XRLog.general(Level.FINE, "Rendering embedded SVG via object tag from: " + path);
                XRLog.general(Level.FINE, "Content is: " + content);
                panel.setAntiAlias(true);
                panel.setSvgURI(new URI(uac.resolveURI(path)));
                panel.setBackground(Color.white);
                //panel.setSvgResourcePath(path);

                int width = panel.getSVGWidth();
                int height = panel.getSVGHeight();
                float ratio = (float)height / (float)width;

                if (cssWidth > 0) {
                    width = cssWidth;
                    height = (int)(cssWidth * ratio);
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
                panel.setScaleToFit(true);
                panel.setPreferredSize(new Dimension(width, height));
                panel.setSize(panel.getPreferredSize());

                cc = panel;
            }/* catch (SVGException e) {
            XRLog.general(Level.WARNING, "Could not replace SVG element; rendering failed"
            + " in SVG renderer. Skipping and using blank JPanel.", e);
            cc = getDefaultJComponent(content, cssWidth, cssHeight);
            }*/ catch (URISyntaxException e) {
                XRLog.general(Level.WARNING, "Could not replace SVG element; rendering failed"
                        + " in SVG renderer. Skipping and using blank JPanel.", e);
                cc = getDefaultJComponent(content, cssWidth, cssHeight);
            }
            if (cc == null) {
                return null;
            } else {
                SwingReplacedElement result = new SwingReplacedElement(cc);
                if (c.isInteractive()) {
                    c.getCanvas().add(cc);
                }
                return result;
            }
        }

        private String getSVGElementContent(Element elem) {
            if (elem.getChildNodes().getLength() > 0) {
                return elem.getFirstChild().getNodeValue();
            } else {
                return "SVG";
            }
        }

        private boolean isSVGEmbedded(Element elem) {
            return elem.getNodeName().equals("object") && elem.getAttribute("type").equals("image/svg+xml");
        }

        private JComponent getDefaultJComponent(String content, int width, int height) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JLabel comp = new JLabel(content);
            panel.add(comp, BorderLayout.CENTER);
            panel.setOpaque(false);
            if (width > 0 && height > 0) {
                panel.setPreferredSize(new Dimension(width, height));
                panel.setSize(panel.getPreferredSize());
            } else {
                panel.setPreferredSize(comp.getPreferredSize());
                panel.setSize(comp.getPreferredSize());
            }
            return panel;
        }

        public void reset() {
        }

        public void remove(Element e) {
        }

        public void setFormSubmissionListener(FormSubmissionListener listener) {
            // nothing to do ?
        }
    }

    @Override
    public BufferedImage renderWebpageToImage(Device device, String url) {
        Java2DRenderer renderer = new Java2DRenderer(url, device.getScreenWidth());

        ChainedReplacedElementFactory cref = new ChainedReplacedElementFactory();
        cref.addFactory(new SwingReplacedElementFactory());
        cref.addFactory(new SVGSalamanderReplacedElementFactory());        
        renderer.getSharedContext().setReplacedElementFactory(cref);
        return renderer.getImage();
    }
}
