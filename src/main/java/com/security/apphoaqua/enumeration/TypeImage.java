package com.security.apphoaqua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TypeImage {
    BANNER_IMAGE("BANNER_IMAGE"),
    PRODUCT_IMAGE("PRODUCT_IMAGE");

    private final String value;
}
