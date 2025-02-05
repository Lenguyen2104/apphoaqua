package com.security.vinclub.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    SUCCESSFUL("000", "Successful", "Thành công"),
    UNAUTHORIZED("001", "Unauthorized", "Không được phép"),
    EMAIL_EXIST("002", "Email exists", "Email đã tồn tại"),
    INVALID_REQUEST_PARAMETER("003", "Invalid request parameter: ", "Tham số yêu cầu không hợp lệ: "),
    USER_NOT_FOUND("004", "User not found", "Không tìm thấy người dùng"),
    PHONE_NUMBER_EXIST("005", "Phone number exists", "Số điện thoại đã tồn tại"),
    CATEGORY_NOT_FOUND("006", "Category not found", "Không tìm thấy danh mục"),
    USER_NAME_EXIST("007", "User name exists", "Tên người dùng đã tồn tại"),
    ROLE_NOT_FOUND("008", "Role not found", "Không tìm thấy vai trò"),
    FILE_NOT_FOUND("009", "File not found", "Không tìm thấy tệp"),
    DEPOSIT_NOT_FOUND("010", "Deposit not found", "Không tìm thấy khoản tiền gửi"),
    WITHDRAW_NOT_FOUND("011", "Withdraw not found", "Không tìm thấy khoản rút tiền"),
    TOKEN_EXPIRED("012", "Token is expired", "Mã thông báo đã hết hạn"),
    ACCOUNT_DEACTIVATED("013", "Account is deactivated", "Tài khoản đã bị vô hiệu hóa"),
    REFERENCE_CODE_NOT_FOUND("014", "Reference code not found", "Không tìm thấy mã tham chiếu"),
    PHONE_EXIST("007", "Phone exists", "Số điện thoại đã tồn tại"),
    INVALID_CREDENTIALS("031", "Invalid Credentials", "Thông tin xác thực không hợp lệ"),
    INTERNAL_SERVER_ERROR("999", "Internal server error", "Lỗi máy chủ nội bộ");

    private final String code;
    private final String message;
    private final String vieMsg;

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    public static ResponseStatus findResponseStatus(String code) {
        for (ResponseStatus v : values()) {
            if (v.getCode().equals(code)) {
                return v;
            }
        }

        return null;
    }
}
