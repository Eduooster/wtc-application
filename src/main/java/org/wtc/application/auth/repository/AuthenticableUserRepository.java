package org.wtc.application.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtc.application.auth.entity.AuthenticableUser;

import java.util.Optional;


@Repository
public interface AuthenticableUserRepository extends JpaRepository<AuthenticableUser, Long> {
    Optional<AuthenticableUser> findByEmail(String subject);
}
