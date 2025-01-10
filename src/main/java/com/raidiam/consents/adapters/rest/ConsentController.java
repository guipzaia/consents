package com.raidiam.consents.adapters.rest;

import com.raidiam.consents.adapters.rest.port.ConsentRequest;
import com.raidiam.consents.adapters.rest.port.ConsentResponse;
import com.raidiam.consents.adapters.rest.port.ConsentUpdateRequest;
import com.raidiam.consents.usecases.createconsent.port.CreateConsentRequest;
import com.raidiam.consents.usecases.createconsent.ICreateConsent;
import com.raidiam.consents.usecases.retrieveconsent.IRetrieveConsent;
import com.raidiam.consents.usecases.retrieveconsent.port.RetrieveConsentRequest;
import com.raidiam.consents.usecases.revokeconsent.IRevokeConsent;
import com.raidiam.consents.usecases.revokeconsent.port.RevokeConsentRequest;
import com.raidiam.consents.usecases.updateconsent.IUpdateConsent;
import com.raidiam.consents.usecases.updateconsent.port.UpdateConsentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ConsentResponse createConsent(@RequestBody @Valid ConsentRequest consentRequest) {

		var createConsentRequest =
				CreateConsentRequest.builder()
					.userId(consentRequest.getUserId())
					.permissions(consentRequest.getPermissions())
					.status(consentRequest.getStatus())
					.build();

		var createConsentResponse = createConsent.execute(createConsentRequest);

		return ConsentResponse.builder()
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
	}
	
	@GetMapping("/{consentId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentResponse retrieveConsent(@PathVariable("consentId") @Valid String consentId) {

		var retrieveConsentRequest =
				RetrieveConsentRequest.builder()
						.consentId(consentId)
						.build();

		var createConsentResponse = retrieveConsent.execute(retrieveConsentRequest);

		return ConsentResponse.builder()
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
	}
	
	@PutMapping("/{consentId}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentResponse updateConsent(@PathVariable("consentId") @Valid String consentId,
											   @RequestBody @Valid ConsentUpdateRequest consentUpdateRequest) {

		var updateConsentRequest =
				UpdateConsentRequest.builder()
						.consentId(consentId)
						.permissions(consentUpdateRequest.getPermissions())
						.status(consentUpdateRequest.getStatus())
						.build();

		var updateConsentResponse = updateConsent.execute(updateConsentRequest);

		return ConsentResponse.builder()
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
	}
	
	@DeleteMapping("/{consentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void revokeConsent(@PathVariable("consentId") @Valid String consentId) {

		var revokeConsentRequest =
				RevokeConsentRequest.builder()
						.consentId(consentId)
						.build();

		revokeConsent.execute(revokeConsentRequest);
	}
}
