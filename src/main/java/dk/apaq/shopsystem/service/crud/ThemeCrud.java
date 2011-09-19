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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class ThemeCrud implements Crud<String, Theme> {

    private static final Logger LOG = LoggerFactory.getLogger(ThemeCrud.class);
    private final Directory dir;

    public ThemeCrud(Directory dir) {
        this.dir = dir;
    }
    
    @Override
    public Theme read(String id) {
        try {
            return new Theme(dir.getDirectory(id));
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
            if(subdir.isBundle() && "theme".equals(subdir.getSuffix())) {
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
