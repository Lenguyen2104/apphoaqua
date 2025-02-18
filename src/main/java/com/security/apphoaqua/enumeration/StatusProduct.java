package com.security.apphoaqua.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusProduct {
    WAIT_FOR_COMPLETION("WAIT_FOR_COMPLETION"),
    COMPLETE("COMPLETE"),;

    private final String value;
}
