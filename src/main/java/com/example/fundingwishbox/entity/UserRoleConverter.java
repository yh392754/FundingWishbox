package com.example.fundingwishbox.entity;

import jakarta.persistence.AttributeConverter;

public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole attribute) {
        return attribute.getLegacyCode();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        return UserRole.ofLegacyCode(dbData);
    }
}
