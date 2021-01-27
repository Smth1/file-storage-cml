package com.roman.filestorage.model.dto;

import com.roman.filestorage.model.File;

import java.util.List;

public class PagedFiles {
    int total;
    List<File> page;

    public PagedFiles() {
    }

    public PagedFiles(int total, List<File> page) {
        this.total = total;
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<File> getPage() {
        return page;
    }

    public void setPage(List<File> page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "PagedFiles{" +
                "total=" + total +
                ", page=" + page +
                '}';
    }
}
