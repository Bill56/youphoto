package cn.bill56.youphoto.adapter;

import android.content.Context;
import android.content.Intent;
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

import cn.bill56.youphoto.R;
import cn.bill56.youphoto.activity.EditPicActivity;

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
        if (viewType == 1) {

        } else if (viewType == 2) {

        } else {

        }
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 获得文件对象的引用
        File f = mData.get(position);
        // 使用毕加索第三方包装载图片
        Picasso.with(mContext)
                .load(f.getAbsoluteFile())
                .error(R.drawable.ic_image_24dp)
                .into(holder.imgFileIcon);
        // 绑定图片文件名
        holder.textFileName.setText(f.getName());
        // 绑定图片文件大小
        // 将字节转成通用的计算机文件大小
        String imgSize = Formatter.formatFileSize(mContext, f.length());
        holder.textFileSize.setText(imgSize);
        // 绑定文件路径
        holder.textFilePath.setText(f.getAbsolutePath());
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
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "点击" + textFileName.getText(), Toast.LENGTH_SHORT).show();
                            // 跳转到图片编辑界面
                            Intent editPicIntent = new Intent(mContext, EditPicActivity.class);
                            // 将被点击的图片路径存入
                            editPicIntent.putExtra("EDIT_PIC",textFilePath.getText());
                            mContext.startActivity(editPicIntent);
                        }
                    }
            );
        }
    }


}
