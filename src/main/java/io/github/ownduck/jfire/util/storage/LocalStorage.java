package io.github.ownduck.jfire.util.storage;

import io.github.ownduck.jfire.util.model.LocalPathInfo;
import io.github.ownduck.jfire.util.Storage;
import io.github.ownduck.jfire.util.config.DirectoryConfig;
import io.github.ownduck.jfire.util.model.StorageResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LocalStorage extends Storage {

    private static final Logger log = LogManager.getLogger(LocalStorage.class);

    private DirectoryConfig config;

    public LocalStorage(DirectoryConfig config) {
        this.config = config;
    }

    private StorageResult successResult(String savedPath,Long spendTime){
        return StorageResult.builder()
                .ok(true)
                .savedPath(savedPath)
                .spendTime(spendTime)
                .message("ok")
                .build();
    }

    private StorageResult errorResult(String message){
        return StorageResult.builder()
                .ok(false)
                .message(message)
                .build();
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public StorageResult upload(InputStream inputStream) {
        return upload(inputStream,null);
    }

    @Override
    public StorageResult upload(InputStream inputStream,String fileName) {
        try{
            Long startTime = System.currentTimeMillis();
            byte[] data = inputStream.readAllBytes();
            if (data.length == 0){
                throw new IOException("上传内容为空");
            }

            LocalPathInfo pathInfo = config.makeSavePath(fileName,data);
            forceMakeDirectory(pathInfo.getParentAbsolutePath());
            File saveFile = pathInfo.getSaveAbsolutePath().toFile();
            FileUtils.writeByteArrayToFile(saveFile,data);
            if (!saveFile.exists() || saveFile.length() == 0){
                throw new IOException("保存失败");
            }
//            System.out.println("saved to absolute path "+saveFile);

            Long spendTime = System.currentTimeMillis() - startTime;
            return successResult(pathInfo.getSavePath(),spendTime);
        }catch (Exception e){
            log.error("upload error {}",e.getMessage());
            return errorResult(e.getMessage());
        }
    }
}
