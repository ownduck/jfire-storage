package io.github.ownduck.jfire.util.config;

import io.github.ownduck.jfire.util.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LocalConfig extends Config {

    private Path rootDirectory;

    public LocalConfig(String rootDirectory) {
        this(rootDirectory,true);
    }

    public LocalConfig(String rootDirectory, boolean useDateSubDirectory) {
        this.rootDirectory = Paths.get(rootDirectory).toAbsolutePath();
        useDateSubDirectory(useDateSubDirectory);
    }

    public BiFunction<String,byte[],String> getHandler(Function<Path,Boolean> saver){
        return (saveKey,data)->{
            String parentDirectory = "";
            if (useDateSubDirectory){
                parentDirectory += TimeUtil.toString(new Date(),dateSubDirectoryFormat)+"/";
            }
            Path rootPath = rootDirectory;
            Path parentAbsolutePath = rootPath.resolve(parentDirectory);
            Path saveAbsolutePath = parentAbsolutePath.resolve(saveKey);
            Boolean saved = saver.apply(saveAbsolutePath);
            return saved ? "/" + parentDirectory + saveKey : null;
        };
    }

    public File resolve(String key){
        String path = StringUtils.strip(key,"/\\");
        Path rootPath = rootDirectory;
        Path savedPath = rootPath.resolve(path).toAbsolutePath();
        File file = savedPath.toFile();
        if (!file.isFile()){
            return null;
        }
        return file;
    }
}
