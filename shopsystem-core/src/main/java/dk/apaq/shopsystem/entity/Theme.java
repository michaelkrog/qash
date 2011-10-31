package dk.apaq.shopsystem.entity;

import dk.apaq.shopsystem.rendering.CmsTag;
import dk.apaq.shopsystem.rendering.TemplateMarkupParserFactory;
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
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a Template for a Website.
 */
public class Theme extends AbstractModule implements Serializable {

    private static final TemplateMarkupParserFactory MARKUP_PARSER_FACTORY = new TemplateMarkupParserFactory();
    private static final Logger LOG = LoggerFactory.getLogger(Theme.class);
    
    private List<Template> templateList;
    private SuffixNodeFilter nodeFilter = new SuffixNodeFilter("html");
    
    private class TemplateInfo {
        private List<Placeholder> placeholders = new ArrayList<Placeholder>();
        private List<ComponentInformation> componentInformations = new ArrayList<ComponentInformation>();
    }
    
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
                    TemplateInfo templateInfo = parseTemplateInfo(markupFile);
                    
                    if(dir.hasFile(codeFileName)) {
                        codeFile = dir.getFile(codeFileName);
                    }
                    
                    newList.add(new Template(markupFile.getBaseName(), templateInfo.placeholders, templateInfo.componentInformations, markupFile, codeFile));
                    
                } catch (IOException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Template registered in theme is not found. [Theme=" + getName() + ";Template=" + markupFile.getBaseName() + "]");
                }
            }
            templateList = Collections.unmodifiableList(newList);
        }
        return templateList;
    }

    
    
    
    private TemplateInfo parseTemplateInfo(File file) throws IOException {
        MarkupParser parser = MARKUP_PARSER_FACTORY.newMarkupParser(new MarkupResourceStream(new VfsResourceStream(file)));
        TemplateInfo templateInfo = new TemplateInfo();
        
        try {
            Markup m = parser.parse();
            boolean inContainer = false;
            boolean inCmsComponentTag = false;
            ComponentInformation componentInformation = null;
            Placeholder currentPlaceHolder = null;
            
            Iterator<MarkupElement> it =  m.iterator();
            while(it.hasNext()) {
                MarkupElement me = it.next();
                if(me instanceof WicketTag) {
                    WicketTag ct = (WicketTag)me;
                    
                    if(ct.isClose() && ct.isContainerTag() && inContainer) {
                        inContainer = false;
                        continue;
                    }
                    
                    if(!ct.isClose() && !ct.hasBehaviors() && !ct.isAutoComponentTag() && !inContainer) {
                        if(ct.isContainerTag()) {
                            currentPlaceHolder = new Placeholder(ct.getId(), true);
                            inContainer = true;
                        } else {
                            currentPlaceHolder = new Placeholder(ct.getId(), false);
                        }
                        
                        templateInfo.placeholders.add(currentPlaceHolder);
                        continue;
                    }
                }
                
                if(me instanceof CmsTag) {
                    CmsTag ct = (CmsTag) me;
                    
                    if(!ct.isClose() && ct.isParameterTag() && inCmsComponentTag) {
                        String name = ct.getAttribute("name");
                        String value = ct.getAttribute("value");
                        ComponentParameter componentParameter = new ComponentParameter();
                        componentParameter.setString(value);
                        componentInformation.getParameterMap().put(name, componentParameter);
                        continue;
                    }
                    
                    if(ct.isClose() && ct.isComponentTag() && inCmsComponentTag) {
                        inCmsComponentTag = false;
                        templateInfo.componentInformations.add(componentInformation);
                        componentInformation = null;
                        
                        continue;
                    }
                    
                    if(!ct.isClose() && ct.isComponentTag() && !inCmsComponentTag && inContainer) {
                        inCmsComponentTag = true;
                        String module = ct.getAttribute("module");
                        String component = ct.getAttribute("component");
                        componentInformation = new ComponentInformation();
                        componentInformation.setModuleName(module);
                        componentInformation.setComponentName(component);
                        componentInformation.setPlaceholderName(currentPlaceHolder.getId());
                        continue;
                    }
                }
                
                
            }
        } catch (ResourceStreamNotFoundException ex) {
            throw new IOException(ex);
        }
        
        return templateInfo;
    }

    @Override
    protected String getBundleSuffix() {
        return "theme";
    }
    

}
