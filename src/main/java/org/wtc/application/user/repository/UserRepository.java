package org.wtc.application.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.user.entity.User;


import javax.swing.text.html.Option;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndDeletedFalse(Long id);

    List<User> findAllByDeletedFalse();


    Optional<User> findByCredentials(AuthenticableUser authenticableUser);

}
