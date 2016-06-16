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
import android.widget.SeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bill56.youphoto.R;
import cn.bill56.youphoto.util.AnimatorUtil;
import cn.bill56.youphoto.util.ImageUtil;
import cn.bill56.youphoto.util.LinearLayoutUtil;
import cn.bill56.youphoto.util.LogUtil;
import cn.bill56.youphoto.util.TimeUtil;
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
    @Bind(R.id.btn_edit_romote)
    Button btnEditRomote;
    // 图片编辑栏布局——旗帜
    @Bind(R.id.btn_edit_flag)
    Button btnEditFlag;
    // 图片编辑栏布局——滤镜
    @Bind(R.id.btn_edit_filter)
    Button btnEditFilter;
    // 图片编辑栏布局——增强
    @Bind(R.id.btn_edit_power)
    Button btnEditPower;
    // 图片编辑栏布局——涂鸦

    // 滤镜选择栏
    @Bind(R.id.ll_edit_filter)
    LinearLayout llEditFilter;
    // 原图
    @Bind(R.id.btn_filter_old)
    Button btnFilterOld;
    // 滤镜选择栏——灰度
    @Bind(R.id.btn_filter_gray)
    Button btnFilterGray;
    // 滤镜选择栏——反转
    @Bind(R.id.btn_filter_reversal)
    Button btnFilterReversal;
    // 滤镜选择栏——怀旧
    @Bind(R.id.btn_filter_nostalgia)
    Button btnFilterNostalgia;
    // 滤镜选择栏——去色
    @Bind(R.id.btn_filter_uncolor)
    Button btnFilterUncolor;
    // 滤镜选择栏——高饱和度
    @Bind(R.id.btn_filter_high_saturation)
    Button btnFilterHighSaturation;
    // 滤镜选择栏——底片
    @Bind(R.id.btn_filter_relief)
    Button btnFilterRelief;

    // 增强的选择栏
    @Bind(R.id.ll_edit_power)
    LinearLayout llEditPower;
    // 增强的选择栏——色调
    @Bind(R.id.seekBar_color)
    SeekBar seekBarColor;
    // 增强的选择栏——饱和度
    @Bind(R.id.seekBar_saturation)
    SeekBar seekBarSaturation;
    // 增强的选择栏——亮度
    @Bind(R.id.seekBar_light)
    SeekBar seekBarLight;
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
    private String imagePath;
    // 保存当前图片的位图对象（原图）
    private Bitmap bitmap = null;
    // 保存正在修改的位图图像（正在编辑中，若保存，则将其引用赋给原图）
    private Bitmap editBitmap = null;
    // 是否放弃修改的标志
    private boolean isQuitUpdate = false;

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
        btnEditRomote.setOnClickListener(buttonClickListener);
        btnEditFlag.setOnClickListener(buttonClickListener);
        btnEditFilter.setOnClickListener(buttonClickListener);
        btnEditPower.setOnClickListener(buttonClickListener);
        // 为图片的滤镜选择栏按钮注册
        btnFilterOld.setOnClickListener(buttonClickListener);
        btnFilterGray.setOnClickListener(buttonClickListener);
        btnFilterReversal.setOnClickListener(buttonClickListener);
        btnFilterNostalgia.setOnClickListener(buttonClickListener);
        btnFilterUncolor.setOnClickListener(buttonClickListener);
        btnFilterHighSaturation.setOnClickListener(buttonClickListener);
        btnFilterRelief.setOnClickListener(buttonClickListener);
    }

    /**
     * 自定义的按钮点击事件监听器
     */
    class OnButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // 通过id判断点击的是哪个按钮
            switch (v.getId()) {
                /**
                 * 以下是操作栏
                 */
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
                /**
                 * 以下是应用栏
                 */
                // 点击的是应用栏的放弃
                case R.id.btn_quit:
                    // 调用按下返回键的方法
                    onBackPressed();
                    break;
                // 点击的是应用栏的保存
                case R.id.btn_save:
                    doPicSave();
                    break;
                /**
                 * 以下是编辑栏
                 */
                // 点击的是编辑栏的旋转
                case R.id.btn_edit_romote:
                    doRomote();
                    break;
                // 点击的是编辑栏的旗帜
                case R.id.btn_edit_flag:
                    doFlag();
                    break;
                // 点击的是编辑栏的滤镜
                case R.id.btn_edit_filter:
                    doFilterSelect();
                    break;
                // 点击的是编辑栏的增强
                case R.id.btn_edit_power:
                    doPowerSelect();
                    break;
                // 点击的是编辑栏的涂鸦
                case R.id.btn_edit_graffiti:
                    break;
                /**
                 * 以下是滤镜选择
                 */
                // 点击的是滤镜选择——原图
                case R.id.btn_filter_old:
                    editBitmap = bitmap;
                    imgEditingPic.setImageBitmap(editBitmap);
                    break;
                // 点击的是滤镜选择——灰度
                case R.id.btn_filter_gray:
                    editBitmap = ImageUtil.handleImage2FilterGray(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    break;
                // 点击的是滤镜选择——反转
                case R.id.btn_filter_reversal:
                    editBitmap = ImageUtil.handleImage2FilterReversal(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                     break;
                // 点击的是滤镜选择——怀旧
                case R.id.btn_filter_nostalgia:
                    editBitmap = ImageUtil.handleImage2FilterNostalgia(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    break;
                // 点击的是滤镜选择——去色
                case R.id.btn_filter_uncolor:
                    editBitmap = ImageUtil.handleImage2FilterUncolor(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    break;
                // 点击的是滤镜选择——高饱和度
                case R.id.btn_filter_high_saturation:
                    editBitmap = ImageUtil.handleImage2FilterHighSaturation(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    break;
                // 点击的是滤镜选择——底片
                case R.id.btn_filter_relief:
                    editBitmap = ImageUtil.handleImage2FilterRelief(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    break;
                default:
                    break;
            }
        }

        /**
         * 点击编辑栏的旋转后执行的方法
         * 图片按中心逆时针旋转90度
         */
        private void doRomote() {
            // 第一次使用
            if (editBitmap == null) {
                // 修改后的图片
                // 设置旋转中心和旋转度数
                editBitmap = ImageUtil.handleImage2Romote(bitmap, 90,
                        bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            } else {
                // 不是第一次使用
                editBitmap = ImageUtil.handleImage2Romote(editBitmap, 90,
                        editBitmap.getWidth() / 2, editBitmap.getHeight() / 2);
            }
            imgEditingPic.setImageBitmap(editBitmap);
        }

        /**
         * 点击编辑栏的旗帜后执行的方法
         * 图片变成旗帜飞扬的效果
         */
        private void doFlag() {
            // 修改后的图片
            editBitmap = ImageUtil.handleImage2Flag(bitmap);
            // 显示
            imgEditingPic.setImageBitmap(editBitmap);
        }

        /**
         * 点击增强按钮后执行
         * 显示增强选择栏的方法
         */
        private void doPowerSelect() {
            // 如果没有显示则开启动画显示
            if (llEditPower.getVisibility() == View.GONE) {
                if (isQuitUpdate) {
                    // 如果是放弃了修改，则将所有的seek置为初始状态
                    seekBarColor.setProgress(50);
                    seekBarSaturation.setProgress(50);
                    seekBarLight.setProgress(50);
                }
                // 关闭其他已经显示的编辑选择栏
                LinearLayoutUtil.hiddenAllLinearLayouts();
                // 开启动画，显示滤镜选择栏
                AnimatorUtil.animateOpen(llEditPower, mHiddenViewMeasuredHeight);
            } else {
                // 显示了则关闭动画显示
                AnimatorUtil.animateClose(llEditPower);
            }
        }

        /**
         * 点击滤镜按钮后执行
         * 显示滤镜选择栏的方法
         */
        private void doFilterSelect() {
            // 如果没有显示则开启动画显示
            if (llEditFilter.getVisibility() == View.GONE) {
                // 关闭其他已经显示的编辑选择栏
                LinearLayoutUtil.hiddenAllLinearLayouts();
                // 开启动画，显示滤镜选择栏
                AnimatorUtil.animateOpen(llEditFilter, mHiddenViewMeasuredHeight);
            } else {
                // 显示了则关闭动画显示
                AnimatorUtil.animateClose(llEditFilter);
            }
        }

        /**
         * 图片保存的方法
         * 生成的图片放在sd/uphotocache下，命名为当前时间戳的字符串表示+.jpg
         */
        private void doPicSave() {
            // 获取设备的根目录
            File sdDir = Environment.getExternalStorageDirectory();
            // 创建缓存目录
            File cacheDir = new File(sdDir, "UPhotocache");
            // 如果不存在目录，则创建
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            // 创建File对象，用于存储修改后的图片,以当前时间为文件名
            File outputImage = new File(cacheDir,
                    TimeUtil.timestamp2string(System.currentTimeMillis()) + ".jpg");
            // 如果存在则删除
            if (outputImage.exists()) {
                outputImage.delete();
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outputImage);
                if (editBitmap != null) {
                    editBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    // 执行到这说明保存成功
                    // 退出编辑界面，显示操作栏,将保存按钮禁用
                    // 调用关闭源动画效果
                    AnimatorUtil.animateClose(llEditActions);
                    // 开启新动画
                    AnimatorUtil.animateOpen(llOptions, mHiddenViewMeasuredHeight);
                    // 关闭自定义控件
                    LinearLayoutUtil.hiddenAllLinearLayouts();
                    // 修改当前图片路径
                    imagePath = outputImage.getAbsolutePath();
                    // 关闭保存按钮的功能
                    btnSave.setEnabled(false);
                } else {
                    ToastUtil.show(EditPicActivity.this,R.string.pic_save_no_update);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // 弹出提示框
                ToastUtil.show(EditPicActivity.this, R.string.pic_share_error);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // 弹出提示框
                ToastUtil.show(EditPicActivity.this, R.string.pic_save_error);
            } finally {
                // 关闭流
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        out = null;
                    }
                }
            }
        }

        /**
         * 图片分享的方法,发送分享广播，让所有可接收分享广播的应用接收，调用对应的接口
         */
        private void doPicShare() {
            Intent intent = new Intent(Intent.ACTION_SEND);
            if (imagePath == null || imagePath.equals("")) {
                // 提示图片不存在
                ToastUtil.show(EditPicActivity.this, R.string.pic_share_error);
            } else {
                // 找到对应的文件引用
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
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 将放弃修改改成false
                            isQuitUpdate = false;
                        }
                    })
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 将放弃修改改成true
                            isQuitUpdate = true;
                            // 当操作栏消失的时候
                            if (llOptions.getVisibility() == View.GONE) {
                                // 调用关闭源动画效果
                                AnimatorUtil.animateClose(llEditActions);
                                // 开启新动画
                                AnimatorUtil.animateOpen(llOptions, mHiddenViewMeasuredHeight);
                                // 关闭自定义控件
                                LinearLayoutUtil.hiddenAllLinearLayouts();
                                // 关闭保存按钮的功能
                                btnSave.setEnabled(false);
                                // 显示原图
                                imgEditingPic.setImageBitmap(bitmap);
                                editBitmap = null;
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
