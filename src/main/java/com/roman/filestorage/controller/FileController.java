package com.roman.filestorage.controller;

import com.roman.filestorage.model.dto.UploadRequest;
import com.roman.filestorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<Model> uploadFile(@RequestBody UploadRequest uploadRequest) {
        Model model = new ConcurrentModel();

        if (uploadRequest.getName() != null && uploadRequest.getSize() > 0) {
            fileService.uploadFile(uploadRequest);

            model.addAttribute("ID", "unique file id");

            return new ResponseEntity<>(model, HttpStatus.OK);
        } else {

            model.addAttribute("success", "false");
            model.addAttribute("error", "error description");

            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Model> deleteFile(@PathVariable String id) {
        Model model = new ConcurrentModel();

        return null;
    }

    @PostMapping("/{id}/tags")
    public String assignTags() {
        return "oiwer";
    }

    @DeleteMapping("/{id}/tags")
    public String removeTags()
    {
        return "kjlkjd";
    }

    @GetMapping()
    public String listFiles() {
        return "lkjflkdfj";
    }
}
