package com.datn.warehousemgmt.repository;

import com.datn.warehousemgmt.dto.request.UserSearchRequest;
import com.datn.warehousemgmt.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByUsernameAndStatus(String username, Boolean status);

    @Query(value = "SELECT DISTINCT u FROM Users u " +
            "WHERE (?1 IS NULL OR ?1 = '' " +
            "OR u.email like ?1 " +
            "OR u.username like ?1 " +
            "OR u.name like ?1 " +
            "OR u.phoneNumber like ?1) " +
            "AND (?2 IS NULL OR u.warehouseId = ?2)")
    Page<Users> getAllUser(String search, Long warehouseId, Pageable pageable);
}
