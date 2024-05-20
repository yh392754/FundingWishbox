package com.example.fundingwishbox.repository;


import com.example.fundingwishbox.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FIleRepository extends JpaRepository<BoardFile, Long> {
}
