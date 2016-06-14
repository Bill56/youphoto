package cn.bill56.youphoto.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 提示框工具类
 * Created by Bill56 on 2016/5/30.
 */
public class ToastUtil {

    /**
     * 根据字符串内容显示
     *
     * @param context 上下文环境
     * @param info    字符串对象
     */
    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 根据字符串内容的id索引值显示
     *
     * @param context 上下文环境
     * @param info    字符串索引ID
     */
    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

}
