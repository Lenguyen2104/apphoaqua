package com.security.vinclub.enumeration;

public enum AppovalStatusEnum {
    PENDING("Chờ duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Từ chối");

    private String description;

    AppovalStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
