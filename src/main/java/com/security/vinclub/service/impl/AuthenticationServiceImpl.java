package com.security.vinclub.service.impl;

import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.authen.ChangePasswordRequest;
import com.security.vinclub.dto.request.authen.RefreshTokenRequest;
import com.security.vinclub.dto.request.authen.SignInRequest;
import com.security.vinclub.dto.request.authen.SignUpUserRequest;
import com.security.vinclub.dto.response.authen.JwtAuthenticationResponse;
import com.security.vinclub.entity.ReferenceCode;
import com.security.vinclub.entity.Role;
import com.security.vinclub.entity.User;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.ReferenceCodeRepository;
import com.security.vinclub.repository.RoleRepository;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.service.AuthenticationService;
import com.security.vinclub.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;
    private final RoleRepository roleRepository;
    private final ReferenceCodeRepository referenceCodeRepository;

    public ResponseBody<Object> registerUser(SignUpUserRequest request) {
        User user = new User();
        var isUsernameExisted = userRepository.existsByUsername(request.getUsername());

        if (isUsernameExisted) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, USER_NAME_EXIST, errorMapping);
        }

        var isPhoneExisted = userRepository.existsByPhone(request.getPhone());

        if (isPhoneExisted) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PHONE_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PHONE_EXIST, errorMapping);
        }
        List<ReferenceCode> referenceCodes = referenceCodeRepository.findByDeletedFalse();
        List<String> refCodes = referenceCodes.stream().map(ReferenceCode::getReferenceCode).toList();
        if(!refCodes.contains(request.getReferenceCode())) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(REFERENCE_CODE_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, REFERENCE_CODE_NOT_FOUND, errorMapping);
        }

        var role = roleRepository.findByName("USER");
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setPhone(request.getPhone());
        user.setReferenceCode(request.getReferenceCode());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRoleId(role.getId());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, user);
        return response;
    }

    public ResponseBody<Object> signIn(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            var errorMapping = ErrorData.builder()
                    .errorKey2(INVALID_CREDENTIALS.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS, errorMapping);
        }
        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey2(INVALID_REQUEST_PARAMETER.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, INVALID_REQUEST_PARAMETER, errorMapping);
        });

        if (!user.isActivated()) {
            var errorMapping = ErrorData.builder()
                    .errorKey2(ACCOUNT_DEACTIVATED.getCode())
                    .build();
            throw  new ServiceSecurityException(HttpStatus.OK, ACCOUNT_DEACTIVATED, errorMapping);
        }

        Role role = roleRepository.findById(user.getRoleId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(ROLE_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, ROLE_NOT_FOUND, errorMapping);
        });

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setUserId(user.getId());
        jwtAuthenticationResponse.setRoleId(user.getRoleId());
        jwtAuthenticationResponse.setUsername(user.getUsername());
        jwtAuthenticationResponse.setRoleName(role.getName());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, jwtAuthenticationResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> changePassword(ChangePasswordRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getOldPassword())
            );
        } catch (AuthenticationException e) {
            var errorMapping = ErrorData.builder()
                    .errorKey2(INVALID_CREDENTIALS.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS, errorMapping);
        }
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey2(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, user);
        return response;
    }

    public ResponseBody<Object> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(INVALID_REQUEST_PARAMETER.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, INVALID_REQUEST_PARAMETER, errorMapping);
        }
        var jwt = jwtService.generateToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
        jwtAuthenticationResponse.setUserId(user.getId());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, jwtAuthenticationResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> checkExpiredToken(String token) {
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey2(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        if (!jwtService.isTokenValid(token, user)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(TOKEN_EXPIRED.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, TOKEN_EXPIRED, errorMapping);
        }
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, true);
        return response;
    }
}
