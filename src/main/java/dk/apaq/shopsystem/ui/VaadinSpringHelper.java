/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.shopsystem.ui;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 *
 * @author michaelzachariassenkrog
 */
public class VaadinSpringHelper {

    public static ApplicationContext getSpringContextFromVaadinContext(com.vaadin.service.ApplicationContext vaadinContext) {
        WebApplicationContext webCtx = (WebApplicationContext) vaadinContext;
        return WebApplicationContextUtils.getWebApplicationContext(webCtx.getHttpSession().getServletContext());
    }
}
