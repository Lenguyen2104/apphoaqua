package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ReferenceCodeService {
    ResponseBody<Object> createReferenceCode();

    ResponseBody<Object> getAllReferenceCodes(Pageable pageable);

    ResponseBody<Object> searchAllReferenceCodes(String searchText, Pageable pageable);
}
