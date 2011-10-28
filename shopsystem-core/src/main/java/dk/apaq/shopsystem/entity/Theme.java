package dk.apaq.shopsystem.entity;

import dk.apaq.shopsystem.rendering.WicketLessMarkupParser;
import dk.apaq.shopsystem.rendering.VfsResourceStream;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a Template for a Website.
 */
public class Theme extends AbstractModule implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Theme.class);
    private List<Template> templateList;
    private SuffixNodeFilter nodeFilter = new SuffixNodeFilter("html");
   
   
    public Theme(Directory dir) throws IOException {
        super(dir);
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
            for(File markupFile:dir.getFiles(nodeFilter)) {
                String name = markupFile.getBaseName();
                String codeFileName = name + ".code";
                File codeFile = null;
                try {
                    List<Placeholder> placeholders = parsePlaceHolders(markupFile);
                    
                    if(dir.hasFile(codeFileName)) {
                        codeFile = dir.getFile(codeFileName);
                    }
                    
                    newList.add(new Template(markupFile.getBaseName(), placeholders, markupFile, codeFile));
                    
                } catch (IOException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Template registered in theme is not found. [Theme=" + getName() + ";Template=" + markupFile.getBaseName() + "]");
                }
            }
            templateList = Collections.unmodifiableList(newList);
        }
        return templateList;
    }

    
    
    
    private List<Placeholder> parsePlaceHolders(File file) throws IOException {
        List<Placeholder> placeHolders = new ArrayList<Placeholder>();
        WicketLessMarkupParser parser = new WicketLessMarkupParser(new MarkupResourceStream(new VfsResourceStream(file)));
        try {
            Markup m = parser.parse();
            boolean inContainer = false;
            Iterator<MarkupElement> it =  m.iterator();
            while(it.hasNext()) {
                MarkupElement me = it.next();
                if(me instanceof ComponentTag) {
                    ComponentTag ct = (ComponentTag)me;
                    
                    if(ct.isClose() && isContainerTag(ct) && inContainer) {
                        inContainer = false;
                        continue;
                    }
                    
                    if(!ct.isClose() && !ct.hasBehaviors() && !ct.isAutoComponentTag() && !inContainer) {
                        if(isContainerTag(ct)) {
                            placeHolders.add(new Placeholder(ct.getId(), true));
                            inContainer = true;
                        } else {
                            placeHolders.add(new Placeholder(ct.getId(), false));
                        }
                    }
                }
            }
        } catch (ResourceStreamNotFoundException ex) {
            throw new IOException(ex);
        }
        
        return placeHolders;
    }

    @Override
    protected String getBundleSuffix() {
        return "theme";
    }
    
    private boolean isContainerTag(ComponentTag ct) {
        return "wicket".equals(ct.getNamespace()) && "container".equals(ct.getName());
    }
    
}
