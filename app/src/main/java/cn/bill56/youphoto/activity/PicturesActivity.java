package cn.bill56.youphoto.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cn.bill56.youphoto.R;

/**
 * 承载显示用该软件修改保存的图片
 * Created by Bill56 on 2016/6/19.
 */
public class PicturesActivity extends BaseActivity {

    // 工具栏
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
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
        getSupportActionBar().setTitle(R.string.activity_pictures_title);
    }

    /**
     * 加载选项菜单
     *
     * @param menu 菜单对象
     * @return true表示不需要提交给父布局
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单布局
        getMenuInflater().inflate(R.menu.pictures_option,menu);
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

}
