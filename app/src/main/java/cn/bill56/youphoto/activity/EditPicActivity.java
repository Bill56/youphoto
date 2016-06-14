package cn.bill56.youphoto.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cn.bill56.youphoto.R;


/**
 * 承载正在编辑图片视图的活动
 * Created by Bill56 on 2016/6/14.
 */
public class EditPicActivity extends BaseActivity {

    // 正在编辑的图片
    private ImageView imgEditingPic;

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
        // 绑定控件
        imgEditingPic = (ImageView) findViewById(R.id.img_editing_pic);
        // 获取传递过来的intent中的数据
        String imagePath = getIntent().getStringExtra("EDIT_PIC");
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(imagePath);
        /*// 压缩图片
        Bitmap newBitmap = compressImage(bitmap);*/
        imgEditingPic.setImageBitmap(bitmap);
    }

    /**
     * 图片质量压缩,保证图片大小小于100kb
     *
     * @param image 原位图
     * @return 压缩后的位图
     */
    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos即清空baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.PNG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }


}
