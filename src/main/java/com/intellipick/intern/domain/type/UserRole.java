package com.intellipick.intern.domain.type;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MASTER("ROLE_MASTER");

    private final String value;

    // Enum 생성자
    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // String 값을 Enum으로 변환
    public static UserRole fromString(String value)throws IllegalArgumentException {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException(value + " 이런 권한은 존재하지 않습니다.");
    }

}
