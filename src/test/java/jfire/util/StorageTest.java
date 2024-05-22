package jfire.util;

import jfire.util.config.DirectoryConfig;
import jfire.util.model.StorageResult;

/**
 * Unit test for simple App.
 */
public class StorageTest
    extends CommonCase
{

    public void testStorage()
    {
        DirectoryConfig config = new DirectoryConfig("F:\\data\\var\\upload");
        config.useOriginalFilename(false);
        Storage storage = Storage.localStorage(config);
        StorageResult result = storage.upload("F:\\data\\tmp\\media\\god_bless.webp");
        assertTrue( "本地上传测试",result.getOk());
        assertNotBlank("本地上传测试",result.getSavedPath());
        System.out.println("saved to "+result.getSavedPath());
    }

}
