package com.raidiam.consents.domain.entities;

import com.raidiam.consents.domain.enums.ConsentPermission;
import com.raidiam.consents.domain.enums.ConsentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Consent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long consentId;
    private String userId;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ConsentPermission> permissions;

    private ConsentStatus status;
    private String createdAt;
    private String updatedAt;

    public String getFormattedConsentId() {
        return String.format("consent-%s", consentId);
    }
}
