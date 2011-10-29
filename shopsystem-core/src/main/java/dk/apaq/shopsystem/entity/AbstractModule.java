package dk.apaq.shopsystem.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.apaq.shopsystem.rendering.BundleInfo;
import dk.apaq.shopsystem.rendering.BundleInfoIO;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.NodeFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public abstract class AbstractModule implements Serializable {

    
    private static final Logger LOG = LoggerFactory.getLogger(AbstractModule.class);
    protected final Directory dir;
    private final File infoFile;
    private BundleInfo info;
    
     protected class SuffixNodeFilter implements NodeFilter {

        private final String suffix;

        public SuffixNodeFilter(String suffix) {
            this.suffix = suffix;
        }
        
        
        @Override
        public boolean accept(Node node) {
            return suffix.equals(node.getSuffix());
        }
        
    }
    
    public AbstractModule(Directory dir) throws IOException {
        if (!dir.isBundle() || !getBundleSuffix().equals(dir.getSuffix())) {
            throw new IllegalArgumentException("The directory is not a '"+getBundleSuffix()+"' bundle.");
        }
        this.dir = dir;
        this.infoFile = dir.getFile("bundle.info");

        info = BundleInfoIO.read(this.infoFile.getInputStream());
    }

    public String getName() {
        return dir.getBaseName();
    }

    public Date getReleaseDate() {
        return info.getReleaseDate();
    }

    public SellerInfo getSellerInfo() {
        return info.getSellerInfo();
    }

    public String getVersion() {
        return info.getVersion();
    }
    
    public Directory getDirectory() {
        return dir;
    }
    
    protected abstract String getBundleSuffix();
    
}
