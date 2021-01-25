package com.roman.filestorage.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "file_index")
public class File {
    @Id
    String id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(type = FieldType.Integer, name = "size")
    int size;

    public File() {
    }

    public File(String name, int size) {
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
}
