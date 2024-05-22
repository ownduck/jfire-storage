package jfire.util.util;

import java.util.Random;
import java.util.UUID;

/**
 * 取随机数的工具
 */
public class RandomUtil {

    /**
     * 取指定长度的随机数
     * @author wuxiao
     * @return
     */
	public static String getRandomStr(Integer len, String format) {
		String chars;
		switch (format.toUpperCase()) {
			case "ALL" :
				chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
				break;
			case "CHAR" :
				chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
				break;
			case "NUMBER" :
				chars = "0123456789";
				break;
			case "PNUMBER" :
				chars = "123456789";
				break;
			default :
				chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
				break;
		}

		String string = "";
		Random rd = new Random();
		Integer pos;
		while ( string.length() < len ){
			pos = Math.abs(rd.nextInt()) % chars.length();
			string += chars.substring(pos,pos+1);
		}
		return string;
	}

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

}
