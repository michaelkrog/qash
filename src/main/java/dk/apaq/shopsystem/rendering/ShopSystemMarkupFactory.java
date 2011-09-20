package dk.apaq.shopsystem.rendering;

import java.io.IOException;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCache;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.loader.IMarkupLoader;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 *
 */
public class ShopSystemMarkupFactory extends MarkupFactory {

    private class ShopSystemMarkupLoader implements IMarkupLoader {

        @Override
        public Markup loadMarkup(MarkupContainer container, MarkupResourceStream markupResourceStream, IMarkupLoader baseLoader, boolean enforceReload) throws IOException, ResourceStreamNotFoundException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    @Override
    public IMarkupLoader getMarkupLoader() {
        return new ShopSystemMarkupLoader();
        //return super.getMarkupLoader();
    }

    @Override
    public IMarkupCache getMarkupCache() {
        return super.getMarkupCache();
    }





}
