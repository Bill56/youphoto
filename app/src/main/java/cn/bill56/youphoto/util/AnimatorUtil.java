package cn.bill56.youphoto.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * 封装了一系列动画效果的工具类
 * Created by Bill56 on 2016/6/15.
 */
public class AnimatorUtil {

    /**
     * 创建下拉动画的方法
     *
     * @param view  作用的视图对象
     * @param start 开始位置
     * @param end   结束为止
     * @return 动画对象
     */
    public static ValueAnimator createDropAnimator(
            final View view, int start, int end) {
        // 创建属性动画对象
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        // 设置动画帧数变化监听器
        animator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    /**
                     * 动画帧数改变的时候回调
                     * @param valueAnimator 属性动画对象
                     */
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        // 获取帧数值
                        int value = (Integer) valueAnimator.getAnimatedValue();
                        // 获取作用对象的布局参数
                        ViewGroup.LayoutParams layoutParams =
                                view.getLayoutParams();
                        // 一步一步设置显示的高度
                        layoutParams.height = value;
                        view.setLayoutParams(layoutParams);
                    }
                });
        return animator;
    }

    /**
     * 开启缓慢升起的动画
     *
     * @param view                     作用的视图对象
     * @param hiddenViewMeasuredHeight 动画执行的高度
     */
    public static void animateOpen(final View view, int hiddenViewMeasuredHeight) {
        // 视图可见
        view.setVisibility(View.VISIBLE);
        // 调用创建下拉动画的方法
        ValueAnimator animator = createDropAnimator(
                view,   // 作用视图
                0,  // 起始位置
                hiddenViewMeasuredHeight);  // 结束位置
        // 开启动画
        animator.start();
    }

    /**
     * 关闭缓慢升起的动画
     *
     * @param view 作用的视图对象
     */
    public static void animateClose(final View view) {
        // 获得作用视图高度
        int origHeight = view.getHeight();
        // 开启属性动画
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        // 添加动画监听器
        animator.addListener(new AnimatorListenerAdapter() {
            /**
             * 动画结束的时候回调
             * @param animation 属性动画对象
             */
            public void onAnimationEnd(Animator animation) {
                // 隐藏作用视图
                view.setVisibility(View.GONE);
            }
        });
        // 开启动画
        animator.start();
    }

}
