package cn.bill56.youphoto.activity;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import cn.bill56.youphoto.R;
import cn.bill56.youphoto.util.LogUtil;
import cn.bill56.youphoto.util.ToastUtil;

/**
 * 承载主界面布局的活动，可以在此界面选择需要编辑的图片
 */
public class MainActivity extends BaseActivity {

    /**
     * 需要用到回传的常量
     */
    // 调用拍照的时候
    public static final int TAKE_PHOTO = 101;
    // 从图库选择
    public static final int CHOOSE_FROM_ALBUM = 102;
    // 剪裁
    public static final int CROP_PHOTO = 103;

    // 图片格式的URI对象
    private Uri imageUri;
    // 用于保存图片的文件引用
    private File outputImage;

    // 工具栏
    private Toolbar toolbar;
    // 记录按下back键后的毫秒数
    private long lastBackPressed;
    // 点击的按钮
    // 选择图片
    private Button btnSelpic;
    // 拍照
    private Button btnTakeaphoto;
    // 选择图片的路径
    private String imageSelPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化导航
        initView();
    }

    /**
     * 初始化导航布局
     */
    private void initView() {
        // 初始化工具栏布局
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        // 设置顶部返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_main_title);
        // 绑定选择按钮
        btnSelpic = (Button) findViewById(R.id.btn_selpic);
        btnTakeaphoto = (Button) findViewById(R.id.btn_takeaphoto);
        // 为按钮设置点击的事件监听器
        OnButtonListener buttonListener = new OnButtonListener();
        btnSelpic.setOnClickListener(buttonListener);
        btnTakeaphoto.setOnClickListener(buttonListener);
    }

    /**
     * 加载选项菜单
     *
     * @param menu 菜单对象
     * @return true表示不需要提交给父布局
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    /**
     * 当选项菜单被选中的时候执行
     *
     * @param item 选中的菜单项
     * @return true表示不需要提交给父布局
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 根据选择的id进行不同的事件响应
        switch (item.getItemId()) {
            // 点击的是向上按钮，将其功能改成返回键
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当选择的活动回传数据的时候调用
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据请求码处理不同的返回结果
        switch (requestCode) {
            case TAKE_PHOTO:
                // 当拍照程序返回结果为正确的时候
                if (resultCode == RESULT_OK) {
                    // 调用剪裁程序活动
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CHOOSE_FROM_ALBUM:
                // 当选择照片正确返回的时候
                if (resultCode == RESULT_OK) {
                    //判断手机系统的版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitkat(data);
                    }
                }
                break;
            case CROP_PHOTO:
                // 当剪裁程序返回的结果是正确的时候
                if (resultCode == RESULT_OK) {
                    // 将剪裁后的图片的路径传递给编辑图片的活动
                    Intent editIntent = new Intent(this, EditPicActivity.class);
                    editIntent.putExtra("EDIT_PIC", imageSelPath);
                    startActivity(editIntent);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 当系统为4.4以上版本的时候，必须以以下方式才可以读取图片
     *
     * @param data 回传的intent对象
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        imageUri = uri;
        if (DocumentsContract.isDocumentUri(this, uri)) {
            LogUtil.d("YYW", "获得了图片的Uri信息");
            // 如果文档类型是uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri
                    .getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                LogUtil.d("YYW", "com.android.providers.media.documents");
            } else if ("com.android.providers.downloads.documents".equals(uri
                    .getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
                LogUtil.d("YYW", "com.android.providers.downloads.documents");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果不是document类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
            LogUtil.d("YYW", "content");
        } else {
            LogUtil.d("YYW", "未知类型");
        }
        imageSelPath = imagePath;
        displayImage(imageSelPath);
    }

    /**
     * 当系统为4.4以下的时候必须以以下方式读取图片
     *
     * @param data 回传的intent对象
     */
    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        imageUri = uri;
        imageSelPath = getImagePath(uri, null);
        displayImage(imageSelPath);
    }

    /**
     * 获得选择的图片的真实路径
     *
     * @param uri       图片的标识符
     * @param selection 选择的选项，可为空
     * @return 选择的图片的真实路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri跟selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 将图片传递给编辑活动的方法
     *
     * @param imagePath 图片路径
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CROP_PHOTO);
        } else {
            ToastUtil.show(this, R.string.fail_get_image);
        }
    }

    /**
     * 按下返回键的时候执行
     */
    @Override
    public void onBackPressed() {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        // 当两次按下的时间在2秒内的时候
        if (currentTime - lastBackPressed < 2000) {
            super.onBackPressed();
        } else {
            ToastUtil.show(this, R.string.activity_toast_quit);
        }
        lastBackPressed = currentTime;
    }

    /**
     * 实现了点击事件监听器的自定义按钮点击类，用于监听按钮点击的事件
     */
    class OnButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 点击的选择图片
                case R.id.btn_selpic:
                    doSelpic();
                    break;
                // 点击的拍照
                case R.id.btn_takeaphoto:
                    doTakeaphoto();
                    break;
                default:
                    break;
            }
        }

        /**
         * 点击选择图片按钮后执行的代码
         */
        private void doSelpic() {
            // 获取设备的根目录
            File sdDir = Environment.getExternalStorageDirectory();
            // 创建缓存目录
            File cacheDir = new File(sdDir, "UPhotocache");
            // 如果不存在目录，则创建
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            // 创建File对象，用于存储牌照后的图片
            outputImage = new File(cacheDir, "output_image.jpg");
            try {
                // 如果存在则删除
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                // 创建一个新的文件
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 启动选择图片的活动
            imageUri = Uri.fromFile(outputImage);
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CHOOSE_FROM_ALBUM);
        }

        /**
         * 点击拍照按钮后执行的代码
         */
        private void doTakeaphoto() {
            // 获取设备的根目录
            File sdDir = Environment.getExternalStorageDirectory();
            // 创建缓存目录
            File cacheDir = new File(sdDir, "UPhotocache");
            // 如果不存在目录，则创建
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            // 创建File对象，用于存储牌照后的图片
            outputImage = new File(cacheDir, "output_image.jpg");
            try {
                // 如果存在则删除
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                // 创建一个新的文件
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = Uri.fromFile(outputImage);
            // 更新保存的路径
            imageSelPath = outputImage.getAbsolutePath();
            // 启动拍照程序
            Intent takeIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 设置回传的请求码
            startActivityForResult(takeIntent, TAKE_PHOTO);
        }

    }

}
