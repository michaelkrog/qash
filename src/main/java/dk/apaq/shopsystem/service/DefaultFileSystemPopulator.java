package dk.apaq.shopsystem.service;

import dk.apaq.vfs.Directory;
import dk.apaq.vfs.File;
import dk.apaq.vfs.FileSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class DefaultFileSystemPopulator implements FileSystemPopulator {

    private String themeInfo = "{\"version\":\"1.0.0\",  \"releaseDate\":\"2011-01-01\", \"seller\": { \"id\":\"qwerty\", \"name\":\"Apaq\", \"email\": \"mic@apaq.dk\"}, templates:{\"Simple\":{ \"description\":\"A simple page template\"}}}";
    private String template = "<html><body></body></html>";
    
    
    @Override
    public void populate(FileSystem fileSystem) {
        try {
            Directory dir = fileSystem.getRoot().
                    getDirectory("System", true).
                    getDirectory("Themes", true).
                    getDirectory("Standard", true).
                    getDirectory("Basic.theme", true);
            
            File infofile = dir.getFile("theme.info", true);
            OutputStreamWriter infoWriter = new OutputStreamWriter(infofile.getOutputStream());
            infoWriter.write(themeInfo);
            infoWriter.close();
            
            File templatefile = dir.getFile("Simple.html", true);
            OutputStreamWriter templateWriter = new OutputStreamWriter(templatefile.getOutputStream());
            templateWriter.write(template);
            templateWriter.close();
            
            
        } catch (IOException ex) {
            Logger.getLogger(DefaultFileSystemPopulator.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    

    
}
