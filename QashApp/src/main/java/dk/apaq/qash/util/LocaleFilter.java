package dk.apaq.qash.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class LocaleFilter implements Filter {

    private class LocaleWrapperingRequest extends HttpServletRequestWrapper {

        private final Locale locale;

        public LocaleWrapperingRequest(HttpServletRequest request, Locale locale) {
            super(request);
            this.locale = locale;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }
    }

    private static final String DEFAULT_LANGUAGE = "en";
    private static final String[] SUPPORTED_LANGUAGES = {"en", "da"};

    private class PathChangingRequest extends HttpServletRequestWrapper {

        private final String pathInfo;
        private final Locale locale;

        public PathChangingRequest(HttpServletRequest request, String context, Locale locale) {
            super(request);
            this.pathInfo = context;
            this.locale = locale;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }

        @Override
        public StringBuffer getRequestURL() {
            StringBuffer sb = new StringBuffer();
            sb.append(getScheme());
            sb.append("://");
            sb.append(getServerName());
            sb.append(":");
            sb.append(getServerPort());
            sb.append(getContextPath());
            sb.append(getServletPath());
            return sb;
        }

        @Override
        public ServletRequest getRequest() {
            return super.getRequest();
        }

        @Override
        public String getServletPath() {
            String path = super.getServletPath();
            //path = path.substring(3);
            return path;
        }


        @Override
        public String getContextPath() {
            String contextPath = pathInfo + super.getContextPath();
            return contextPath;
        }

        @Override
        public String getPathInfo() {
            String pathInfo = super.getPathInfo();
            return pathInfo;
        }

        @Override
        public String getPathTranslated() {
            String pathTranslated = super.getPathTranslated();
            return pathTranslated;
        }

        @Override
        public String getRequestURI() {
            String uri = super.getRequestURI();
            if(!uri.startsWith(pathInfo)) {
                uri = pathInfo + uri;
            }
            //uri = uri.substring(3);
            //insert pathinfo
            return uri;
        }

        @Override
        public String getRealPath(String path) {
            return super.getRealPath(path);
        }

    }

    private class PathChangingRequest2 extends HttpServletRequestWrapper {

        private final String pathInfo;
        private final Locale locale;

        public PathChangingRequest2(HttpServletRequest request, String context, Locale locale) {
            super(request);
            this.pathInfo = context;
            this.locale = locale;
        }

        @Override
        public Locale getLocale() {
            return locale;
        }

        @Override
        public StringBuffer getRequestURL() {
            StringBuffer sb = new StringBuffer();
            sb.append(getScheme());
            sb.append("://");
            sb.append(getServerName());
            sb.append(":");
            sb.append(getServerPort());
            sb.append(getContextPath());
            sb.append(getServletPath());
            return sb;
        }

        @Override
        public String getServletPath() {
            String path = super.getServletPath();
            path = path.substring(3);
            return path;
        }

        
        @Override
        public String getContextPath() {
            String contextPath = /*pathInfo +*/ super.getContextPath();
            return contextPath;
        }

        @Override
        public String getPathInfo() {
            String pathInfo = super.getPathInfo();
            return pathInfo;
        }

        @Override
        public String getPathTranslated() {
            String pathTranslated = super.getPathTranslated();
            return pathTranslated;
        }

        @Override
        public String getRequestURI() {
            String uri = super.getRequestURI();
            uri = uri.substring(3);
            //insert pathinfo
            return uri;
        }

        @Override
        public String getRealPath(String path) {
            return super.getRealPath(path);
        }

    }
    
    private List<String> excludes = new ArrayList<String>();
    private String[] ignoreList = {"/api", "/annex"/*, "/code.icepush"*/};

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    private boolean isIgnored(String path) {
        for (String ignorePath : ignoreList) {
            if (path.startsWith(ignorePath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            String geo = (String) httpRequest.getSession().getAttribute("geo");

            String path = httpRequest.getServletPath();
            boolean useDefault = false;

            if (isIgnored(path)) {
                chain.doFilter(request, response);
                return;
            }

            if (httpRequest.getQueryString() != null) {
                path += "?" + httpRequest.getQueryString();
            }


            Locale locale = getLocaleFromPath(path);

            if (locale == null) {
                locale = request.getLocale();

                if(DEFAULT_LANGUAGE.equals(locale.getLanguage())) {
                    chain.doFilter(request, response);
                    return;
                }

                httpResponse.sendRedirect("/" + locale.getLanguage() + path);
                return;
            }

            //The first part of he path actually matched a 2-letter
            //language code. Remove it from the path now that we have a locale.
            path = path.substring(3);

            if ("".equals(path)) {
                path = "/";
            }


            if (!isLanguageSupported(locale)) {
                //TODO Should get af default locale from LocaleUtil
                locale = Locale.ENGLISH;

                httpResponse.sendRedirect("/" + locale.getLanguage() + path);
                return;
            }

            request = new PathChangingRequest2(httpRequest, "/" + locale.getLanguage(), locale);
            //httpRequest.getRequestDispatcher(path).forward(new PathChangingRequest(httpRequest, "/" + locale.getLanguage(), locale), response);
            //return;

        }

        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String excludeParam = config.getInitParameter("excludeList");
        if (excludeParam != null) {
            excludes.addAll(Arrays.asList(excludeParam.split(",")));
        }
    }

    private Locale getLocaleFromPath(String path) {
        //The context of the users request revolve his wanted locale
        //if first path element if two-lettered and one of the supported locales
        //then we resolve the locale, remove the context and set the locale as property
        //if no matching path element is found, then default to english.
        String lang = "en";
        Locale locale = null;

        if (path != null && path.length() > 2) {
            String tmp = path;

            //It should according to standard start with a slash
            //We will remove it before splitting to array
            tmp = tmp.substring(1);

            String[] array = tmp.split("/");
            if (array[0].length() == 2) {
                lang = array[0];

                locale = getLocaleFromLanguageCode(lang);
            }

        }
        return locale;
    }

    private Locale getLocaleFromCountryCode(String country) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getCountry().equals(country)) {
                return locale;
            }
        }
        return null;
    }

    private Locale getLocaleFromLanguageCode(String language) {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().equals(language)) {
                return locale;
            }
        }
        return null;
    }

    private boolean isLanguageSupported(Locale locale) {
        for (String language : SUPPORTED_LANGUAGES) {
            if (language.equals(locale.getLanguage())) {
                return true;
            }
        }
        return false;
    }
}
