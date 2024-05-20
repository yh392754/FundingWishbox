package com.example.fundingwishbox.service;


import com.example.fundingwishbox.dto.BoardDto;
import com.example.fundingwishbox.entity.Board;
import com.example.fundingwishbox.entity.BoardFile;
import com.example.fundingwishbox.entity.User;
import com.example.fundingwishbox.repository.BoardRepository;
import com.example.fundingwishbox.repository.FIleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class BoardService {

    private final BoardRepository boardRepository;
    private final FIleRepository fileRepository;

    @Transactional
    public void updateHits(Long id){
        boardRepository.updateHits(id);
    }

    @Transactional
    public void save(BoardDto boardDto, User writer) throws IOException {

        if(boardDto.getBoardFile().isEmpty()) {
            Board board = Board.toSaveEntity(boardDto, writer );
            boardRepository.save(board);
        }
        else{
            Board boardEntity = Board.toSaveFileEntity(boardDto,writer);
            Long savedId = boardRepository.save(boardEntity).getId();
            Board board =boardRepository.findById(savedId).get();

            for(MultipartFile boardFile: boardDto.getBoardFile()){


                /*MultipartFile boardFile = boardDto.getBoardFile();*/
                String originalFileName = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() +"_" + originalFileName;
                String savePath = "C:/Users/PC/Desktop/images/" + storedFileName;
                boardFile.transferTo(new File(savePath));

                BoardFile boardFileEntity = BoardFile.toBoardFileEntity(board, originalFileName, storedFileName);
                fileRepository.save(boardFileEntity);
            }

        }
    }

    @Transactional
    public List<BoardDto> list() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for(Board board: boardList){
            boardDtoList.add(BoardDto.of(board));
        }
        return boardDtoList;
    }

    @Transactional
    public BoardDto findById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if(optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            BoardDto boardDto = BoardDto.of(board);
            return boardDto;
        }
        else
            return null;
    }

    @Transactional
    public Page<BoardDto> abc(String keyword, Pageable pageable) {

        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;


        Page<Board> boardList;
        if (keyword == null || keyword.isEmpty()) {
            boardList = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "Title")));
        } else {
            boardList = boardRepository.findByTitleContaining(keyword, PageRequest.of(page,pageLimit,Sort.by(Sort.Direction.DESC,"Title")));
        }
        Page<BoardDto> boardDtoList = boardList.map(board -> new BoardDto(board.getId(),board.getTitle(),board.getBody(),board.getUser().getNickname()));


        return boardDtoList;
    }


}
