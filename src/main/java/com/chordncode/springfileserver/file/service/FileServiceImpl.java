package com.chordncode.springfileserver.file.service;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
import com.chordncode.springfileserver.util.DirInfo;
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
    public PageInfo<FileDto> list(Long dirId, Long page) throws Exception {
        long totalRow = 0L;
        String currentDir = null;
        DirInfo dirInfo = null;
        if(dirId != null){
            totalRow = fileRepo.countRowBydirId(dirId);
            FileInfo directory = fileRepo.findByFileId(dirId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            currentDir = directory.getSavePath() + File.separator + directory.getSaveName();
            dirInfo = DirInfo.builder().dirId(dirId).dirName(currentDir).parentDirId(directory.getDirId()).build();
        } else {
            totalRow = fileRepo.countRowOnHome();
            dirInfo = DirInfo.builder().dirId(dirId).dirName("home").build();
        }

        if(page == null) page = 1L;
        PageInfo<FileDto> pageInfo = new PageInfo<>(dirInfo, page, totalRow, 10, 10);
        
        List<FileDto> fileList = null;
        if(dirId != null){
            fileList = fileRepo.findAllInRow(pageInfo.getDirInfo().getDirId(), pageInfo.getStartRow(), pageInfo.getEndRow()).stream().map(fileInfo -> {
                return new FileDto(fileInfo);
            }).collect(Collectors.toList());
        } else {
            fileList = fileRepo.findAllInRowInHome(pageInfo.getStartRow(), pageInfo.getEndRow()).stream().map(fileInfo -> {
                return new FileDto(fileInfo);
            }).collect(Collectors.toList());
        }
        pageInfo.setContentList(fileList);

        return pageInfo;
    }

    @Override
    public String select(Long fileId){
        FileDto fileInfo = new FileDto(fileRepo.findById(fileId).orElse(null));
        return fileContextPath + File.separator + fileInfo.getSavePath() + File.separator + fileInfo.getSaveName();
    }

    @Override
    public ResultStatus upload(Long dirId, MultipartFile[] uploadFile) throws Exception {
        
        String currentDir = null;
        if(dirId != null){
            FileInfo dirInfo = fileRepo.findById(dirId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            currentDir = dirInfo.getSavePath() + File.separator + dirInfo.getSaveName();
        } else {
            currentDir = "home";
        }

        for(MultipartFile file :  uploadFile){
            if(!file.isEmpty()){
                File uploadPath = new File(this.baseUploadPath, currentDir);
                if(!uploadPath.exists()) Files.createDirectories(uploadPath.toPath());

                String saveFilename = UUID.randomUUID().toString();
                String originalFilename = file.getOriginalFilename();
                String fileFormatType = null;
                if(originalFilename != null && !originalFilename.equals("")){
                    fileFormatType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                }
                FileInfo fileInfo = FileInfo.builder()
                                            .saveName(saveFilename)
                                            .originalName(originalFilename)
                                            .savePath(currentDir)
                                            .formatType(fileFormatType)
                                            .size(file.getSize())
                                            .dirId(dirId)
                                            .dirYn("N")
                                            .createdAt(LocalDateTime.now())
                                            .build();

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
        
        Resource resource = new FileSystemResource(new File(baseUploadPath, fileInfo.getSavePath() + File.separator + fileInfo.getSaveName()));
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
        if(fileInfo.getFormatType().equalsIgnoreCase("dir")){
            FileUtils.deleteDirectory(new File(baseUploadPath, fileInfo.getSavePath() + File.separator + fileInfo.getSaveName()));
            fileRepo.deleteDirById(fileId);
        } else {
            Files.deleteIfExists(new File(baseUploadPath, fileInfo.getSavePath() + File.separator + fileInfo.getSaveName()).toPath());
            fileRepo.deleteById(fileId);
        }
    }

    @Override
    public void update(FileDto fileDto) throws Exception {
        FileInfo fileInfo = fileRepo.findById(fileDto.getFileId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileInfo.setOriginalName(fileDto.getOriginalName() + "." + fileInfo.getFormatType());
        fileRepo.save(fileInfo);
    }

    @Override
    public ResultStatus createNewDir(Long dirId, String newDirName) throws Exception {
        
        String currentDir = null;
        if(dirId != null){
            FileInfo dirInfo = fileRepo.findById(dirId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            currentDir = dirInfo.getSavePath() + File.separator + dirInfo.getSaveName();
        } else {
            currentDir = "home";
        }

        File newDir = new File(this.baseUploadPath, currentDir + File.separator + newDirName);
        if(Files.exists(newDir.toPath())){
            return ResultStatus.DUPLICATED;
        }
        Files.createDirectories(newDir.toPath());
        FileInfo dir = FileInfo.builder()
                               .saveName(newDirName)
                               .originalName(newDirName)
                               .savePath(currentDir)
                               .formatType("dir")
                               .size(0L)
                               .dirId(dirId)
                               .dirYn("Y")
                               .createdAt(LocalDateTime.now())
                               .build();
        fileRepo.save(dir);
        return ResultStatus.SUCCESS;
    }

}
