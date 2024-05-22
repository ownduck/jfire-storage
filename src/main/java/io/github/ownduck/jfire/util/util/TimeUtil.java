package io.github.ownduck.jfire.util.util;


import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * 日期工具类
 * @author bo.sun
 *
 */
public final class TimeUtil {

	public static String toString(Date date,String pattern){
		return FastDateFormat.getInstance(pattern).format(date);
	}
}
