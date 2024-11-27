package com.example.potplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.potplayer.Bean.Imgs;
import com.example.potplayer.R;

import java.util.List;

public class ImgsAdapter extends RecyclerView.Adapter<ImgsAdapter.ViewHolder>{

    private Context context;
    private List<Imgs> list;

    private LayoutInflater layoutInflater;

    public ImgsAdapter(Context context, List<Imgs> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.img_item, parent, false); // 注意这里的布局文件名
        return new ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Imgs imgs = list.get(position);

        holder.imgid.setImageResource(imgs.getImgid());

        holder.imgname.setText(imgs.getImgname());
    }


    private void callPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    @Override
    public long getItemId(int i) {
        // 通常情况下，如果你不需要稳定的ID，可以返回 RecyclerView.NO_ID
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemCount() {
        return list.size(); // 返回列表中的项目数量
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgid;
        TextView imgname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // 必须调用 RecyclerView.ViewHolder 的构造函数
            imgid = itemView.findViewById(R.id.img_img);
            imgname = itemView.findViewById(R.id.img_t);
        }
    }
}
