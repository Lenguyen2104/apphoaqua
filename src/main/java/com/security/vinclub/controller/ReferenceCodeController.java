package com.security.vinclub.controller;

import com.security.vinclub.common.BussinessCommon;
import com.security.vinclub.dto.request.users.UpdateUserRequest;
import com.security.vinclub.service.ReferenceCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.security.vinclub.constant.Constant.*;
import static com.security.vinclub.constant.Constant.DEFAULT_PAGE_NUMBER;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class ReferenceCodeController {
    private final ReferenceCodeService referenceCodeService;
    enum SortBy {
        UPDATEDATE("updatedDate"),
        CREATEDATE("createdDate"),
        ;

        private String field;

        private SortBy(String field) {
            this.field = field;
        }
    }
    @PostMapping("/code/create")
    public ResponseEntity<Object> createReferenceCode() {
        return ResponseEntity.ok(referenceCodeService.createReferenceCode());
    }

    @GetMapping("/code/all")
    public ResponseEntity<Object> getAllReferenceCodes(@RequestParam(defaultValue = DEFAULT_SORT_BY) SortBy sortBy,
                                                     @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                                     @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                     @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page) {
        Sort sort = JpaSort.unsafe(direction, sortBy.field);
        Pageable pageable = BussinessCommon.castToPageable(page, sort, size);
        return ResponseEntity.ok(referenceCodeService.getAllReferenceCodes(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAllReferenceCodes(@RequestParam(defaultValue = DEFAULT_SORT_BY) SortBy sortBy,
                                                        @RequestParam(defaultValue = DEFAULT_DIRECTION) Sort.Direction direction,
                                                        @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(value = "search_text") String searchText) {
        Sort sort = JpaSort.unsafe(direction, sortBy.field);
        Pageable pageable = BussinessCommon.castToPageable(page, sort, size);
        return ResponseEntity.ok(referenceCodeService.searchAllReferenceCodes(searchText, pageable));
    }
}
