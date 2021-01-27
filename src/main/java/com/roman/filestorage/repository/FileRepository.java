package com.roman.filestorage.repository;

import com.roman.filestorage.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface FileRepository extends ElasticsearchRepository<File, String> {
    Optional<File> findById(String id);
}
