package io.github.ownduck.jfire.util.config;

import io.github.ownduck.jfire.util.model.LocalPathInfo;
import io.github.ownduck.jfire.util.util.TimeUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class DirectoryConfig extends Config {

    private Path rootDirectory;

    public DirectoryConfig(String rootDirectory) {
        this(rootDirectory,true);
    }

    public DirectoryConfig(String rootDirectory, boolean useDateSubDirectory) {
        this.rootDirectory = Paths.get(rootDirectory).toAbsolutePath();
        useDateSubDirectory(useDateSubDirectory);
    }

    public LocalPathInfo makeSavePath(String originalFilename, byte[] data){
        Path rootPath = rootDirectory;
        String parentDirectory = "";
        if (useDateSubDirectory){
            parentDirectory += TimeUtil.toString(new Date(),dateSubDirectoryFormat)+"/";
        }
        Path parentAbsolutePath = rootPath.resolve(parentDirectory);
        String saveKey = makeSaveName(originalFilename,data);
        Path saveAbsolutePath = parentAbsolutePath.resolve(saveKey);

        LocalPathInfo pathInfo = new LocalPathInfo();
        pathInfo.setRootPath(rootPath);
        pathInfo.setParentDirectory(parentDirectory);
        pathInfo.setParentAbsolutePath(parentAbsolutePath);
        pathInfo.setSaveKey(saveKey);
        pathInfo.setSavePath("/"+parentDirectory+saveKey);
        pathInfo.setSaveAbsolutePath(saveAbsolutePath);
        return pathInfo;
    }
}
