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
        for (BaseActivity activity : activities) {
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
        for (BaseActivity ba : activities) {
            if (!ba.isFinishing() && ba != activity) {
                ba.finish();
            }
        }
    }

}
