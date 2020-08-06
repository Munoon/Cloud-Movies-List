package com.movies.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movies.common.error.ErrorInfo;
import com.movies.common.error.ErrorType;
import com.movies.common.util.ErrorUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

@Slf4j
@Configuration
@AllArgsConstructor
public class ZuulExceptionFilter extends ZuulFilter {
    private ObjectMapper objectMapper;

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Object exception = ctx.getThrowable();

        try {
            if (!(exception instanceof ZuulException)) {
                return null;
            }

            ZuulException zuulException = (ZuulException) exception;
            log.error("ZuulException detected: {}", zuulException.getMessage(), zuulException);

            ctx.remove("throwable");
            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_VALUE);
            ctx.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

            var requestURL = ctx.getRequest().getRequestURL();
            var message = ErrorUtils.getRootCause(zuulException).getLocalizedMessage();
            var errorInfo = new ErrorInfo(requestURL, ErrorType.APPLICATION_EXCEPTION, message);

            String errorInfoJson = objectMapper.writeValueAsString(errorInfo);
            ctx.setResponseBody(errorInfoJson);
        } catch (IOException e) {
            log.error("Error parse zuul exception", e);
        }

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getThrowable() != null;
    }

    @Override
    public int filterOrder() {
        return FilterOrderCollection.ZUUL_EXCEPTION_FILTER_ORDER;
    }

    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }
}
