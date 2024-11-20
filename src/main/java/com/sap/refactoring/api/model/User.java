package com.sap.refactoring.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class User {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private List<String> roles;

    public User(String name, String email, List<String> roles) {
        this.name = name;
        this.email = email;
        this.roles = roles;
    }
}
