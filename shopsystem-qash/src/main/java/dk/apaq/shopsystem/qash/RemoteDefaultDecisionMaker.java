package dk.apaq.shopsystem.qash;

import dk.apaq.printing.core.DefaultPrinterDecisionMaker;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterManager;
import dk.apaq.printing.core.PrinterManagerPlugin;
import dk.apaq.printing.remoteclient.RemoteClientPlugin;

/**
 *
 * @author michael
 */
public class RemoteDefaultDecisionMaker implements DefaultPrinterDecisionMaker {

    @Override
    public Printer getDefaultPrinter(PrinterManager manager) {
        Printer printer = null;
        for (int i = 0; i < manager.getPluginCount(); i++) {
            PrinterManagerPlugin plugin = manager.getPlugin(i);
            printer = plugin.getDefaultPrinter();

            if (printer != null && plugin instanceof RemoteClientPlugin) {
                return printer;
            }
        }
        return printer;
    }
}
