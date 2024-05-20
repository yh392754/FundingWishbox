package com.example.fundingwishbox.controller;


import com.example.fundingwishbox.config.PrincipalDetails;
import com.example.fundingwishbox.dto.BoardDto;
import com.example.fundingwishbox.entity.User;
import com.example.fundingwishbox.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor

public class BoardController {

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Security 로그인");
    }

    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm() {
        return "Board/boardSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDto boardDto, Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User writer = principalDetails.getUser(); // 현재 로그인한 사용자의 User 객체를 가져옵니다.
        boardService.save(boardDto, writer);
        return "Board/boardHome";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page = 1) Pageable pageable) {


        boardService.updateHits(id);
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("board", boardDto);
        model.addAttribute("page", pageable.getPageNumber());
        return "Board/boardDetail";
    }


    @GetMapping("/list/search")
    public String listForm(Model model, @RequestParam(name = "keyword", required = false) String keyword, @PageableDefault(page = 1) Pageable pageable) {
        Page<BoardDto> BoardDtoList = boardService.abc(keyword, pageable);

        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < BoardDtoList.getTotalPages()) ? startPage + blockLimit - 1 : BoardDtoList.getTotalPages();

        model.addAttribute("dto", BoardDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        return "Board/boardList";
    }
}

