package com.chordncode.springfileserver.data.compositeKey;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthKey implements Serializable {
    
    private String userId;
    private String userAuth;

}
