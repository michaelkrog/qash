package dk.apaq.shopsystem.ui;

import com.vaadin.Application;
import dk.apaq.shopsystem.service.OrganisationService;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 *
 * @author michael
 */
public class VaadinServiceHolder {
    
    private static final WeakHashMap<Application, OrganisationService> MAP = new WeakHashMap<Application, OrganisationService>();
    
    public static OrganisationService getService(Application app) {
        return MAP.get(app);
    }
    
    public static void setService(Application app, OrganisationService service) {
        MAP.put(app, service);
    }
    
}
