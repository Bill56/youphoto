package cn.bill56.youphoto.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bill56.youphoto.R;
import cn.bill56.youphoto.util.AnimatorUtil;
import cn.bill56.youphoto.util.LogUtil;


/**
 * 承载正在编辑图片视图的活动
 * Created by Bill56 on 2016/6/14.
 */
public class EditPicActivity extends BaseActivity {

    /**
     * 对应视图中的控件对象
     */
    // 工具栏
    @Bind(R.id.toolBar)
    Toolbar toolbar;
    // 操作选项栏布局
    @Bind(R.id.ll_options)
    LinearLayout llOptions;
    // 操作选项栏按钮——编辑
    @Bind(R.id.btn_pic_edit)
    Button btnPicEdit;
    // 操作选项栏按钮——分享
    @Bind(R.id.btn_pic_share)
    Button btnPicShare;

    // 图片编辑栏布局
    @Bind(R.id.ll_edit_actions)
    LinearLayout llEditActions;
    // 图片编辑栏布局——旋转

    // 图片编辑栏布局——旗帜

    // 图片编辑栏布局——滤镜

    // 图片编辑栏布局——增强

    // 图片编辑栏布局——涂鸦


    // 正在编辑的图片
    @Bind(R.id.img_editing_pic)
    ImageView imgEditingPic;

    /**
     *临时存储变量值的临时成员变量
     */
    // 动画作用视图的高度
    private int mHiddenViewMeasuredHeight;
    // 像素密度
    private float mDensity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pic);
        // 初始化视图
        initview();
    }

    /**
     * 初始化绑定的布局视图
     */
    private void initview() {
        // 通过黄牛刀插件快速绑定控件
        ButterKnife.bind(this);
        // 用工具栏代替ActionBar
        setSupportActionBar(toolbar);
        // 设置表土为空
        getSupportActionBar().setTitle("");
        // 绑定控件
        imgEditingPic = (ImageView) findViewById(R.id.img_editing_pic);
        // 获取传递过来的intent中的数据
        String imagePath = getIntent().getStringExtra("EDIT_PIC");
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(imagePath);
        // 显示需要编辑的图片
        imgEditingPic.setImageBitmap(bitmap);
        // 为显示动画做准备
        // 获取像素密度
        mDensity = getResources().getDisplayMetrics().density;
        // 获取布局的高度
        mHiddenViewMeasuredHeight = (int) (mDensity * 80 + 0.5);
        // 注册事件监听
        registerListener();
    }

    /**
     * 为活动中的控件注册事件监听
     */
    private void registerListener() {
        // 创建按钮点击事件监听器
        OnButtonClickListener buttonClickListener = new OnButtonClickListener();
        btnPicEdit.setOnClickListener(buttonClickListener);
        btnPicShare.setOnClickListener(buttonClickListener);
    }

    /**
     * 自定义的按钮点击事件监听器
     */
    class OnButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // 通过id判断点击的是哪个按钮
            switch (v.getId()) {
                // 点击的是编辑按钮
                case R.id.btn_pic_edit:
                    // 开启动画，让操作栏消失，编辑栏显示
                    LogUtil.d(LogUtil.TAG,"点击了edit");
                    llOptions.setVisibility(View.GONE);
                    AnimatorUtil.animateOpen(llEditActions, mHiddenViewMeasuredHeight);
                    break;
                case R.id.btn_quit:
                    // 调用按下返回键的方法
                    onBackPressed();
                    break;
                default:
                    break;
            }
        }


    }

    /**
     * 按下返回键的时候执行
     */
    @Override
    public void onBackPressed() {
        // 当操作栏消失的时候
        if (llOptions.getVisibility() == View.GONE) {
            // 调用关闭源动画效果
            AnimatorUtil.animateClose(llEditActions);
            // 开启新动画
            AnimatorUtil.animateOpen(llOptions, mHiddenViewMeasuredHeight);
        }
    }

}
