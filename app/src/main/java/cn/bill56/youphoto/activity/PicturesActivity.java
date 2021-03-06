package cn.bill56.youphoto.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false),
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

    // 动作模式
    private ActionMode actionMode;
    // 位置集合
    public Set<Integer> positionSet = new HashSet<>();
    // 指向自身的实例
    public static PicturesActivity instance;
    // 选项存储对象
    private SharedPreferences preferences;
    // 选项菜单对象
    private MenuItem itemLayout;

    /**
     * 创建活动的时候回调
     *
     * @param savedInstanceState 保存实例的对象
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 回调父类
        super.onCreate(savedInstanceState);
        // 加载布局
        setContentView(R.layout.activity_pictures);
        // 将自身赋给instance
        instance = this;
        // 初始化导航
        initView();
        // 为recycleView注册上下文菜单
//        registerForContextMenu(recyclerViewPictures);
    }

    /**
     * 初始化导航布局
     */
    private void initView() {
        // 初始化工具栏布局
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        // 将工具栏替换成actionBar
        setSupportActionBar(toolbar);
        // 获得ActionBar
        ActionBar actionBar = getSupportActionBar();
        // 设置顶部返回键
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 设置顶部标题
        actionBar.setTitle(R.string.activity_pictures_title);
        // 绑定线性布局
        llFileListEmpty = (LinearLayout) findViewById(R.id.ll_file_list_empt);
        // 绑定可回收列表视图
        recyclerViewPictures = (RecyclerView) findViewById(R.id.recyclerView_pictures);
        // 获得选项存储对象
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // 从选项存储中获取布局索引值，默认为0，即线性布局
        selectedLayout = preferences.getInt("SELECT_LAYOUT", 0);
        // 设置列表的布局管理器
        recyclerViewPictures.setLayoutManager(layoutManagers[selectedLayout]);
        // 获得数据列表
        getImageFiles();
        // 图片文件夹存在，且有修改的图片
        if (imgFiles != null && imgFiles.size() > 0) {
            // 让适配器装载数据
            pictureAdapter = new PictureAdapter(this, imgFiles);
            // 设置适配器
            recyclerViewPictures.setAdapter(pictureAdapter);
            pictureAdapter.setOnItemClickListener(new PictureAdapter.OnItemClickListener() {
                /**
                 * 自定义图片点击选择的回调
                 * @param view     事件源
                 * @param position 位置
                 */
                @Override
                public void onItemClick(View view, int position) {
                    if (actionMode != null) {
                        // 如果当前处于多选状态，则进入多选状态的逻辑
                        // 维护当前已选的position
                        addOrRemove(position);
                    } else {
                        // 如果不是多选状态，则进入点击事件的业务逻辑
                        // TODO something
                        Toast.makeText(PicturesActivity.this, "点击" +
                                imgFiles.get(position).getName(), Toast.LENGTH_SHORT).show();
                        // 跳转到图片编辑界面
                        Intent editPicIntent = new Intent(PicturesActivity.this, EditPicActivity.class);
                        // 将被点击的图片路径存入
                        editPicIntent.putExtra("EDIT_PIC", imgFiles.get(position).getAbsolutePath());
                        // 启动活动
                        startActivity(editPicIntent);
                    }
                }

                /**
                 * 自定义图片长按后回调
                 * @param view     事件源
                 * @param position 位置
                 */
                @Override
                public void onItemLongClick(View view, int position) {
                    if (actionMode == null) {
                        actionMode = startSupportActionMode(new ActionModeCallBack());
                    }
                }
            });
        } else {
            // 数据为空的时候显示的背景图
            // 调用方法
            showLayoutWhenDataEmpty();
        }
    }

    /**
     * 数据为空的时候显示的背景图
     */
    public void showLayoutWhenDataEmpty() {
        // 隐藏列表视图
        recyclerViewPictures.setVisibility(View.GONE);
        // 显示图片不存在的视图
        llFileListEmpty.setVisibility(View.VISIBLE);
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
        // 获取布局菜单对象
        itemLayout = menu.findItem(R.id.action_layout);
        // 设置背景
        itemLayout.setIcon(layoutIcons[selectedLayout]);
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
                // 回调点击返回键的方法
                onBackPressed();
                break;
            // 点击的是布局切换菜单
            case R.id.action_layout:
                // 执行切换布局方法
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
        if (selectedLayout == layoutIcons.length - 1) {
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

    /**
     * 创建列表选项的上下文菜单
     *
     * @param menu     上下文菜单对象
     * @param v        视图对象
     * @param menuInfo 菜单信息
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 如果注册的上下文菜单是recyclerViewPictures的时候执行
        if (v == recyclerViewPictures) {
            // 创建选项菜单
            getMenuInflater().inflate(R.menu.pictures_context, menu);
        }
    }

    /**
     * 当选项菜单某一项被点击的时候执行
     *
     * @param item 被点击的菜单项
     * @return true表示本层已处理完毕，无需交由上层，否则需要交由上层
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 获得被点击的菜单信息
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // 获取被点击的上下文菜单项id
        switch (item.getItemId()) {
            // 点击的是删除菜单项
            case R.id.action_remove:
                break;
            default:
                break;
        }
        return true;
    }

    private void addOrRemove(int position) {
        if (positionSet.contains(position)) {
            // 如果包含，则撤销选择
            positionSet.remove(position);
        } else {
            // 如果不包含，则添加
            positionSet.add(position);
        }
        if (positionSet.size() == 0) {
            // 如果没有选中任何的item，则退出多选模式
            actionMode.finish();
        } else {
            // 设置ActionMode标题
            actionMode.setTitle(positionSet.size() +
                    getString(R.string.activity_pictures_selected));
            // 更新列表界面，否则无法显示已选的item
            pictureAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 实现了ActionMode.Callback的类
     */
    class ActionModeCallBack implements ActionMode.Callback {

        /**
         * 创建动作模式
         *
         * @param mode 动作模式
         * @param menu 菜单对象
         * @return true表示本层处理完毕，无需交给上层，否则需要交给上层
         */
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // 当动作为空的时候
            if (actionMode == null) {
                // 将当前动作模式赋值给成员变量
                actionMode = mode;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.pictures_context, menu);
                return true;
            } else {
                return false;
            }
        }

        /**
         * 准备开始动作的时候执行
         *
         * @param mode 动作模式
         * @param menu 菜单对象
         * @return true表示本层处理完毕，无需交给上层，否则需要交给上层
         */
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        /**
         * 动作被执行的时候执行
         *
         * @param mode 动作模式
         * @param item 菜单项对象
         * @return true表示本层处理完毕，无需交给上层，否则需要交给上层
         */
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // 判断当前的被点击的菜单项
            switch (item.getItemId()) {
                // 点击的如果是删除
                case R.id.action_remove:
                    // 删除已选
                    Set<File> valueSet = new HashSet<>();
                    // 找到被删除的位置
                    for (int position : positionSet) {
                        valueSet.add(pictureAdapter.getItem(position));
                    }
                    // 删除该位置的文件
                    for (File val : valueSet) {
                        pictureAdapter.remove(val);
                    }
                    // 销毁
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        /**
         * 动作被销毁的时候执行
         *
         * @param mode 动作模式
         */
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            // 清空集合
            positionSet.clear();
            // 通知适配器
            pictureAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 按下返回键的时候执行
     */
    @Override
    public void onBackPressed() {
        // 从选项存储中获取用户是否需要询问
        boolean isHint = preferences.getBoolean("IS_HINT", true);
        // 获取选项存储的编辑器对象
        final SharedPreferences.Editor editor = preferences.edit();
        // 需要提醒，则弹出对话框询问用户是否保存当前布局
        if (isHint) {
            // 弹出对话框
            // 创建构建器
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 查找视图
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_hint, null);
            // 获取视图中的复选框
            final CheckBox cbNoHint = (CheckBox) dialogView.findViewById(R.id.checkbox_no_hint);
            // 设置标题和布局
            builder.setTitle(R.string.dialog_layout_hint_title)
                    .setView(dialogView);
            // 设置对话框选择确定后执行的方法
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                /**
                 * 点击对话框中的确定按钮执行
                 * @param dialog    事件源所在的对话框对象
                 * @param which 被执行的选项
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 将选择存储选项存储
                    editor.putBoolean("IS_HINT", !cbNoHint.isChecked());
                    // 将当前选择布局也存储选项存储
                    editor.putInt("SELECT_LAYOUT", selectedLayout);
                    editor.commit();
                    // 让对话框消失
                    dialog.dismiss();
                    // 销毁活动
                    finish();
                }
            });
            // 设置对话框选择取消后执行的方法
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                /**
                 * 点击对话框中的取消按钮执行
                 * @param dialog    事件源所在的对话框对象
                 * @param which 被执行的选项
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 关闭对话框
                    dialog.dismiss();
                    // 销毁活动
                    finish();
                }
            });
            // 创建对话框
            AlertDialog dialog = builder.create();
            // 显示出来
            dialog.show();
        } else {
            // 否则直接保存当前布局索引到选项存储
            editor.putInt("SELECT_LAYOUT", selectedLayout);
            // 提交编辑器
            editor.commit();
            super.onBackPressed();
        }
    }

}
