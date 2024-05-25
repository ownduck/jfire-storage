package io.github.ownduck.jfire.util.config;

import io.github.ownduck.jfire.util.util.EncryptUtil;
import io.github.ownduck.jfire.util.util.TimeUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StaticCredentialsProvider;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Date;
import java.util.function.BiFunction;

public class RedisConfig extends Config {

    private String host;

    private Integer port = 6379;

    private String password;

    private Integer database = 0;

    private Duration timeout = Duration.ofMillis(10000L);

    private RedisStringCommands<String, String> commands;

    public RedisConfig(String host,Integer port) {
        this(host,port,null,0,true);
    }

    public RedisConfig(String host,Integer port,String password) {
        this(host,port,password,0,true);
    }

    public RedisConfig(String host,Integer port,String password,Integer database) {
        this(host,port,password,database,true);
    }

    public RedisConfig(String host,Integer port,String password,Integer database, boolean useDateSubDirectory) {
        this.host = host;
        this.port = port != null ? port : 6379;
        this.password = password;
        this.database = database != null ? database : 0;
        useDateSubDirectory(useDateSubDirectory);
        this.init();
    }

    private void init(){
        RedisURI uri = RedisURI.create(host,port);
        uri.setTimeout(timeout);
        uri.setDatabase(database);
        if (StringUtils.isNotBlank(password)){
            uri.setCredentialsProvider(new StaticCredentialsProvider(null,password.toCharArray()));
        }
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStringCommands<String, String> commands = connection.sync();
        this.commands = commands;
    }

    private String getCacheKey(String saveKey) {
        return "jfire/storage/"+saveKey;
    }

    public BiFunction<String,byte[],String> getHandler(BiFunction<RedisStringCommands<String, String>,String,Boolean> saver){
        return (saveKey,data)->{
            String parentDirectory = "";
            if (useDateSubDirectory){
                parentDirectory += TimeUtil.toString(new Date(),dateSubDirectoryFormat)+"/";
            }
            String savePath = parentDirectory + saveKey;
            String cacheKey = getCacheKey(savePath);
            Boolean saved = saver.apply(commands,cacheKey);
            return saved ? "/" + savePath : null;
        };
    }

    public byte[] resolve(String key){
        String path = StringUtils.strip(key,"/\\");
        String cacheKey = getCacheKey(path);
        String value = commands.get(cacheKey);
        byte[] data = EncryptUtil.base64decode(value);
        return data;
    }
}
