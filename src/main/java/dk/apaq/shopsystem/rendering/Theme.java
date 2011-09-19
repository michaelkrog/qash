package dk.apaq.shopsystem.rendering;

import com.google.gson.Gson;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a Template for a Website.
 */
public class Theme implements Serializable {

    private final Directory dir;
    private final File infoFile;
    private ThemeInfo info;
    private Gson gson = new Gson();

    private class Template {
        private String description;

        public String getDescription() {
            return description;
        }

    }

    private static class ThemeInfo {
        private String version;
        private Date releaseDate;
        private SellerInfo sellerInfo;
        private Map<String, Template> templateMap = new HashMap<String, Template>();

        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return sellerInfo;
        }

        public Map<String, Template> getTemplateMap() {
            return templateMap;
        }
        
    }

    public Theme(Directory dir) throws IOException {
        if(!dir.isBundle() || !"theme".equals(dir.getSuffix())) {
            throw new IllegalArgumentException("The directory is not a theme bundle.");
        }
        this.dir = dir;
        this.infoFile = dir.getFile("theme.info");

        info = gson.fromJson(new InputStreamReader(infoFile.getInputStream()), ThemeInfo.class);
    }

    public String getVersion() {
        return info.getVersion();
    }

    public Date getReleaseDate() {
        return info.getReleaseDate();
    }

    public SellerInfo getSellerInfo() {
        return info.getSellerInfo();
    }

    public String getName() {
        return dir.getBaseName();
    }

    public List<Template> listTemplates() {
        List<Template> templates = new ArrayList<Template>();

        for(File file : dir.getFiles()) {
            if("html".equals(file.getSuffix())) {
                //templates.add(new Template(file));
            }
        }
        return Collections.unmodifiableList(templates);
    }

    
}
