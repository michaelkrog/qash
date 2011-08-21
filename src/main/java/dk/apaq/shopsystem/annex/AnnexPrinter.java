/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import dk.apaq.shopsystem.entity.Order;
import dk.apaq.shopsystem.entity.Store;
import java.awt.print.Printable;
import java.io.OutputStream;

/**
 *
 * @author michael
 */
public interface AnnexPrinter {

    public void print(Printable printable, PageSize pageSize, OutputStream output) throws Exception;
    
}
