package dk.apaq.shopsystem.webrenderer;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author michael
 */
public class CachingImageRenderer extends AbstractImageRenderer {

    private static final long DEFAULT_TTL = 60000; //1 minute
    private final ImageRenderer wrappedRenderer;
    private final Directory cacheDir;
    private final long ttl;
    private int hits;
    private int misses;
    private int expireds;
    

    public CachingImageRenderer(ImageRenderer wrappedRenderer, Directory cacheDir) {
        this.wrappedRenderer = wrappedRenderer;
        this.cacheDir = cacheDir;
        this.ttl = DEFAULT_TTL;
    }

    public CachingImageRenderer(ImageRenderer wrappedRenderer, Directory cacheDir, long ttl) {
        this.wrappedRenderer = wrappedRenderer;
        this.cacheDir = cacheDir;
        this.ttl = ttl;
    }

    @Override
    public BufferedImage renderWebpageToImage(Device device, String url) {
        try {
            String filename = getCacheId(device, url) + ".png";
            if (cacheDir.hasFile(filename)) {
                File file = cacheDir.getFile(filename);
                if(!isExpired(file)) {
                    hits++;
                    return ImageIO.read(file.getInputStream());
                } else {
                    expireds++;
                    file.delete();
                }
            }
            
            misses++;
            BufferedImage image = wrappedRenderer.renderWebpageToImage(device, url);
            if(image==null) {
                return null;
            }
            
            File file = cacheDir.getFile(filename, true);
            ImageIO.write(image, "png", file.getOutputStream());
            
            return image;
            
        } catch (IOException ex) {
            return null;
        }

    }

    private String getCacheId(Device device, String url) {
        return url.hashCode() + "_" + device.getScreenWidth() + "_" + device.getScreenHeight();
    }
    
    private boolean isExpired(File file) {
        return System.currentTimeMillis() - file.getLastModified().getTime() > ttl;
    }

    public int getHits() {
        return hits;
    }

    public int getExpireds() {
        return expireds;
    }

    public int getMisses() {
        return misses;
    }
    
    
}
