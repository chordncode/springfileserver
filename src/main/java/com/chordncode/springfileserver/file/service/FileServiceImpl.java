package com.chordncode.springfileserver.file.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.chordncode.springfileserver.data.dto.FileDto;
import com.chordncode.springfileserver.data.entity.FileInfo;
import com.chordncode.springfileserver.file.repository.FileRepository;
import com.chordncode.springfileserver.util.PageInfo;
import com.chordncode.springfileserver.util.ResultStatus;

@Service
public class FileServiceImpl implements IFileService {

    private FileRepository fileRepo;
    private String baseUploadPath;
    private String fileContextPath;
    public FileServiceImpl(FileRepository fileRepo){
        this.fileRepo = fileRepo;
        this.baseUploadPath = System.getProperty("user.dir")
                                + File.separator + "src"
                                + File.separator + "main"
                                + File.separator + "resources"
                                + File.separator + "static"
                                + File.separator + "uploads";
        this.fileContextPath = File.separator + "static"
                                + File.separator + "uploads";
    }
    
    @Override
    public PageInfo<FileDto> list(Long page) {
        long totalRow = fileRepo.count();

        if(page == null) page = 1L;
        PageInfo<FileDto> pageInfo = new PageInfo<>(page, totalRow, 10, 10);
        
        List<FileDto> fileList = fileRepo.findAllInRow(pageInfo.getStartRow(), pageInfo.getEndRow()).stream().map(fileInfo -> {
            return new FileDto(fileInfo);
        }).collect(Collectors.toList());
        pageInfo.setContentList(fileList);

        return pageInfo;
    }

    @Override
    public String select(Long fileId){
        FileDto fileInfo = new FileDto(fileRepo.findById(fileId).orElse(null));
        return fileContextPath + File.separator + fileInfo.getSavePath();
    }

    @Override
    public ResultStatus upload(MultipartFile[] uploadFile) throws IOException {
        
        for(MultipartFile file :  uploadFile){
            if(!file.isEmpty()){
                String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).replace("-", File.separator);
                File uploadPath = new File(this.baseUploadPath, today);
                if(!uploadPath.exists()) Files.createDirectories(uploadPath.toPath());

                FileInfo fileInfo = new FileInfo();
                String saveFilename = UUID.randomUUID().toString();
                fileInfo.setSaveName(saveFilename);
                String originalFilename = file.getOriginalFilename();
                fileInfo.setOriginalName(originalFilename);
                String fileFormatType = null;
                if(originalFilename != null && !originalFilename.equals("")){
                    fileFormatType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                }
                fileInfo.setSavePath(today + File.separator + saveFilename);
                fileInfo.setFormatType(fileFormatType);
                fileInfo.setSize(file.getSize());
                fileInfo.setCreatedAt(LocalDateTime.now());

                fileRepo.save(fileInfo);
                File saveFile = new File(uploadPath, saveFilename);
                file.transferTo(saveFile.toPath());
            }
        }

        return ResultStatus.SUCCESS;
    }

    @Override
    public ResponseEntity<Resource> download(Long fileId) throws Exception {

        FileInfo fileInfo = fileRepo.findById(fileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        Resource resource = new FileSystemResource(new File(baseUploadPath, fileInfo.getSavePath()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.add("Content-Disposition", "attachment;filename=\""
                    + new String(fileInfo.getOriginalName().getBytes("UTF-8"), "ISO-8859-1")
                    + "\"");

        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

    @Override
    public void delete(Long fileId) throws Exception {

        FileInfo fileInfo = fileRepo.findById(fileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Files.deleteIfExists(new File(baseUploadPath, fileInfo.getSavePath()).toPath());
        fileRepo.deleteById(fileId);
    }

    @Override
    public void update(FileDto fileDto) throws Exception {
        FileInfo fileInfo = fileRepo.findById(fileDto.getFileId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileInfo.setOriginalName(fileDto.getOriginalName() + "." + fileInfo.getFormatType());
        fileRepo.save(fileInfo);
    }

}
