package com.raidiam.consents.adapters.rest;

import com.raidiam.consents.adapters.rest.port.ConsentRequest;
import com.raidiam.consents.adapters.rest.port.ConsentResponse;
import com.raidiam.consents.adapters.rest.port.ConsentUpdateRequest;
import com.raidiam.consents.domain.exceptions.ConsentPermissionsWithDuplicateValueException;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.ICreateConsent;
import com.raidiam.consents.usecases.retrieveconsent.IRetrieveConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.revokeconsent.IRevokeConsent;
import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;
import com.raidiam.consents.usecases.updateconsent.IUpdateConsent;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import com.raidiam.consents.utils.EnumDuplicateChecker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.raidiam.consents.utils.ErrorMessage.*;

@RestController
@RequestMapping("/consents")
public class ConsentController {

	@Autowired
	private ICreateConsent createConsent;

	@Autowired
	private IRetrieveConsent retrieveConsent;

	@Autowired
	private IUpdateConsent updateConsent;

	@Autowired
	private IRevokeConsent revokeConsent;

	private final Logger logger = LoggerFactory.getLogger(ConsentController.class);

	/**
	 * Create consent with initial status of AWAITING_AUTHORISATION
	 *
	 * @param consentRequest 	Request payload
	 * @return ConsentResponse	Created consent details
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ConsentResponse createConsent(@RequestBody @Valid ConsentRequest consentRequest) {

		logger.info("Create consent request: {}", consentRequest);

		// Checks for duplicate permissions values
		if (EnumDuplicateChecker.hasDuplicateItems(consentRequest.getPermissions())) {
			throw new ConsentPermissionsWithDuplicateValueException(DUPLICATE_PERMISSIONS_DETECTED);
		}

		var createConsentRequest =
				CreateConsentRequest.builder()
					.userId(consentRequest.getUserId())
					.permissions(consentRequest.getPermissions())
					.status(consentRequest.getStatus())
					.build();

		var createConsentResponse = createConsent.execute(createConsentRequest);

		var consentResponse =
				ConsentResponse.builder()
					.consentId(createConsentResponse.getConsentId())
					.userId(createConsentResponse.getUserId())
					.permissions(createConsentResponse.getPermissions())
					.status(createConsentResponse.getStatus())
					.createdAt(createConsentResponse.getCreatedAt())
					.updatedAt(createConsentResponse.getUpdatedAt())
					.meta(ConsentResponse.Meta.builder()
							.requestDateTime(createConsentResponse.getRequestDateTime())
							.build())
					.build();

		logger.info("Consent created successfully: {}", consentResponse);

		return consentResponse;
	}

	/**
	 *  Retrieve details of a specific consent
	 *
	 * @param consentId			Consent identification
	 * @return ConsentResponse	Consent details
	 */
	@GetMapping("/{consentId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentResponse retrieveConsent(
			@PathVariable("consentId")
			@Pattern(regexp = "consent-\\d+", message = INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN)
			@NotNull(message = REQUIRED_CONSENT_ID_PATH_PARAMETER)
			@Valid String consentId) {

		logger.info("Retrieve consent request: consentId={}", consentId);

		var retrieveConsentRequest =
				RetrieveConsentRequest.builder()
						.consentId(consentId)
						.build();

		var createConsentResponse = retrieveConsent.execute(retrieveConsentRequest);

		var consentReponse =
				ConsentResponse.builder()
					.consentId(createConsentResponse.getConsentId())
					.userId(createConsentResponse.getUserId())
					.permissions(createConsentResponse.getPermissions())
					.status(createConsentResponse.getStatus())
					.createdAt(createConsentResponse.getCreatedAt())
					.updatedAt(createConsentResponse.getUpdatedAt())
					.meta(ConsentResponse.Meta.builder()
							.requestDateTime(createConsentResponse.getRequestDateTime())
							.build())
					.build();

		logger.info("Consent retrieved successfully: {}", consentReponse);

		return consentReponse;
	}

	/**
	 * Update consent details
	 *
	 * @param consentId				Consent identification
	 * @param consentUpdateRequest	Requested update details
	 * @return ConsentResponse		Updated consent details
	 */
	@PutMapping("/{consentId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentResponse updateConsent(
			@PathVariable("consentId")
			@Pattern(regexp = "consent-\\d+", message = INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN)
			@NotNull(message = REQUIRED_CONSENT_ID_PATH_PARAMETER)
			@Valid String consentId,
		    @RequestBody @Valid ConsentUpdateRequest consentUpdateRequest) {

		logger.info("Update consent request: consentId={} - {}", consentId, consentUpdateRequest);

		// Checks for duplicate permissions values
		if (EnumDuplicateChecker.hasDuplicateItems(consentUpdateRequest.getPermissions())) {
			throw new ConsentPermissionsWithDuplicateValueException(DUPLICATE_PERMISSIONS_DETECTED);
		}

		var updateConsentRequest =
				UpdateConsentRequest.builder()
						.consentId(consentId)
						.permissions(consentUpdateRequest.getPermissions())
						.status(consentUpdateRequest.getStatus())
						.build();

		var updateConsentResponse = updateConsent.execute(updateConsentRequest);

		var consentResponse =
				ConsentResponse.builder()
					.consentId(updateConsentResponse.getConsentId())
					.userId(updateConsentResponse.getUserId())
					.permissions(updateConsentResponse.getPermissions())
					.status(updateConsentResponse.getStatus())
					.createdAt(updateConsentResponse.getCreatedAt())
					.updatedAt(updateConsentResponse.getUpdatedAt())
					.meta(ConsentResponse.Meta.builder()
							.requestDateTime(updateConsentResponse.getRequestDateTime())
							.build())
					.build();

		logger.info("Consent updated successfully: {}", consentResponse);

		return consentResponse;
	}

	/**
	 * Revoke consent
	 *
	 * @param consentId		Consent identification
	 */
	@DeleteMapping("/{consentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void revokeConsent(
			@PathVariable("consentId")
			@Pattern(regexp = "consent-\\d+", message = INVALID_CONSENT_ID_PATH_PARAMETER_PATTERN)
			@NotNull(message = REQUIRED_CONSENT_ID_PATH_PARAMETER)
			@Valid String consentId) {

		logger.info("Revoke consent request: consentId={}", consentId);

		var revokeConsentRequest =
				RevokeConsentRequest.builder()
						.consentId(consentId)
						.build();

		revokeConsent.execute(revokeConsentRequest);

		logger.info("Consent revoked successfully: consentId={}", consentId);
	}
}
