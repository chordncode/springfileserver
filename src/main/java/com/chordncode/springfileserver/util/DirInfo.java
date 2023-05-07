package com.chordncode.springfileserver.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirInfo {
    
    private Long dirId;
    private String dirName;
    private Long parentDirId;

}
