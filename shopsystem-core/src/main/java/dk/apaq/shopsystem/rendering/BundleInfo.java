package dk.apaq.shopsystem.rendering;

import dk.apaq.shopsystem.entity.SellerInfo;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author michael
 */
public class BundleInfo implements Serializable {
 
    
        private String version;
        private Date releaseDate;
        private SellerInfo seller;
        
        public String getVersion() {
            return version;
        }

        public Date getReleaseDate() {
            return releaseDate;
        }

        public SellerInfo getSellerInfo() {
            return seller;
        }

    
}
