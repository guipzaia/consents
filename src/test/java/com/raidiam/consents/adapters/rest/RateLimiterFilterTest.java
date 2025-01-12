package com.raidiam.consents.adapters.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RateLimiterFilterTest {

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @Test
    public void processRequest() throws Exception {

        // Arrange
        ReflectionTestUtils.setField(rateLimitingFilter, "tps", 1);
        var httpServletRequest = new MockHttpServletRequest();
        var httpServletResponse = new MockHttpServletResponse();
        var filterChain = new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

            }
        };

        // Act
        rateLimitingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        assertFalse(httpServletResponse.getContentAsString().contains("Too many requests"));
    }

    @Test
    public void throttleRequest() throws Exception {

        // Arrange
        ReflectionTestUtils.setField(rateLimitingFilter, "tps", 0);
        var httpServletRequest = new MockHttpServletRequest();
        var httpServletResponse = new MockHttpServletResponse();
        var filterChain = new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

            }
        };

        // Act
        rateLimitingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        assertTrue(httpServletResponse.getContentAsString().contains("Too many requests"));
    }
}
