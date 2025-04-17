package io.camunda.server.dentist.repository;

import io.camunda.server.dentist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndPassword(String name, String password);
}
