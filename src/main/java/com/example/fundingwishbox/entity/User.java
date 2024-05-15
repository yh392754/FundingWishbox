package com.example.fundingwishbox.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 수정 하는 거에서 builder 를 쓰면 새로운 객체를 생성하는거기 때문에 null 값이 들어갔던 것 같음.
    //. Builder 패턴은 새로운 객체를 생성하고 초기화할 때 사용하는 것
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
