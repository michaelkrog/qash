package dk.apaq.shopsystem.qash;

import com.vaadin.Application;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterManager;
import java.util.WeakHashMap;

/**
 * A facade class with static methods for handling the current <code>PrinterManager</code>.
 * @author michael
 */
public class PrintFacade {
    
    private static final WeakHashMap<Application, PrinterManager> managerMap = new WeakHashMap<Application, PrinterManager>();
    private static final String BASE_PRINTTYPE_SETTING = "PRINTTYPE_";
    public static final String PRINT_ON_COMPLETE_SETTING = "PRINT_ON_COMPLETE";

    /**
     * Retrieves the <code>PrinterManager</code> for the current <code>Application</code>.
     * If no PrinterManager is yet created, it will create a new, store it and return it.
     * 
     * @return The PrinterManager. Never null.
     */
    public static synchronized PrinterManager getManager(Application app) {
        PrinterManager manager = managerMap.get(app);
        if(manager==null) {
            manager = new PrinterManager();
            managerMap.put(app, manager);
        }
        return manager;
    }
    
    
    public static String generatePrintTypeSettingKey(Printer printer) {
        if(printer==null) {
            throw new NullPointerException("ps must not be null");
        }
        return BASE_PRINTTYPE_SETTING + printer.getId();
    }
}
