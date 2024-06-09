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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/security-login/board")
public class BoardController {

    private final BoardService boardService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "Funding Wish Box");
    }

    @GetMapping("/save")
    public String saveForm() {
        return "Board/boardSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDto boardDto) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User writer = principalDetails.getUser();
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
        Page<BoardDto> boardDtoList = boardService.abc(keyword, pageable);

        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardDtoList.getTotalPages()) ? startPage + blockLimit - 1 : boardDtoList.getTotalPages();

        model.addAttribute("dto", boardDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword);
        return "Board/boardList";
    }
}
