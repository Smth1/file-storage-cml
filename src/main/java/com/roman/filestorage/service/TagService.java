package com.roman.filestorage.service;

import com.roman.filestorage.model.File;
import com.roman.filestorage.model.dto.PagedFiles;
import com.roman.filestorage.repository.FileRepository;

import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final FileRepository fileRepository;
    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public TagService(FileRepository fileRepository, @Qualifier("myClient") RestHighLevelClient restHighLevelClient) {
        this.fileRepository = fileRepository;
        this.restHighLevelClient = restHighLevelClient;
    }

    public ResponseEntity<Model> removeTags(String fileId, String[] tags) {
        Model model = new ExtendedModelMap();
        Optional<File> files = fileRepository.findById(fileId);

        if (files.isPresent()) {
            File file = files.get();

            if (!containsAllTags(file, tags)) {
                model.addAttribute("success", false);
                model.addAttribute("error", "tag not found on file");
                return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
            }

            removeTags(file, tags);
            fileRepository.save(file);
            model.addAttribute("success", true);

            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        } else {
            model.addAttribute("success", false);
            model.addAttribute("error", "file not found");

            return new ResponseEntity<>(model, HttpStatus.NOT_FOUND);
        }
    }

    public boolean containsAllTags(File file, String[] tags) {
        String[] tags1 = file.getTags();
        Set<String> set = new HashSet<>(Arrays.asList(tags));
        Set<String> set1 = new HashSet<>(Arrays.asList(tags1));

        for (String el: set) {
            if (!set1.contains(el))
                return false;
        }
        return true;
    }

    public void removeTags(File file, String[] tags) {
        String[] tags1 = file.getTags();
        Set<String> set = new HashSet<>(Arrays.asList(tags));
        Set<String> set1 = new HashSet<>(Arrays.asList(tags1));

        for (String el: set) {
            set1.remove(el);
        }

        tags1 = set1.toArray(new String[0]);
        file.setTags(tags1);
    }

    public ResponseEntity<PagedFiles> listFiles(String[] tags, int page, int size) throws IOException {
        PagedFiles searchtags = searchtags(tags, page, size);

        return new ResponseEntity<>(searchtags, HttpStatus.OK);
    }

    public PagedFiles searchtags(String[] tags, int page, int size) {
        PagedFiles pagedFiles = new PagedFiles();
        String str = Arrays.stream(tags).map(el -> "\""+el+"\"")
                .collect(Collectors.toList()).toString();

        Page<Object> tagsResponse = fileRepository.findByTagUsingDeclaredQuery(str, PageRequest.of(page, size));

        List<File> filesOnPage = tagsResponse.get().map(el -> (File)el ).collect(Collectors.toList());
        pagedFiles.setTotal((int)tagsResponse.getTotalElements());
        pagedFiles.setPage(filesOnPage);

        return pagedFiles;
    }
}
