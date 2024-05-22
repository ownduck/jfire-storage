package jfire.util.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FileUtil {

    private static final Logger log = LogManager.getLogger(FileUtil.class);

    private static final Map<String,String> mimes = CollectionUtil.ofMap(
            "application/internet-property-stream", "acx",
            "application/postscript", "ai eps ps",
            "audio/x-aiff", "aif aifc aiff",
            "video/x-ms-asf", "asf asr asx lsf lsx",
            "audio/basic", "au snd",
            "video/x-msvideo", "avi",
            "application/olescript", "axs",
            "text/plain", "bas c h txt asc",
            "application/x-bcpio", "bcpio",
            "application/octet-stream", "bin class dms exe lha lzh ani avb bpk cur dll dmg ico tad ttf",
            "image/bmp", "bmp",
            "application/vnd.ms-pkiseccat", "cat",
            "application/x-cdf", "cdf",
            "application/x-x509-ca-cert", "cer crt der",
            "application/x-msclip", "clp",
            "image/x-cmx", "cmx",
            "image/cis-cod", "cod",
            "application/x-cpio", "cpio",
            "application/x-mscardfile", "crd",
            "application/pkix-crl", "crl",
            "application/x-csh", "csh",
            "text/css", "css",
            "application/x-director", "dcr dir dxr",
            "application/x-msdownload", "dll",
            "application/msword", "doc dot",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx",
            "application/x-dvi", "dvi",
            "text/x-setext", "etx",
            "application/envoy", "evy",
            "application/fractals", "fif",
            "x-world/x-vrml", "flr vrml wrl wrz xaf xof",
            "image/gif", "gif ifm",
            "application/x-gtar", "gtar",
            "application/x-gzip", "gz x-gzip",
            "application/x-hdf", "hdf",
            "application/winhlp", "hlp",
            "application/mac-binhex40", "hqx",
            "application/hta", "hta",
            "text/x-component", "htc",
            "text/html", "htm html stm dhtml hts",
            "text/webviewhtml", "htt",
            "image/x-icon", "ico",
            "image/ief", "ief",
            "application/x-iphone", "iii",
            "application/x-internet-signup", "ins isp",
            "image/pipeg", "jfif",
            "image/jpeg", "jpeg jpg jpe jpz",
            "image/pjpeg", "jpg",
            "application/x-javascript", "js",
            "application/x-latex", "latex",
            "video/x-la-asf", "lsf lsx",
            "application/x-msmediaview", "m13 m14 mvb",
            "audio/x-mpegurl", "m3u m3url",
            "application/x-troff-man", "man",
            "application/x-msaccess", "mdb",
            "application/x-troff-me", "me",
            "message/rfc822", "mht mhtml nws",
            "audio/mid", "mid rmi",
            "application/x-msmoney", "mny",
            "video/quicktime", "mov qt",
            "video/x-sgi-movie", "movie",
            "video/mpeg", "mp2 mpa mpe mpeg mpg mpv2",
            "audio/mpeg", "mp3 mpga",
            "application/vnd.ms-project", "mpp",
            "application/x-troff-ms", "ms",
            "application/oda", "oda",
            "application/pkcs10", "p10",
            "application/x-pkcs12", "p12 pfx",
            "application/x-pkcs7-certificates", "p7b spc",
            "application/x-pkcs7-mime", "p7c p7m",
            "application/x-pkcs7-certreqresp", "p7r",
            "application/x-pkcs7-signature", "p7s",
            "image/x-portable-bitmap", "pbm",
            "application/pdf", "pdf",
            "image/x-portable-graymap", "pgm",
            "application/ynd.ms-pkipko", "pko",
            "application/x-perfmon", "pma pmc pml pmr pmw",
            "image/x-png", "png",
            "image/x-portable-anymap", "pnm",
            "application/vnd.ms-powerpoint", "pot pps ppt pot",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx",
            "image/x-portable-pixmap", "ppm",
            "application/pics-rules", "prf",
            "application/x-mspublisher", "pub",
            "audio/x-pn-realaudio", "ra ram rm rmm rmvb",
            "image/x-cmu-raster", "ras",
            "image/x-rgb", "rgb",
            "application/x-troff", "roff t tr",
            "application/rtf", "rtf",
            "text/richtext", "rtx",
            "application/x-msschedule", "scd",
            "text/scriptlet", "sct",
            "application/set-payment-initiation", "setpay",
            "application/set-registration-initiation", "setreg",
            "application/x-sh", "sh",
            "application/x-shar", "shar",
            "application/x-stuffit", "sit sea",
            "application/futuresplash", "spl",
            "application/x-wais-source", "src",
            "application/vnd.ms-pkicertstore", "sst",
            "application/vnd.ms-pkistl", "stl",
            "application/x-sv4cpio", "sv4cpio",
            "application/x-sv4crc", "sv4crc",
            "application/x-tar", "tar taz tgz",
            "application/x-tcl", "tcl",
            "application/x-tex", "tex",
            "application/x-texinfo", "texi texinfo",
            "application/x-compressed", "tgz",
            "image/tiff", "tif tiff",
            "application/x-msterminal", "trm",
            "text/tab-separated-values", "tsv",
            "text/iuls", "uls",
            "application/x-ustar", "ustar",
            "text/x-vcard", "vcf",
            "audio/x-wav", "wav",
            "application/vnd.ms-works", "wcm wdb wks wps",
            "application/x-msmetafile", "wmf",
            "application/x-mswrite", "wri",
            "image/x-xbitmap", "xbm",
            "application/vnd.ms-excel", "xla xlc xlm xls xlt xlw",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx",
            "image/x-xpixmap", "xpm",
            "image/x-xwindowdump", "xwd",
            "application/x-compress", "z",
            "application/zip", "zip nar",
            "application/x-zip-compressed", "zip",
            "application/vnd.lotus-1-2-3", "123",
            "video/3gpp", "3gp",
            "application/x-authoware-bin", "aab",
            "application/x-authoware-map", "aam",
            "application/x-authoware-seg", "aas",
            "audio/X-Alpha5", "als",
            "application/x-mpeg", "amc",
            "application/astound", "asd asn",
            "application/x-asap", "asp",
            "audio/amr-wb", "awb",
            "application/bld", "bld",
            "application/bld2", "bld2",
            "application/x-MS-bmp", "bmp",
            "application/x-bzip2", "bz2",
            "image/x-cals", "cal mil",
            "application/x-cnc", "ccn",
            "application/x-cocoa", "cco",
            "application/x-netcdf", "cdf nc",
            "magnus-internal/cgi", "cgi",
            "application/x-chat", "chat",
            "application/x-cmx", "cmx",
            "application/x-cult3d-object", "co",
            "application/mac-compactpro", "cpt",
            "chemical/x-csml", "csm csml",
            "x-lml/x-evm", "dcm evm",
            "image/x-dcx", "dcx",
            "application/x-dot", "dot",
            "drawing/x-dwf", "dwf",
            "application/x-autocad", "dwg dxf",
            "application/x-expandedbook", "ebk",
            "chemical/x-embl-dl-nucleotide", "emb embl",
            "image/x-eri", "eri",
            "audio/echospeech", "es esl",
            "application/x-earthtime", "etc",
            "application/x-envoy", "evy",
            "image/x-freehand", "fh4 fh5 fhc",
            "image/fif", "fif",
            "application/x-maker", "fm",
            "image/x-fpx", "fpx",
            "video/isivideo", "fvi",
            "chemical/x-gaussian-input", "gau",
            "application/x-gca-compressed", "gca",
            "x-lml/x-gdb", "gdb",
            "application/x-gps", "gps",
            "text/x-hdml", "hdm hdml",
            "x-conference/x-cooltalk", "ice",
            "image/ifs", "ifs",
            "audio/melody", "imy",
            "application/x-NET-Install", "ins",
            "application/x-ipscript", "ips",
            "application/x-ipix", "ipx",
            "audio/x-mod", "it itz m15 mdz mod s3m s3z stm ult xm xmz",
            "i-world/i-vrml", "ivr",
            "image/j2k", "j2k",
            "text/vnd.sun.j2me.app-descriptor", "jad",
            "application/x-jam", "jam",
            "application/java-archive", "jar",
            "application/x-java-jnlp-file", "jnlp",
            "application/jwc", "jwc",
            "application/x-kjx", "kjx",
            "x-lml/x-lak", "lak",
            "application/fastman", "lcc",
            "application/x-digitalloca", "lcl lcr",
            "application/lgh", "lgh",
            "x-lml/x-lml", "lml",
            "x-lml/x-lmlpack", "lmlpack",
            "application/x-lzh", "lzh",
            "audio/ma1", "ma1",
            "audio/ma2", "ma2",
            "audio/ma3", "ma3",
            "audio/ma5", "ma5",
            "magnus-internal/imagemap", "map",
            "application/mbedlet", "mbd",
            "application/x-mascot", "mct",
            "text/x-vmel", "mel",
            "application/x-mif", "mi mif",
            "audio/midi", "mid midi",
            "audio/x-mio", "mio",
            "application/x-skt-lbs", "mmf",
            "video/x-mng", "mng",
            "application/x-mocha", "moc mocha",
            "application/x-yumekara", "mof",
            "chemical/x-mdl-molfile", "mol",
            "chemical/x-mopac-input", "mop",
            "audio/x-mpeg", "mp2 mp3",
            "video/mp4", "mp4 mpg4",
            "application/vnd.mpohun.certificate", "mpc",
            "application/vnd.mophun.application", "mpn",
            "application/x-mapserver", "mps",
            "text/x-mrml", "mrl",
            "application/x-mrm", "mrm",
            "application/metastream", "mts mtx mtz mzv rtg",
            "image/nbmp", "nbmp",
            "x-lml/x-ndb", "ndb",
            "application/ndwn", "ndwn",
            "application/x-nif", "nif",
            "application/x-scream", "nmz",
            "image/vnd.nok-oplogo-color", "nokia-op-logo",
            "application/x-netfpx", "npx",
            "audio/nsnd", "nsnd",
            "application/x-neva1", "nva",
            "application/x-AtlasMate-Plugin", "oom",
            "audio/x-pac", "pac",
            "audio/x-epac", "pae",
            "application/x-pan", "pan",
            "image/x-pcx", "pcx",
            "image/x-pda", "pda",
            "chemical/x-pdb", "pdb xyz",
            "application/font-tdpfr", "pfr",
            "image/x-pict", "pict",
            "application/x-perl", "pm",
            "application/x-pmd", "pmd",
            "image/png", "png pnz",
            "application/x-cprplayer", "pqf",
            "application/cprplayer", "pqi",
            "application/x-prc", "prc",
            "application/x-ns-proxy-autoconfig", "proxy",
            "application/listenup", "ptlk",
            "video/x-pv-pvx", "pvx",
            "audio/vnd.qcelp", "qcp",
            "image/x-quicktime", "qti qtif",
            "text/vnd.rn-realtext3d", "r3t",
            "application/x-rar-compressed", "rar",
            "application/rdf+xml", "rdf",
            "image/vnd.rn-realflash", "rf",
            "application/x-richlink", "rlf",
            "audio/x-rmf", "rmf",
            "application/vnd.rn-realplayer", "rnx",
            "image/vnd.rn-realpix", "rp",
            "audio/x-pn-realaudio-plugin", "rpm",
            "text/vnd.rn-realtext", "rt",
            "x-lml/x-gps", "rte trk wpt",
            "video/vnd.rn-realvideo", "rv",
            "application/x-rogerwilco", "rwc",
            "application/x-supercard", "sca",
            "application/e-score", "sdf",
            "text/x-sgml", "sgm sgml",
            "magnus-internal/parsed-html", "shtml",
            "application/presentations", "shw",
            "image/si6", "si6",
            "image/vnd.stiwap.sis", "si7",
            "image/vnd.lgtwap.sis", "si9",
            "application/vnd.symbian.install", "sis",
            "application/x-Koan", "skd skm skp skt",
            "application/x-salsa", "slc",
            "audio/x-smd", "smd smz",
            "application/smil", "smi smil",
            "application/studiom", "smp",
            "text/x-speech", "spc talk",
            "application/x-sprite", "spr sprite",
            "application/x-spt", "spt",
            "application/hyperstudio", "stk",
            "image/vnd", "svf",
            "image/svg-xml", "svg",
            "image/svh", "svh",
            "x-world/x-svr", "svr",
            "application/x-shockwave-flash", "swf swfl",
            "application/x-timbuktu", "tbp tbt",
            "application/vnd.eri.thm", "thm",
            "application/x-tkined", "tki tkined",
            "application/toc", "toc",
            "image/toy", "toy",
            "audio/tsplayer", "tsi",
            "application/dsptype", "tsp",
            "application/t-time", "ttz",
            "application/x-uuencode", "uu uue",
            "application/x-cdlink", "vcd",
            "video/vdo", "vdo",
            "audio/vib", "vib",
            "video/vivo", "viv vivo",
            "application/vocaltec-media-desc", "vmd",
            "application/vocaltec-media-file", "vmf",
            "application/x-dreamcast-vms-info", "vmi",
            "application/x-dreamcast-vms", "vms",
            "audio/voxware", "vox",
            "audio/x-twinvq-plugin", "vqe",
            "audio/x-twinvq", "vqf vql",
            "x-world/x-vream", "vre vrw",
            "x-world/x-vrt", "vrt",
            "workbook/formulaone", "vts",
            "audio/x-ms-wax", "wax",
            "image/vnd.wap.wbmp", "wbmp",
            "application/vnd.xara", "web xar",
            "image/wavelet", "wi",
            "application/x-InstallShield", "wis",
            "video/x-ms-wm", "wm",
            "audio/x-ms-wma", "wma",
            "application/x-ms-wmd", "wmd",
            "text/vnd.wap.wml", "wml",
            "application/vnd.wap.wmlc", "wmlc",
            "text/vnd.wap.wmlscript", "wmls wmlscript ws",
            "application/vnd.wap.wmlscriptc", "wmlsc wsc",
            "audio/x-ms-wmv", "wmv",
            "video/x-ms-wmx", "wmx",
            "application/x-ms-wmz", "wmz",
            "image/x-up-wpng", "wpng",
            "video/wavelet", "wv",
            "video/x-ms-wvx", "wvx",
            "application/x-wxl", "wxl",
            "application/x-xdma", "xdm xdma",
            "application/vnd.fujixerox.docuworks", "xdw",
            "application/xhtml+xml", "xht xhtm xhtml",
            "application/x-excel", "xll",
            "text/xml", "xml xsit xsl",
            "application/x-xpinstall", "xpi",
            "text/xul", "xul",
            "application/x-yz1", "yz1",
            "application/x-zaurus-zac", "zac",
            "image/webp", "webp"
    );

    public static Boolean append(File file, int length, byte[] data){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file,true);
            byte[] write = Arrays.copyOf(data,length);
            fos.write(write);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage(),e);
            return false;
        }finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    log.info(e.getMessage(),e);
                }
            }
        }
    }

    public static byte[] tail(File file, int length){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] read = new byte[length];
            long skip = file.length()-length;
            fis.skip(skip);
            fis.read(read);
            return read;
        } catch (Exception e) {
            log.info(e.getMessage(),e);
            return new byte[]{};
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    log.info(e.getMessage(),e);
                }
            }
        }
    }

    public static String getMimeType(byte[] prefix){
        Tika tika = new Tika();
        if (prefix.length > 1024){
            byte[] bytes = new byte[1024];
            System.arraycopy(prefix, 0, bytes, 0, 1024);
            prefix = bytes;
        }
        String mimeType = tika.detect(prefix);
        return mimeType;
    }

    public static String getMimeType(String extension){
        if (StringUtils.isBlank(extension)){
            return null;
        }
        Optional<Map.Entry<String,String>> result = mimes.entrySet().stream()
                .filter(item->item.getValue().equals(extension)|| item.getValue().contains(extension+" ") || item.getValue().contains(" "+extension))
                .findFirst();
        if (result.isPresent()){
            return result.get().getKey();
        }
        return null;
    }

    public static String getMimeType(File file){
        String mimeType = null;
        mimeType = getMimeType(FilenameUtils.getExtension(file.getName()));
        if (StringUtils.isBlank(mimeType)){
            try {
                byte[] bytes = new byte[1024];
                FileInputStream fis = new FileInputStream(file);
                fis.read(bytes);
                mimeType = getMimeType(bytes);
            }catch (IOException e){
                log.error(e.getMessage(),e);
            }
        }
        return mimeType;
    }

    public static String mime2ext(String mime){
        String exts = mimes.get(mime);
        if ( StringUtils.isBlank(exts)){
            return null;
        }
        return StringUtils.split(exts," ")[0];
    }

    public static Boolean mkdir(String path){
        File f = new File(path);
        if (f.isDirectory()){
            return true;
        }
        try{
            FileUtils.forceMkdir(f);
            if (f.isDirectory()){
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        try{
            if (SystemUtil.isLinux()){
                String cmd = String.format("mkdir -p %s",path);
                Runtime.getRuntime().exec(cmd);
                if (f.isDirectory()){
                    return true;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return false;
    }

    public static Boolean setWritable(String path){
        File f = new File(path);
        if (!f.exists()){
            log.error("路径不存在{}",path);
            return false;
        }
        if (f.canWrite()){
            return true;
        }
        try{
            f.setWritable(true);
            if (f.canWrite()){
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        try{
            Set<PosixFilePermission> perms = Set.of(PosixFilePermission.OWNER_WRITE,PosixFilePermission.GROUP_WRITE,PosixFilePermission.OTHERS_WRITE);
            Files.setPosixFilePermissions(Paths.get(path), perms);
            if (f.canWrite()){
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        try{
            if (SystemUtil.isLinux()){
                String cmd = String.format("chmod +w %s",path);
                Runtime.getRuntime().exec(cmd);
                if (f.canWrite()){
                    return true;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return false;
    }

    public static File tmpFile(String extension){
        File tmpFile = null;
        try {
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");
            String tmpFileName = RandomUtil.getRandomStr(6,"ALL").toLowerCase();
            if (StringUtils.isBlank(extension))
                extension = "tmp";
            Path filePath = Paths.get(tempDirectoryPath,tmpFileName+"."+extension);
            if (SystemUtil.isLinux()){
                Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
                tmpFile = Files.createFile(filePath,PosixFilePermissions.asFileAttribute(permissions)).toFile();
            }else{
                tmpFile = Files.createFile(filePath).toFile();
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return tmpFile;
    }
}
