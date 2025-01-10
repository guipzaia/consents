package com.raidiam.consents.adapters.rest.port;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConsentUpdateRequest {

    @Size(min = 1, message = "Field permissions must have at least one permission in the list")
    @Size(max = 3, message = "Field permissions must have at most three permissions in the list")
    private List<ConsentPermission> permissions;

    private ConsentStatus status;
}
