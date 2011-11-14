package dk.apaq.shopsystem.qash;

import com.vaadin.Application;
import dk.apaq.printing.core.PrinterManager;
import java.util.WeakHashMap;

/**
 *
 * @author michael
 */
public class PrintFacade {
    
    private static final WeakHashMap<Application, PrinterManager> managerMap = new WeakHashMap<Application, PrinterManager>();
    
    public static synchronized PrinterManager getManager(Application app) {
        PrinterManager manager = managerMap.get(app);
        if(manager==null) {
            manager = new PrinterManager();
            managerMap.put(app, manager);
        }
        return manager;
    }
}
