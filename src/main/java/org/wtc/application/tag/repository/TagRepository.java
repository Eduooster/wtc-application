package org.wtc.application.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.tag.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByIdAndDeletedFalse(Long id);
    List<Tag> findAllByDeletedFalse();


}
