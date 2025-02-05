package com.security.vinclub.controller;

import com.security.vinclub.common.BussinessCommon;
import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.DepositService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.security.vinclub.constant.Constant.*;

@RestController
@RequestMapping("/api/v1/deposit")
@RequiredArgsConstructor
public class DepositController {
    private final DepositService depositService;
    private final Validator validator;

    enum SortBy {
        UPDATEDATE("updatedDate"),
        CREATEDATE("createdDate"),
        ;

        private String field;

        private SortBy(String field) {
            this.field = field;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createDepositTransaction(@RequestBody CreateDepositRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(depositService.createDepositTransaction(request));
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Object> approveDeposit(@PathVariable("id") String id) {
        return ResponseEntity.ok(depositService.approveDeposit(id));
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<Object> rejectDeposit(@PathVariable("id") String id) {
        return ResponseEntity.ok(depositService.rejectDeposit(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<Object> getPendingDeposits(@RequestParam(defaultValue = DEFAULT_SORT_BY) SortBy sortBy,
                                                     @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                                     @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                     @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page) {
        Sort sort = JpaSort.unsafe(direction, sortBy.field);
        Pageable pageable = BussinessCommon.castToPageable(page, sort, size);
        return ResponseEntity.ok(depositService.getPendingDeposits(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchPendingDeposits(@RequestParam(defaultValue = DEFAULT_SORT_BY) SortBy sortBy,
                                                     @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                                     @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                     @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "search_text") String searchText) {
        Sort sort = JpaSort.unsafe(direction, sortBy.field);
        Pageable pageable = BussinessCommon.castToPageable(page, sort, size);
        return ResponseEntity.ok(depositService.searchPendingDeposits(searchText, pageable));
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
