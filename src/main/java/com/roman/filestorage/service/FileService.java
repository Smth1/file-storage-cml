package com.roman.filestorage.service;

import com.roman.filestorage.model.File;
import com.roman.filestorage.model.dto.UploadRequest;
import com.roman.filestorage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void uploadFile(UploadRequest uploadRequest) {
        File file = new File();
        file.setName(uploadRequest.getName());
        file.setSize(uploadRequest.getSize());

        File save = fileRepository.save(file);
    }
}
