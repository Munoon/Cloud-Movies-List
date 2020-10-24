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
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.zip.GZIPInputStream;

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
        try (InputStream responseDataStream = new GZIPInputStream(getResponseDataStream())) {
            String responseData = StreamUtils.copyToString(responseDataStream, StandardCharsets.UTF_8);
            ctx.setResponseBody(responseData);
        } catch (IOException e) {
            log.error("Error reading response", e);
            throw new ZuulException(
                    e, ERROR_READING_SERVER_RESPONSE_MESSAGE,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_READING_SERVER_RESPONSE_MESSAGE
            );
        }
    }

    private static InputStream getResponseDataStream() throws IOException {
        RequestContext ctx = RequestContext.getCurrentContext();
        InputStream responseDataStream = ctx.getResponseDataStream();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        responseDataStream.transferTo(outputStream);

        byte[] byteArray = outputStream.toByteArray();
        ctx.setResponseDataStream(new ByteArrayInputStream(byteArray));
        return new ByteArrayInputStream(byteArray);
    }

    public static UserTo parseResponseAsUserTo(ObjectMapper objectMapper) throws ZuulException {
        return readResponseAsModel(objectMapper, UserTo.class);
    }

    public static <T> T readResponseAsModel(ObjectMapper objectMapper, Class<T> tClass) throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            return objectMapper.readValue(ctx.getResponseBody(), tClass);
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
        int responseStatusCode = context.getResponseStatusCode();
        return responseStatusCode >= 200 && responseStatusCode < 400;
    }
}
