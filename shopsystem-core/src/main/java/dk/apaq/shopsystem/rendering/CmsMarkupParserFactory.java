package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.Page;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.markup.parser.filter.RelativePathPrefixHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.RenderPageRequestHandler;

/**
 *
 * @author michael
 */
public class CmsMarkupParserFactory extends MarkupFactory {

    /**
     * The id automatically assigned to tags without an id which we need to prepend a relative path
     * to.
     */
    public static final String WICKET_RELATIVE_PATH_PREFIX_CONTAINER_ID = "_relative_path_prefix_";
    /** List of attribute names considered */
    private static final String attributeNames[] = new String[]{"href", "src", "background"};
    private static final List<String> tagNames = Arrays.asList(new String[]{"img", "link", "script"});
    
    private class CmsMarkupParser extends MarkupParser {

        public CmsMarkupParser(MarkupResourceStream resource) {
            super(resource);
        }

        @Override
        protected MarkupFilterList initializeMarkupFilters(Markup markup) {
            MarkupFilterList list = new MarkupFilterList();
            list.add(new TemplateResourcePathAppender());
            list.addAll(super.initializeMarkupFilters(markup));
            return list;
        }
        
        
    }
    
    /**
     * Behavior that adds a prefix to src, href and background attributes to make them
     * context-relative
     */
    public static final Behavior RELATIVE_PATH_BEHAVIOR = new Behavior() {

        private static final long serialVersionUID = 1L;

        @Override
        public void onComponentTag(Component component, ComponentTag tag) {
            // Modify all relevant attributes
            for (String attrName : attributeNames) {
                String attrValue = tag.getAttributes().getString(attrName);

                if ((attrValue != null) && (attrValue.startsWith("/") == false)
                        && (!attrValue.contains(":")) && !(attrValue.startsWith("#"))) {
                    tag.getAttributes().put(attrName, rewriteToTemplateRelative(attrValue, RequestCycle.get()));

                }
            }
        }
        
        private String rewriteToTemplateRelative(String orgUrl, RequestCycle rc) {
            String themeName = "<unknown>";
            if(rc.getActiveRequestHandler() != null && rc.getActiveRequestHandler() instanceof RenderPageRequestHandler) {
                IRequestablePage page = ((RenderPageRequestHandler)rc.getActiveRequestHandler()).getPage();
                if(page instanceof CmsPage) {
                    themeName = ((CmsPage)page).getTheme().getName();
                }
            }
            
            Request request = rc.getRequest();
            Url url = new Url(request.getUrl());
            url.resolveRelative(Url.parse("./"));
                    
            StringBuilder sb = new StringBuilder();
            sb.append(request.getContextPath());
            sb.append(request.getFilterPath());
            sb.append("/");
            sb.append(url.toString());
            sb.append("_/themes/");
            sb.append(themeName);
            sb.append("/");
            sb.append(orgUrl);
            return sb.toString();
        }
    };

    private class TemplateResourcePathAppender extends AbstractMarkupFilter {

        @Override
        protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {
            if (tag.isClose()) {
                return tag;
            }

            // Don't touch any wicket:id component and any auto-components and any not supported tag names
            if ((tag instanceof WicketTag) || (tag.isAutolinkEnabled() == true)
                    || (tag.getAttributes().get("wicket:id") != null) || !tagNames.contains(tag.getName())) {
                return tag;
            }

            // Work out whether we have any attributes that require us to add a
            // behavior that prepends the relative path.
            for (String attrName : attributeNames) {
                String attrValue = tag.getAttributes().getString(attrName);
                if ((attrValue != null) && (attrValue.startsWith("/") == false)
                        && (!attrValue.contains(":")) && !(attrValue.startsWith("#"))) {
                    if (tag.getId() == null) {
                        tag.setId(WICKET_RELATIVE_PATH_PREFIX_CONTAINER_ID);
                        tag.setAutoComponentTag(true);
                    }
                    tag.addBehavior(RELATIVE_PATH_BEHAVIOR);
                    tag.setModified(true);
                    break;
                }
            }


            return tag;
        }
    }

    @Override
    public MarkupParser newMarkupParser(MarkupResourceStream resource) {
        MarkupParser parser = new CmsMarkupParser(resource);
        return parser;
    }
}
