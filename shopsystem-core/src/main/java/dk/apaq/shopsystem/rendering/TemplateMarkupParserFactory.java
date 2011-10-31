package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.rendering.markupfilter.StyleSheetSorter;
import dk.apaq.shopsystem.rendering.markupfilter.TemplateResourcePathAppender;
import java.util.Iterator;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.IMarkupFilter;
import org.apache.wicket.markup.parser.filter.WicketLinkTagHandler;
import org.apache.wicket.markup.parser.filter.WicketRemoveTagHandler;

/**
 *
 * @author michael
 */
public class TemplateMarkupParserFactory extends MarkupFactory {

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
    
    
    @Override
    public MarkupParser newMarkupParser(MarkupResourceStream resource) {
        MarkupParser parser = new TemplateMarkupParser(resource);
        return parser;
    }
}
