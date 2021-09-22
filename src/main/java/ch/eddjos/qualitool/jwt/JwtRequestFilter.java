package ch.eddjos.qualitool.jwt;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.eddjos.qualitool.auth.BenutzerService;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
//    @Autowired
//    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private BenutzerService benutzerService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");


        String username = null;
        String jwtToken = null;
// JWT Token is in the form "Bearer token". Remove Bearer word and get
// only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.debug("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.debug("JWT Token has expired");
            } catch (SignatureException e) {
                logger.info("Token authentication failed. IP: {}:{}, Token Header: {}", request.getRemoteAddr(), request.getRemotePort() , requestTokenHeader);
            }
        } else {
            logger.trace("JWT Token does not begin with Bearer String");
        }


// Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.benutzerService.loadUserByUsername(username);
// if token is valid configure Spring Security to manually set
// authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// After setting the Authentication in the context, we specify
// that the current user is authenticated. So it passes the
// Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                logger.trace("Token authentication sucess. IP: {}:{}", request.getRemoteAddr(), request.getRemotePort());
            } else {
                logger.info("Token authentication failed. IP: {}:{}, Token: {}", request.getRemoteAddr(), request.getRemotePort() , jwtToken);
            }
        } else {
            if(requestTokenHeader!=null) {
                logger.info("Token authentication failed. IP: {}:{}, Token Header: {}", request.getRemoteAddr(), request.getRemotePort(), requestTokenHeader);
            }
        }
        chain.doFilter(request, response);
    }
}
