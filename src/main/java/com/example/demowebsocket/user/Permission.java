package com.example.demowebsocket.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    Client_READ("client:read"),
    Client_UPDATE("client:update"),
    Client_CREATE("client:create"),
    Client_DELETE("client:delete")

    ;

    @Getter
    private final String permission;
}
