package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    String TABLE = "user";

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByUsername(String username);
    boolean existsById(String id);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByPhone(String phoneNumber);

    @Query(value = "SELECT * FROM " + TABLE +
            " WHERE email LIKE %:email% AND CONCAT(firstName,lastName) LIKE %:username% ", nativeQuery = true)
    Page<User> findByEmailAndUsername(@Param("email") String email, @Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM " + TABLE + " u WHERE u.deleted = FALSE and u.id != :currentUserId")
    Page<User> findAllUsers(String currentUserId, Pageable pageable);

    @Query("SELECT u FROM " + TABLE + " u WHERE u.deleted = FALSE and u.roleId = :roleId and u.id != :currentUserId")
    Page<User> findAllAdmins(String currentUserId, String roleId, Pageable pageable);

    @Query("SELECT u FROM " + TABLE + " u WHERE " +
            "(u.phone IS NULL OR LOWER(u.phone) LIKE %:searchText%)" +
            "OR (u.username IS NULL OR LOWER(u.username) LIKE %:searchText%)" +
            "OR (u.fullName IS NULL OR LOWER(u.fullName) LIKE %:searchText%)" +
            "OR (u.email IS NULL OR LOWER(u.email) LIKE %:searchText%)" +
            "AND u.deleted = FALSE AND u.id != :currentUserId "
    )
    Page<User> searchAllUsers(String currentUserId, String searchText, Pageable pageable);

    List<User> findByIdIn(List<String> userIds);
}
