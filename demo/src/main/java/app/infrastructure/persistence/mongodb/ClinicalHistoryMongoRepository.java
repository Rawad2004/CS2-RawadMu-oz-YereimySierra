// File: src/main/java/app/infrastructure/persistence/mongodb/ClinicalHistoryMongoRepository.java
package app.infrastructure.persistence.mongodb;

import app.domain.model.ClinicalHistoryEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ClinicalHistoryMongoRepository extends MongoRepository<ClinicalHistoryEntry, String> {

    Optional<ClinicalHistoryEntry> findByPatientNationalId(String patientNationalId);

    @Query("{ 'patient_national_id': ?0, 'visit_data.?1': { $exists: true } }")
    boolean existsByPatientNationalIdAndVisitDate(String patientNationalId, LocalDate visitDate);
}