/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.qash.util;

import com.vaadin.terminal.gwt.server.ApplicationServlet;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.icepush.servlet.MainServlet;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.artur.icepush.JavascriptProvider;

/**
 *
 * @author michaelzachariassenkrog
 */
public class ICEPushServlet extends ApplicationServlet {

    private MainServlet ICEPushServlet;

    private JavascriptProvider javascriptProvider;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        try {
            super.init(servletConfig);
        } catch (ServletException e) {
            if (e.getMessage().equals(
                    "Application not specified in servlet parameters")) {
                // Ignore if application is not specified to allow the same
                // servlet to be used for only push in portals
            } else {
                throw e;
            }
        }

        ICEPushServlet = new MainServlet(servletConfig.getServletContext());

        try {
            String contextPath = getServletContext().getContextPath();

            //temporary
            contextPath = "/register";
            javascriptProvider = new JavascriptProvider(contextPath);

            ICEPush.setCodeJavascriptLocation(javascriptProvider
                    .getCodeLocation());
        } catch (IOException e) {
            throw new ServletException("Error initializing JavascriptProvider",
                    e);
        }
    }

    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null
                && pathInfo.equals("/" + javascriptProvider.getCodeName())) {
            // Serve icepush.js
            serveIcePushCode(request, response);
            return;
        }

        if (request.getRequestURI().endsWith(".icepush")) {
            // Push request
            try {
                ICEPushServlet.service(request, response);
            } catch (ServletException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // Vaadin request
            super.service(request, response);
        }
    }

    private void serveIcePushCode(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String icepushJavscript = javascriptProvider.getJavaScript();

        response.setHeader("Content-Type", "text/javascript");
        response.getOutputStream().write(icepushJavscript.getBytes());
    }

    @Override
    public void destroy() {
        super.destroy();
        ICEPushServlet.shutdown();
    }
}

