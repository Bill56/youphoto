package cn.bill56.youphoto.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 自定义的显示图片的View，由于会用到涂鸦重绘，所以采用surfaceView
 * 重绘时会开启子线程，提高程序效率
 */
public class EditImageSufaceView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    // holder类对象
    private SurfaceHolder mHolder;
    // 画布对象
    private Canvas mCanvas;
    // 是否在绘图，表示手指的触摸
    private boolean mIsDrawing;
    // 路径对象
    private Path mPath;
    // 画笔对象
    private Paint mPaint;
    // 是否开启了涂鸦功能的标志
    public boolean mIsPaiting;
    // 该视图所承载的位图对象
    public Bitmap curBitmap = null;

    // 构造方法
    public EditImageSufaceView(Context context) {
        super(context);
        initView();
    }

    // 构造方法
    public EditImageSufaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    // 构造方法
    public EditImageSufaceView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    // 构造方法
    public EditImageSufaceView(Context context, AttributeSet attrs,
                               int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 初始化视图的方法
     */
    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        // 设置聚焦
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        mPath = new Path();
        mPaint = new Paint();
        // 设置默认画笔
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(40);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    /**
     * 子线程中执行的方法
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        while (mIsDrawing) {
            draw();
        }
        long end = System.currentTimeMillis();
        // 50 - 100
        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 绘图的具体逻辑代码
     */
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
//            mCanvas.drawColor(Color.BLACK);
            mCanvas.drawBitmap(curBitmap, 0, 0, null);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     * 检测手指点击的事件
     *
     * @param event 事件对象
     * @return True表示不需要返回给上层处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsPaiting) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return true;
    }

    public void setImageBitmap(Bitmap bitmap) {
        curBitmap = bitmap;
    }

}