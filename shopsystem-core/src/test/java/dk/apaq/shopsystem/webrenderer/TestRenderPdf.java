package dk.apaq.shopsystem.webrenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author michael
 */
public class TestRenderPdf {
    
    public static void main(String[] args) throws FileNotFoundException {
        FlyingSaucerRenderer flyingSaucerRenderer = new FlyingSaucerRenderer();
        flyingSaucerRenderer.renderWebpageToPdf(new FileOutputStream("site.pdf"), "file:///Users/michael/site.html");
    }
}
