package com.movies.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.common.user.UserTo;
import com.movies.gateway.utils.SecurityUtils;
import com.movies.gateway.utils.ZuulFilterUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class UpdateUserDetailsFilter extends ZuulFilter {
    private static final List<AntPathRequestMatcher> pathMatchers = Collections.singletonList(
            new AntPathRequestMatcher("/users/profile/update", HttpMethod.POST.name(), false)
    );
    private ObjectMapper objectMapper;

    @Override
    public Object run() throws ZuulException {
        UserTo userTo = ZuulFilterUtils.addResponseBodyAndParseResponseAsUserTo(objectMapper);
        log.info("Update user details {}", userTo);
        SecurityUtils.updateUser(userTo);
        return null;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        return pathMatchers.stream().anyMatch(predicate -> predicate.matches(request));
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterOrderCollection.UPDATE_USER_DETAILS_FILTER_ORDER;
    }
}
