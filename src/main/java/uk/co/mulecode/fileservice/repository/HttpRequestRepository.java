package uk.co.mulecode.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.co.mulecode.fileservice.repository.dto.HttpRequestEntity;

import java.util.UUID;

@Repository
interface HttpRequestRepository extends JpaRepository<HttpRequestEntity, UUID> {
}
