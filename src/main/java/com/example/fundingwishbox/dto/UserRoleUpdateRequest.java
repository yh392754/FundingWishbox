package com.example.fundingwishbox.dto;

import com.example.fundingwishbox.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleUpdateRequest {

    private UserRole role;

}
