package com.example.fundingwishbox.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {
    USER("USER", 1),
    ADMIN("ADMIN", 2),
    BlACKLIST("BlACKLIST",3);

    private String desc;
    private Integer legacyCode;

    UserRole(String desc , Integer legacyCode){
        this.desc = desc;
        this.legacyCode = legacyCode;
    }

    public static UserRole ofLegacyCode(Integer legacyCode) {
        return Arrays.stream(UserRole.values())
                .filter(e -> e.getLegacyCode().equals(legacyCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("상태 코드에 legacyCode : [%s]가 존재하지 않습니다.", legacyCode)));
    }

}
