package dk.apaq.shopsystem.service;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author michael
 */
public class DefaultFileSystemPopulator implements FileSystemPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultFileSystemPopulator.class);
    
    private String themeInfo = "{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}, templates:{\"Simple\":{ \"description\":\"A simple page template\"}}}";
    private String template = "<html><body><div wicket:id=\"placeholder_1\"/></body></html>";
    private String moduleInfo = "{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}, components:{\"SingleImage\":{\"description\":\"A simple component for displaying an image\",  \"parameters\":{ \"path\":{ \"type\":\"String\",  \"default\":\"\",  \"optionalText\":\"The filesystem path for the image\" },  \"title\":{  \"type\":\"String\", \"default\":\"\",  \"optionalText\":\"The title of the image\" },\"styleclass\":{  \"type\":\"String\",  \"default\":\"\", \"optionalText\":\"Special styleclass to append to image\"} }    } } }";
    private String componentCode = "function render(){}";
    private String componentMarkup = "<wicket:panel><div>This is from a component</div></wicket:panel>";
    
    @Override
    public void populate(FileSystem fileSystem) {
        try {
            Directory root = fileSystem.getRoot();
            root.getDirectory("Organisations", true);

            root.getDirectory("Users", true);
            
            root.getDirectory("System", true).
                getDirectory("Modules", true).
                getDirectory("Standard", true);
            
            root.getDirectory("System", true).
                getDirectory("Modules", true).
                getDirectory("Optional", true);
            
            root.getDirectory("System", true).
                getDirectory("Themes", true).
                getDirectory("Standard", true);
            
            root.getDirectory("System", true).
                getDirectory("Themes", true).
                getDirectory("Optional", true);

            Directory themeDir = root.
                    getDirectory("System", true).
                    getDirectory("Themes", true).
                    getDirectory("Standard", true).
                    getDirectory("Basic.theme", true);
            
            Directory moduleDir = root.
                    getDirectory("System", true).
                    getDirectory("Modules", true).
                    getDirectory("Standard", true).
                    getDirectory("Image.module", true);
            
            File infofile = themeDir.getFile("theme.info", true);
            OutputStreamWriter infoWriter = new OutputStreamWriter(infofile.getOutputStream());
            infoWriter.write(themeInfo);
            infoWriter.close();
            
            File templatefile = themeDir.getFile("Simple.html", true);
            OutputStreamWriter templateWriter = new OutputStreamWriter(templatefile.getOutputStream());
            templateWriter.write(template);
            templateWriter.close();
            
            File moduleInfofile = moduleDir.getFile("module.info", true);
            OutputStreamWriter moduleInfoWriter = new OutputStreamWriter(moduleInfofile.getOutputStream());
            moduleInfoWriter.write(moduleInfo);
            moduleInfoWriter.close();
            
            File componentCodeFile = moduleDir.getFile("SingleImage.code", true);
            OutputStreamWriter componentCodeWriter = new OutputStreamWriter(componentCodeFile.getOutputStream());
            componentCodeWriter.write(componentCode);
            componentCodeWriter.close();
            
            File componentMarkupFile = moduleDir.getFile("SingleImage.html", true);
            OutputStreamWriter componentMarkupWriter = new OutputStreamWriter(componentMarkupFile.getOutputStream());
            componentMarkupWriter.write(componentMarkup);
            componentMarkupWriter.close();
            
            
        } catch (IOException ex) {
            LOG.warn("Unable to populate filesystem.", ex);
        }
                
    }
    
    

    
}
