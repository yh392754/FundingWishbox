package com.example.fundingwishbox.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_file_table")

public class File extends Time{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="board_id")
    private Board board;

    public static File toBoardFileEntity(Board boardEntity, String originalFileName, String storedFileName) {
        return  File.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .board(boardEntity)
                .build();
    }


}