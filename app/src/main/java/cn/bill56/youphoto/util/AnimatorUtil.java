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
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int value = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams =
                                view.getLayoutParams();
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
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(
                view,
                0,
                hiddenViewMeasuredHeight);
        animator.start();
    }

    /**
     * 关闭缓慢升起的动画
     *
     * @param view 作用的视图对象
     */
    public static void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

}
