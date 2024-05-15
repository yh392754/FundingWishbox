package com.example.fundingwishbox.dto;


import com.example.fundingwishbox.entity.User;
import com.example.fundingwishbox.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    private Long id;
    private String loginId;
    private String nickname;
    private String nowPassword;
    private String newPassword;
    private String newPasswordCheck;
    private UserRole userRole;

    private LocalDateTime boardCreatedTime;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .userRole(user.getRole())
                .nowPassword(user.getPassword())
                .boardCreatedTime(user.getCreateTime())
                .build();
    }

    public UserDto(String loginId, String nickname, UserRole userRole, Long id) {
        this.loginId= loginId;
        this.nickname = nickname;
        this.userRole = userRole;
        this.id = id;
    }

}
