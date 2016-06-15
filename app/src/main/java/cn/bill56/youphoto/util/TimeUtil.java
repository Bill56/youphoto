package cn.bill56.youphoto.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 跟时间相关的方法封装工具类
 * Created by Bill56 on 2016/6/15.
 */
public class TimeUtil {

    /**
     * 将时间戳对象转成纯数字的字符串
     *
     * @param currentTime 当前的时间
     * @return 字符串对象
     */
    public static String timestamp2string(long currentTime) {
        // 根据当前毫秒数创建时间戳
        Timestamp timestamp = new Timestamp(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String str = sdf.format(timestamp);
        return str;
    }

}
