package com.security.apphoaqua.repository;

import com.security.apphoaqua.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, String> {
    String TABLE = "bank";

    Optional<Bank> findByUserId(String userId);
}
