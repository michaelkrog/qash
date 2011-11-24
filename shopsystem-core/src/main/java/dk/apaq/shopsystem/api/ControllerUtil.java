package dk.apaq.shopsystem.api;

import dk.apaq.filter.limit.Limit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author krog
 */
public class ControllerUtil {

    /**
     * Ensures that a Limit is not null and does not exceed maxLimit.
     * If limit is null it will se set to defaultLimit. If limit exceeds
     * maxLimit it will be set to maxLimit.
     */
    public static Integer validateLimit(Integer limit, int defaultLimit, int maxLimit) {

        // For server protection, a limit of 1000 can't be exceeded
        if (limit == null) {
            limit = defaultLimit;
        }
        if (limit > maxLimit) {
            limit = maxLimit;
        }

        return limit;
    }

    public static Integer validateLimit(Integer limit) {
        return validateLimit(limit, 100, 1000);
    }

    public static Limit validateAndCreateLimit(int limit) {
        return new Limit(validateLimit(limit));
    }
    
    /**
     * determines URL of child resource based on the full URL of the given request,
     * appending the path info with the given childIdentifier using a UriTemplate.
     */
    public static String getLocationForChildResource(HttpServletRequest request, Object childIdentifier) {
        StringBuffer url = request.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
        return template.expand(childIdentifier).toASCIIString();
    }
}
