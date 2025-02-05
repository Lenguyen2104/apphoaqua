package com.security.vinclub.controller;

import com.security.vinclub.common.BussinessCommon;
import com.security.vinclub.dto.request.withdraw.CreateWithdrawRequest;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.service.WithdrawService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.security.vinclub.constant.Constant.*;

@RestController
@RequestMapping("/api/v1/withdraw")
@RequiredArgsConstructor
public class WithdrawController {
    private final WithdrawService withdrawService;
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
    public ResponseEntity<Object> createWithdrawTransaction(@RequestBody CreateWithdrawRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(withdrawService.createWithdrawTransaction(request));
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Object> approveWithdraw(@PathVariable("id") String id) {
        return ResponseEntity.ok(withdrawService.approveWithdraw(id));
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<Object> rejectWithdraw(@PathVariable("id") String id) {
        return ResponseEntity.ok(withdrawService.rejectWithdraw(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<Object> getPendingWithdraws(@RequestParam(defaultValue = DEFAULT_SORT_BY) SortBy sortBy,
                                                      @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                                      @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                      @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page) {
        Sort sort = JpaSort.unsafe(direction, sortBy.field);
        Pageable pageable = BussinessCommon.castToPageable(page, sort, size);
        return ResponseEntity.ok(withdrawService.getPendingWithdraws(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchPendingWithdraws(@RequestParam(defaultValue = DEFAULT_SORT_BY) SortBy sortBy,
                                                        @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                                        @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "search_text") String searchText) {
        Sort sort = JpaSort.unsafe(direction, sortBy.field);
        Pageable pageable = BussinessCommon.castToPageable(page, sort, size);
        return ResponseEntity.ok(withdrawService.searchPendingWithdraws(searchText, pageable));
    }


    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
