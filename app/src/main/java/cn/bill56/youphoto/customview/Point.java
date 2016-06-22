package cn.bill56.youphoto.customview;

/**
 * 封装点击后的点的类
 * Created by Bill56 on 2016/6/20.
 */
public class Point {

    // x坐标
    private float x;
    // y坐标
    private float y;

    /**
     * 构造方法
     *
     * @param x x坐标
     * @param y y坐标
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // getter与setter方法
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * 重写的toString方法
     * @return
     */
    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
