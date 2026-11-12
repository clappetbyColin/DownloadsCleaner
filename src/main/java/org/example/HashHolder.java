package org.example;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.List;
import java.nio.file.Path;

public class HashHolder {
    private final String contentHash;
    private final Path path;
    private final Long size;
    public HashHolder(String contentHash, Path path, Long size){
        this.contentHash = contentHash;
        this.path = path;
        this.size = size;
    }
    public String getContentHash() {
        return contentHash;
    }
    public Path getPath() {
        return path;
    }
    public Long getSize() {
        return size;
    }
    public String getName() {
        return path.getFileName().toString();
    }
}
