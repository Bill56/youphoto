package cn.bill56.youphoto.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import cn.bill56.youphoto.R;

/**
 * 欢迎页面，延时2秒
 *
 * @author Bill56
 *
 */
public class WelcomeActivity extends BaseActivity {

    // 是否是第一次安装进来
    private boolean isFirstIn = false;
    // 延时的时间
    private static final int TIME = 2000;
    // 跳转到哪一个界面的参数
    // 跳转到登录界面
    private static final int GO_LOGIN = 1000;
    // 跳转到引导界面
    private static final int GO_GUIDE = 1001;
    // 沉睡时间结束后的处理
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // 跳转到登录界面
                case GO_LOGIN:
                    goLogin();
                    break;
                // 跳转到引导界面
                case GO_GUIDE:
                    goGuide();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 执行初始化方法
        init();
    }

    /**
     * 进行初始化的一些操作： 判断是否是第一次进入该程序
     */
    private void init() {
        // 获取SharedPreferences实例
        SharedPreferences preferences = getSharedPreferences("welcome_state",
                MODE_PRIVATE);
        // 获取该文件中有没有设置该值，如果没有设置说明是第一次进入
        isFirstIn = preferences.getBoolean("IS_FIRST_IN", true);
        // 不是第一次进入
        if (!isFirstIn) {
            handler.sendEmptyMessageDelayed(GO_LOGIN, TIME);
        } else {
            handler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
            // 将值储存起来，并设为false
            // 获取Editor实例
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("IS_FIRST_IN", false);
            editor.commit();
        }
    }

    /**
     * 跳转到登录界面的方法
     */
    private void goLogin() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * 跳转到引导界面的方法
     */
    private void goGuide() {
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        finish();
    }

}
