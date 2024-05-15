package com.example.fundingwishbox.repository;

import com.example.fundingwishbox.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    @Modifying
    @Query(value = "update Board b set b.hits=b.hits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}
