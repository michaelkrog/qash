package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.rendering.Template;
import dk.apaq.shopsystem.rendering.Theme;
import dk.apaq.vfs.Directory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ThemeCrud implements Crud<String, Theme> {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeCrud.class);
    private final Directory standardDir;
    private final Directory orgDir;

    public ThemeCrud(Directory standardThemeFolder, Directory organsiationThemeFolder) {
        this.standardDir = standardThemeFolder;
        this.orgDir = organsiationThemeFolder;
    }

    @Override
    public Theme read(String id) {
        try {
            if (orgDir.hasDirectory(id)) {
                return new Theme(orgDir.getDirectory(id));
            }

            if (standardDir.hasDirectory(id)) {
                return new Theme(standardDir.getDirectory(id));
            }

        } catch (IOException ex) {
        }
        
        return null;

    }

    @Override
    public List<String> listIds() {
        List<String> idlist = new ArrayList<String>();

        for (Directory subdir : standardDir.getDirectories()) {
            if (subdir.isBundle() && "theme".equals(subdir.getSuffix())) {
                idlist.add(subdir.getName());
            }
        }

        for (Directory subdir : orgDir.getDirectories()) {
            String name = subdir.getName();
            if (subdir.isBundle() && "theme".equals(subdir.getSuffix()) && !idlist.contains(name)) {
                idlist.add(name);
            }
        }

        return Collections.unmodifiableList(idlist);
    }

    @Override
    public List<String> listIds(Limit limit) {
        throw new UnsupportedOperationException("listing ids with limit not supported.");
    }
}
