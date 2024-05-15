package com.example.fundingwishbox.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity

public class Board extends Time{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private int hits;
    private int fileAttach;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<BoardFile> boardFiles;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFileEntityList = new ArrayList<>();


    public static Board toSaveEntity(BoardDto boardDto, User writer) {
        return Board.builder()
                .title(boardDto.getTitle())
                .body(boardDto.getBody())
                .user(writer)
                .fileAttach(0)
                .hits(boardDto.getBoardHits())
                .build();
    }

    public static Board toSaveFileEntity(BoardDto boardDto, User writer) {
        return Board.builder()
                .title(boardDto.getTitle())
                .body(boardDto.getBody())
                .user(writer)
                .hits(boardDto.getBoardHits())
                .fileAttach(1) // Set the fileAttach field to 1 indicating that a file is attached
                .build();
    }

}
