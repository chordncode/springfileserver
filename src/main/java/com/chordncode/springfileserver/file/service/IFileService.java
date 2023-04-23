package com.chordncode.springfileserver.file.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.chordncode.springfileserver.data.dto.FileDto;
import com.chordncode.springfileserver.util.PageInfo;
import com.chordncode.springfileserver.util.ResultStatus;

public interface IFileService {

    /**
     * 파일 목록 조회
     * 
     * @param page 현재 페이지
     * @return 파일 목록
     */
    public PageInfo<FileDto> list(Long page);

    /**
     * 파일 조회
     * 
     * @param fileId 파일 번호
     * @return 파일 전체 경로
     */
    public String select(Long fileId);
    
    /**
     * 파일 업로드
     * 
     * @param uploadFile 업로드한 파일들
     * @return 성공 : "SUCCESS", 실패 : "FAILED"
     * @throws IOException
     */
    public ResultStatus upload(MultipartFile[] uploadFile) throws IOException;

    /**
     * 파일 다운로드
     * 
     * @param fileId 다운로드할 파일 번호
     * @return 응답으로 반환할 ResponseEntity 객체
     * @throws Exception
     */
    public ResponseEntity<Resource> download(Long fileId) throws Exception;

    /**
     * 파일 삭제
     * 
     * @param fileId 삭제할 파일 번호
     * @throws Exception
     */
    public void delete(Long fileId) throws Exception;

    /**
     * 파일명 수정
     * 
     * @param fileDto 수정할 파일 번호와 파일명
     * @throws Exception
     */
    public void update(FileDto fileDto) throws Exception;

}
