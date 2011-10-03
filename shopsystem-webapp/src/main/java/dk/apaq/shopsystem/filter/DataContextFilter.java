package dk.apaq.shopsystem.filter;

import dk.apaq.filter.core.CompareFilter;
import dk.apaq.shopsystem.context.DataContext;
import dk.apaq.shopsystem.entity.Domain;
import dk.apaq.shopsystem.entity.Organisation;
import dk.apaq.shopsystem.service.SystemService;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author krog
 */
public class DataContextFilter implements Filter {
    
    private static final Logger LOG = LoggerFactory.getLogger(DataContextFilter.class);
    private FilterConfig filterConfig = null;
    private ApplicationContext ac;
    private SystemService service;
    
    public DataContextFilter() {
    }

    /**
     *
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        String serverName = request.getServerName();

        //Optimize this by putting results in a map and listen for updateevents.
        dk.apaq.filter.Filter filter = new CompareFilter("name", serverName, CompareFilter.CompareType.Equals);
        List<String> idlist = service.getDomains().listIds(filter, null); 
        
        if (!idlist.isEmpty()) {
            Domain domain = service.getDomains().read(idlist.get(0));
            
            if(domain.getWebsite()!=null) {
                DataContext.setWebsite(domain.getWebsite());

                Organisation org = domain.getWebsite().getOrganisation();
                if (org != null) {
                    DataContext.setService(service.getOrganisationService(org));
                }
            }
        }
        
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            if (t instanceof ServletException) {
                throw (ServletException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            sendProcessingError(t, response);
        } finally {
            //Reset the datacontext
            DataContext.detach();
        }
        
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter 
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter 
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            ac = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
            service = ac.getBean(SystemService.class);
        }
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
}
