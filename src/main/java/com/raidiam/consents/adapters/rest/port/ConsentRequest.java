package com.raidiam.consents.adapters.rest.port;

import java.util.List;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class ConsentRequest {

	@Pattern(regexp = "user-\\d+", message = "Field userId must have the pattern 'user-N' (N = number)")
	private String userId;

	@Size(min = 1, message = "Field permissions must have at least one permission in the list")
	@Size(max = 3, message = "Field permissions must have at most three permissions in the list")
	private List<ConsentPermission> permissions;

	private ConsentStatus status;
}
