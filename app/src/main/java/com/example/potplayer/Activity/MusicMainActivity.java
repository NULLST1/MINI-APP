package com.example.potplayer.Activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.oneapp.R;

import com.example.potplayer.Service.MusicplayService;
import com.example.potplayer.adapter.MusicAdapter;
import com.example.potplayer.Bean.Musicmes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MusicMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private ImageView change;
    private ImageView look,last,stop,next;
    private TextView username;
    private TextView singer;
    private TextView music;
    private TextView album;
    private static TextView tv_progress;
    private static TextView tv_total;
    private static SeekBar sb;//滑动条

    public List<Musicmes> musicmesList = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private ListView listView;
    private MusicplayService.MusicControl musicControl;
    MyServiceConn conn;
    private static final int REQUEST_CODE = 1;
    private int playPosition = -1;//记录正在播放的位置
    private int playsort = 0; //默认顺序播放，1---随机播放，2-----循环播放
    int iconsort = 0;
    private boolean isUnbind = false;//记录服务是否被解绑
    String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicmain);
        checkPermission();
        initview();


        Intent intent = getIntent();
        name = intent.getStringExtra("username");


        username.setText("欢迎您！"+name);

        musicAdapter = new MusicAdapter(MusicMainActivity.this,musicmesList);
        loadLocalMusicData();
        listView.setAdapter(musicAdapter);

        Intent intent1 = new Intent(this, MusicplayService.class);//创建意图对象
        conn = new MyServiceConn();//创建服务连接对象
        bindService(intent1, conn, BIND_AUTO_CREATE);  //绑定服务





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //获取当前播放的位置
                playPosition = position;
                Musicmes musicmes = musicmesList.get(position);
                //显示当前播放歌曲信息
                singer.setText(musicmes.getSinger());
                music.setText(musicmes.getSongname());
                album.setText(musicmes.getMmusicAlbum());
                //停止音乐播放
                musicControl.stopMusic();

                //重置多媒体播放器
                // mediaPlayer.reset();
                //设置新的路径
                // mediaPlayer.setDataSource(musicmes.getPath());
                musicControl.playItemMusic(musicmes.getPath());

            }
        });
    }


    public void LastNextMusic(Musicmes musicmes)
    {
        //显示当前播放歌曲信息
        singer.setText(musicmes.getSinger());
        music.setText(musicmes.getSongname());
        album.setText(musicmes.getMmusicAlbum());
        //停止音乐播放
        musicControl.stopMusic();

        //重置多媒体播放器
        //mediaPlayer.reset();
        //设置新的路径

        musicControl.playItemMusic(musicmes.getPath());


    }

    //初始化界面控件
    private void initview() {

        change = findViewById(R.id.change);
        look = findViewById(R.id.look);
        last = findViewById(R.id.iv_last);
        stop = findViewById(R.id.iv_stop);
        next = findViewById(R.id.iv_next);
        singer = findViewById(R.id.music_gs);
        music = findViewById(R.id.music_gq);
        username = findViewById(R.id.user_name);
        listView = findViewById(R.id.order_listview);
        album = findViewById(R.id.music_album);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_total = (TextView) findViewById(R.id.tv_total);
        sb = (SeekBar) findViewById(R.id.sbm);
        findViewById(R.id.im_a).setOnClickListener(this);
        findViewById(R.id.im_f).setOnClickListener(this);
        findViewById(R.id.im_o).setOnClickListener(this);
        findViewById(R.id.im_b).setOnClickListener(this);

        last.setOnClickListener(this);
        stop.setOnClickListener(this);
        next.setOnClickListener(this);
        look.setOnClickListener(this);
        change.setOnClickListener(this);
        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {                          //滑动条进度改变时，会调用此方法
                if (progress == seekBar.getMax()) { //当滑动条滑到末端时
                    if(playsort == 0)
                    {
                        if(playPosition >= 0 && playPosition != musicmesList.size() - 1)
                        {
                            playPosition = playPosition + 1;

                        }
                        else if(playPosition == musicmesList.size()-1)
                        {
                            playPosition = 0;
                        }
                    }
                    else if(playsort == 1)
                    {
                        Random random = new Random();
                         playPosition = random.nextInt(musicmesList.size()-1);
                    }
                    Musicmes musicmes = musicmesList.get(playPosition);
                    LastNextMusic(musicmes);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//滑动条开始滑动时调用
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //滑动条停止滑动时调用

                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();//获取seekBar的进度
                musicControl.seekTo(progress);         //改变播放进度
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.look:
               if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    showToast("该应用无访问存储权限");
                    checkPermission();
                }
                loadLocalMusicData();
                if(playPosition == -1)
                {
                    showToast("当前无音乐正在播放，请选择你所要播放的音乐");
                    return;
                }
                else
                {
                    musicControl.stopMusic();
                    Intent intent = new Intent(MusicMainActivity.this,MusicActivity.class);
                    intent.putParcelableArrayListExtra("musiclist", (ArrayList<? extends Parcelable>) musicmesList);
                    intent.putExtra("username",name);
                    intent.putExtra("playposition",playPosition+"");
                    startActivity(intent);
                }

                break;
            case R.id.change:
                AlertDialog dialog;
                AlertDialog.Builder builder=new AlertDialog.Builder(MusicMainActivity.this)
                        .setTitle("设置播放顺序")
                        .setIcon(R.drawable.sz)
                        .setSingleChoiceItems(new String[]{"顺序播放","随机播放","循环播放"},
                                iconsort, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        iconsort=which;

                                    }
                                })
                        .setPositiveButton("确定", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        playsort = iconsort;
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("取消", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                dialog=builder.create();
                dialog.show();
                break;
            case R.id.iv_last:
                if(playPosition == -1)
                {
                    showToast("当前无音乐正在播放，请选择你所要播放的音乐");
                    return;
                }
                else if(playPosition == 0)
                {
                    showToast("当前歌曲为第一首，没有上一曲了！！！");
                    return;
                }
                else
                {
                    playPosition = playPosition - 1;
                    Musicmes musicmes = musicmesList.get(playPosition);
                    LastNextMusic(musicmes);
                }
                break;
            case R.id.iv_stop:
                if(playPosition == -1)
                {
                    showToast("当前无音乐正在播放，请选择你所要播放的音乐");
                    return;
                }
                else if(musicControl.isplay())
                {
                    //正在播放音乐，进行暂停操作
                    musicControl.pauseMusic();
                    stop.setImageResource(R.drawable.pasue);
                }
                else
                {
                    //此时没有播放音乐，点击开始播放音乐
                    musicControl.playMusic();
                    stop.setImageResource(R.drawable.play);
                }
                break;
            case R.id.iv_next:
                if(playPosition == -1)
                {
                    showToast("当前无音乐正在播放，请选择你所要播放的音乐");
                    return;
                }
                else if(playPosition == musicmesList.size()-1)
                {
                    showToast("当前歌曲为最后一首歌曲，没有下一曲了！！！");
                    return;
                }
                else
                {
                  //  Log.i("geqn1","当前这一首歌曲为"+playPosition+"          ssdff      "+musicmesList.get(playPosition).toString());
                    playPosition = playPosition + 1;
                    Musicmes musicmes = musicmesList.get(playPosition);
                   // Log.i("gw2","下一首歌曲为"+playPosition+"          ssdff      "+musicmes.toString());
                    LastNextMusic(musicmes);

                }
                break;
            case R.id.im_f:
                Intent intentf = new Intent(MusicMainActivity.this,FirstActivity.class);

                intentf.putExtra("username",name);

                startActivity(intentf);
                break;
            case R.id.im_b:
                Intent intentb = new Intent(MusicMainActivity.this, WebActivity.class);

                intentb.putExtra("username",name);

                startActivity(intentb);
                break;
            case R.id.im_a:
                Intent intenta = new Intent(MusicMainActivity.this, TweetActivity.class);

                intenta.putExtra("username",name);

                startActivity(intenta);
                break;
            case R.id.im_o:
                Intent intento = new Intent(MusicMainActivity.this,OwnActivity.class);

                intento.putExtra("username",name);

                startActivity(intento);
                break;
        }

    }




    private void loadLocalMusicData() {
        /*加载本地存储当中的音乐mp3文件到集合当中*/
        //1.获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        // 2.获取本地音乐存储的Ur地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //EXTERNAL_ CONTENT_ URI;
        //3.开始查询地址
        Cursor cursor = resolver.query(uri,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //4.遍历Cursor
        int id=0;
        while (cursor. moveToNext()) {
            @SuppressLint("Range") String song = cursor.getString(cursor.getColumnIndex (MediaStore.Audio.Media.TITLE));//歌曲名
            @SuppressLint("Range") String singer = cursor.getString (cursor.getColumnIndex (MediaStore.Audio.Media.ARTIST));//歌手
            @SuppressLint("Range") String album = cursor.getString (cursor.getColumnIndex (MediaStore.Audio.Media.ALBUM));//专辑名称
            id++;
            String sid = String.valueOf(id);
            @SuppressLint("Range") String path = cursor.getString (cursor. getColumnIndex(MediaStore.Audio.Media.DATA));//存储路径
            @SuppressLint("Range") Long duration = cursor.getLong (cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));//歌曲时长
            SimpleDateFormat sdf = new SimpleDateFormat( " mm:ss");
            String time = sdf.format (new Date(duration));
            //将一行当中的数据封装到对象当中
            Musicmes musicmes = new Musicmes(sid,song,singer,time,album,path);
            musicmesList.add(musicmes) ;
            //数据源变化，提示适配器更新
            musicAdapter.notifyDataSetChanged();
        }

    }
    public static Handler handler = new Handler() {//创建消息处理器对象
        //在主线程中处理从子线程发送过来的消息
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData(); //获取从子线程发送过来的音乐播放进度
            int duration = bundle.getInt("duration");                  //歌曲的总时长
            int currentPostition = bundle.getInt("currentPosition");//歌曲当前进度
            sb.setMax(duration);                //设置SeekBar的最大值为歌曲总时长
            sb.setProgress(currentPostition);//设置SeekBar当前的进度位置
            //歌曲的总时长
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;
            if (minute < 10) {              //如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute; //在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {             //如果歌曲的时间中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute + ":" + strSecond);
            //歌曲当前播放时长
            minute = currentPostition / 1000 / 60;
            second = currentPostition / 1000 % 60;
            if (minute < 10) {             //如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }
            if (second < 10) {               //如果歌曲的时间中的秒钟小于10
                strSecond = "0" + second;  //在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_progress.setText(strMinute + ":" + strSecond);
        }
    };
    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            musicControl = (MusicplayService.MusicControl) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }
    private void unbind(boolean isUnbind){
        if(!isUnbind){                  //判断服务是否被解绑
            musicControl.pauseMusic();//暂停播放音乐
            unbindService(conn);      //解绑服务
        }
    }


    //动态申请SD卡权限

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //多个权限一起申请
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                //拒绝
            } else {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbind(isUnbind); //解绑服务
    }


    public void showToast(String message)
    {
        Toast.makeText(MusicMainActivity.this,message,Toast.LENGTH_LONG).show();
    }


}