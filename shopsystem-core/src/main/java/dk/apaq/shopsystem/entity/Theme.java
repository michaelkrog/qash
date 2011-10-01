package dk.apaq.shopsystem.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.apaq.shopsystem.rendering.CustomMarkupParser;
import dk.apaq.shopsystem.rendering.VfsResourceStream;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Node;
import dk.apaq.vfs.NodeFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
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
    private HtmlNodeFilter nodeFilter = new HtmlNodeFilter();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private class HtmlNodeFilter implements NodeFilter {

        @Override
        public boolean accept(Node node) {
            return "html".equals(node.getSuffix());
        }
        
    }
    
    private static class ThemeInfo {

        private String version;
        private Date releaseDate;
        private SellerInfo seller;
        //private Map<String, TemplateInfo> templates = new HashMap<String, TemplateInfo>();

        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return seller;
        }

        /*public Map<String, TemplateInfo> getTemplateMap() {
            return templates;
        }*/
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

    public Template getTemplate(String name) {
        for(Template template : listTemplates()) {
            if(name.equals(template.getName())) {
                return template;
            }
        }
        return null;
    }
    
    public List<Template> listTemplates() {
        if (templateList == null) {
            List<Template> newList = new ArrayList<Template>();
            for(File file:dir.getFiles(nodeFilter)) {
                try {
                    List<String> placeholders = parsePlaceHolders(file);
                    newList.add(new Template(file.getBaseName(), file, placeholders));
                } catch (IOException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Template registered in theme is not found. [Theme=" + getName() + ";Template=" + file.getBaseName() + "]");
                }
            }
            templateList = Collections.unmodifiableList(newList);
        }
        return templateList;
    }
    private List<String> parsePlaceHolders(File file) throws IOException {
        List<String> placeHolders = new ArrayList<String>();
        CustomMarkupParser parser = new CustomMarkupParser(new MarkupResourceStream(new VfsResourceStream(file)));
        try {
            Markup m = parser.parse();
            Iterator<MarkupElement> it =  m.iterator();
            while(it.hasNext()) {
                MarkupElement me = it.next();
                if(me instanceof ComponentTag) {
                    ComponentTag ct = (ComponentTag)me;
                    if(!ct.isClose()) {
                        placeHolders.add(ct.getId());
                    }
                }
            }
        } catch (ResourceStreamNotFoundException ex) {
            throw new IOException(ex);
        }
        return placeHolders;
    }
}
