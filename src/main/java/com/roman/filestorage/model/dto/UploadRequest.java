package com.roman.filestorage.model.dto;

public class UploadRequest {
    String name;
    int size;

    public UploadRequest() {
    }

    public UploadRequest(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
