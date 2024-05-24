package io.github.ownduck.jfire.util.storage;

import io.github.ownduck.jfire.util.Storage;
import io.github.ownduck.jfire.util.config.RedisConfig;
import io.github.ownduck.jfire.util.model.StorageResult;
import io.github.ownduck.jfire.util.util.EncryptUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;

public class RedisStorage extends Storage {

    private static final Logger log = LogManager.getLogger(RedisStorage.class);

    private RedisConfig config;

    public RedisStorage(RedisConfig config) {
        this.config = config;
    }

    @Override
    public StorageResult upload(InputStream inputStream,String fileName) {
        try{
            Long startTime = System.currentTimeMillis();
            byte[] data = inputStream.readAllBytes();
            if (data.length == 0){
                throw new IOException("上传内容为空");
            }

            BiFunction<String,byte[],String> saver = config.getHandler((commands,cacheKey)->{
                try {
                    String value = EncryptUtil.base64encode(data);
                    String result = commands.set(cacheKey,value);
                    return "OK".equals(result);
                } catch (Exception e) {
                    return false;
                }
            });
            String savedPath = saver.apply(fileName,data);
            if (StringUtils.isBlank(savedPath)){
                throw new IOException("保存失败");
            }
//            System.out.println("saved to path "+savedPath);

            Long spendTime = System.currentTimeMillis() - startTime;
            return successResult(savedPath,spendTime);
        }catch (Exception e){
            log.error("upload error {}",e.getMessage());
            return errorResult(e.getMessage());
        }
    }

    @Override
    public InputStream get(String key) {
        try {
            byte[] data = config.resolve(key);
            if (data == null){
                return null;
            }
            return new ByteArrayInputStream(data);
        } catch (Exception e) {
            log.error("{} get error {}",RedisStorage.class.getSimpleName(),e.getMessage());
            return null;
        }
    }
}
