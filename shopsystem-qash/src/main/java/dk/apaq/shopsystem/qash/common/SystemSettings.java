package dk.apaq.shopsystem.qash.common;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang.time.DateUtils;
import org.vaadin.browsercookies.BrowserCookies;

/**
 * Defines a class that manages settings for a terminal. The settings is stored in Cookies in the remote browser on the Terminal..
 * @author michaelzachariassenkrog
 */
public class SystemSettings {

    private static Map<Application, BrowserCookies> map = new WeakHashMap<Application, BrowserCookies>();
    private final BrowserCookies cookies;
    private final Date expireDate = DateUtils.addYears(new Date(), 2);

    private SystemSettings(BrowserCookies cookies) {
        this.cookies = cookies;
    }

    /**
     * Retrieves a values using the given key.
     */
    public String get(String key) {
        return cookies.getCookie(key);
    }

    /**
     * Sets a setting using the specified key and value.
     */
    public void set(String key, String value) {
        cookies.setCookie(key, value, expireDate);
    }

    /**
     * Gets a <code>SystemSetting</code> for a given Application.
     */
    public static SystemSettings getInstance(Application app) {
        BrowserCookies cookies = map.get(app);
        if(cookies == null) {
            cookies = createCookies(app);
            map.put(app, cookies);
        }
        return new SystemSettings(cookies);
    }

    private static BrowserCookies createCookies(Application app) {
        //look for already added browsercookies
        Iterator<Component> it = app.getMainWindow().getComponentIterator();
        while(it.hasNext()) {
            Component c = it.next();
            if(c instanceof BrowserCookies) {
                return (BrowserCookies)c;
            }
        }

        //...else create and add a new one.
        BrowserCookies cookies = new BrowserCookies();
        app.getMainWindow().addComponent(cookies);
        return cookies;
    }
}
