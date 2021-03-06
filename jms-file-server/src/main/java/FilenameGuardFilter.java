import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @Description TODO
 * @Date 2019/5/20 0020 下午 4:03
 * @Created by Pengrenjun
 */
public class FilenameGuardFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(FilenameGuardFilter.class);

    public void destroy() {
        // nothing to destroy
    }

    public void init(FilterConfig config) throws ServletException {
        // nothing to init
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            GuardedHttpServletRequest guardedRequest = new GuardedHttpServletRequest(httpRequest);
            chain.doFilter(guardedRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    private static class GuardedHttpServletRequest extends HttpServletRequestWrapper {

        public GuardedHttpServletRequest(HttpServletRequest httpRequest) {
            super(httpRequest);
        }

        private String guard(String filename) {
            String guarded = filename.replace(":", "_");
            if (LOG.isDebugEnabled()) {
                LOG.debug("guarded " + filename + " to " + guarded);
            }
            return guarded;
        }

        @Override
        public String getParameter(String name) {
            if (name.equals("Destination")) {
                return guard(super.getParameter(name));
            } else {
                return super.getParameter(name);
            }
        }

        @Override
        public String getPathInfo() {
            return guard(super.getPathInfo());
        }

        @Override
        public String getPathTranslated() {
            return guard(super.getPathTranslated());
        }

        @Override
        public String getRequestURI() {
            return guard(super.getRequestURI());
        }
    }
}
