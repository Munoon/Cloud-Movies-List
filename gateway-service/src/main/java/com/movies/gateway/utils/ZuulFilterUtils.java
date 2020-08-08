package com.movies.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.movies.common.user.UserTo;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
public class ZuulFilterUtils {
    private static final String ERROR_READING_SERVER_RESPONSE_MESSAGE = "Error reading microservice response";
    private static final String RESPONSE_PARSE_ERROR_MESSAGE = "Can't parse microservice response";

    public static UserTo addResponseBodyAndParseResponseAsUserTo(ObjectMapper objectMapper) throws ZuulException {
        addResponseBodyToContext();
        return parseResponseAsUserTo(objectMapper);
    }

    public static void addResponseBodyToContext() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        try (InputStream responseDataStream = ctx.getResponseDataStream();
             InputStreamReader reader = new InputStreamReader(responseDataStream, StandardCharsets.UTF_8)) {
            String responseData = CharStreams.toString(reader);
            ctx.setResponseBody(responseData);
        } catch (IOException e) {
            log.error("Error reading response", e);
            throw new ZuulException(
                    e, ERROR_READING_SERVER_RESPONSE_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_READING_SERVER_RESPONSE_MESSAGE
            );
        }
    }

    public static UserTo parseResponseAsUserTo(ObjectMapper objectMapper) throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            return objectMapper.readValue(ctx.getResponseBody(), UserTo.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing response", e);
            throw new ZuulException(
                    e, RESPONSE_PARSE_ERROR_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), RESPONSE_PARSE_ERROR_MESSAGE
            );
        }
    }

    public static boolean isRequestMatch(Collection<AntPathRequestMatcher> matchers) {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        return matchers.stream().anyMatch(predicate -> predicate.matches(request));
    }

    public static boolean isResponseOk() {
        RequestContext context = RequestContext.getCurrentContext();
        return context.getResponseStatusCode() >= 200 && context.getResponseStatusCode() < 400;
    }
}
