package io.github.ownduck.jfire.util;

import io.github.ownduck.jfire.util.storage.LocalStorage;
import io.github.ownduck.jfire.util.config.DirectoryConfig;
import io.github.ownduck.jfire.util.model.StorageResult;
import io.github.ownduck.jfire.util.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class Storage {

    protected Storage() {
    }

    public static final Storage localStorage(DirectoryConfig config){
        return new LocalStorage(config);
    }

    protected void forceMakeDirectory(Path path) throws IOException {
        String absolutePath = path.toAbsolutePath().toString();
        if (!FileUtil.mkdir(absolutePath)){
            throw new IOException("创建目录失败");
        }
        if (!FileUtil.setWritable(absolutePath)){
            throw new IOException("目录不可写");
        }
    }

    public abstract StorageResult upload(MultipartFile file);

    public abstract StorageResult upload(String filePath);

    public abstract StorageResult upload(File file);

    public abstract StorageResult upload(InputStream inputStream);

    public abstract StorageResult upload(InputStream inputStream,String fileName);
}
