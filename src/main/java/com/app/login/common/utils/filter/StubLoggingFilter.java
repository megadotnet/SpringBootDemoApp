package com.app.login.common.utils.filter;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Spring Web filter for logging request and response.
 * @author megadotnet
 * @see org.springframework.web.filter.AbstractRequestLoggingFilter
 * @see ContentCachingRequestWrapper
 * @see ContentCachingResponseWrapper
 */
@Slf4j
@Component
public class StubLoggingFilter extends OncePerRequestFilter {
    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    /**
     * 执行过滤器的主要逻辑
     *
     * 此方法根据请求是否为异步分发来决定执行过滤的策略如果请求是异步分发的，则直接将请求和响应委托给过滤链的下一个过滤器；
     * 否则，将请求和响应包装后再传递给自定义的过滤逻辑这种方法允许在处理请求和响应之前或之后执行自定义逻辑
     *
     * @param request 用于处理客户端请求的HttpServletRequest对象
     * @param response 用于向客户端发送响应的HttpServletResponse对象
     * @param filterChain 代表过滤器链的对象，用于将请求和响应传递给链中的下一个过滤器
     * @throws ServletException 如果过滤过程中抛出Servlet异常
     * @throws IOException 如果过滤过程中发生I/O错误
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            // 如果请求是异步分发的，直接将请求和响应传递给过滤链的下一个过滤器
            filterChain.doFilter(request, response);
        } else {
            // 如果请求不是异步分发的，对请求和响应进行包装，然后调用自定义的过滤逻辑
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    /**
     * 执行过滤处理的包装方法
     * 该方法主要用于在请求处理前后添加额外的逻辑，例如缓存请求和响应内容
     *
     * @param request  包装后的请求对象，用于访问请求内容
     * @param response 包装后的响应对象，用于访问和修改响应内容
     * @param filterChain 过滤链对象，用于将请求传递给下一个过滤器或最终的目标资源
     *
     * @throws ServletException 如果过滤过程中发生Servlet相关的错误
     * @throws IOException 如果过滤过程中发生I/O错误
     */
    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 在请求处理前执行的逻辑，例如缓存请求内容
            beforeRequest(request, response);

            // 将请求传递给过滤链中的下一个元素，继续处理请求
            filterChain.doFilter(request, response);
        }
        finally {
            // 在请求处理后执行的逻辑，例如使用缓存的请求和响应内容进行额外处理
            afterRequest(request, response);

            // 将缓存的响应内容复制到实际的响应对象中，确保响应内容被正确返回
            response.copyBodyToResponse();
        }
    }

    /**
     * 在处理请求前执行的操作
     *
     * 该方法主要用于在发送请求之前，记录请求的头部信息和来源IP
     * 这有助于跟踪和调试API请求，确保在开发和生产环境中保持详细的日志记录
     *
     * @param request 包装后的请求对象，允许我们访问请求内容和头部信息
     * @param response 包装后的响应对象，虽然在此方法中未使用，但提供给未来可能需要访问响应信息的情况
     */
    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        // 检查日志级别是否允许记录信息级别的日志
        if (log.isInfoEnabled()) {
            // 记录请求头部信息和来源IP，格式为“IP地址|>”
            logRequestHeader(request, request.getRemoteAddr() + "|>");
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequestBody(request, request.getRemoteAddr() + "|>");
            logResponse(response, request.getRemoteAddr() + "|<");
        }
    }

    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
        val queryString = request.getQueryString();
        if (queryString == null) {
            log.info("{} {} {}", prefix, request.getMethod(), request.getRequestURI());
        } else {
            log.info("{} {} {}?{}", prefix, request.getMethod(), request.getRequestURI(), queryString);
        }
        Collections.list(request.getHeaderNames()).forEach(headerName ->
                Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
                        log.info("{} {}: {}", prefix, headerName, headerValue)));
        log.info("{}", prefix);
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        val content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, request.getContentType(), request.getCharacterEncoding(), prefix);
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
        val status = response.getStatus();
        log.info("{} {} {}", prefix, status, HttpStatus.valueOf(status).getReasonPhrase());
        response.getHeaderNames().forEach(headerName ->
                response.getHeaders(headerName).forEach(headerValue ->
                        log.info("{} {}: {}", prefix, headerName, headerValue)));
        log.info("{}", prefix);
        val content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getContentType(), response.getCharacterEncoding(), prefix);
        }
    }

    private static void logContent(byte[] content, String contentType, String contentEncoding, String prefix) {
        val mediaType = MediaType.valueOf(contentType);
        val visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                val contentString = new String(content, contentEncoding);
                Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> log.info("{} {}", prefix, line));
            } catch (UnsupportedEncodingException e) {
                log.info("{} [{} bytes content]", prefix, content.length);
            }
        } else {
            log.info("{} [{} bytes content]", prefix, content.length);
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}