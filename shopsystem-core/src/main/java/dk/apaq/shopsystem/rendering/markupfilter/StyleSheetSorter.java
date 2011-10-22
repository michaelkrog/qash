package dk.apaq.shopsystem.rendering.markupfilter;

import dk.apaq.filter.Filter;
import dk.apaq.shopsystem.rendering.media.DeviceInfo;
import dk.apaq.shopsystem.rendering.media.FilterTranslatorForCssMedia;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 *
 * @author michael
 */
public class StyleSheetSorter extends AbstractMarkupFilter {

    private static final String WICKET_STYLESHEET_SORTER_PREFIX_CONTAINER_ID = "_stylesheet_sorting_prefix_";
    private static final List<String> tagNames = Arrays.asList(new String[]{"link"});
    private final DeviceInfo deviceInfo;

    public StyleSheetSorter() {
        deviceInfo = new DeviceInfo();
        Request request = RequestCycle.get().getRequest();
        IRequestParameters params = request.getQueryParameters();
        for (String paramName : params.getParameterNames()) {
            if (paramName.startsWith("device.")) {
                deviceInfo.set(paramName.substring(7), params.getParameterValue(paramName).toString());
            }
        }
    }

    @Override
    protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {
        if (tag.isClose()) {
            return tag;
        }

        if(deviceInfo.getInfoCount()==0) {
            return tag;
        }
        
        // Don't touch any wicket:id component and any auto-components and any not supported tag names
        if ((tag instanceof WicketTag) || (tag.isAutolinkEnabled() == true)
                || (tag.getAttributes().get("wicket:id") != null) || !tagNames.contains(tag.getName())) {
            return tag;
        }

        String media = tag.getAttribute("media");

        if (media != null && deviceInfo.getInfoCount() > 0) {
            Filter filter = FilterTranslatorForCssMedia.translate(media);
            if (!filter.passesFilter(deviceInfo)) {
                tag.setIgnore(true);
            }
        }

        return tag;
    }
}
