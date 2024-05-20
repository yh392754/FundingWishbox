package com.example.fundingwishbox.service;


import com.example.fundingwishbox.dto.JoinRequest;
import com.example.fundingwishbox.dto.UserDto;
import com.example.fundingwishbox.dto.UserRoleUpdateRequest;
import com.example.fundingwishbox.entity.User;
import com.example.fundingwishbox.entity.UserRole;
import com.example.fundingwishbox.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public void join(JoinRequest request) {
        userRepository.save(User.fromDto(request,encoder));
    }

    public User myInfo(String loginId) {
        return userRepository.findByLoginId(loginId).get();
    }

    public BindingResult editValid(UserDto dto, BindingResult bindingResult, String loginId) {
        User loginUser = userRepository.findByLoginId(loginId).get();

        if (dto.getNowPassword().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 비어있습니다."));
        } else if (!encoder.matches(dto.getNowPassword(), loginUser.getPassword())) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 틀렸습니다."));
        }

        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            bindingResult.addError(new FieldError("dto", "newPasswordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (dto.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 비어있습니다."));
        } else if (dto.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (!dto.getNickname().equals(loginUser.getNickname()) && userRepository.existsByNickname(dto.getNickname())) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 중복됩니다."));
        }

        return bindingResult;
    }

    @Transactional
    public void edit(UserDto dto, String loginId) {
        User user = userRepository.findByLoginId(loginId).get();
        if (dto.getNewPassword().equals("")) {
            user.edit(user.getPassword(), dto.getNickname());
        } else {
            user.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
        }
        userRepository.save(user);
    }

    @Transactional
    public User delete(String loginId) {
        User loginUser = userRepository.findByLoginId(loginId).get();
        userRepository.delete(loginUser);
        return loginUser;
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateRole(Long id, UserRoleUpdateRequest request) {
        User user = userRepository.findById(id).get();
        user.setRole(request.getRole());
        return userRepository.save(user);
    }

    public User getUserById(String userId) {
        User user = userRepository.findByLoginId(userId).get();
        return user;
    }

    public User getLoginUserByLoginId(String loginId) throws IllegalAccessException {
        if (loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();
        if(user.getRole() == UserRole.BlACKLIST){
            throw new IllegalAccessException("Blacklist user");
        }


        return user;
    }

    @Transactional
    public Page<UserDto> searchAndPaging(String keyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;


        Page<User> userList;
        if (keyword == null || keyword.isEmpty()) {
            userList = userRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "loginId")));
        } else {
            userList = userRepository.findByNicknameContaining(keyword, PageRequest.of(page,pageLimit,Sort.by(Sort.Direction.DESC,"loginId")));
        }

        Page<UserDto> userDtoList = userList.map(user -> new UserDto(user.getLoginId(), user.getNickname(), user.getRole(), user.getId()));

        return userDtoList;
    }

}
