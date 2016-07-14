package cn.bill56.youphoto.util;

import java.util.ArrayList;
import java.util.List;

import cn.bill56.youphoto.activity.BaseActivity;

/**
 * 用于管理自定义活动的工具类
 * Created by Bill56 on 2016/5/14.
 */
public class ActivityUtil {

    // 当前程序的所有活动
    public static List<BaseActivity> activities = new ArrayList<>();

    /**
     * 添加活动
     *
     * @param activity 当前添加的活动
     */
    public static void addActivity(BaseActivity activity) {
        activities.add(activity);
    }

    /**
     * 移除活动
     *
     * @param activity 当前移除的活动
     */
    public static void removeActivity(BaseActivity activity) {
        activities.remove(activity);
    }

    /**
     * 销毁所有活动
     */
    public static void finishAll() {
        // 遍历活动列表，销毁所有活动
        for (BaseActivity activity : activities) {
            // 当前活动没有正在执行销毁操作，则销毁
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 销毁除参数外的所有活动
     *
     * @param activity 不希望被销毁的活动
     */
    public static void finishExcept(BaseActivity activity) {
        // 遍历活动列表
        for (BaseActivity ba : activities) {
            // 当前活动没有正在执行销毁操作，并且不是希望被销毁的活动
            if (!ba.isFinishing() && ba != activity) {
                // 销毁
                ba.finish();
            }
        }
    }

}
