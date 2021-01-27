package com.roman.filestorage.controller;

import com.roman.filestorage.model.dto.PagedFiles;
import com.roman.filestorage.model.dto.UploadRequest;
import com.roman.filestorage.service.FileService;
import com.roman.filestorage.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
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
        Model model = new ExtendedModelMap();

        if (uploadRequest.getName() != null && uploadRequest.getSize() > 0) {
            String id = fileService.uploadFile(uploadRequest);

            model.addAttribute("ID", id);

            return new ResponseEntity<>(model, HttpStatus.OK);
        } else {

            model.addAttribute("success", "false");
            model.addAttribute("error", "error description");

            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Model> deleteFile(@PathVariable String id) {
        ResponseEntity<Model> response = fileService.deleteFile(id);
        return response;
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<Model> assignTags(@RequestBody String[] tags, @PathVariable String id) {
        Model model = new ExtendedModelMap();
        fileService.assignTags(id, tags);
        model.addAttribute("success", true);
        return new ResponseEntity<Model>(model, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/tags")
    public ResponseEntity<Model> removeTags(@RequestBody String[] tags, @PathVariable String id)
    {
        return tagService.removeTags(id, tags);
    }

    @GetMapping()
    public ResponseEntity<PagedFiles> listFiles(@RequestParam(required = false) String[] tags,
                                                @RequestParam(required = false, defaultValue = "0") Integer page,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) throws IOException {
        return tagService.listFiles(tags, page, size);
    }
}
