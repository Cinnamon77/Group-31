package com.bookkeeping.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author bookkeeping
 **/
public class RedisKey {

    /**
     * 天气key
     * @return String
     */
    public static String getWeatherKey() {
        return "weather:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 节假日key
     * @return String
     */
    public static String getFestival() {
        return "festival:"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
