package cn.bill56.youphoto.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import cn.bill56.youphoto.R;
import cn.bill56.youphoto.adapter.GuideViewPagerAdapter;

/**
 * 承载引导页的活动，当用户第一次安装使用该软件时会弹出引导页
 *
 * @author Bill56
 *
 */
public class GuideActivity extends BaseActivity implements OnPageChangeListener {

    // 承载引导页的ViewPager实例
    private ViewPager vp;
    // 承载vp的adapter实例
    private GuideViewPagerAdapter guideAdapter;
    // 承载引导页的view集合
    private List<View> views;
    // 承载导航点的数组
    private ImageView[] dots;
    // 承载导航点的id数组
    private int[] ids = { R.id.image_p1, R.id.image_p2, R.id.image_p3 };
    // 进去界面的按钮
    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载引导页面布局
        setContentView(R.layout.activity_guide);
        // 初始化导航图
        initViews();
        // 初始化导航点
        initDots();
    }

    /**
     * 初始化引导页的内容
     */
    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        // 添加引导界面
        views.add(inflater.inflate(R.layout.guide_one, null));
        views.add(inflater.inflate(R.layout.guide_two, null));
        views.add(inflater.inflate(R.layout.guide_three, null));
        // 实例化adapter
        guideAdapter = new GuideViewPagerAdapter(this, views);
        vp = (ViewPager) findViewById(R.id.vp_guide);
        // 绑定adapter
        vp.setAdapter(guideAdapter);
        // 设置监听事件
        vp.setOnPageChangeListener(this);
        // 获取进入按钮的实例
        btnEnter = (Button) views.get(2).findViewById(R.id.button_enter);
        // 设置按钮的监听事件
        btnEnter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 初始化导航点
     */
    private void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // 当滑动状态发生改变时调用

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // 当页面被滑动时调用

    }

    @Override
    public void onPageSelected(int arg0) {
        // 当页面被选中时调用
        for (int i = 0; i < ids.length; i++) {
            if (arg0 == i) {
                // 将导航点设置为被选中的图片
                dots[i].setImageResource(R.drawable.login_point_selected);
            } else {
                // 将导航点设置为没有被选中的图片
                dots[i].setImageResource(R.drawable.login_point);
            }
        }
    }

}
