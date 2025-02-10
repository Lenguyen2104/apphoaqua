package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import org.springframework.data.domain.Pageable;

public interface ReferenceCodeService {
    ResponseBody<Object> createReferenceCode();

    ResponseBody<Object> getAllReferenceCodes(Pageable pageable);

    ResponseBody<Object> searchAllReferenceCodes(String searchText, Pageable pageable);
}
