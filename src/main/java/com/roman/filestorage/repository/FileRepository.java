package com.roman.filestorage.repository;

import com.roman.filestorage.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FileRepository extends ElasticsearchRepository<File, String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
    Page<File> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);
}
