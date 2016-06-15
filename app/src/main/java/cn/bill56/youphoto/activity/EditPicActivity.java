package cn.bill56.youphoto.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bill56.youphoto.R;
import cn.bill56.youphoto.util.AnimatorUtil;
import cn.bill56.youphoto.util.LogUtil;
import cn.bill56.youphoto.util.ToastUtil;


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
    // 工具栏按钮——放弃
    @Bind(R.id.btn_quit)
    Button btnQuit;
    // 工具栏按钮——保存
    @Bind(R.id.btn_save)
    Button btnSave;

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
     * 临时存储变量值的临时成员变量
     */
    // 动画作用视图的高度
    private int mHiddenViewMeasuredHeight;
    // 像素密度
    private float mDensity;
    // 编辑图片的路径
    private  String imagePath;


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
        imagePath = getIntent().getStringExtra("EDIT_PIC");
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
        // 给工具栏的按钮注册
        btnQuit.setOnClickListener(buttonClickListener);
        btnSave.setOnClickListener(buttonClickListener);
        // 为操作栏的按钮注册
        btnPicEdit.setOnClickListener(buttonClickListener);
        btnPicShare.setOnClickListener(buttonClickListener);
        // 为图片编辑栏的按钮注册

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
                    LogUtil.d(LogUtil.TAG, "点击了edit");
                    llOptions.setVisibility(View.GONE);
                    AnimatorUtil.animateOpen(llEditActions, mHiddenViewMeasuredHeight);
                    // 让按钮可以保存
                    btnSave.setEnabled(true);
                    break;
                // 点击的是分享按钮
                case R.id.btn_pic_share:
                    doPicShare();
                    break;
                // 点击的是应用栏的放弃
                case R.id.btn_quit:
                    // 调用按下返回键的方法
                    onBackPressed();
                    break;
                default:
                    break;
            }
        }

        /**
         * 图片分享的方法,发送分享广播，让所有可接收分享广播的应用接收，调用对应的接口
         */
        private void doPicShare() {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (imagePath == null || imagePath.equals("")) {
               // 提示图片不存在
                ToastUtil.show(EditPicActivity.this,R.string.pic_share_error);
            } else {
                File f = new File(imagePath);
                if (f != null && f.exists() && f.isFile()) {
                    intent.setType("image/*");
                    Uri u = Uri.fromFile(f);
                    intent.putExtra(Intent.EXTRA_STREAM, u);
                }
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, EditPicActivity.this.getTitle()));
        }

    }

    /**
     * 按下返回键的时候执行
     */
    @Override
    public void onBackPressed() {
        // 当已经能保存，即编辑过了的时候
        if (btnSave.isEnabled()) {
            // 创建对话框，询问是否保存
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 设置对话款规定属性
            builder.setTitle(R.string.dialog_info_title)
                    .setCancelable(false)
                    .setMessage(R.string.dialog_info_quit_msg)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 当操作栏消失的时候
                            if (llOptions.getVisibility() == View.GONE) {
                                // 调用关闭源动画效果
                                AnimatorUtil.animateClose(llEditActions);
                                // 开启新动画
                                AnimatorUtil.animateOpen(llOptions, mHiddenViewMeasuredHeight);
                                // 关闭保存按钮的功能
                                btnSave.setEnabled(false);
                                dialog.dismiss();
                            }
                        }
                    });
            // 创建并显示对话框
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

}
