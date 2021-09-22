package ch.eddjos.qualitool.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class StaticContentFilter implements Filter /*extends OncePerRequestFilter*/ {
/*
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        Filter a = this;

        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT,OPTIONS");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
*/

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)response;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        res.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

}
