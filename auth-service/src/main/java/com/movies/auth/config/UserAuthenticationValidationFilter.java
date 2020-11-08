package com.movies.auth.config;

import com.movies.auth.authentications.UserAuthenticationService;
import com.movies.common.AuthorizedUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@AllArgsConstructor
public class UserAuthenticationValidationFilter extends GenericFilterBean {
    private UserAuthenticationService service;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !(authentication.getPrincipal() instanceof AuthorizedUser) // false for oauth client request
                || !(authentication.getDetails() instanceof WebAuthenticationDetails)
                || ((WebAuthenticationDetails) authentication.getDetails()).getSessionId() == null) {
            chain.doFilter(req, resp);
            return;
        }

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String sessionId = details.getSessionId();
        boolean testResult = service.testSession(sessionId);

        if (!testResult) {
            ((HttpServletRequest) req).logout();
            SecurityContextHolder.getContext().setAuthentication(null);
            logger.info("Logged out authentication " + authentication);
        }

        chain.doFilter(req, resp);
    }
}
