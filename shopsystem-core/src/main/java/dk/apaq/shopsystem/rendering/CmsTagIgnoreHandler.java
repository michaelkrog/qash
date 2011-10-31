package dk.apaq.shopsystem.rendering;

import java.text.ParseException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;

/**
 *
 * @author krog
 */
public class CmsTagIgnoreHandler extends AbstractMarkupFilter {

    @Override
    protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {
        // Don't touch any wicket:id component and any auto-components and any not supported tag names
        if ((tag instanceof WicketTag) || (tag.isAutolinkEnabled() == true) || (tag.getAttributes().get("wicket:id") != null)) {
            return tag;
        }
        
        if("cms".equalsIgnoreCase(tag.getNamespace())) {
            tag.setId("_cms_tag_to_be_removed_");
            tag.setIgnore(true);
        }
        return tag;
    }
    
}
