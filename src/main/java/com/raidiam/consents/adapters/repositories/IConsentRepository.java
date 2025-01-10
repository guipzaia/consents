package com.raidiam.consents.adapters.repositories;

import com.raidiam.consents.domain.entities.Consent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConsentRepository extends JpaRepository<Consent, Long> {
}
