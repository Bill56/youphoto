package cn.bill56.youphoto.customview;

import android.graphics.Paint;

import java.util.List;

/**
 * 保存一次涂鸦的类：
 * 从按下手指到手指离开为一次涂鸦
 * Created by Bill56 on 2016/6/22.
 */
public class GraffitiLayer {

    // 一次涂鸦的所有记录点，按顺序存储
    private List<Point> mPoints;
    // 一次涂鸦的画笔对象
    private Paint mPait;

    /**
     * 构造方法
     *
     * @param points 坐标点集
     * @param pait   画笔对象
     */
    public GraffitiLayer(List<Point> points, Paint pait) {
        this.mPoints = points;
        this.mPait = pait;
    }

    // getter与setter方法
    public List<Point> getmPoints() {
        return mPoints;
    }

    public void setmPoints(List<Point> mPoints) {
        this.mPoints = mPoints;
    }

    public Paint getmPait() {
        return mPait;
    }

    public void setmPait(Paint mPait) {
        this.mPait = mPait;
    }

}
