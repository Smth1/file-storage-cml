package com.roman.filestorage.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

@Document(indexName = "file_index", createIndex = false)
public class File {
    @Id
    String id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(type = FieldType.Integer, name = "size")
    Integer size;

    @Field(type = Keyword)
    private String[] tags;

    public File() {
    }

    public File(String name, int size) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
