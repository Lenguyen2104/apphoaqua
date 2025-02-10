package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.authen.RefreshTokenRequest;
import com.security.apphoaqua.dto.request.authen.SignInRequest;
import com.security.apphoaqua.dto.request.authen.SignUpUserRequest;

public interface AuthenticationService {

    ResponseBody<Object> registerUser(SignUpUserRequest signUpUserRequest);

    ResponseBody<Object> signIn(SignInRequest signInRequest);

//    ResponseBody<Object> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseBody<Object> refreshToken(RefreshTokenRequest refreshTokenRequest);

    ResponseBody<Object> checkExpiredToken(String token);
}
