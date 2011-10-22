package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.rendering.markupfilter.StyleSheetSorter;
import dk.apaq.shopsystem.rendering.markupfilter.TemplateResourcePathAppender;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;

/**
 *
 * @author michael
 */
public class CmsMarkupParserFactory extends MarkupFactory {


    private class CmsMarkupParser extends MarkupParser {

        public CmsMarkupParser(MarkupResourceStream resource) {
            super(resource);
        }

        @Override
        protected MarkupFilterList initializeMarkupFilters(Markup markup) {
            MarkupFilterList list = new MarkupFilterList();
            list.add(new StyleSheetSorter());
            list.add(new TemplateResourcePathAppender());
            
            list.addAll(super.initializeMarkupFilters(markup));
            return list;
        }
        
        
    }
    
    
    @Override
    public MarkupParser newMarkupParser(MarkupResourceStream resource) {
        MarkupParser parser = new CmsMarkupParser(resource);
        return parser;
    }
}
