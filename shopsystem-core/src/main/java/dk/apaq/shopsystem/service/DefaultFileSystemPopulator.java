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
    
    private String themeInfo = "{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}}";
    private String template = "<html><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /><body><div wicket:id=\"placeholder_1\"/></body></html>";
    private String stylesheet = "body { background:yellow;}";
    private String moduleInfo = "{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}}";
    
    private String imageComponentCode = "/*\n" +
                                    "*@Description(value=\"A simple image component\")\n"+
                                    "*@Parameter(name=\"path\",type=\"String\",optionalText=\"The filesystem path for the image\")\n"+
                                    "*/\n"+
                                    "function render(){\n" +
                                    "var gui = new JavaImporter(org.apache.wicket.markup.html.image);\n" +
                                    "with(gui) {parent.addComponent(new Image(\"image\");};\n"+
                                    "}\n";
    private String imageComponentMarkup = "<wicket:panel><img wicket:id=\"image\" /></wicket:panel>";
    
    private String labelComponentCode = "/*\n" +
                                    "*@Description(value=\"A simple label component\")\n"+
                                    "*@Parameter(name=\"text\",type=\"String\",optionalText=\"The text for the label\")\n"+
                                    "*/\n"+
                                    "function render(){\n" +
                                    "var gui = new JavaImporter(org.apache.wicket.markup.html.basic);\n" +
                                    "with(gui) {parent.addComponent(new Label(\"text\", parameters.get(\"text\").getString()));};\n"+
                                    "}\n";
    private String labelComponentMarkup = "<wicket:panel><span wicket:id=\"text\" /></wicket:panel>";
    
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
                    getDirectory("Standard.module", true);
            
            File infofile = themeDir.getFile("theme.info", true);
            OutputStreamWriter infoWriter = new OutputStreamWriter(infofile.getOutputStream());
            infoWriter.write(themeInfo);
            infoWriter.close();
            
            File templatefile = themeDir.getFile("Simple.html", true);
            OutputStreamWriter templateWriter = new OutputStreamWriter(templatefile.getOutputStream());
            templateWriter.write(template);
            templateWriter.close();

            File stylefile = themeDir.getFile("style.css", true);
            OutputStreamWriter styleWriter = new OutputStreamWriter(stylefile.getOutputStream());
            styleWriter.write(stylesheet);
            styleWriter.close();

            File moduleInfofile = moduleDir.getFile("module.info", true);
            OutputStreamWriter moduleInfoWriter = new OutputStreamWriter(moduleInfofile.getOutputStream());
            moduleInfoWriter.write(moduleInfo);
            moduleInfoWriter.close();
            
            File imageComponentCodeFile = moduleDir.getFile("Image.code", true);
            OutputStreamWriter imageComponentCodeWriter = new OutputStreamWriter(imageComponentCodeFile.getOutputStream());
            imageComponentCodeWriter.write(imageComponentCode);
            imageComponentCodeWriter.close();
            
            File imageComponentMarkupFile = moduleDir.getFile("Image.html", true);
            OutputStreamWriter imageComponentMarkupWriter = new OutputStreamWriter(imageComponentMarkupFile.getOutputStream());
            imageComponentMarkupWriter.write(imageComponentMarkup);
            imageComponentMarkupWriter.close();
            
            File labelComponentCodeFile = moduleDir.getFile("Label.code", true);
            OutputStreamWriter labelComponentCodeWriter = new OutputStreamWriter(labelComponentCodeFile.getOutputStream());
            labelComponentCodeWriter.write(labelComponentCode);
            labelComponentCodeWriter.close();
            
            File labelComponentMarkupFile = moduleDir.getFile("Label.html", true);
            OutputStreamWriter labelComponentMarkupWriter = new OutputStreamWriter(labelComponentMarkupFile.getOutputStream());
            labelComponentMarkupWriter.write(labelComponentMarkup);
            labelComponentMarkupWriter.close();
            
            
        } catch (IOException ex) {
            LOG.warn("Unable to populate filesystem.", ex);
        }
                
    }
    
    

    
}
