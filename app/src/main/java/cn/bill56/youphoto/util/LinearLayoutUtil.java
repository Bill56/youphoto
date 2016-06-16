package cn.bill56.youphoto.util;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bill56.youphoto.customview.EditSelectLinearLayout;

/**
 * 管理自定义线性布局的工具类
 * Created by Bill56 on 2016/6/16.
 */
public class LinearLayoutUtil {

    // 当前程序的所有自定义的线性布局
    public static List<EditSelectLinearLayout> linearLayouts = new ArrayList<>();

    /**
     * 添加活动
     *
     * @param linearLayout 当前添加的线性布局
     */
    public static void addLinearLayout(EditSelectLinearLayout linearLayout) {
        linearLayouts.add(linearLayout);
    }

    /**
     * 隐藏所有的已显示的自定线性布局布局
     */
    public static void hiddenAllLinearLayouts() {
        // 遍历所有自定义线性布局
        for(EditSelectLinearLayout editSelectLinearLayout : linearLayouts) {
            // 如果已经显示了，则调用动画关闭
            if (editSelectLinearLayout.getVisibility() == View.VISIBLE) {
                AnimatorUtil.animateClose(editSelectLinearLayout);
            }
        }
    }

}
