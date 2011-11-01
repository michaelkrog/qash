package dk.apaq.shopsystem.rendering.template;

import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.entity.Placeholder;
import dk.apaq.shopsystem.rendering.CmsTag;
import dk.apaq.shopsystem.rendering.CmsTagIdentifier;
import dk.apaq.shopsystem.rendering.VfsResourceStream;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.IMarkupFilter;
import org.apache.wicket.markup.parser.filter.WicketLinkTagHandler;
import org.apache.wicket.markup.parser.filter.WicketRemoveTagHandler;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 *
 * @author krog
 */
public class TemplateInformationExtractor {
    
    private static final Class[] IGNORED_CLASSES = {WicketRemoveTagHandler.class,WicketLinkTagHandler.class};

    private class TemplateMarkupParser extends MarkupParser {

        public TemplateMarkupParser(MarkupResourceStream resource) {
            super(resource);
        }

        @Override
        protected MarkupFilterList initializeMarkupFilters(Markup markup) {
            MarkupFilterList list = new MarkupFilterList();
            list.add(new CmsTagIdentifier(markup.getMarkupResourceStream()));
            MarkupFilterList superList = super.initializeMarkupFilters(markup);
            
            Iterator<IMarkupFilter> it = superList.iterator();
            while(it.hasNext()) {
                IMarkupFilter filter = it.next();
                for(Class ignoredClass : IGNORED_CLASSES) {
                    if(filter.getClass() == ignoredClass) {
                        it.remove();
                        break;
                    }
                }
            }
            
            list.addAll(superList);
            return list;
        }
        
        
    }
    
    public class TemplateInformation {
        private final List<ComponentInformation> componentInformations;
        private final List<Placeholder> placeholders;

        private TemplateInformation(List<ComponentInformation> componentInformations, List<Placeholder> placeholders) {
            this.componentInformations = componentInformations;
            this.placeholders = placeholders;
        }

        public List<ComponentInformation> getComponentInformations() {
            return componentInformations;
        }

        public List<Placeholder> getPlaceholders() {
            return placeholders;
        }
    }
    
    public TemplateInformation readTemplateInformation(File file) throws IOException {
        MarkupParser parser = new TemplateMarkupParser(new MarkupResourceStream(new VfsResourceStream(file)));
        List<ComponentInformation> componentInformations = new ArrayList<ComponentInformation>();
        List<Placeholder> placeholders = new ArrayList<Placeholder>();
        TemplateInformation templateInfo = new TemplateInformation(componentInformations, placeholders);
        
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
                        
                        placeholders.add(currentPlaceHolder);
                        continue;
                    }
                }
                
                if(me instanceof CmsTag) {
                    CmsTag ct = (CmsTag) me;
                    
                    if(!ct.isClose() && ct.isParameterTag() && inCmsComponentTag) {
                        String name = ct.getAttribute("name");
                        String value = ct.getAttribute("value");
                        String type = ct.getAttribute("type");
                        
                        //type is optional - default it to string
                        type = type == null ? "String" : type;
                        
                        ComponentParameter componentParameter = createComponentParameter(file, value, type);
                        componentInformation.getParameterMap().put(name, componentParameter);
                        continue;
                    }
                    
                    if(ct.isClose() && ct.isComponentTag() && inCmsComponentTag) {
                        inCmsComponentTag = false;
                        componentInformations.add(componentInformation);
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
    
    private ComponentParameter createComponentParameter(File templatFile, String value, String type) {
        ComponentParameter cp = new ComponentParameter();
        if("Path".equalsIgnoreCase(type)) {
            Path path = null;
            if(!value.startsWith("/")) {
                path = templatFile.getPath();
                Path relativePath = new Path(value);
                for(int i=0;i<relativePath.getLevels();i++) {
                    path.addLevel(relativePath.getLevel(i));
                }
            } else {
                path = new Path(value);
            }
            
            cp.setPath(path);
        } else {
            cp.setString(value);
        }
        
        return cp;
    }
}
