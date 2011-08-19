/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.annex;

import java.io.OutputStream;
import javax.swing.JComponent;

/**
 *
 * @author michael
 */
public interface AnnexComposer {

    public void compose(JComponent component, OutputStream output) throws Exception;

}
