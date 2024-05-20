package com.example.fundingwishbox.repository;

import com.example.fundingwishbox.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);
    //List<User> findByNicknameContaining(String keyword);

    Page<User> findByNicknameContaining(String keyword, Pageable pageable);

}
