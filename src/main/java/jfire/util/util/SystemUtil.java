package jfire.util.util;

import com.sun.jna.Platform;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class SystemUtil {

    private static final Logger log = LoggerFactory.getLogger(SystemUtil.class);

    private static final String JOIN_DELIMITER = ";;";

    private static final String USER_DIR = System.getProperties().getProperty("user.dir");

    public static OsEnum OS = OsEnum.OTHER;

    public static OsArchEnum ARCH = OsArchEnum.X64;

    public enum OsEnum {
        WINDOWS,
        LINUX,
        OTHER;
    }

    public enum OsArchEnum {
        X64,
        X86,
    }

    static {
        String osName = System.getProperties().getProperty("os.name").toLowerCase();
        if (osName.contains("linux")){
            OS = OsEnum.LINUX;
        }else if (osName.contains("win")){
            OS = OsEnum.WINDOWS;
        }else if (Platform.isLinux()){
            OS = OsEnum.LINUX;
        }else if (Platform.isWindows()){
            OS = OsEnum.WINDOWS;
        }

        if (Platform.is64Bit()){
            ARCH = OsArchEnum.X64;
        }else if (StringUtils.contains(System.getProperties().getProperty("os.arch"),"64")){
            ARCH = OsArchEnum.X64;
        }else{
            ARCH = OsArchEnum.X86;
        }

        if (OS.equals(OsEnum.WINDOWS)){
            File icejniDllFile = new File(USER_DIR,"ICE_JNIRegistry.dll");
            if (!icejniDllFile.isFile()){
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if(ARCH.equals(OsArchEnum.X64)){ //判断是否为64位系统
                        InputStream is = classLoader.getResourceAsStream("dll/ICE_JNIRegistry64.dll");
                        FileUtils.copyInputStreamToFile(is,icejniDllFile);
                    }else{
                        InputStream is = classLoader.getResourceAsStream("dll/ICE_JNIRegistry32.dll");
                        FileUtils.copyInputStreamToFile(is,icejniDllFile);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            }
        }

    }

    public static Boolean isOs(OsEnum os){
        return OS.equals(os);
    }

    public static Boolean isLinux(){
        return isOs(OsEnum.LINUX);
    }

    public static Boolean isWindows(){
        return isOs(OsEnum.WINDOWS);
    }

}
