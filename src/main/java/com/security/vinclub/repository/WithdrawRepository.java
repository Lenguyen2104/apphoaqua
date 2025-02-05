package com.security.vinclub.repository;

import com.security.vinclub.dto.response.withdraw.PendingWithdrawResponse;
import com.security.vinclub.entity.Withdraw;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WithdrawRepository extends JpaRepository<Withdraw, String> {
    String TABLE = "withdraw";
    @Query("SELECT NEW com.security.vinclub.dto.response.withdraw.PendingWithdrawResponse(d.id, u.id, u.username, u.phone, d.status, d.accountNumber, d.accountName, d.bankName, d.amount, d.createdDate) " +
            "FROM " + TABLE + " d JOIN user u ON u.id = d.userId WHERE d.status = 0 " )
    Page<Withdraw> findByStatus(AppovalStatusEnum appovalStatusEnum, Pageable pageable);
    @Query("SELECT NEW com.security.vinclub.dto.response.withdraw.PendingWithdrawResponse(d.id, u.id, u.username, u.phone, d.status, d.accountNumber, d.accountName, d.bankName, d.amount, d.createdDate) " +
            "FROM " + TABLE + " d JOIN user u ON u.id = d.userId WHERE d.status = 0 " +
            "AND (d.accountNumber IS NULL OR LOWER(d.accountNumber) LIKE  %:searchText%) " +
            "OR (d.accountName IS NULL OR LOWER(d.accountName) LIKE  %:searchText%) " +
            "OR (u.username IS NULL OR LOWER(u.username) LIKE  %:searchText%) " +
            "OR (u.phone IS NULL OR LOWER(u.phone) LIKE  %:searchText%) " +
            "OR (d.bankName IS NULL OR LOWER(d.bankName) LIKE  %:searchText%)")
    Page<PendingWithdrawResponse> searchPendingWithdraws(String searchText, Pageable pageable);
}
