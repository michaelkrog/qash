/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.app.util;

import com.vaadin.Application;
import com.vaadin.ui.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang.time.DateUtils;
import org.vaadin.browsercookies.BrowserCookies;

/**
 *
 * @author michaelzachariassenkrog
 */
public class SystemSettings {

    private static Map<Application, BrowserCookies> map = new WeakHashMap<Application, BrowserCookies>();
    private final BrowserCookies cookies;
    private final Date expireDate = DateUtils.addYears(new Date(), 2);

    private SystemSettings(BrowserCookies cookies) {
        this.cookies = cookies;
    }

    public String get(String key) {
        return cookies.getCookie(key);
    }

    public void set(String key, String value) {
        cookies.setCookie(key, value, expireDate);
    }

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
