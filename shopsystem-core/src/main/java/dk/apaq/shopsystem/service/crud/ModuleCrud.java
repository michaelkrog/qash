package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.entity.Module;
import dk.apaq.vfs.Directory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ModuleCrud implements Crud<String, Module> {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleCrud.class);
     private final Directory standardDir;
    private final Directory orgDir;

    public ModuleCrud(Directory standardThemeFolder, Directory organsiationThemeFolder) {
        this.standardDir = standardThemeFolder;
        this.orgDir = organsiationThemeFolder;
    }

    @Override
    public Module read(String id) {
        try {
            String name = id + ".module";
            if (orgDir.hasDirectory(name)) {
                return new Module(orgDir.getDirectory(name));
            }

            if (standardDir.hasDirectory(name)) {
                return new Module(standardDir.getDirectory(name));
            }

        } catch (IOException ex) {
        }

        return null;

    }

    @Override
    public List<String> listIds() {
        List<String> idlist = new ArrayList<String>();

        for (Directory subdir : standardDir.getDirectories()) {
            if (subdir.isBundle() && "module".equals(subdir.getSuffix())) {
                idlist.add(subdir.getName());
            }
        }

        for (Directory subdir : orgDir.getDirectories()) {
            String name = subdir.getName();
            if (subdir.isBundle() && "module".equals(subdir.getSuffix()) && !idlist.contains(name)) {
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
