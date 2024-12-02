package com.xmcy.crypto.model;

import java.util.Arrays;

public enum SortDirection {
    ASC, DESC;

    public static SortDirection fromString(String value) {
        return Arrays.stream(SortDirection.values())
                .filter(sortDirection -> sortDirection.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(SortDirection.ASC);
    }
}
