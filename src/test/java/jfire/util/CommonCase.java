package jfire.util;

import junit.framework.TestCase;

public class CommonCase extends TestCase {

    protected static void assertNotBlank(String message,String object){
        assertNotNull(message, object);
        if (object.trim().isEmpty()) {
            fail(message);
        }
    }
}
