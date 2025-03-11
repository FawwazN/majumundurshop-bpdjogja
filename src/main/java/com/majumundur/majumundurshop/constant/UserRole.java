package com.majumundur.majumundurshop.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_MERCHANT("Merchant"),
    ROLE_USER("User");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public static UserRole findByDescription(String description) {
        for (UserRole role : values()) {
            if (role.description.equalsIgnoreCase(description)) {
                return role;
            }
        }
        return null;
    }
}
