package com.roman.filestorage.controller;

import com.roman.filestorage.model.dto.PagedFiles;
import com.roman.filestorage.model.dto.UploadRequest;
import com.roman.filestorage.service.FileService;
import com.roman.filestorage.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    private final TagService tagService;

    @Autowired
    public FileController(FileService fileService, TagService tagService) {
        this.fileService = fileService;
        this.tagService = tagService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Model> uploadFile(@RequestBody UploadRequest uploadRequest) {
        return fileService.uploadFile(uploadRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Model> deleteFile(@PathVariable String id) {
        return fileService.deleteFile(id);
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Model> assignTags(@RequestBody String[] tags, @PathVariable String id) {
        return fileService.assignTags(id, tags);
    }

    @DeleteMapping("/{id}/tags")
    public ResponseEntity<Model> removeTags(@RequestBody String[] tags, @PathVariable String id) {
        return tagService.removeTags(id, tags);
    }

    @GetMapping()
    public ResponseEntity<PagedFiles> listFiles(@RequestParam(required = false) String[] tags,
                                                @RequestParam(required = false, defaultValue = "0") Integer page,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) throws IOException {
        return tagService.listFiles(tags, page, size);
    }
}
