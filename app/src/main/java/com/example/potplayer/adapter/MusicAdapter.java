package com.example.potplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.potplayer.R;
import com.example.potplayer.Bean.Musicmes;

import java.util.List;

public class MusicAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Musicmes> list;
    public MusicAdapter(Context context, List<Musicmes> list)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.list=list;
    }
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertview == null)
        {
            convertview=layoutInflater.inflate(R.layout.music_item_layout,null);
            viewHolder = new ViewHolder(convertview);
            convertview.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertview.getTag();
        }
        Musicmes musicmes = (Musicmes) getItem(position);
        viewHolder.tvId.setText(musicmes.getId());
        viewHolder.tvMusic.setText(musicmes.getSongname());
        viewHolder.tvSinger.setText(musicmes.getSinger());
        viewHolder.tvTime.setText(musicmes.getMusictime());
        viewHolder.tvAm.setText(musicmes.getMmusicAlbum());
        return convertview;
    }

    class ViewHolder{
        TextView tvMusic,tvSinger,tvTime,tvId,tvAm;
        public ViewHolder(View view)
        {
           tvId = (TextView) view.findViewById(R.id.item_id);
           tvMusic = (TextView) view.findViewById(R.id.item_musicname);
           tvSinger = (TextView) view.findViewById(R.id.item_musicplayer);
           tvTime = (TextView) view.findViewById(R.id.item_time);
           tvAm = (TextView) view.findViewById(R.id.item_Am);
        }
    }
}
