package cn.bill56.youphoto.util;

import android.util.Log;

/**
 * 程序开发过程中调试日志的工具类
 * Created by Bill56 on 2016/5/30.
 */
public class LogUtil {

    // 普通
    public static final int VERBOSE = 1;
    // 调试
    public static final int DEBUG = 2;
    // 信息
    public static final int INFO = 3;
    // 警告
    public static final int WARN = 4;
    // 错误
    public static final int ERROR = 5;
    // 开发完毕后显示的
    public static final int NOTHING = 6;

    // 当前级别
    public static final int LEVEL = VERBOSE;
    // 调试的标志
    public static final String TAG = "YYW";

    /**
     * 打印普通的日志
     *
     * @param tag 标志
     * @param msg 打印的信息
     */
    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * 打印调试的日志
     *
     * @param tag 标志
     * @param msg 打印的信息
     */
    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * 打印信息的日志
     *
     * @param tag 标志
     * @param msg 打印的信息
     */
    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * 打印警告的日志
     *
     * @param tag 标志
     * @param msg 打印的信息
     */
    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印错误的日志
     *
     * @param tag 标志
     * @param msg 打印的信息
     */
    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

}
