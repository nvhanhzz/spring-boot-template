package sti.project.template.config.log;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpLoggingFilter implements Filter {

    private static final Logger log = LoggerConfig.HTTP;

    private static final int MAX_BODY_LENGTH = 2000;

    private static final Set<String> SKIP_PATHS = Set.of(
            "/actuator/health",
            "/actuator/info",
            "/swagger-ui",
            "/v3/api-docs",
            "/api-docs",
            "/favicon.ico");

    private static final Pattern SENSITIVE_PATTERN = Pattern.compile(
            "\"(password|token|secret|authorization|apikey|api_key|accessToken|access_token|refreshToken|refresh_token|currentPassword|newPassword)\"\\s*:\\s*\"[^\"]*\"",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (shouldSkip(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequest(wrappedRequest);
            logResponse(wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private boolean shouldSkip(String uri) {
        return SKIP_PATHS.stream().anyMatch(uri::startsWith);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();
        String fullPath = queryString != null
                ? request.getRequestURI() + "?" + queryString
                : request.getRequestURI();

        String clientIp = getClientIp(request);

        String headers = Collections.list(request.getHeaderNames()).stream()
                .filter(name -> !name.equalsIgnoreCase("authorization"))
                .filter(name -> !name.equalsIgnoreCase("cookie"))
                .map(name -> name + "=" + request.getHeader(name))
                .collect(Collectors.joining(", "));

        String body = getBody(request.getContentAsByteArray());

        log.info(">>> {} {} {} | Headers: [{}] | Body: {}",
                clientIp,
                request.getMethod(),
                fullPath,
                headers,
                body);
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        String body = getBody(response.getContentAsByteArray());

        log.info("<<< {} | {}ms | Body: {}",
                response.getStatus(),
                duration,
                body);
    }

    private String getBody(byte[] content) {
        if (content == null || content.length == 0) {
            return "(empty)";
        }

        String body = new String(content, StandardCharsets.UTF_8);
        body = maskSensitiveData(body);

        if (body.length() > MAX_BODY_LENGTH) {
            return body.substring(0, MAX_BODY_LENGTH) + "...(truncated)";
        }

        return body;
    }

    private String maskSensitiveData(String data) {
        return SENSITIVE_PATTERN.matcher(data).replaceAll("\"$1\":\"****\"");
    }
}
