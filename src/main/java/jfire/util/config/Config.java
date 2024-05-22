package jfire.util.config;

import jfire.util.util.FileUtil;
import jfire.util.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;

public abstract class Config {

    protected Boolean useOriginalFilename = false;

    protected boolean useDateSubDirectory = true;

    protected String dateSubDirectoryFormat = "yyyyMMdd";

    public void useOriginalFilename(Boolean use) {
        this.useOriginalFilename = use;
    }

    public Boolean isUseOriginalFilename() {
        return useOriginalFilename;
    }

    public void useDateSubDirectory(Boolean use) {
        this.useDateSubDirectory = use;
    }

    public void setDateSubDirectoryFormat(String dateSubDirectoryFormat) {
        this.dateSubDirectoryFormat = dateSubDirectoryFormat;
    }

    public String makeSaveName(String originalFilename,byte[] data){
        if (useOriginalFilename && StringUtils.isNotBlank(originalFilename)){
            return originalFilename;
        }
        String extension = "";
        if (data != null && data.length > 0){
            String contentType = FileUtil.getMimeType(data);
            extension = FileUtil.mime2ext(contentType);
            extension = StringUtils.isNotBlank(extension) ? "."+extension : "";
        }
        String filename = RandomUtil.getRandomStr(25,"ALL");
        return filename + extension;
    }

    public String makeSaveName(String originalFilename){
        return makeSaveName(originalFilename,null);
    }

}
