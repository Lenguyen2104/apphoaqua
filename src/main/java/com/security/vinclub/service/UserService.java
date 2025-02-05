package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.users.UpdateUserRequest;
import com.security.vinclub.dto.request.users.UserSearchRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();

    ResponseBody<Object> getUserIdDetail(String userId);

    ResponseBody<Object> updateUser(UpdateUserRequest request);

    ResponseBody<Object> deleteUserById(String userId);
    ResponseBody<Object> activateUserById(String userId);
    ResponseBody<Object> getAllUsers(UserSearchRequest request);
    ResponseBody<Object> searchAllUsers(UserSearchRequest request);

    ResponseBody<Object> addFundToUser(String username, String amount);
    ResponseBody<Object> deductFundFromUser(String username, String amount);

}
