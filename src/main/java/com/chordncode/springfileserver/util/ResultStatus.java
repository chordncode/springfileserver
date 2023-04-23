package com.chordncode.springfileserver.util;

public enum ResultStatus {
    
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    NONE("NONE");

    private final String resultStatus;

    private ResultStatus(String resultStatus){
        this.resultStatus = resultStatus;
    }

    public String getStatus(){
        return this.resultStatus;
    }

    public boolean isSuccess(){
        if(this.resultStatus.equals("SUCCESS")) return true;
        return false;
    }
}
