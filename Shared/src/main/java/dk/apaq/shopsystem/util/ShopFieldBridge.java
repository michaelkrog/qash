/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.util;

import dk.apaq.shopsystem.model.Store;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.StringBridge;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ShopFieldBridge implements StringBridge {

    public String objectToString(Object object) {
        return ((Store)object).getId();
    }



}
