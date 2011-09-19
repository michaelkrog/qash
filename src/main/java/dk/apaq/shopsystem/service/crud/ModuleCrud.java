package dk.apaq.shopsystem.service.crud;

import dk.apaq.crud.Crud;
import dk.apaq.filter.limit.Limit;
import dk.apaq.shopsystem.rendering.Module;
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
    private final Directory dir;

    public ModuleCrud(Directory dir) {
        this.dir = dir;
    }
    
    @Override
    public Module  read(String id) {
        try {
            return new Module(dir.getDirectory(id));
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public List<String> listIds() {
        return listIds(null);
    }

    @Override
    public List<String> listIds(Limit limit) {
        List<String> idlist = new ArrayList<String>();

        int count=0;
        for(Directory subdir : dir.getDirectories()) {
            if(subdir.isBundle() && "module".equals(subdir.getSuffix())) {
                if(limit==null || count>=limit.getOffset()) {
                    idlist.add(subdir.getName());
                }

                if(limit!=null && count>=(limit.getOffset() + limit.getCount())) {
                    break;
                }
            }
        }

        return Collections.unmodifiableList(idlist);
    }

}
