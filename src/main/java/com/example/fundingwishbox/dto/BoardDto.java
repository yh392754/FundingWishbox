package com.example.fundingwishbox.dto;


import com.example.fundingwishbox.entity.Board;
import com.example.fundingwishbox.entity.BoardFile;
import lombok.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BoardDto {
    private Long id;
    private String title;
    private String body;
    private String writerId;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdateTime;
    private User user;


    private List<MultipartFile> boardFile;
    private List <String> originalFileName;
    private List <String> storedFileName;
    private int fileAttached;


    public static BoardDto of(Board board) {
        BoardDto.BoardDtoBuilder builder = BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .body(board.getBody())
                .boardHits(board.getHits())
                .boardCreatedTime(board.getCreateTime())
                .writerId(board.getUser().getNickname());

        if (board.getBoardFiles() != null && !board.getBoardFiles().isEmpty()) {
            // 파일이 첨부된 경우
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();


            for(BoardFile boardFile : board.getBoardFileEntityList()) {
                originalFileNameList.add(boardFile.getOriginalFileName());
                storedFileNameList.add(boardFile.getStoredFileName());

            }
            builder.originalFileName(originalFileNameList);
            builder.storedFileName(storedFileNameList);


            builder.fileAttached(1);
        } else {
            // 파일이 첨부되지 않은 경우
            builder.fileAttached(0);
        }
        return builder.build();

    }


    public BoardDto(Long id,String title, String body, String writerId) {
        this.id =id;
        this.title = title;
        this.body = body;
        this.writerId = writerId;
    }
}
