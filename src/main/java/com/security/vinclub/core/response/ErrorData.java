package com.security.vinclub.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorData {

    final public static String ERROR_TYPE  = "security";

    final public static String ERROR_KEY_SERV_SECURITY = "service-security";

    private String code;
    private String msg;

    @JsonProperty("invalid_fields")
    private Object invalidFields;

    private Object error;
    private String cause;
    private String timestamp;

    @JsonProperty("error_key1")
    private String errorKey1;

    @JsonProperty("error_key2")
    private String errorKey2;
}
