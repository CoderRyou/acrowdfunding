package com.atguigu.atcrowdfunding.utils;

public class Page {
    Integer pageNo;
    Integer pageSize;
    Integer startIndex;
    Integer totalSize;

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Page(Integer pageNo, Integer pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.startIndex = (pageNo-1)*pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getStartIndex() {
        return startIndex;
    }
}
