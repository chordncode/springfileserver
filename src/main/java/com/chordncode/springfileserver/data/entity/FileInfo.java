package com.chordncode.springfileserver.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="file_info")
public class FileInfo {
    
    @Id
    @Column(name="file_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long fileId;

    @Column(name="save_name", nullable=false)
    private String saveName;

    @Column(name="original_name", nullable=false)
    private String originalName;

    @Column(name="save_path", nullable=false)
    private String savePath;

    @Column(name="format_type")
    private String formatType;

    @Column(name="size", nullable=false)
    private Long size;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;
    
}
