package uk.co.mulecode.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.mulecode.fileservice.repository.dto.HttpRequestEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HttpRequestRepository extends JpaRepository<HttpRequestEntity, UUID> {

    Optional<HttpRequestEntity> findByRequestId(UUID uuid);

    Optional<HttpRequestEntity> findByRequestIpAddress(String uuid);
}
