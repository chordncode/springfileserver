package com.chordncode.springfileserver.home.service;

import com.chordncode.springfileserver.util.ResultStatus;

public interface IHomeService {
    
    /**
     * 비밀번호 변경
     * 
     * @param newPw 새로운 비밀번호
     * @return 성공 : "SUCCESS", 실패 : "FAILED"
     */
    public ResultStatus changePw(String newPw);

}
