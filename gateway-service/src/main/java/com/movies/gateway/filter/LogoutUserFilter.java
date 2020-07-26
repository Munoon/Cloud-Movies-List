package com.movies.gateway.filter;

import com.movies.gateway.utils.ZuulFilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class LogoutUserFilter extends ZuulFilter {
    private static final String LOGOUT_ERROR_MESSAGE = "Error logout user";
    private static final List<AntPathRequestMatcher> pathMatchers = Collections.singletonList(
            new AntPathRequestMatcher("/users/profile/update/email", HttpMethod.POST.name(), false)
    );

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            log.error("Error logout user on path {}", request.getServletPath(), e);
            throw new ZuulException(
                    e, LOGOUT_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), LOGOUT_ERROR_MESSAGE
            );
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return ZuulFilterUtils.isRequestMatch(pathMatchers);
    }

    @Override
    public int filterOrder() {
        return FilterOrderCollection.LOGOUT_USER_FILTER_ORDER;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
