package com.roman.filestorage.service;

import com.roman.filestorage.model.File;
import com.roman.filestorage.model.dto.UploadRequest;
import com.roman.filestorage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String uploadFile(UploadRequest uploadRequest) {
        File file = new File(uploadRequest.getName(), uploadRequest.getSize());
        file.setName(uploadRequest.getName());
        file.setSize(uploadRequest.getSize());

        fileRepository.save(file);

        return file.getId();
    }

    public ResponseEntity<Model> deleteFile(String id) {
        Model model = new ExtendedModelMap();
        Optional<File> files = fileRepository.findById(id);

        if (files.isPresent()) {

            fileRepository.delete(files.get());

            model.addAttribute("success", true);

            return new ResponseEntity<>(model, HttpStatus.OK);
        } else {
            model.addAttribute("success", false);
            model.addAttribute("error", "file not found");

            return new ResponseEntity<>(model, HttpStatus.NOT_FOUND);
        }
    }

    public void assignTags(String fileId, String[] tags) {
        Optional<File> files = fileRepository.findById(fileId);
        if (files.isPresent()) {
            File file = files.get();
            String[] tags1 = file.getTags();
            if (tags1 != null) {
                String[] newTags = new String[tags.length + tags1.length];
                System.arraycopy(tags1, 0, newTags, 0, tags1.length);
                System.arraycopy(tags, 0, newTags, tags1.length, tags.length);
                file.setTags(newTags);
            } else {
                file.setTags(tags);
            }
            fileRepository.save(file);
        }
    }
}
