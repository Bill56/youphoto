package cn.bill56.youphoto.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.bill56.youphoto.util.ActivityUtil;

/**
 * 承载所有活动的基类
 * Created by Bill56 on 2016/6/14.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 当活动被创建的时候执行
     *
     * @param savedInstanceState 保存实例的对象
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 回调父类
        super.onCreate(savedInstanceState);
        // 添加到活动管理类
        ActivityUtil.addActivity(this);
    }

    /**
     * 当活动被销毁的时候执行
     */
    @Override
    protected void onDestroy() {
        // 回调父类
        super.onDestroy();
        // 荣活动管理类移除
        ActivityUtil.removeActivity(this);
    }

}
