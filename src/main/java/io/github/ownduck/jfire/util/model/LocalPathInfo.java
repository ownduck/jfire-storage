package io.github.ownduck.jfire.util.model;

import lombok.Data;

import java.nio.file.Path;

@Data
public class LocalPathInfo {

    private Path rootPath;

    private String parentDirectory;

    private Path parentAbsolutePath;

    private String saveKey;

    private String savePath;

    private Path saveAbsolutePath;
}
