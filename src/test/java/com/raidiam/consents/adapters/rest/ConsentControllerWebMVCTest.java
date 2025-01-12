package com.raidiam.consents.adapters.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.raidiam.consents.adapters.rest.port.ConsentErrorResponse;
import com.raidiam.consents.adapters.rest.port.ConsentRequest;
import com.raidiam.consents.adapters.rest.port.ConsentUpdateRequest;
import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static com.raidiam.consents.domain.messages.ErrorMessage.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsentController.class)
public class ConsentControllerWebMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsentController consentController;

    @MockitoBean
    private RateLimitingFilter rateLimitingFilter;

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(consentController).setControllerAdvice(ConsentControllerAdvice.class).build();
    }

    /*
        Tests for create consent
     */

    @Test
    public void tryCreateConsentWithNoRequestBody() throws Exception {

        // Arrange
        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(REQUEST_BODY_MISSING))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        post("/consents")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void tryCreateConsentWithNoRequiredFields() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentRequest.builder()
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(
                                        "Field userId is required",
                                        "Field permissions is required",
                                        "Field status is required"
                                ))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        post("/consents")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryCreateConsentWithInvalidUserId() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                    ConsentRequest.builder()
                        .userId("user1-X")
                        .permissions(List.of(ConsentPermission.READ_DATA))
                        .status(ConsentStatus.AWAITING_AUTHORISATION)
                        .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field userId must have the pattern 'user-N' (N = number)"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
            post("/consents")
                .content(bodyRequest)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryCreateConsentWithEmptyPermissions() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentRequest.builder()
                                .userId("user-12345")
                                .permissions(List.of())
                                .status(ConsentStatus.AWAITING_AUTHORISATION)
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field permissions must have at least one permission in the list"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        post("/consents")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryCreateConsentWithMuchPermissions() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentRequest.builder()
                                .userId("user-12345")
                                .permissions(List.of(
                                        ConsentPermission.READ_DATA,
                                        ConsentPermission.WRITE_DATA,
                                        ConsentPermission.DELETE_DATA,
                                        ConsentPermission.WRITE_DATA
                                ))
                                .status(ConsentStatus.AWAITING_AUTHORISATION)
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field permissions must have at most three permissions in the list"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        post("/consents")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryCreateConsentWithInvalidPermissions() throws Exception {

        // Arrange
        String bodyRequest = "{\"userId\": \"user-12345\", \"permissions\": [\"INVALID_PERMISSION\"], \"status\": \"AWAITING_AUTHORISATION\"}";

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field permissions must be one of the values in the list [READ_DATA, WRITE_DATA, DELETE_DATA]"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        post("/consents")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryCreateConsentWithInvalidStatus() throws Exception {

        // Arrange
        String bodyRequest = "{\"userId\": \"user-12345\", \"permissions\": [\"READ_DATA\"], \"status\": \"INVALID_STATUS\"}";

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field status must be one of the values in the list [AUTHORISED, AWAITING_AUTHORISATION, REJECT]"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        post("/consents")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    /*
        Tests for retrieve consent
     */

    @Test
    public void tryRetrieveConsentWithInvalidPathParameter() throws Exception {

        // Arrange
        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        get("/consents/consent1-X")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    /*
        Tests for update consent
     */

    @Test
    public void tryUpdateConsentWithInvalidPathParameter() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentUpdateRequest.builder()
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent1-X")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyRequest)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryUpdateConsentWithNoRequestBody() throws Exception {

        // Arrange
        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(REQUEST_BODY_MISSING))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent-12345")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }
    @Test
    public void tryUpdateConsentWithNoRequiredFields() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentUpdateRequest.builder()
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(
                                        "Field permissions is required",
                                        "Field status is required"
                                ))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent-12345")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryUpdateConsentWithEmptyPermissions() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentUpdateRequest.builder()
                                .permissions(List.of())
                                .status(ConsentStatus.AWAITING_AUTHORISATION)
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field permissions must have at least one permission in the list"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent-12345")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryUpdateConsentWithMuchPermissions() throws Exception {

        // Arrange
        String bodyRequest =
                objectWriter.writeValueAsString(
                        ConsentUpdateRequest.builder()
                                .permissions(List.of(
                                        ConsentPermission.READ_DATA,
                                        ConsentPermission.WRITE_DATA,
                                        ConsentPermission.DELETE_DATA,
                                        ConsentPermission.WRITE_DATA
                                ))
                                .status(ConsentStatus.AWAITING_AUTHORISATION)
                                .build()
                );

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field permissions must have at most three permissions in the list"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent-12345")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryUpdateConsentWithInvalidPermissions() throws Exception {

        // Arrange
        String bodyRequest = "{\"permissions\": [\"INVALID_PERMISSION\"], \"status\": \"AWAITING_AUTHORISATION\"}";

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field permissions must be one of the values in the list [READ_DATA, WRITE_DATA, DELETE_DATA]"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent-12345")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryUpdateConsentWithInvalidStatus() throws Exception {

        // Arrange
        String bodyRequest = "{\"permissions\": [\"READ_DATA\"], \"status\": \"INVALID_STATUS\"}";

        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of("Field status must be one of the values in the list [AUTHORISED, AWAITING_AUTHORISATION, REJECT]"))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        put("/consents/consent-12345")
                                .content(bodyRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    /*
        Tests for delete consent
     */

    @Test
    public void tryDeleteConsentWithInvalidPathParameter() throws Exception {

        // Arrange
        String expectedResponse =
                objectWriter.writeValueAsString(
                        ConsentErrorResponse.builder()
                                .message(INVALID_INPUT)
                                .errors(List.of(INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN))
                                .build()
                );

        // Act & Assert
        mockMvc.perform(
                        delete("/consents/consent1-X")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));

    }

    @Test
    public void tryDeleteConsentGetsInternalServerError() throws Exception {

        // Act & Assert
        mockMvc.perform(
                        delete("/consents")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isInternalServerError());
    }
}
