package dk.apaq.shopsystem.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private ModuleInfo info;
    //private ClassDeserializer classDeserializer = new ClassDeserializer();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    
    private static class ModuleInfo {

        private String version;
        private Date releaseDate;
        private SellerInfo seller;
        
        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return seller;
        }

    }
    
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

        info = gson.fromJson(new InputStreamReader(infoFile.getInputStream()), ModuleInfo.class);
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
