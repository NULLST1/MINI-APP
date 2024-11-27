package com.example.potplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.potplayer.Bean.NotepadBean;
import com.example.potplayer.R;

import java.util.List;

public class NotepadAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<NotepadBean> list;
    public NotepadAdapter(Context context, List<NotepadBean> list)
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
            convertview=layoutInflater.inflate(R.layout.first_item_ayout,null);
            viewHolder = new ViewHolder(convertview);
            convertview.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertview.getTag();
        }
        NotepadBean noteInfo = (NotepadBean) getItem(position);
        viewHolder.tvNotepadContext.setText(noteInfo.getContent());
        viewHolder.tvNotepadTime.setText(noteInfo.getNotepadtime());
        return convertview;
    }

    class ViewHolder{
        TextView tvNotepadContext;
        TextView tvNotepadTime;
        public ViewHolder(View view)
        {
            tvNotepadContext =(TextView) view.findViewById(R.id.item_comment);
            tvNotepadTime = (TextView) view.findViewById(R.id.tvtime);
        }
    }
}
