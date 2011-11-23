package dk.apaq.shopsystem.rendering.template;

import dk.apaq.shopsystem.entity.ComponentInformation;
import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.entity.Placeholder;
import dk.apaq.shopsystem.rendering.CmsTagIdentifier;
import dk.apaq.vfs.File;
import dk.apaq.vfs.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.parser.IMarkupFilter;
import org.apache.wicket.markup.parser.IXmlPullParser;
import org.apache.wicket.markup.parser.XmlPullParser;
import org.apache.wicket.markup.parser.XmlTag;
import org.apache.wicket.markup.parser.filter.WicketLinkTagHandler;
import org.apache.wicket.markup.parser.filter.WicketRemoveTagHandler;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class TemplateInformationExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateInformationExtractor.class);

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
        try {
            List<ComponentInformation> componentInformations = new ArrayList<ComponentInformation>();
            List<Placeholder> placeholders = new ArrayList<Placeholder>();
            TemplateInformation templateInfo = new TemplateInformation(componentInformations, placeholders);

            IXmlPullParser xmlParser = new XmlPullParser();
            xmlParser.parse(file.getInputStream());
            boolean inContainer = false;
            boolean inCmsComponentTag = false;
            ComponentInformation componentInformation = null;
            Placeholder currentPlaceHolder = null;

            IXmlPullParser.HttpTagType tagType = null;
            while ((tagType = xmlParser.next()) != IXmlPullParser.HttpTagType.NOT_INITIALIZED) {
                XmlTag tag = xmlParser.getElement();

                if ("wicket".equals(tag.getNamespace())) {
                    if (tag.isClose() && "container".equals(tag.getName()) && inContainer) {
                        inContainer = false;
                        continue;
                    }

                    if (!tag.isClose() && !inContainer) {
                        CharSequence id = tag.getAttribute("wicket:id");
                        if (id != null) {
                            String idString = id.toString();
                            if ("container".equals(tag.getName())) {
                                currentPlaceHolder = new Placeholder(idString, true);
                                inContainer = true;
                            } else {
                                currentPlaceHolder = new Placeholder(idString, false);
                            }
                        }
                        placeholders.add(currentPlaceHolder);
                        continue;
                    }
                }

                if ("cms".equals(tag.getNamespace())) {

                    if (!tag.isClose() && "parameter".equals(tag.getName()) && inCmsComponentTag) {
                        String name = tag.getAttribute("name") == null ? null : tag.getAttribute("name").toString();
                        String value = tag.getAttribute("value") == null ? null : tag.getAttribute("value").toString();
                        String type = tag.getAttribute("type") == null ? null : tag.getAttribute("type").toString();

                        //type is optional - default it to string
                        type = type == null ? "String" : type;

                        ComponentParameter componentParameter = createComponentParameter(file, value, type);
                        componentInformation.getParameterMap().put(name, componentParameter);
                        continue;
                    }

                    if (tag.isClose() && "module".equals(tag.getName()) && inCmsComponentTag) {
                        inCmsComponentTag = false;
                        if(componentInformation!=null) {
                            componentInformations.add(componentInformation);
                            componentInformation = null;
                        }
                        continue;
                    }

                    if (!tag.isClose() && "module".equals(tag.getName()) && !inCmsComponentTag && inContainer) {
                        inCmsComponentTag = true;
                        String name = tag.getAttribute("name") == null ? null : tag.getAttribute("name").toString();
                        if(name!=null) {
                            componentInformation = new ComponentInformation();
                            componentInformation.setModuleName(name);
                            componentInformation.setPlaceholderName(currentPlaceHolder.getId());
                        }
                        continue;
                    }
                }
            }

            return templateInfo;
        } catch (ParseException ex) {
            throw new IOException("Error parsing template.", ex);
        } catch (ResourceStreamNotFoundException ex) {
            throw new IOException("Error parsing template.", ex);
        }
    }

    private ComponentParameter createComponentParameter(File templatFile, String value, String type) {
        ComponentParameter cp = new ComponentParameter();
        if ("Path".equalsIgnoreCase(type)) {
            Path path = null;
            if (!value.startsWith("/")) {
                try {
                    path = templatFile.getParent().getPath();
                    Path relativePath = new Path(value);
                    for (int i = 0; i < relativePath.getLevels(); i++) {
                        path.addLevel(relativePath.getLevel(i));
                    }
                } catch (FileNotFoundException ex) {
                    LOG.error("Unknown error occured.", ex);
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
