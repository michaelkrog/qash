package dk.apaq.shopsystem;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that gives access to Qash configuration.
 * @author michael
 */
public class QashConfig {

	private static Properties props = new Properties();
	private static Logger logger = Logger.getLogger(QashConfig.class.getName());
	
	static{
		try {
			props.load(QashConfig.class.getResourceAsStream("/META-INF/qash_config.properties"));
		} catch (Exception e) {
			logger.warning("Could not load qash_config.properties. Sysadmin user/pass defaulted to root/admin. "+e.getMessage());
		}
	}
	
	public static String getProperty(String key){
		return getProperty(key, null);
	}

        public static String getProperty(String key, String defaultValue){
		String value = props.getProperty(key);
                if(value==null)
                    value=defaultValue;
                logger.log(Level.FINE, "Config value retrieved [key="+key+"; value="+value+"]");
                return value;
	}
}
