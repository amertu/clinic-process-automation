package io.camunda.server.clinic.repository;

import io.camunda.server.clinic.dto.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    Optional<Treatment> findByUserIdAndStatus(Long userId, Treatment.AppointmentStatus status);
}
