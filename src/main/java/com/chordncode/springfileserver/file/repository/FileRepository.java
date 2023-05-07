package com.chordncode.springfileserver.file.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chordncode.springfileserver.data.entity.FileInfo;

public interface FileRepository extends JpaRepository<FileInfo, Long> {

    @Query(value = 
            "SELECT COUNT(*) " +
            "  FROM file_info f " +
            " WHERE f.dir_id IS NULL ",
            nativeQuery = true
    )
    Long countRowOnHome();

    @Query(value = 
            "SELECT COUNT(*) " +
            "  FROM file_info f " +
            " WHERE f.dir_id = :dirId ",
            nativeQuery = true
    )
    Long countRowBydirId(Long dirId);

    @Query(value =
            "SELECT * " +
            "  FROM file_info f " +
            " WHERE f.file_id = :dirId ",
            nativeQuery = true
    )
    Optional<FileInfo> findByFileId(Long dirId);
    
    @Query(value =
            "WITH v AS ( " +
            "SELECT f.file_id, f.save_name, f.original_name, f.save_path, f.format_type, f.size, f.dir_id, f.dir_yn, f.created_at, " +
            "       ROW_NUMBER() OVER(ORDER BY f.dir_yn desc, f.created_at desc) rownum " +
            "  FROM file_info f " +
            " WHERE f.dir_id = :dirId " +
            ") " +
            "SELECT * " +
            "  FROM v " +
            " WHERE v.rownum BETWEEN :startRow AND :endRow",
            nativeQuery = true
    )
    List<FileInfo> findAllInRow(Long dirId, Long startRow, Long endRow);

    @Query(value =
            "WITH v AS ( " +
            "SELECT f.file_id, f.save_name, f.original_name, f.save_path, f.format_type, f.size, f.dir_id, f.dir_yn, f.created_at, " +
            "       ROW_NUMBER() OVER(ORDER BY f.dir_yn desc, f.created_at desc) rownum " +
            "  FROM file_info f " +
            " WHERE f.dir_id IS NULL " +
            ") " +
            "SELECT * " +
            "  FROM v " +
            " WHERE v.rownum BETWEEN :startRow AND :endRow",
            nativeQuery = true
    )
    List<FileInfo> findAllInRowInHome(Long startRow, Long endRow);
    
    @Query(value = 
            "DELETE " + 
            "  FROM file_info " +
            " WHERE file_id = :fileId " + 
            "    OR dir_id = :fileId",
            nativeQuery = true
    )
    void deleteDirById(Long fileId);

}
