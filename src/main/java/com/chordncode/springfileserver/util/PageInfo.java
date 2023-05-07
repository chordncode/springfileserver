package com.chordncode.springfileserver.util;

import java.util.List;

import lombok.Data;

@Data
public class PageInfo<T> {
    
    private long currentPage;
    private long rowSize;
    private long pageSize;

    private long totalRows;
    private long startRow;
    private long endRow;

    private long totalPages;
    private long startPage;
    private long endPage;

    private List<T> contentList;

    private DirInfo dirInfo;

    public PageInfo(DirInfo dirInfo, long currentPage, long totalRow, long rowSize, long pageSize){

        this.dirInfo = dirInfo;

        this.currentPage = currentPage;
        this.totalRows = totalRow;
        this.rowSize = rowSize;
        this.pageSize = pageSize;

        this.startRow = (this.currentPage - 1) * this.rowSize + 1;
        this.endRow = this.startRow + this.rowSize - 1;
        if(this.endRow > this.totalRows) this.endRow = this.totalRows;

        this.totalPages = (long) Math.ceil((double) this.totalRows / this.rowSize);
        this.startPage = (this.currentPage - 1) / this.pageSize * this.pageSize + 1;
        this.endPage = this.startPage + this.pageSize  - 1;
        if(this.endPage > this.totalPages) this.endPage = this.totalPages;

    }

}
