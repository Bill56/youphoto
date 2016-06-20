package cn.bill56.youphoto.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bill56.youphoto.R;
import cn.bill56.youphoto.adapter.PictureAdapter;

/**
 * 承载显示用该软件修改保存的图片
 * Created by Bill56 on 2016/6/19.
 */
public class PicturesActivity extends BaseActivity {

    // 工具栏
    private Toolbar toolbar;
    // 线性布局，当word不存在的时候
    private LinearLayout llFileListEmpty;
    // 列表视图
    private RecyclerView recyclerViewPictures;
    // 绑定的视图的适配器
    private PictureAdapter pictureAdapter;
    // 数据
    private List<File> imgFiles;
    // 布局描述的数组列表
    private RecyclerView.LayoutManager[] layoutManagers = {
            // 线性布局，即列表
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true),
            // 网格布局，即一行显示两列
            new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false),
            // 瀑布流布局
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    };
    // 布局菜单按钮的图标数组
    private int[] layoutIcons = {
            // 列表布局图标
            R.drawable.ic_view_list_24dp,
            // 网格布局图标
            R.drawable.ic_view_module_24dp,
            // 瀑布流布局图标
            R.drawable.ic_view_quilt_24dp
    };
    // 当前选择的布局索引，以数组下标做索引值
    private int selectedLayout;

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
        // 绑定线性布局
        llFileListEmpty = (LinearLayout) findViewById(R.id.ll_file_list_empt);
        // 绑定可回收列表视图
        recyclerViewPictures = (RecyclerView) findViewById(R.id.recyclerView_pictures);
        // 设置列表的布局管理器
        recyclerViewPictures.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        // 获得数据列表
        getImageFiles();
        // 图片文件夹存在，且有修改的图片
        if (imgFiles != null && imgFiles.size() > 0) {
            // 让适配器装载数据
            pictureAdapter = new PictureAdapter(this, imgFiles);
            // 设置适配器
            recyclerViewPictures.setAdapter(pictureAdapter);
        } else {
            // 隐藏列表视图
            recyclerViewPictures.setVisibility(View.GONE);
            // 显示图片不存在的视图
            llFileListEmpty.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获得已经修改的图片列表
     */
    private void getImageFiles() {
        // 获取设备的根目录
        File sdDir = Environment.getExternalStorageDirectory();
        // 创建缓存目录
        File cacheDir = new File(sdDir, "UPhotocache");
        // 如果存在目录，则读取
        if (cacheDir.exists()) {
            // 创建数据列表
            if (imgFiles == null)
                imgFiles = new ArrayList<>();
            // 获取cacheDir的所有子文件，除去output_image.jpg
            // 获得目录中的所有文件
            File[] files = cacheDir.listFiles();
            // 遍历文件夹，获取资源
            for (File imgFile : files) {
                // 如果当前图片文件的名字不是output_image.jpg，不区分大小写
                if (!"output_image.jpg".equalsIgnoreCase(imgFile.getName())) {
                    imgFiles.add(imgFile);
                }
            }
        } else {
            // 不做任何处理
        }


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
        getMenuInflater().inflate(R.menu.pictures_option, menu);
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
            // 点击的是布局切换菜单
            case R.id.action_layout:
                doLayoutSwitch(item);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 做布局切换的方法
     *
     * @param item 被点击的选项菜单
     */
    private void doLayoutSwitch(MenuItem item) {
        // 判断当前的布局索引是否为数组的长度-1，其表示下一个索引应该修改为0
        // 即进行循环切换
        if (selectedLayout == layoutIcons.length-1) {
            selectedLayout = 0;
        } else {
            // 否则下一个索引直接加1
            selectedLayout++;
        }
        // 根据索引值，直接改变布局图标
        item.setIcon(layoutIcons[selectedLayout]);
        // 根据索引值，改变布局形式
        recyclerViewPictures.setLayoutManager(layoutManagers[selectedLayout]);
    }

}
