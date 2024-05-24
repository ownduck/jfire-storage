package io.github.ownduck.jfire.util.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptUtil {

    private static final Charset encoding = StandardCharsets.UTF_8;

    public static String base64encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] base64decode(String encoded) {
        return Base64.getDecoder().decode(encoded);
    }

}
