package com.hmanh.ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(999, "Invalid message key"),
    FORBIDDEN( 403, "valid token but insufficient permissions" ),
    UNAUTHENTICATED( 1007, "Unauthenticated" ),
    ;
    private Integer code;
    private String message;
}
