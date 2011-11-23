package dk.apaq.shopsystem.rendering.module;

import dk.apaq.shopsystem.entity.ComponentParameter;
import dk.apaq.shopsystem.rendering.CmsApplication;
import java.util.Map;
import org.apache.wicket.Application;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

/**
 *
 * @author michael
 */
public class Image extends CmsModule {

    public Image(String id) {
        super(id);
       
    }

    @Override
    public void compose() {
        String path = parameters.get("path").getString();
        ResourceReference resourceRef;
        CmsApplication application = (CmsApplication) Application.get();
        PageParameters pp = new PageParameters();
        pp.add("orgid", application.getOrgansiationService().readOrganisation().getId());
        pp.add("siteid", webSite.getId());
        
        if(path.startsWith("/Content/")) {
            resourceRef = application.getContentResourceReference();
            path=path.substring(9);
        } else if(path.startsWith("/Themes/")) {
            resourceRef = application.getThemeResourceReference();
            path=path.substring(8);
            int firstSlash = path.indexOf("/");
            pp.add("themename", path.substring(0,firstSlash));
            path = path.substring(firstSlash+1);
            
        }  else {
            throw new IllegalArgumentException("Path should start with /Content or /Themes");
        }
         String[] patharray = path.split("/");
         for(int i=0;i<patharray.length;i++) {
            pp.set(i, patharray[i]);
        }
       add(new org.apache.wicket.markup.html.image.Image("image", resourceRef, pp));
        
        
    }
    
    
    
    
    
}
