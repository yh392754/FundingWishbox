package com.example.fundingwishbox.controller;


import com.example.fundingwishbox.dto.JoinRequest;
import com.example.fundingwishbox.dto.LoginRequest;
import com.example.fundingwishbox.dto.UserDto;
import com.example.fundingwishbox.dto.UserRoleUpdateRequest;
import com.example.fundingwishbox.entity.User;
import com.example.fundingwishbox.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/security-login")
public class UserController {

    private final UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Funding Wish Box");
    }

    @GetMapping(value = {"", "/"})
    public String home(Model model, Authentication auth) throws IllegalAccessException {

        if (auth != null) {
            User loginUser;
            try {
                loginUser = userService.getLoginUserByLoginId(auth.getName());
            } catch (IllegalAccessException e) {
                model.addAttribute("errorMessage", "You are blacklisted and cannot log in.");
                return "redirect:/security-login/login";
            }
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Funding Wish Box");

        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {

        // loginId 중복 체크
        if (userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if (userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }
        // password와 passwordCheck가 같은지 체크
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "바밀번호가 일치하지 않습니다."));
        }

        if (bindingResult.hasErrors()) {
            return "join";
        }

        userService.join(joinRequest);

        return "redirect:/security-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/info")
    public String userInfo(Model model, Authentication auth) throws IllegalAccessException {

        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        if (loginUser == null) {
            return "redirect:/security-login/login";
        }

        model.addAttribute("user", loginUser);
        return "info";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {

        return "admin";
    }

    @GetMapping("/edit")
    public String detailForm(Model model, Authentication auth) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "edit";

    }

    // auth.getName() 이 session 을 사용 할때 httpsession 과 비슷한 역할을 해주는 놈
    // 누구인지 찾아 주는 역할인데 현재 로그인 하고 있는 사용자의 정보를 가지고 오는 역할
    @PostMapping("/edit")
    public String detail(@Valid @ModelAttribute UserDto dto, BindingResult bindingResult,
                         Authentication auth) {

        if (dto.getNowPassword() != null) {
            if (userService.editValid(dto, bindingResult, auth.getName()).hasErrors()) {
                return "edit";
            }
            userService.edit(dto, auth.getName());
        }

        return "redirect:/security-login/info";


    }

    @GetMapping("/delete")
    public String delete(Authentication auth) {
        userService.delete(auth.getName());
        return "redirect:/security-login";
    }


    @GetMapping("/admin/userList/search")
    public String search(Model model, @RequestParam(name = "keyword", required = false) String keyword, @PageableDefault(page = 1) Pageable pageable) {
        Page<UserDto> UserDtoList = userService.searchAndPaging(keyword,pageable);

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < UserDtoList.getTotalPages()) ? startPage + blockLimit - 1 : UserDtoList.getTotalPages();

        model.addAttribute("dto", UserDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);

        return "userList";
    }


    @GetMapping("/delete/{id}")
    public String adminDelete(@PathVariable Long id, @RequestParam("currentPage") int currentPage) {
        userService.deleteById(id);
        return "redirect:/security-login/admin/userList/search?page=" + currentPage;
    }


    @PostMapping("/{id}/update-role")
    public String updateRole(@PathVariable Long id,
                             @ModelAttribute UserRoleUpdateRequest request,
                             @RequestParam("currentPage") int currentPage) {

        userService.updateRole(id, request);
        return "redirect:/security-login/admin/userList/search?page=" + currentPage;
    }

    @GetMapping("/boardhome")
    public String boardHome(Model model){
        return "Board/boardHome";
    }

}