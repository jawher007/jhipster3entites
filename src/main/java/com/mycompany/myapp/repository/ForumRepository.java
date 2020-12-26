package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Forum;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Forum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("select forum from Forum forum where forum.user.login = ?#{principal.username}")
    List<Forum> findByUserIsCurrentUser();
}
