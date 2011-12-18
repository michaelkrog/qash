package dk.apaq.shopsystem.util;

import dk.apaq.shopsystem.service.SystemService;
import dk.apaq.shopsystem.service.SystemServiceHolder;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author michael
 */
public class SystemServiceFilter implements Filter {

    private ServletContext servletContext;
    private ApplicationContext applicationContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SystemServiceHolder.setSystemService(applicationContext.getBean(SystemService.class));

        try {
            chain.doFilter(request, response);
        } finally {
            SystemServiceHolder.setSystemService(null);
        }
    }

    @Override
    public void destroy() {
    }
}
