package com.example.fundingwishbox.entity;


import com.example.fundingwishbox.dto.JoinRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User extends Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String nickname;

    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    private String provider;
    private String providerId;


    public void edit(String newPassword, String newNickname) {
        this.password = newPassword;
        this.nickname = newNickname;
    }

    public void editGoogle(String newNickname) {
        this.nickname = newNickname;
    }


    public static User fromDto(JoinRequest req, PasswordEncoder passwordEncoder){
        String encodedPassword = passwordEncoder.encode(req.getPassword());
        return User.builder()
                .loginId(req.getLoginId())
                .password(encodedPassword)
                .nickname(req.getNickname())
                .role(UserRole.USER)
                .build();
    }

    public void setRole(UserRole role) {
        this.role =role;
    }
}
