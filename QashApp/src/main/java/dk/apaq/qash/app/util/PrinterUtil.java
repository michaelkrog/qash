/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.util;

import javax.print.PrintService;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PrinterUtil {

    private static final String BASE_PRINTTYPE_SETTING = "PRINTTYPE_";
    public static final String PRINT_ON_COMPLETE_SETTING = "PRINT_ON_COMPLETE";

    public static String generatePrintTypeSettingKey(PrintService ps) {
        if(ps==null) {
            throw new NullPointerException("ps must not be null");
        }
        return BASE_PRINTTYPE_SETTING + ps.getName();
    }


}
