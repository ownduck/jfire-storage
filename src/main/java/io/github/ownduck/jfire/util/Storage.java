package io.github.ownduck.jfire.util;

import io.github.ownduck.jfire.util.config.LocalConfig;
import io.github.ownduck.jfire.util.config.RedisConfig;
import io.github.ownduck.jfire.util.model.StorageResult;
import io.github.ownduck.jfire.util.storage.LocalStorage;
import io.github.ownduck.jfire.util.storage.RedisStorage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class Storage {

    private static final Logger log = LogManager.getLogger(Storage.class);

    protected Storage() {
    }

    public static final Storage localStorage(LocalConfig config){
        return new LocalStorage(config);
    }

    public static final Storage redisStorage(RedisConfig config){
        return new RedisStorage(config);
    }

    protected StorageResult successResult(String savedPath,Long spendTime){
        return StorageResult.builder()
                .ok(true)
                .savedPath(savedPath)
                .spendTime(spendTime)
                .message("ok")
                .build();
    }

    protected StorageResult errorResult(String message){
        return StorageResult.builder()
                .ok(false)
                .message(message)
                .build();
    }

    public StorageResult upload(MultipartFile file) {
        try(
                InputStream is = file.getInputStream();
        ){
            return upload(is,file.getOriginalFilename());
        }catch (Exception e){
            log.error("upload error {}",e.getMessage());
            return errorResult(e.getMessage());
        }
    }

    public StorageResult upload(String filePath) {
        try{
            if (StringUtils.isBlank(filePath)){
                throw new IOException("文件路径为空");
            }
            return upload(new File(filePath));
        }catch (Exception e){
            log.error("upload error {}",e.getMessage());
            return errorResult(e.getMessage());
        }
    }

    public StorageResult upload(File file) {
        try(
                FileInputStream fis = new FileInputStream(file);
        ){
            return upload(fis, file.getName());
        }catch (Exception e){
            log.error("upload error {}",e.getMessage());
            return errorResult(e.getMessage());
        }
    }

    public StorageResult upload(InputStream inputStream) {
        return upload(inputStream,null);
    }

    public abstract StorageResult upload(InputStream inputStream,String originalFilename);

    public abstract InputStream get(String key);
}
