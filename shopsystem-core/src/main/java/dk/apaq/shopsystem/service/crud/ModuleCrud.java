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

        } catch (IOException ex) {
            LOG.warn("Error reading module.", ex);
        }

        return null;

    }

    @Override
    public List<String> listIds() {
        List<String> idlist = new ArrayList<String>();

        for (Directory subdir : standardDir.getDirectories()) {
            if (subdir.isBundle() && "module".equals(subdir.getSuffix())) {
                idlist.add(subdir.getBaseName());
            }
        }

        return Collections.unmodifiableList(idlist);
    }

    @Override
    public List<Module> list() {
        List<String> idlist = listIds();
        List<Module> moduleList = new ArrayList();
        
        for(String id : idlist) {
            moduleList.add(read(id));
        }
        
        return moduleList;

    }

    
    @Override
    public List<String> listIds(Limit limit) {
        throw new UnsupportedOperationException("listing with limit not supported.");
    }

    @Override
    public List<Module> list(Limit limit) {
        throw new UnsupportedOperationException("listing with limit not supported.");
    }
    
    

}
