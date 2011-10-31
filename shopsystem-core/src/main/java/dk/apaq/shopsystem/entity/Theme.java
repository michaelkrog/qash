package dk.apaq.shopsystem.entity;

import dk.apaq.shopsystem.rendering.template.TemplateInformationExtractor;
import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a Template for a Website.
 */
public class Theme extends AbstractModule implements Serializable {

    private static final TemplateInformationExtractor TEMPLATE_INFORMATION_EXTRACTOR = new TemplateInformationExtractor();
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
                    TemplateInformationExtractor.TemplateInformation info = TEMPLATE_INFORMATION_EXTRACTOR.readTemplateInformation(markupFile);
                    
                    if(dir.hasFile(codeFileName)) {
                        codeFile = dir.getFile(codeFileName);
                    }
                    
                    newList.add(new Template(markupFile.getBaseName(), info.getPlaceholders(), info.getComponentInformations(), markupFile, codeFile));
                    
                } catch (IOException ex) {
                    //Cannot find file - warn about it but otherwise ignore it.
                    LOG.warn("Template registered in theme is not found. [Theme=" + getName() + ";Template=" + markupFile.getBaseName() + "]");
                }
            }
            templateList = Collections.unmodifiableList(newList);
        }
        return templateList;
    }

    
    @Override
    protected String getBundleSuffix() {
        return "theme";
    }
    

}
