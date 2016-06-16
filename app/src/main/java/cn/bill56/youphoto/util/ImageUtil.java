package cn.bill56.youphoto.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 图片的算法封装类，将图片进行效果处理后返回的结果
 * Created by Bill56 on 2016/6/16.
 */
public class ImageUtil {

    /**
     * 将图片变成旗帜飞扬的效果
     *
     * @param bm 原图
     * @return 修改后的图片
     */
    public static Bitmap handleImage2Flag(Bitmap bm) {
        // 根据原图新建一幅位图
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 以位图为背景创建画布
        Canvas canvas = new Canvas(bmp);
        // 需要绘图的变量
        final int WIDTH = 200;
        final int HEIGHT = 200;
        int COUNT = (WIDTH + 1) * (HEIGHT + 1);
        float[] verts = new float[COUNT * 2];
        float[] orig = new float[COUNT * 2];
        float A;
        float bitmapWidth = bmp.getWidth();
        float bitmapHeight = bmp.getHeight();
        // 初始化布局效果
        int index = 0;
        for (int y = 0; y <= HEIGHT; y++) {
            float fy = bitmapHeight * y / HEIGHT;
            for (int x = 0; x <= WIDTH; x++) {
                float fx = bitmapWidth * x / WIDTH;
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy + 100;
                index += 1;
            }
        }
        A = 50;
        // 改变像素点效果
        for (int j = 0; j <= HEIGHT; j++) {
            for (int i = 0; i <= WIDTH; i++) {
                verts[(j * (WIDTH + 1) + i) * 2 + 0] += 0;
                float offsetY =
                        (float) Math.sin((float) i / WIDTH * 2 * Math.PI +
                                Math.PI);
                verts[(j * (WIDTH + 1) + i) * 2 + 1] =
                        orig[(j * WIDTH + i) * 2 + 1] + offsetY * A;
            }
        }
        canvas.drawBitmapMesh(bm, WIDTH, HEIGHT,
                verts, 0, null, 0, null);
        return bmp;
    }

    /**
     * 将原图旋转
     *
     * @param bm     原图
     * @param degree 旋转角度
     * @param x      旋转中心点的x坐标
     * @param y      旋转中心点的y坐标
     * @return 修改后的图片
     */
    public static Bitmap handleImage2Romote(Bitmap bm, float degree, int x, int y) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 创建变化矩阵
        Matrix matrix = new Matrix();
        // 设置变换矩阵的值
        matrix.setRotate(degree, x, y);
        // 创建画布
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bm, matrix, null);
        return bmp;
    }

    /**
     * 将原图滤镜设为灰度效果
     *
     * @param bm 原图
     * @return 灰度效果图
     */
    public static Bitmap handleImage2FilterGray(Bitmap bm) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 以位图为背景创建画布
        Canvas canvas = new Canvas(bmp);
        // 颜色矩阵——呈现灰度的颜色矩阵
        float[] colorMatrixPots = new float[]{
                0.33f, 0.59f, 0.11f, 0, 0,
                0.33f, 0.59f, 0.11f, 0, 0,
                0.33f, 0.59f, 0.11f, 0, 0,
                0, 0, 0, 1, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        // 将矩阵设置到android的矩阵中
        colorMatrix.set(colorMatrixPots);
        // 设置画笔
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 将原图滤镜设为反转效果
     *
     * @param bm 原图
     * @return 反转效果图
     */
    public static Bitmap handleImage2FilterReversal(Bitmap bm) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 以位图为背景创建画布
        Canvas canvas = new Canvas(bmp);
        // 颜色矩阵——呈现反转的颜色矩阵
        float[] colorMatrixPots = new float[]{
                -1, 0, 0, 1, 1,
                0, -1, 0, 1, 1,
                0, 0, -1, 1, 1,
                0, 0, 0, 1, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        // 将矩阵设置到android的矩阵中
        colorMatrix.set(colorMatrixPots);
        // 设置画笔
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 将原图滤镜设为怀旧效果
     *
     * @param bm 原图
     * @return 怀旧效果图
     */
    public static Bitmap handleImage2FilterNostalgia(Bitmap bm) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 以位图为背景创建画布
        Canvas canvas = new Canvas(bmp);
        // 颜色矩阵——呈现怀旧的颜色矩阵
        float[] colorMatrixPots = new float[]{
                0.393f, 0.769f, 0.189f, 0, 0,
                0.349f, 0.686f, 0.168f, 0, 0,
                0.272f, 0.534f, 0.131f, 0, 0,
                0, 0, 0, 1, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        // 将矩阵设置到android的矩阵中
        colorMatrix.set(colorMatrixPots);
        // 设置画笔
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 将原图滤镜设为去色效果
     *
     * @param bm 原图
     * @return 去色效果图
     */
    public static Bitmap handleImage2FilterUncolor(Bitmap bm) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 以位图为背景创建画布
        Canvas canvas = new Canvas(bmp);
        // 颜色矩阵——呈现去色的颜色矩阵
        float[] colorMatrixPots = new float[]{
                1.5f, 1.5f, 1.5f, 0, -1,
                1.5f, 1.5f, 1.5f, 0, -1,
                1.5f, 1.5f, 1.5f, 0, -1,
                0, 0, 0, 1, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        // 将矩阵设置到android的矩阵中
        colorMatrix.set(colorMatrixPots);
        // 设置画笔
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 将原图滤镜设为高饱和度效果
     *
     * @param bm 原图
     * @return 高饱和度效果图
     */
    public static Bitmap handleImage2FilterHighSaturation(Bitmap bm) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 以位图为背景创建画布
        Canvas canvas = new Canvas(bmp);
        // 颜色矩阵——呈现去色的颜色矩阵
        float[] colorMatrixPots = new float[]{
                1.438f, -0.122f, -0.016f, 0, -0.03f,
                -0.062f, 1.378f, -0.016f, 0, 0.05f,
                -0.062f, -0.122f, 1.483f, 0, -0.02f,
                0, 0, 0, 1, 0};
        ColorMatrix colorMatrix = new ColorMatrix();
        // 将矩阵设置到android的矩阵中
        colorMatrix.set(colorMatrixPots);
        // 设置画笔
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 将原图滤镜设为底片效果
     *
     * @param bm 原图
     * @return 底片效果图
     */
    public static Bitmap handleImage2FilterRelief(Bitmap bm) {
        // 创建图片副本
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 图片属性
        int width = bm.getWidth();
        int height = bm.getHeight();
        int color;
        // 三原色加透明度
        int r, g, b, a;
        int[] oldPx = new int[width * height];
        int[] newPx = new int[width * height];
        bm.getPixels(oldPx, 0, width, 0, 0, width, height);
        // 遍历像素点
        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);
            // 底片的算法
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;
            // 超出结果的判断
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            newPx[i] = Color.argb(a, r, g, b);
        }
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

}
