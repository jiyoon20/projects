package me.jooie.minicafe.domain;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String value;

    UserRole(String value){
        this.value= value;
    }

    public String getValue(){
        return value;
    }
}
