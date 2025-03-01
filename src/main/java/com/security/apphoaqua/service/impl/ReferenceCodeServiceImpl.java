package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.common.SecurityContext;
import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.response.referencecode.GetReferenceCodeResponse;
import com.security.apphoaqua.entity.ReferenceCode;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.ReferenceCodeRepository;
import com.security.apphoaqua.repository.UserRepository;
import com.security.apphoaqua.service.ReferenceCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.security.apphoaqua.core.response.ResponseStatus.SUCCESS;
import static com.security.apphoaqua.core.response.ResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReferenceCodeServiceImpl implements ReferenceCodeService {
    private final ReferenceCodeRepository referenceCodeRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createReferenceCode() {
        String currentUserId = SecurityContext.getCurrentUserId();
        var user = userRepository.findById(currentUserId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        ReferenceCode refCode = new ReferenceCode();
        refCode.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        refCode.setReferenceCode(generateRandomCode(6));
        refCode.setCreatedDate(LocalDateTime.now());
        refCode.setCreatedBy(user.getId());
        referenceCodeRepository.save(refCode);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, refCode);
        return response;
    }

    @Override
    public ResponseBody<Object> getAllReferenceCodes(Pageable pageable) {
        Page<GetReferenceCodeResponse> refCodes = referenceCodeRepository.findAllRefCodes(pageable);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, refCodes);
        return response;
    }

    @Override
    public ResponseBody<Object> searchAllReferenceCodes(String searchText, Pageable pageable) {
        Page<GetReferenceCodeResponse> refCodes = referenceCodeRepository.searchAllRefCodes(searchText.toLowerCase(), pageable);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, refCodes);
        return response;
    }

    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        sb.append("vic");
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length()); // Lấy chỉ số ngẫu nhiên
            sb.append(characters.charAt(index)); // Thêm ký tự ngẫu nhiên vào chuỗi
        }

        return sb.toString();
    }
}
