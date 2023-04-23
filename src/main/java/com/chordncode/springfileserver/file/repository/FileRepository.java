package com.chordncode.springfileserver.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chordncode.springfileserver.data.entity.FileInfo;

public interface FileRepository extends JpaRepository<FileInfo, Long> {
    
    @Query(value = "WITH v AS ( " +
          "SELECT f.file_id, f.save_name, f.original_name, f.save_path, f.format_type, f.size, f.created_at, " +
          "       ROW_NUMBER() OVER(ORDER BY f.created_at desc) rownum " +
          "  FROM file_info f " +
          ") " +
          "SELECT * " +
          "  FROM v " +
          " WHERE v.rownum BETWEEN :startRow AND :endRow", nativeQuery = true)
    List<FileInfo> findAllInRow(Long startRow, Long endRow);

}
