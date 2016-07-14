package cn.bill56.youphoto.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Set;

import cn.bill56.youphoto.R;
import cn.bill56.youphoto.activity.EditPicActivity;
import cn.bill56.youphoto.activity.PicturesActivity;
import cn.bill56.youphoto.util.ToastUtil;

/**
 * 跟显示图片的可回收视图绑定的适配器类
 * Created by Bill56 on 2016/6/20.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    // 上下文环境
    private Context mContext;
    // 布局加载器
    private LayoutInflater inflater;
    // 数据列表
    private List<File> mData;

    /**
     * 构造方法
     *
     * @param context 上下文环境
     * @param data    绑定的数据列表
     */
    public PictureAdapter(Context context, List<File> data) {
        // 初始化成员变量
        mContext = context;
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 创建视图项
     *
     * @param parent   父元素
     * @param viewType 视图类系
     * @return 视图项
     */
    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        // 加载布局
        View v = inflater.inflate(R.layout.card_picture, parent, false);
        return new ViewHolder(v);
    }

    /**
     * 绑定视图项的数据
     *
     * @param holder   视图项
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 获得文件对象的引用
        File f = mData.get(position);
        // 将文件与视图项进行绑定
        holder.setData(f, position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                /**
                 * 被监听对象点击后回调
                 * @param v 事件源
                 */
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                /**
                 * 被监听对象长按后回调
                 * @param v 事件源
                 * @return true表示本层已经处理完毕，false表示要给父层再处理
                 */
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(v, position);
                    return false;
                }
            });
        }
    }

    /**
     * 获得数据的总数
     *
     * @return 数据总数
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 删除某一个文件
     *
     * @param file 待删除的文件对象
     */
    public void remove(File file) {
        // 从磁盘删除
        boolean result = file.delete();
        // 删除成功，从内存中也删除
        if (result) {
            // 从列表删除
            mData.remove(file);
            // 通知适配器
            notifyDataSetChanged();
            // 判断数据长度
            if (mData == null || mData.size() == 0) {
                // 如果数据的长度为0，则调用活动中显示列表为空的布局
                PicturesActivity.instance.showLayoutWhenDataEmpty();
            }
        } else {
            // 提示用户删除失败
            ToastUtil.show(mContext, R.string.activity_pictures_remove_error);
        }

    }

    /**
     * 获得文件对象
     *
     * @param pos 位置
     * @return 文件对象
     */
    public File getItem(int pos) {
        return mData.get(pos);
    }

    /**
     * ViewHolder类，用于管理回收资源
     *
     * @author Bill56
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // 图片缩略图
        ImageView imgFileIcon;
        // 图片文件名
        TextView textFileName;
        // 图片文件大小
        TextView textFileSize;
        // 图片文件路径
        TextView textFilePath;

        /**
         * 构造方法
         *
         * @param itemView 视图项
         */
        public ViewHolder(final View itemView) {
            super(itemView);
            // 绑定视图对象
            imgFileIcon = (ImageView) itemView.findViewById(R.id.image_file_icon);
            textFileName = (TextView) itemView.findViewById(R.id.text_file_name);
            textFileSize = (TextView) itemView.findViewById(R.id.text_file_size);
            textFilePath = (TextView) itemView.findViewById(R.id.text_file_path);
        }

        /**
         * 绑定数据
         *
         * @param item     被绑定的文件项
         * @param position 被绑定的文件项所在的索引号
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void setData(File item, int position) {
            Set<Integer> positionSet = PicturesActivity.instance.positionSet;
            if (positionSet.contains(position)) {
                itemView.setBackground(PicturesActivity.instance.
                        getResources().getDrawable(R.drawable.bg_selected));
            } else {
                itemView.setBackground(PicturesActivity.instance.
                        getResources().getDrawable(R.drawable.btn_common));
            }
            // 绑定图片文件名
            textFileName.setText(item.getName());
            // 绑定图片文件大小
            // 将字节转成通用的计算机文件大小
            String imgSize = Formatter.formatFileSize(mContext, item.length());
            textFileSize.setText(imgSize);
            // 绑定文件路径
            textFilePath.setText(item.getAbsolutePath());
            // 使用毕加索第三方包装载图片
            Picasso.with(mContext)
                    // 装载的路径
                    .load(item.getAbsoluteFile())
                            // 装载出错时候的缩略图
                    .error(R.drawable.ic_image_24dp)
                            // 装载的目标视图对象
                    .into(imgFileIcon);
        }
    }

    /**
     * 当每一项被点击的时候的回调接口
     */
    public interface OnItemClickListener {

        /**
         * 被点击的时候执行
         *
         * @param view     事件源
         * @param position 位置
         */
        void onItemClick(View view, int position);

        /**
         * 长按的时候执行
         *
         * @param view     事件源
         * @param position 位置
         */
        void onItemLongClick(View view, int position);
    }

    // 回调对象
    private OnItemClickListener onItemClickListener;

    /**
     * 设置点击回调接口
     *
     * @param onItemClickListener 回调接口
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
