package com.raidiam.consents.adapters.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.raidiam.consents.adapters.rest.port.ConsentErrorResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.raidiam.consents.utils.ErrorMessage.TOO_MANY_REQUESTS;

@Component
public class RateLimitingFilter implements Filter {

    private final HttpServletRequest httpServletRequest;
    @Value("${custom.transctions-per-second}")
    private int tps;

    private final LoadingCache<String, Integer> cacheLoader;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    /**
     * Constructor: Creates a new CacheLoader expiring after 1 second
     */
    public RateLimitingFilter(HttpServletRequest httpServletRequest) {
        super();
        this.cacheLoader =
                Caffeine.newBuilder()
                    .expireAfterWrite(1, TimeUnit.SECONDS)
                    .build(
                        new CacheLoader<String, Integer>() {
                            @Override
                            public @Nullable Integer load(String s) throws Exception {
                                return 0;
                            }
                        }
                    );
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                     to for further processing
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Get IP Address
        var ipAddress = request.getRemoteAddr();

        // Increment number of requests per IP Address
        var requestsPerIpAddress = Optional.of(cacheLoader.get(ipAddress)).orElse(0) + 1;
        cacheLoader.put(ipAddress, requestsPerIpAddress);

        // Check rate limiting filter
        if (requestsPerIpAddress > tps) {
            String bodyResponse =
                    objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                            .message(TOO_MANY_REQUESTS)
                            .build()
                    );

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            httpResponse.setHeader("Content-Type", "application/json");
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write(bodyResponse);

            logger.error("Too many requests: {}", httpRequest);

            return;
        }

        chain.doFilter(request, response);
    }
}
