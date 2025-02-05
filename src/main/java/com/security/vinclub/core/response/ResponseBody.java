package com.security.vinclub.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody<T> {

    @JsonProperty("result_code")
    private int resultCode;

    @JsonProperty("result_msg")
    private String resultMsg;

    @JsonProperty("result_data")
    private T resultData;

    @JsonProperty("error_data")
    private ErrorData errorData;

    public void setOperationError(String resultMsg, ErrorData errorData) {
        this.resultCode = 0;
        this.resultMsg = resultMsg;
        this.errorData = errorData;
    }

    public void setOperationFail(String resultMsg, T errorData) {
        this.resultCode = 0;
        this.resultMsg = resultMsg;
        this.resultData = errorData;
    }

    public void setOperationSuccess(String resultMsg, T resultData) {
        this.resultCode = 1;
        this.resultMsg = resultMsg;
        this.resultData = resultData;
    }
}
