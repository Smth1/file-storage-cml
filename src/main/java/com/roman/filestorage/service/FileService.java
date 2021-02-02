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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public ResponseEntity<Model> uploadFile(UploadRequest uploadRequest) {
        Model model = new ExtendedModelMap();

        if (uploadRequest.getName() != null
                && uploadRequest.getName().length() > 0
                && uploadRequest.getSize() >= 0) {
            String id = saveFile(uploadRequest);

            model.addAttribute("ID", id);

            return new ResponseEntity<>(model, HttpStatus.OK);
        } else {

            if (uploadRequest.getName() == null || uploadRequest.getName().length() == 0) {
                model.addAttribute("error", "empty file name");
            }

            if (uploadRequest.getSize() < 0) {
                model.addAttribute("error", "the file size must be positive");
            }

            model.addAttribute("success", "false");

            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
    }

    public String saveFile(UploadRequest uploadRequest) {
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

    public ResponseEntity<Model> assignTags(String fileId, String[] tags) {
        Optional<File> files = fileRepository.findById(fileId);
        Set<String> tagSet = new HashSet<>(Arrays.asList(tags));
        Model model = new ExtendedModelMap();

        if (files.isPresent()) {
            File file = files.get();
            String[] tags1 = file.getTags();
            if (tags1 != null) {
                tagSet.addAll(Arrays.asList(tags1));
            }
            file.setTags(tagSet.toArray(new String[0]));
            fileRepository.save(file);
            model.addAttribute("success", true);

            return new ResponseEntity<Model>(model, HttpStatus.OK);
        } else {
            model.addAttribute("error", "file not exist");

            return new ResponseEntity<>(model, HttpStatus.NOT_FOUND);
        }
    }
}
