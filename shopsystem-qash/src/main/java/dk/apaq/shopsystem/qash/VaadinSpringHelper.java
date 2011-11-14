package dk.apaq.shopsystem.qash;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Class for accessiing Spring ApplicationContext from within Vaadin.
 * @author michaelzachariassenkrog
 */
public class VaadinSpringHelper {

    public static ApplicationContext getSpringContextFromVaadinContext(com.vaadin.service.ApplicationContext vaadinContext) {
        WebApplicationContext webCtx = (WebApplicationContext) vaadinContext;
        return WebApplicationContextUtils.getWebApplicationContext(webCtx.getHttpSession().getServletContext());
    }
}
