package com.chordncode.springfileserver.data.dto;

import java.time.format.DateTimeFormatter;

import com.chordncode.springfileserver.data.entity.FileInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    
    private Long fileId;
    private String saveName;
    private String originalName;
    private String savePath;
    private String formatType;
    private Long size;
    private String createdAt;

    public FileDto(FileInfo file){
        this.fileId = file.getFileId();
        this.saveName = file.getSaveName();
        this.originalName = file.getOriginalName();
        this.savePath = file.getSavePath();
        this.formatType = file.getFormatType();
        this.size = file.getSize();
        this.createdAt = file.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
