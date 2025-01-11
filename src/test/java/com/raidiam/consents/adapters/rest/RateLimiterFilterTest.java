package com.raidiam.consents.adapters.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsentController.class)
public class RateLimiterFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsentController consentController;

    @MockitoBean
    private RateLimitingFilter rateLimitingFilter;

    @Test
    public void throttleRequests() throws Exception {

        // Arrange
        ReflectionTestUtils.setField(rateLimitingFilter, "tps", 0);

        // Act & Assert
        for (int i = 0; i < 30; i++) {
            mockMvc.perform(
                    get("/consents/consent-1")
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        }
    }
}
