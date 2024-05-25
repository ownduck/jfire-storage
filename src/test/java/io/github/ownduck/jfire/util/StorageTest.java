package io.github.ownduck.jfire.util;

import io.github.ownduck.jfire.util.config.LocalConfig;
import io.github.ownduck.jfire.util.config.RedisConfig;
import io.github.ownduck.jfire.util.model.StorageResult;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unit test for simple App.
 */
public class StorageTest
    extends CommonCase
{

    public void testLocalStorage()
    {
        LocalConfig config = new LocalConfig("F:\\data\\var\\upload");
        config.useOriginalFilename(false);
        Storage storage = Storage.localStorage(config);
        StorageResult result = storage.upload("F:\\data\\tmp\\media\\god_bless.webp");
        assertTrue( "本地上传测试 ok",result.getOk());
        assertNotBlank("本地上传测试 savedPath",result.getSavedPath());
        System.out.println("saved to "+result.getSavedPath());

        if (result.getOk()){
            try {
                String key = result.getSavedPath();
                InputStream inputStream = storage.get(key);
                assertTrue( "本地上传测试 length",inputStream.available() > 0);
                System.out.println("saved length "+inputStream.available());
            } catch (IOException e) {
                //
            }
        }
    }

    public void testRedisStorage()
    {
        RedisConfig config = new RedisConfig("localhost",6379);
        Storage storage = Storage.redisStorage(config);
        StorageResult result = storage.upload("F:\\data\\tmp\\media\\god_bless.webp");
        assertTrue( "本地上传测试 ok",result.getOk());
        assertNotBlank("本地上传测试 savedPath",result.getSavedPath());
        System.out.println("saved to "+result.getSavedPath());

        if (result.getOk()){
            try {
                String key = result.getSavedPath();
                InputStream inputStream = storage.get(key);
                assertTrue( "本地上传测试 length",inputStream.available() > 0);
                System.out.println("saved length "+inputStream.available());
            } catch (IOException e) {
                //
            }
        }
    }

}
