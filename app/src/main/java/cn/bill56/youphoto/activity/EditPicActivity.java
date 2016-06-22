package cn.bill56.youphoto.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bill56.youphoto.R;
import cn.bill56.youphoto.customview.GraffitiLayer;
import cn.bill56.youphoto.customview.Point;
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
    @Bind(R.id.btn_edit_graffiti)
    Button btnbGraffiti;

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
    // 滤镜选择栏——浮雕
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

    // 涂鸦画笔选择栏
    @Bind(R.id.ll_edit_graffiti)
    LinearLayout llEditGaffiti;
    // 涂鸦画笔选择栏——seekbarsize
    @Bind(R.id.seekBar_pait_size)
    SeekBar seekBarPaitSize;
    // 涂鸦画笔选择栏——白色
    @Bind(R.id.btn_graffiti_white)
    ImageButton btnPaitWhite;
    // 涂鸦画笔选择栏——红色
    @Bind(R.id.btn_graffiti_red)
    ImageButton btnPaitRed;
    // 涂鸦画笔选择栏——绿色
    @Bind(R.id.btn_graffiti_green)
    ImageButton btnPaitGreen;
    // 涂鸦画笔选择栏——蓝色
    @Bind(R.id.btn_graffiti_blue)
    ImageButton btnPaitBlue;
    // 涂鸦画笔选择栏——黄色
    @Bind(R.id.btn_graffiti_yellow)
    ImageButton btnPaitYellow;
    // 涂鸦画笔选择栏——撤销
    @Bind(R.id.imgBtn_undo)
    ImageButton btnImgUndo;
    // 涂鸦画笔选择栏——重做
    @Bind(R.id.imgBtn_redo)
    ImageButton btnImgRedo;

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
    // 涂鸦的位图
    private Bitmap panel;
    // 是否放弃修改的标志
    private boolean isQuitUpdate = false;
    // 是否在涂鸦的标志
    private boolean mIsDrawing = false;

    /**
     * 涂鸦的图层栈
     */
    // 创建涂鸦图层栈
    private Stack<GraffitiLayer> layers = new Stack<GraffitiLayer>();
    // 创建保存出栈的图层
    private Stack<GraffitiLayer> popLayers = new Stack<>();

    /**
     * 监听器对象
     */
    private OnButtonClickListener buttonClickListener;
    private OnSeekBarProgressChangedListener seekBarProgressChangedListener;
    private OnImgTouchListener imgTouchListener;

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
        buttonClickListener = new OnButtonClickListener();
        // 创建seekbar改变事件监听器
        seekBarProgressChangedListener = new OnSeekBarProgressChangedListener();
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
        btnbGraffiti.setOnClickListener(buttonClickListener);
        // 为图片的滤镜选择栏按钮注册
        btnFilterOld.setOnClickListener(buttonClickListener);
        btnFilterGray.setOnClickListener(buttonClickListener);
        btnFilterReversal.setOnClickListener(buttonClickListener);
        btnFilterNostalgia.setOnClickListener(buttonClickListener);
        btnFilterUncolor.setOnClickListener(buttonClickListener);
        btnFilterHighSaturation.setOnClickListener(buttonClickListener);
        btnFilterRelief.setOnClickListener(buttonClickListener);
        // 为图片的增强拖动seekbar注册监听器
        seekBarColor.setOnSeekBarChangeListener(seekBarProgressChangedListener);
        seekBarSaturation.setOnSeekBarChangeListener(seekBarProgressChangedListener);
        seekBarLight.setOnSeekBarChangeListener(seekBarProgressChangedListener);
        // 为涂鸦的颜色选择和画笔粗细选择监听器
        btnPaitWhite.setOnClickListener(buttonClickListener);
        btnPaitRed.setOnClickListener(buttonClickListener);
        btnPaitGreen.setOnClickListener(buttonClickListener);
        btnPaitBlue.setOnClickListener(buttonClickListener);
        btnPaitYellow.setOnClickListener(buttonClickListener);
        seekBarPaitSize.setOnSeekBarChangeListener(seekBarProgressChangedListener);
        // 为涂鸦的撤销和重做注册监听器
        btnImgUndo.setOnClickListener(buttonClickListener);
        btnImgRedo.setOnClickListener(buttonClickListener);
        // 添加图片的触摸事件监听器
        imgTouchListener = new OnImgTouchListener();
        imgEditingPic.setOnTouchListener(imgTouchListener);
    }

    /**
     * 自定义类
     * 按钮点击事件监听器
     */
    class OnButtonClickListener implements View.OnClickListener {

        /**
         * 重写的父类方法，当监听的按钮被点击后触发执行
         *
         * @param v 事件源
         */
        @SuppressLint("NewApi")
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
                    doGraffiti();
                    break;
                /**
                 * 以下是滤镜选择
                 */
                // 点击的是滤镜选择——原图
                case R.id.btn_filter_old:
                    editBitmap = bitmap;
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 将增强效果还原
                    seekBarColor.setProgress(127);
                    seekBarSaturation.setProgress(127);
                    seekBarLight.setProgress(127);
                    // 赋空涂鸦
                    panel = null;
                    break;
                // 点击的是滤镜选择——灰度
                case R.id.btn_filter_gray:
                    editBitmap = ImageUtil.handleImage2FilterGray(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 赋空涂鸦
                    panel = null;
                    break;
                // 点击的是滤镜选择——反转
                case R.id.btn_filter_reversal:
                    editBitmap = ImageUtil.handleImage2FilterReversal(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 赋空涂鸦
                    panel = null;
                    break;
                // 点击的是滤镜选择——怀旧
                case R.id.btn_filter_nostalgia:
                    editBitmap = ImageUtil.handleImage2FilterNostalgia(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 赋空涂鸦
                    panel = null;
                    break;
                // 点击的是滤镜选择——去色
                case R.id.btn_filter_uncolor:
                    editBitmap = ImageUtil.handleImage2FilterUncolor(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 赋空涂鸦
                    panel = null;
                    break;
                // 点击的是滤镜选择——高饱和度
                case R.id.btn_filter_high_saturation:
                    editBitmap = ImageUtil.handleImage2FilterHighSaturation(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 赋空涂鸦
                    panel = null;
                    break;
                // 点击的是滤镜选择——浮雕
                case R.id.btn_filter_relief:
                    editBitmap = ImageUtil.handleImage2FilterRelief(bitmap);
                    imgEditingPic.setImageBitmap(editBitmap);
                    // 赋空涂鸦
                    panel = null;
                    break;
                /**
                 * 以下是涂鸦的画笔选择
                 */
                // 点击的是白色
                case R.id.btn_graffiti_white:
                    // 设置画笔颜色
                    imgTouchListener.paint.setColor(Color.WHITE);
                    break;
                // 点击的是红色
                case R.id.btn_graffiti_red:
                    // 设置画笔颜色
                    imgTouchListener.paint.setColor(Color.RED);
                    break;
                // 点击的是绿色
                case R.id.btn_graffiti_green:
                    // 设置画笔颜色
                    imgTouchListener.paint.setColor(Color.GREEN);
                    break;
                // 点击的是蓝色
                case R.id.btn_graffiti_blue:
                    // 设置画笔颜色
                    imgTouchListener.paint.setColor(Color.BLUE);
                    break;
                // 点击的是黄色
                case R.id.btn_graffiti_yellow:
                    // 设置画笔颜色
                    imgTouchListener.paint.setColor(Color.YELLOW);
                    break;
                /**
                 * 以下是涂鸦的撤销和重做
                 */
                case R.id.imgBtn_undo:
                    doImgUndo();
                    break;
                case R.id.imgBtn_redo:
                    doImgRedo();
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
            // 关闭涂鸦画笔
            mIsDrawing = false;
            // 赋空涂鸦
            panel = null;
        }

        /**
         * 点击编辑栏的旗帜后执行的方法
         * 图片变成旗帜飞扬的效果
         */
        private void doFlag() {
            // 第一次使用
            if (editBitmap == null) {
                // 修改后的图片
                editBitmap = ImageUtil.handleImage2Flag(bitmap);
            } else {
                editBitmap = ImageUtil.handleImage2Flag(editBitmap);
            }
            // 显示
            imgEditingPic.setImageBitmap(editBitmap);
            // 关闭涂鸦画笔
            mIsDrawing = false;
            // 赋空涂鸦
            panel = null;
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
                    seekBarColor.setProgress(127);
                    seekBarSaturation.setProgress(127);
                    seekBarLight.setProgress(127);
                }
                // 关闭其他已经显示的编辑选择栏
                LinearLayoutUtil.hiddenAllLinearLayouts();
                // 开启动画，显示滤镜选择栏
                AnimatorUtil.animateOpen(llEditPower, mHiddenViewMeasuredHeight);
            } else {
                // 显示了则关闭动画显示
                AnimatorUtil.animateClose(llEditPower);
            }
            // 关闭涂鸦画笔
            mIsDrawing = false;
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
            // 关闭涂鸦画笔
            mIsDrawing = false;
        }

        /**
         * 点击涂鸦后执行的方法
         * 显示涂鸦的画笔颜色选择和粗细选择
         */
        private void doGraffiti() {
            // 如果没有显示则开启动画显示
            if (llEditGaffiti.getVisibility() == View.GONE) {
                // 关闭其他已经显示的编辑选择栏
                LinearLayoutUtil.hiddenAllLinearLayouts();
                // 开启动画，显示滤镜选择栏
                AnimatorUtil.animateOpen(llEditGaffiti, mHiddenViewMeasuredHeight);
                // 启动涂鸦画板的画笔
                mIsDrawing = true;
            } else {
                // 显示了则关闭动画显示
                AnimatorUtil.animateClose(llEditGaffiti);
                mIsDrawing = false;
            }
        }

        /**
         * 点击涂鸦的撤销按钮后执行的方法
         * 撤销最近的一次涂鸦
         */
        private void doImgUndo() {
            // 当涂鸦的图层不为空且数量不为0的时候执行
            if (layers != null && layers.size() > 0) {
                // 将最近一个图层出栈
                GraffitiLayer popGraffitiLayer = layers.pop();
                // 将出栈元素放入出栈的图层栈
                popLayers.push(popGraffitiLayer);
                // 重绘涂鸦板
                drawGraffitiLayer();
            } else {
                ToastUtil.show(EditPicActivity.this,R.string.editpic_toast_no_undo);
            }
        }

        /**
         * 点击涂鸦的重做按钮后执行的方法
         * 将撤销的涂鸦重做回来
         */
        private void doImgRedo() {
            // 当涂鸦的图层不为空且数量不为0的时候执行
            if (popLayers != null && popLayers.size() > 0) {
                // 获得最近一次撤销的涂鸦图层
                GraffitiLayer recentPopLayer = popLayers.pop();
                // 将获得的元素放入涂鸦图层栈
                layers.push(recentPopLayer);
                // 重绘涂鸦板
                drawGraffitiLayer();
            } else {
                ToastUtil.show(EditPicActivity.this, R.string.editpic_toast_no_redo);
            }
        }

        // 绘制涂鸦画板
        private void drawGraffitiLayer() {
            panel = null;
            editBitmap = null;
            imgTouchListener.initPanel();
            // 遍历图层
            for (int i = 0; i < layers.size(); i++) {
                // 获得每个图层的点集
                List<Point> layerPoints = layers.get(i).getmPoints();
                // 获得每个图层的画笔对象
                Paint layerPaint = layers.get(i).getmPait();
                // 遍历点集，将前一个点作为后一个点的起始点进行涂鸦线绘制
                for (int j = 1; j < layerPoints.size(); j++) {
                    // 获得前一个点对象
                    Point formerP = layerPoints.get(j - 1);
                    // 获得当前点对象
                    Point currentP = layerPoints.get(j);
                    // 绘制涂鸦线
                    imgTouchListener.canvas.drawLine(formerP.getX(), formerP.getY(),
                            currentP.getX(), currentP.getY(),
                            layerPaint);
                }
            }
            imgEditingPic.setImageBitmap(editBitmap);
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
                    // 关闭涂鸦画笔
                    mIsDrawing = false;
                } else {
                    ToastUtil.show(EditPicActivity.this, R.string.pic_save_no_update);
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
     * 自定义类：
     * 当seekbar进度条拖动改变的时候的事件监听器
     */
    class OnSeekBarProgressChangedListener implements SeekBar.OnSeekBarChangeListener {

        // 进度条的最大值
        private final int MAX_VALUE = 255;
        // 进度条的最小值
        private final int MID_VALUE = 127;
        // 保存色调，饱和度，亮度的值得成员变量
        private float mHue, mStauration, mLum;

        /**
         * 当监听的seekbar的进度值被改变的时候触发执行
         *
         * @param seekBar  事件源
         * @param progress 进度值
         * @param fromUser 是否由用户改变
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 通过获取seekBar的Id来判断拖动的是哪个seekbar
            switch (seekBar.getId()) {
                // 拖动的是色调
                case R.id.seekBar_color:
                    mHue = (progress - MID_VALUE) * 1.0F / MID_VALUE * 180;
                    break;
                // 拖动的是饱和度
                case R.id.seekBar_saturation:
                    mStauration = progress * 1.0F / MID_VALUE;
                    break;
                // 拖动的是亮度
                case R.id.seekBar_light:
                    mLum = progress * 1.0F / MID_VALUE;
                    break;
                // 拖动的画笔粗细
                case R.id.seekBar_pait_size:
                    imgTouchListener.paint.setStrokeWidth(progress);
                    break;
                default:
                    break;
            }
            // 当选择的seekBar不是画笔粗细的时候
            if (seekBar.getId() != R.id.seekBar_pait_size) {
                // 改变图片的效果
                // 第一次使用
                if (editBitmap == null) {
                    // 改变原图
                    editBitmap = ImageUtil.handleImageEffect(bitmap, mHue, mStauration, mLum);
                } else {
                    // 改变正在编辑的图片
                    editBitmap = ImageUtil.handleImageEffect(editBitmap, mHue, mStauration, mLum);
                }
                // 显示
                imgEditingPic.setImageBitmap(editBitmap);
                // 赋空涂鸦
                panel = null;
            }
        }

        /**
         * 当监听的seekbar开始滑动的时候执行
         *
         * @param seekBar 事件源
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * 当监听的seekbar停止滑动的时候执行
         *
         * @param seekBar 事件源
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * 自定义类：
     * 当图片显示的控件的被触摸的时候执行
     */
    class OnImgTouchListener implements View.OnTouchListener {

        // 涂鸦的画布
        Canvas canvas;
        // 涂鸦的画笔
        Paint paint = new Paint();
        // 记录按下的坐标的x值
        private float downX;
        // 记录按下的坐标的y值
        private float downY;
        // 点集对象
        private List<Point> points = null;

        /**
         * 手指点击的时候执行
         *
         * @param v     事件源
         * @param event 事件对象
         * @return true表示不需要返回给上层处理
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 当涂鸦工具启用的时候
            if (mIsDrawing) {
                switch (event.getAction()) {
                    //按下触发
                    case MotionEvent.ACTION_DOWN:
                        LogUtil.d(LogUtil.TAG, "按下");
                        // 初始化画板
                        initPanel();
                        downX = event.getX();
                        downY = event.getY();
                        // 记录下坐标值，并且封装到点对象
                        Point startP = new Point(downX, downY);
                        // 创建点集列表
                        points = new ArrayList<Point>();
                        // 将起点添加到列表
                        points.add(startP);
                        break;
                    //移动触发
                    case MotionEvent.ACTION_MOVE:
                        LogUtil.d(LogUtil.TAG, "移动");
                        float moveX = event.getX();
                        float moveY = event.getY();
                        // 将点添加到点集
                        points.add(new Point(moveX, moveY));
                        canvas.drawLine(downX, downY, moveX, moveY, paint);
                        imgEditingPic.setImageBitmap(editBitmap);
                        downX = moveX;
                        downY = moveY;
                        break;
                    //松开触发
                    case MotionEvent.ACTION_UP:
                        LogUtil.d(LogUtil.TAG, "松开");
                        // 拷贝画笔
                        Paint curPait = new Paint(paint);
                        // 将点集对象放到一次涂鸦图层对象中
                        GraffitiLayer graffitiLayer = new GraffitiLayer(points, curPait);
                        // 将图层对象入栈
                        layers.push(graffitiLayer);
                        break;
                    default:
                        break;
                }
            }
            //false只触发按下  true全触发
            return true;
        }

        /**
         * 初始化涂鸦画板
         */
        public void initPanel() {
            if (panel == null) {
                //画纸
                panel = Bitmap.createBitmap(imgEditingPic.getWidth(), // 涂鸦的宽度
                        imgEditingPic.getHeight(),  // 涂鸦的高度
                        Bitmap.Config.ARGB_8888);   // 涂鸦的调色板
                canvas = new Canvas(panel);
                //指定颜色
//                paint.setColor(Color.WHITE);
                //指定宽度
                paint.setStrokeWidth(25);
                RectF rectF = new RectF(0, 0, imgEditingPic.getWidth(), imgEditingPic.getHeight());
                if (editBitmap == null) {
                    canvas.drawBitmap(bitmap, null, rectF, null);
                } else {
                    canvas.drawBitmap(editBitmap, null, rectF, null);
                }
                // 将创建好的画布给editBitmap;
                editBitmap = panel;
                imgEditingPic.setImageBitmap(editBitmap);
            }
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
                                // 关闭涂鸦画笔
                                mIsDrawing = false;
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
