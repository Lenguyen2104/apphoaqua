package com.security.apphoaqua.common;


import com.security.apphoaqua.config.Message;
import com.security.apphoaqua.exception.RestExceptionHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class BussinessCommon {

    public static Pageable castToPageable(Integer page, Sort sort, int numberOfPage) {
        try {
            return PageRequest.of(page - 1, numberOfPage, sort);
        } catch (Exception e) {
            throw new RestExceptionHandler(Message.INVALID_PAGE);
        }
    }

}