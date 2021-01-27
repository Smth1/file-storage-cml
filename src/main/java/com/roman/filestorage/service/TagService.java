package com.roman.filestorage.service;

import com.roman.filestorage.model.File;
import com.roman.filestorage.model.dto.PagedFiles;
import com.roman.filestorage.repository.FileRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.*;

@Service
public class TagService {
    private final FileRepository fileRepository;
    private RestHighLevelClient restHighLevelClient;

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

    public PagedFiles searchtags(String[] tags, int page, int size) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(tags != null) {
            for (String tag : tags) {
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tags", tag);
                boolQueryBuilder.must(termQueryBuilder);
            }
        }
        SearchRequest searchRequest = new SearchRequest("file_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return parseHits(searchResponse.getHits(), page, size);
    }

    public PagedFiles parseHits(SearchHits searchHits, int page, int size) {
        SearchHit[] hits = searchHits.getHits();
        List<File> files = new ArrayList<>();
        PagedFiles pagedFiles = new PagedFiles();

        for (int i=page*size; i < (page + 1)*size && i < hits.length; i ++) {

            Map<String, Object> sourceAsMap = hits[i].getSourceAsMap();
            File file = new File();
            file.setId((String) sourceAsMap.get("id"));
            if (sourceAsMap.get("tags") != null)
                file.setTags(((ArrayList<String>) sourceAsMap.get("tags")).toArray(new String[0]));
            file.setSize((Integer) sourceAsMap.get("size"));
            file.setName((String) sourceAsMap.get("name"));
            files.add(file);
        }
        pagedFiles.setPage(files);
        pagedFiles.setTotal(hits.length);

        return pagedFiles;
    }
}
