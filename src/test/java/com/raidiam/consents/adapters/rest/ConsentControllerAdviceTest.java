package com.raidiam.consents.adapters.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static com.raidiam.consents.domain.messages.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ConsentControllerAdviceTest {

    @InjectMocks
    private ConsentControllerAdvice consentControllerAdvice;

    /**
     * Functional Test was developed in use case class
     * This unit test validated the method and increase code coverage
     */
    @Test
    public void handleConsentNotFoundException() {

        // Arrange & Act
        var response = consentControllerAdvice.handleConsentNotFound(
                new RuntimeException(CONSENT_NOT_FOUND), new WebRequest() {
                    @Override
                    public String getHeader(String headerName) {
                        return "";
                    }

                    @Override
                    public String[] getHeaderValues(String headerName) {
                        return new String[0];
                    }

                    @Override
                    public Iterator<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public String getParameter(String paramName) {
                        return "";
                    }

                    @Override
                    public String[] getParameterValues(String paramName) {
                        return new String[0];
                    }

                    @Override
                    public Iterator<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return Map.of();
                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return "";
                    }

                    @Override
                    public String getRemoteUser() {
                        return "";
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(long lastModifiedTimestamp) {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(String etag) {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
                        return false;
                    }

                    @Override
                    public String getDescription(boolean includeClientInfo) {
                        return "";
                    }

                    @Override
                    public Object getAttribute(String name, int scope) {
                        return null;
                    }

                    @Override
                    public void setAttribute(String name, Object value, int scope) {

                    }

                    @Override
                    public void removeAttribute(String name, int scope) {

                    }

                    @Override
                    public String[] getAttributeNames(int scope) {
                        return new String[0];
                    }

                    @Override
                    public void registerDestructionCallback(String name, Runnable callback, int scope) {

                    }

                    @Override
                    public Object resolveReference(String key) {
                        return null;
                    }

                    @Override
                    public String getSessionId() {
                        return "";
                    }

                    @Override
                    public Object getSessionMutex() {
                        return null;
                    }
                });

        // Assert
        assertEquals(CONSENT_NOT_FOUND, response.getMessage());
    }

    /**
     * Functional Test was developed in use case class
     * This unit test validated the method and increase code coverage
     */
    @Test
    public void handlePermissionsAndStatusNotValid() {

        // Arrange & Act
        var response = consentControllerAdvice.handlePermissionsAndStatusNotValid(
                new RuntimeException(INVALID_CONSENT_INITIAL_STATUS), new WebRequest() {
                    @Override
                    public String getHeader(String headerName) {
                        return "";
                    }

                    @Override
                    public String[] getHeaderValues(String headerName) {
                        return new String[0];
                    }

                    @Override
                    public Iterator<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public String getParameter(String paramName) {
                        return "";
                    }

                    @Override
                    public String[] getParameterValues(String paramName) {
                        return new String[0];
                    }

                    @Override
                    public Iterator<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return Map.of();
                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return "";
                    }

                    @Override
                    public String getRemoteUser() {
                        return "";
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(long lastModifiedTimestamp) {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(String etag) {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
                        return false;
                    }

                    @Override
                    public String getDescription(boolean includeClientInfo) {
                        return "";
                    }

                    @Override
                    public Object getAttribute(String name, int scope) {
                        return null;
                    }

                    @Override
                    public void setAttribute(String name, Object value, int scope) {

                    }

                    @Override
                    public void removeAttribute(String name, int scope) {

                    }

                    @Override
                    public String[] getAttributeNames(int scope) {
                        return new String[0];
                    }

                    @Override
                    public void registerDestructionCallback(String name, Runnable callback, int scope) {

                    }

                    @Override
                    public Object resolveReference(String key) {
                        return null;
                    }

                    @Override
                    public String getSessionId() {
                        return "";
                    }

                    @Override
                    public Object getSessionMutex() {
                        return null;
                    }
                });

        // Assert
        assertEquals(INVALID_INPUT, response.getMessage());
        assertTrue(response.getErrors().contains(INVALID_CONSENT_INITIAL_STATUS));
    }

    /**
     * Functional Test was developed in use case class
     * This unit test validated the method and increase code coverage
     */
    @Test
    public void handleInternalServerError() {

        // Arrange & Act
        var response = consentControllerAdvice.handleDefaultError(
                new Exception(INTERNAL_SERVER_ERROR), new WebRequest() {
                    @Override
                    public String getHeader(String headerName) {
                        return "";
                    }

                    @Override
                    public String[] getHeaderValues(String headerName) {
                        return new String[0];
                    }

                    @Override
                    public Iterator<String> getHeaderNames() {
                        return null;
                    }

                    @Override
                    public String getParameter(String paramName) {
                        return "";
                    }

                    @Override
                    public String[] getParameterValues(String paramName) {
                        return new String[0];
                    }

                    @Override
                    public Iterator<String> getParameterNames() {
                        return null;
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return Map.of();
                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public String getContextPath() {
                        return "";
                    }

                    @Override
                    public String getRemoteUser() {
                        return "";
                    }

                    @Override
                    public Principal getUserPrincipal() {
                        return null;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return false;
                    }

                    @Override
                    public boolean isSecure() {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(long lastModifiedTimestamp) {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(String etag) {
                        return false;
                    }

                    @Override
                    public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
                        return false;
                    }

                    @Override
                    public String getDescription(boolean includeClientInfo) {
                        return "";
                    }

                    @Override
                    public Object getAttribute(String name, int scope) {
                        return null;
                    }

                    @Override
                    public void setAttribute(String name, Object value, int scope) {

                    }

                    @Override
                    public void removeAttribute(String name, int scope) {

                    }

                    @Override
                    public String[] getAttributeNames(int scope) {
                        return new String[0];
                    }

                    @Override
                    public void registerDestructionCallback(String name, Runnable callback, int scope) {

                    }

                    @Override
                    public Object resolveReference(String key) {
                        return null;
                    }

                    @Override
                    public String getSessionId() {
                        return "";
                    }

                    @Override
                    public Object getSessionMutex() {
                        return null;
                    }
                });

        // Assert
        assertEquals(INTERNAL_SERVER_ERROR, response.getMessage());
    }
}
