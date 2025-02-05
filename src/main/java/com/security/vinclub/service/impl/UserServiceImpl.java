package com.security.vinclub.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.vinclub.common.SecurityContext;
import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.users.UpdateUserRequest;
import com.security.vinclub.dto.request.users.UserSearchRequest;
import com.security.vinclub.dto.response.users.UserDetailResponse;
import com.security.vinclub.entity.Role;
import com.security.vinclub.entity.User;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.RoleRepository;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final String DEFAULT_SORT_FIELD = "createdDate";
    private final RoleRepository roleRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public ResponseBody<Object> getUserIdDetail(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        Role role = roleRepository.findById(user.getRoleId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(ROLE_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, ROLE_NOT_FOUND, errorMapping);
        });

        UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roleId(user.getRoleId())
                .roleName(role.getName())
                .imageUrl(user.getImageId())
                .referenceCode(user.getReferenceCode())
                .totalAmount(user.getTotalAmount())
                .lastDepositAmount(user.getLastDepositAmount())
                .lastDepositDate(user.getLastDepositDate())
                .lastWithDrawAmount(user.getLastWithDrawAmount())
                .lastWithdrawDate(user.getLastWithdrawDate())
                .createDate(user.getCreatedDate())
                .activated(user.isActivated())
                .build();
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, userDetailResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> updateUser(UpdateUserRequest request) {
        var userModel = userRepository.findById(request.getUserId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        this.validateEmailAndPhoneNumber(request.getEmail(), request.getPhoneNumber(), userModel.getEmail(), userModel.getPhone());

        userModel.setFullName(request.getFullName());
        userModel.setEmail(request.getEmail());
        userModel.setPhone(request.getPhoneNumber());
        userModel.setRoleId(request.getRoleId());
        userRepository.save(userModel);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, null);
        return response;
    }

    @Override
    public ResponseBody<Object> deleteUserById(String userId) {
        var userModel = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        userModel.setDeleted(true);
        userRepository.save(userModel);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, null);
        return response;
    }

    @Override
    public ResponseBody<Object> activateUserById(String userId) {
        var userModel = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        if(userModel.isActivated()) {
            userModel.setActivated(false);
        } else {
            userModel.setActivated(true);
        }
        userRepository.save(userModel);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, null);
        return response;
    }

    private void validateEmailAndPhoneNumber(String email, String phoneNumber, String emailPresent, String phonePresent) {
        var existsEmail = userRepository.existsByEmail(email);
        if (!Objects.equals(email, emailPresent) && existsEmail) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(EMAIL_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, EMAIL_EXIST, errorMapping);
        }
        if (!StringUtils.isBlank(phoneNumber) && !Objects.equals(phoneNumber, phonePresent)) {
            var existsPhoneNumber = userRepository.existsByPhone(phoneNumber);
            if (existsPhoneNumber) {
                var errorMapping = ErrorData.builder()
                        .errorKey1(PHONE_NUMBER_EXIST.getCode())
                        .build();
                throw new ServiceSecurityException(HttpStatus.OK, PHONE_NUMBER_EXIST, errorMapping);
            }
        }
    }

    @Override
    public ResponseBody<Object> getAllUsers(UserSearchRequest request) {
        var mapper = new ObjectMapper();
        var json = mapper.createObjectNode();

        String currentUserId = SecurityContext.getCurrentUserId();

        Pageable pageable;

        if (request.getSortBy() == null || request.getSortBy().isEmpty()) {
            request.setSortBy(DEFAULT_SORT_FIELD);
        }

        if (request.getSortDirection() == null || request.getSortDirection().isEmpty()) {
            request.setSortDirection("asc");
        }

        if (request.getSortDirection().equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).descending());
        } else {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).ascending());
        }

        Page<User> listUserPage = userRepository.findAllUsers(currentUserId, pageable);

        var users = listUserPage.getContent();
        List<String> roleIds = users.stream().map(User::getRoleId).toList();

        List<Role> roles = roleRepository.findByIdIn(roleIds);
        Map<String, String> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getId, Role::getName));

        var userResponse = users.stream().map(user ->
                UserDetailResponse.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .roleId(user.getRoleId())
                        .roleName(roleMap.get(user.getRoleId()))
                        .imageUrl(user.getImageId())
                        .referenceCode(user.getReferenceCode())
                        .totalAmount(user.getTotalAmount())
                        .lastDepositAmount(user.getLastDepositAmount())
                        .lastDepositDate(user.getLastDepositDate())
                        .lastWithDrawAmount(user.getLastWithDrawAmount())
                        .lastWithdrawDate(user.getLastWithdrawDate())
                        .createDate(user.getCreatedDate())
                        .activated(user.isActivated())
                        .build());

        json.putPOJO("page_number", request.getPageNumber());
        json.putPOJO("total_records", listUserPage.getTotalElements());
        json.putPOJO("page_size", request.getPageSize());
        json.putPOJO("list_user", userResponse);
        json.putPOJO("total_page", listUserPage.getTotalPages());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);

        return response;
    }

    @Override
    public ResponseBody<Object> searchAllUsers(UserSearchRequest request) {
        var mapper = new ObjectMapper();
        var json = mapper.createObjectNode();

        String currentUserId = SecurityContext.getCurrentUserId();

        Pageable pageable;

        if (request.getSortBy() == null || request.getSortBy().isEmpty()) {
            request.setSortBy(DEFAULT_SORT_FIELD);
        }

        if (request.getSortDirection() == null || request.getSortDirection().isEmpty()) {
            request.setSortDirection("asc");
        }

        if (request.getSortDirection().equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).descending());
        } else {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).ascending());
        }

        String searchText = request.getSearchText() != null ? request.getSearchText() : "";
        Page<User> listUserPage = userRepository.searchAllUsers(currentUserId, searchText.toLowerCase(), pageable);

        var users = listUserPage.getContent();
        List<String> roleIds = users.stream().map(User::getRoleId).toList();

        List<Role> roles = roleRepository.findByIdIn(roleIds);
        Map<String, String> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getId, Role::getName));

        var userResponse = users.stream().map(user ->
                UserDetailResponse.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .roleId(user.getRoleId())
                        .roleName(roleMap.get(user.getRoleId()))
                        .imageUrl(user.getImageId())
                        .referenceCode(user.getReferenceCode())
                        .totalAmount(user.getTotalAmount())
                        .lastDepositAmount(user.getLastDepositAmount())
                        .lastDepositDate(user.getLastDepositDate())
                        .lastWithDrawAmount(user.getLastWithDrawAmount())
                        .lastWithdrawDate(user.getLastWithdrawDate())
                        .createDate(user.getCreatedDate())
                        .activated(user.isActivated())
                        .build());

        json.putPOJO("page_number", request.getPageNumber());
        json.putPOJO("total_records", listUserPage.getTotalElements());
        json.putPOJO("page_size", request.getPageSize());
        json.putPOJO("list_user", userResponse);
        json.putPOJO("total_page", listUserPage.getTotalPages());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);

        return response;
    }

    @Override
    public ResponseBody<Object> addFundToUser(String username, String amount) {
        var userModel = userRepository.findByUsername(username).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        BigDecimal currentAmount = userModel.getTotalAmount() == null ? BigDecimal.ZERO : userModel.getTotalAmount();
        userModel.setTotalAmount(currentAmount.add(safeConvert(amount)));
        userRepository.save(userModel);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, userModel);

        return response;
    }

    @Override
    public ResponseBody<Object> deductFundFromUser(String username, String amount) {
        var userModel = userRepository.findByUsername(username).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        BigDecimal currentAmount = userModel.getTotalAmount() == null ? BigDecimal.ZERO : userModel.getTotalAmount();
        if (currentAmount.compareTo(BigDecimal.ZERO) > 0) {
            currentAmount = currentAmount.subtract(safeConvert(amount)).max(BigDecimal.ZERO);
            userModel.setTotalAmount(currentAmount);
            userRepository.save(userModel);
        }
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, userModel);

        return response;
    }

    public static BigDecimal safeConvert(String str) {
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            System.err.println("Chuỗi không hợp lệ: " + str);
            return BigDecimal.ZERO;
        }
    }
}
