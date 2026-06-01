package com.dev.infoLens.user.internal;


import com.dev.infoLens.user.internal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    long deleteByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Optional<Long> findIdByUsername(@Param("username") String username);

    @Query("SELECT u.username FROM User u WHERE u.id = :userID")
    Optional<String> findUserNameByID(@Param("userID") Long userID);

}

/*
    For projection
    public interface UserIdOnly {
        Long getId();
    }
    Optional<UserIdOnly> findIdByUsername(String username);
*/
