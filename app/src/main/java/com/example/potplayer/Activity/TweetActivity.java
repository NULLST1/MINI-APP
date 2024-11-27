package com.example.potplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.potplayer.Bean.Bartext;
import com.example.potplayer.Bean.Imgs;
import com.example.potplayer.Bean.Musicmes;
import com.example.oneapp.R;
import com.example.potplayer.adapter.BartextAdapter;
import com.example.potplayer.adapter.ImgsAdapter;

import java.util.ArrayList;
import java.util.List;

public class TweetActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Bartext> bartexts=new ArrayList<Bartext>();//定义数据列表

    private List<Imgs> imgs=new ArrayList<Imgs>();//定义数据列表
    private int[] icons = {R.drawable.t1,R.drawable.t2,R.drawable.t3,R.drawable.t4,R.drawable.t5,R.drawable.t6};
private String[] titles =
        {"人生没有彩排，每一天都是现场直播。", "海纳百川，有容乃大。", "星光不问赶路人，时光不负有心人。", "滴水穿石，非一日之功。","千里之行，始于足下。","微笑面对生活，生活也会对你微笑。"};

    private String[] bart = {"阅读", "日签", "专题", "发现", "问答", "长篇","休闲","娱乐"};
        Intent intent;
        String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);



        Intent mian = getIntent();
        name = mian.getStringExtra("username");

        findViewById(R.id.im_q).setOnClickListener(this);
        findViewById(R.id.im_f).setOnClickListener(this);
        findViewById(R.id.im_o).setOnClickListener(this);
        findViewById(R.id.im_b).setOnClickListener(this);

        for (int i = 0;i < 8;i++) {
            Bartext bartext = new Bartext(bart[i]);
            bartexts.add(bartext);
        }


        RecyclerView recyclerView = findViewById(R.id.recyle_bar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        BartextAdapter bartextAdapter = new BartextAdapter(TweetActivity.this,bartexts);
        recyclerView.setAdapter(bartextAdapter);


        for (int i = 0;i < 6;i++) {
            Imgs imgs1 = new Imgs(titles[i],icons[i]);
            imgs.add(imgs1);
        }

        RecyclerView recyclerView1 = findViewById(R.id.recyle_img);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1); // 设置为1列
        recyclerView1.setLayoutManager(gridLayoutManager);

        ImgsAdapter imgsAdapter = new ImgsAdapter(TweetActivity.this,imgs);
        recyclerView1.setAdapter(imgsAdapter);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.im_f:
                Intent intentf = new Intent(TweetActivity.this,FirstActivity.class);

                intentf.putExtra("username",name);

                startActivity(intentf);
                break;
            case R.id.im_q:
                Intent intentq = new Intent(TweetActivity.this,MusicMainActivity.class);

                intentq.putExtra("username",name);

                startActivity(intentq);
                break;
            case R.id.im_b:
                Intent intentb = new Intent(TweetActivity.this, WebActivity.class);

                intentb.putExtra("username",name);

                startActivity(intentb);
                break;
            case R.id.im_o:
                Intent intento = new Intent(TweetActivity.this,OwnActivity.class);

                intento.putExtra("username",name);

                startActivity(intento);
                break;

        }

    }
}