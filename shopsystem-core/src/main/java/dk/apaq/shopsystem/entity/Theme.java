package dk.apaq.shopsystem.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a Template for a Website.
 */
public class Theme implements Serializable {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Theme.class);
    private final Directory dir;
    private final File infoFile;
    private ThemeInfo info;
    private List<Template> templateList;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private static class ThemeInfo {

        private String version;
        private Date releaseDate;
        private SellerInfo seller;
        private Map<String, TemplateInfo> templates = new HashMap<String, TemplateInfo>();

        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return seller;
        }

        public Map<String, TemplateInfo> getTemplateMap() {
            return templates;
        }
    }

    private static class TemplateInfo {

        private String description;

        public String getDescription() {
            return description;
        }
    }

    public Theme(Directory dir) throws IOException {
        if (!dir.isBundle() || !"theme".equals(dir.getSuffix())) {
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
        if (templateList == null) {
            List<Template> newList = new ArrayList<Template>();
            for (Map.Entry<String, TemplateInfo> entry : info.templates.entrySet()) {
                try {
                    File file = dir.getFile(entry.getKey() + ".html");
                    newList.add(new Template(entry.getKey(), entry.getValue().getDescription(), file));
                } catch (FileNotFoundException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Template registered in theme is not found. [Theme=" + getName() + ";Template=" + entry.getKey() + "]");
                }
            }
            templateList = Collections.unmodifiableList(newList);
        }
        return templateList;
    }
}
