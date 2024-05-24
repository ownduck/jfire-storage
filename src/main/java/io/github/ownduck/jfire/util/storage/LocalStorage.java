package io.github.ownduck.jfire.util.storage;

import io.github.ownduck.jfire.util.Storage;
import io.github.ownduck.jfire.util.config.DirectoryConfig;
import io.github.ownduck.jfire.util.model.StorageResult;
import io.github.ownduck.jfire.util.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.BiFunction;

public class LocalStorage extends Storage {

    private static final Logger log = LogManager.getLogger(LocalStorage.class);

    private DirectoryConfig config;

    public LocalStorage(DirectoryConfig config) {
        this.config = config;
    }

    @Override
    public StorageResult upload(InputStream inputStream,String fileName) {
        try{
            long startTime = System.currentTimeMillis();
            byte[] data = inputStream.readAllBytes();
            if (data.length == 0){
                throw new IOException("上传内容为空");
            }

            BiFunction<String,byte[],String> saver = config.getHandler((path)->{
                try {
                    forceMakeDirectory(path.getParent());
                    File saveFile = path.toFile();
                    FileUtils.writeByteArrayToFile(saveFile,data);
                    return saveFile.exists() && saveFile.length() > 0;
                } catch (IOException e) {
                    return false;
                }
            });
            String savedPath = saver.apply(fileName,data);
            if (StringUtils.isBlank(savedPath)){
                throw new IOException("保存失败");
            }
//            System.out.println("saved to path "+savedPath);

            long spendTime = System.currentTimeMillis() - startTime;
            return successResult(savedPath,spendTime);
        }catch (Exception e){
            log.error("upload error {}",e.getMessage());
            return errorResult(e.getMessage());
        }
    }

    private void forceMakeDirectory(Path path) throws IOException {
        String absolutePath = path.toAbsolutePath().toString();
        if (!FileUtil.mkdir(absolutePath)){
            throw new IOException("创建目录失败");
        }
        if (!FileUtil.setWritable(absolutePath)){
            throw new IOException("目录不可写");
        }
    }

    @Override
    public InputStream get(String key) {
        try {
            File file = config.resolve(key);
            if (file == null){
                return null;
            }
            return new FileInputStream(file);
        } catch (Exception e) {
            log.error("{} get error {}",LocalStorage.class.getSimpleName(),e.getMessage());
            return null;
        }
    }
}
