package io.camunda.server.dentist.repository;

import io.camunda.server.dentist.dto.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    Optional<Treatment> findByUserIdAndStatus(Long userId, Treatment.AppointmentStatus status);
}
