package com.polovyi.ivan.tutorials.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Log4j2
@RequiredArgsConstructor
public class RequestLogFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(requestWrapper, responseWrapper);
        logRequest(requestWrapper);
        logResponse(responseWrapper);
    }

    @SneakyThrows
    private void logRequest(ContentCachingRequestWrapper request) {
        String parameters = parametersToString(request.getParameterMap());
        String headers = headersToString(Collections.list(request.getHeaderNames()), request::getHeader);
        String body = new String(request.getContentAsByteArray());
        Map<String, String> logMap = new LinkedHashMap<>() {{
            put("\nParameters:", parameters);
            put("\nHeaders:", headers);
            put("\nBody:", body);
        }};
        String logString = joinMapIntoString(logMap);
        log.info("\nREQUEST:\nAPI: {}", request.getMethod() + request.getRequestURI() + logString);
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        String headers = headersToString(response.getHeaderNames(), response::getHeader);
        String body = new String(response.getContentAsByteArray());
        Map<String, String> logMap = new LinkedHashMap<>() {{
            put("\nStatus", String.valueOf(response.getStatus()));
            put("\nHeaders:", headers);
            put("\nBody:", body);
        }};
        String logString = joinMapIntoString(logMap);
        log.info("\nRESPONSE: {}", logString);
        response.copyBodyToResponse();
    }

    private static String joinMapIntoString(Map<String, String> logMap) {
        return logMap.entrySet().stream()
                .filter(e -> StringUtils.isNotBlank(e.getValue()))
                .map(e -> String.join(" ", e.getKey(), e.getValue()))
                .collect(Collectors.joining(""));
    }

    @SneakyThrows
    private String headersToString(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        return headerNames.stream()
                .map(header -> String.join("=", header, headerValueResolver.apply(header)))
                .collect(Collectors.joining("\n"));
    }

    private String parametersToString(Map<String, String[]> parameterMap) {
        return parameterMap.entrySet().stream()
                .map(param -> String.join("=", param.getKey(), Arrays.toString(param.getValue())))
                .collect(Collectors.joining("\n"));
    }
}
