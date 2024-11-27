package com.example.potplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.potplayer.Bean.Bartext;
import com.example.potplayer.R;

import java.util.List;

public class BartextAdapter extends RecyclerView.Adapter<BartextAdapter.ViewHolder>{

    private Context context;
    private List<Bartext> list;

    private LayoutInflater layoutInflater;

    public BartextAdapter(Context context, List<Bartext> list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bar_item, parent, false); // 注意这里的布局文件名
        return new ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bartext bartext = list.get(position);

        holder.barname.setText(bartext.getName());

        holder.barname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.barname.setTypeface(null, Typeface.BOLD); // 设置字体加粗
                holder.barname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // 设置字号为20sp

            }
        });
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

        TextView barname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView); // 必须调用 RecyclerView.ViewHolder 的构造函数
            barname = itemView.findViewById(R.id.bar_t);
        }
    }
}
