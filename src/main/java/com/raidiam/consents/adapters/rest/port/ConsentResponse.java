package com.raidiam.consents.adapters.rest.port;

import java.util.List;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsentResponse {

	private String consentId;
	private String userId;
	private List<ConsentPermission> permissions;
	private ConsentStatus status;
	private String createdAt;
	private String updatedAt;
	private Meta meta;

	@Data
	@Builder
    public static class Meta {
		private String requestDateTime;
	}
}
