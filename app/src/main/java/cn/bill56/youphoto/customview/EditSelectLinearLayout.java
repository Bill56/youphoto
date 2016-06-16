package cn.bill56.youphoto.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.bill56.youphoto.util.LinearLayoutUtil;

/**
 * 自定义的线性布局，用于管理编辑选择后显示的布局的显示方式
 * Created by Bill56 on 2016/6/16.
 */
public class EditSelectLinearLayout extends LinearLayout {

    public EditSelectLinearLayout(Context context) {
        super(context);
        LinearLayoutUtil.addLinearLayout(this);
    }

    public EditSelectLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayoutUtil.addLinearLayout(this);
    }

    public EditSelectLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LinearLayoutUtil.addLinearLayout(this);
    }

    public EditSelectLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LinearLayoutUtil.addLinearLayout(this);
    }

}
