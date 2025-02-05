package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.authen.ChangePasswordRequest;
import com.security.vinclub.dto.request.authen.RefreshTokenRequest;
import com.security.vinclub.dto.request.authen.SignInRequest;
import com.security.vinclub.dto.request.authen.SignUpUserRequest;

public interface AuthenticationService {

    ResponseBody<Object> registerUser(SignUpUserRequest signUpUserRequest);

    ResponseBody<Object> signIn(SignInRequest signInRequest);

    ResponseBody<Object> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseBody<Object> refreshToken(RefreshTokenRequest refreshTokenRequest);

    ResponseBody<Object> checkExpiredToken(String token);
}
