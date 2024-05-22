package jfire.util.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {

    private static final Logger log = LogManager.getLogger(CollectionUtil.class);

    public static <K,V> Map<K,V> ofMap(Object... values){
        Map<K,V> map = new HashMap<K,V>();
        for (int i=0;i<values.length-1;i++){
            try{
                K key = (K)values[i];
                V value = (V)values[i+1];
                map.put(key,value);
            }catch (Exception e){
                log.error("{}.{} error {}",CollectionUtil.class,"ofMap",e.getMessage());
            }
        }
        return map;
    }
}
