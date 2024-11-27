package com.example.potplayer.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.oneapp.R;

import com.example.potplayer.Bean.Musicmes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener{
    private static SeekBar sb;
    private static TextView tv_progress, tv_total;
    private ObjectAnimator animator;
    private List<Musicmes> list = new ArrayList<>();
    private int playposition = 1;
    private int pausePosition;
    private int playsort = 0; //默认顺序播放，1---随机播放，2-----循环播放
    int iconsort = 0;
    TimerTask task;
    String name;

    private Timer timer;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);


         init();
    }
    private void init() {
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_total = (TextView) findViewById(R.id.tv_total);
        sb = (SeekBar) findViewById(R.id.sb);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.change).setOnClickListener(this);
        findViewById(R.id.btn_last).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);

        findViewById(R.id.im_a).setOnClickListener(this);
        findViewById(R.id.im_q).setOnClickListener(this);
        findViewById(R.id.im_f).setOnClickListener(this);
        findViewById(R.id.im_o).setOnClickListener(this);
        findViewById(R.id.im_b).setOnClickListener(this);

        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {                          //滑动条进度改变时，会调用此方法
                if (progress == seekBar.getMax()) { //当滑动条滑到末端时，结束动画
                    if(playsort == 0)
                    {
                        if(playposition >= 0 && playposition != list.size() - 1)
                        {
                            playposition = playposition + 1;

                        }
                        else if(playposition == list.size()-1)
                        {
                            playposition = 0;
                        }
                    }
                    else if(playsort == 1)
                    {
                        Random random = new Random();
                        playposition = random.nextInt(list.size()-1);
                    }
                    Musicmes musicmes = list.get(playposition);
                    lastnextmusic(musicmes);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//滑动条开始滑动时调用
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { //滑动条停止滑动时调用

                //根据拖动的进度改变音乐播放进度
                int progress = seekBar.getProgress();//获取seekBar的进度
                seekTo(progress);         //改变播放进度
            }
        });
        ImageView iv_music = (ImageView) findViewById(R.id.iv_music);
        animator = ObjectAnimator.ofFloat(iv_music, "rotation", 0f, 360.0f);
        animator.setDuration(10000);  //动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);  //-1表示设置动画无限循环

        Intent intentmain = getIntent();
        list = intentmain.getParcelableArrayListExtra("musiclist");
        playposition = Integer.valueOf(intentmain.getStringExtra("playposition"));
        name = intentmain.getStringExtra("username");

        mediaPlayer = new MediaPlayer();//创建音乐播放器对象

        Musicmes musicmes = list.get(playposition);
        try {
            mediaPlayer.setDataSource(musicmes.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last:                //播放按钮点击事件
                if(playposition == 0)
                {
                    showToast("当前歌曲为第一首，没有上一曲了！！！");
                    return;
                }
                else
                {
                    playposition = playposition - 1;
                    Musicmes musicmes = list.get(playposition);
                    lastnextmusic(musicmes);
                }
                break;
            case R.id.btn_pause:               //暂停按钮点击事件
                if(mediaPlayer.isPlaying())
                {
                    //正在播放音乐，进行暂停操作
                    pauseMusic();
                    animator.pause();              //暂停播放动画
                }
                else
                {
                    //此时没有播放音乐，点击开始播放音乐
                    playMusic();
                    animator.start();
                }

                break;
            case R.id.btn_next:     //继续播放按钮点击事件
                if(playposition == list.size()-1)
                {
                    showToast("当前歌曲为最后一首歌曲，没有下一曲了！！！");
                    return;
                }
                else
                {
                    playposition = playposition + 1;
                    Musicmes musicmes = list.get(playposition);
                    lastnextmusic(musicmes);
                }
                break;
            case R.id.btn_exit:                //退出按钮点击事件
                Intent intentex = new Intent(MusicActivity.this, MusicMainActivity.class);
                intentex.putExtra("username",name);
                finish();
                startActivity(intentex);                        //关闭音乐播放界面
                break;
            case R.id.back:
                Intent intentba = new Intent(MusicActivity.this, MusicMainActivity.class);
                intentba.putExtra("username",name);
                finish();
                startActivity(intentba);
                break;
            case R.id.change:
                AlertDialog dialog;
                AlertDialog.Builder builder=new AlertDialog.Builder(MusicActivity.this)
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
            case R.id.im_f:
                Intent intentf = new Intent(MusicActivity.this,FirstActivity.class);

                intentf.putExtra("username",name);

                startActivity(intentf);
                break;
            case R.id.im_q:
                Intent intentq = new Intent(MusicActivity.this,MusicMainActivity.class);

                intentq.putExtra("username",name);

                startActivity(intentq);
                break;
            case R.id.im_b:
                Intent intentb = new Intent(MusicActivity.this, WebActivity.class);

                intentb.putExtra("username",name);

                startActivity(intentb);
                break;
            case R.id.im_a:
                Intent intenta = new Intent(MusicActivity.this, TweetActivity.class);

                intenta.putExtra("username",name);

                startActivity(intenta);
                break;
            case R.id.im_o:
                Intent intento = new Intent(MusicActivity.this,OwnActivity.class);

                intento.putExtra("username",name);

                startActivity(intento);
                break;
        }
    }

    //暂停音乐
    public void pauseMusic() {
        if(mediaPlayer != null && mediaPlayer.isPlaying())//不为空，并且在播放
        {
            pausePosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            // stop.setImageResource(R.drawable.add);
        }
    }

    //播放音乐(两种情况）
    public void playMusic() {
        if(mediaPlayer != null && !mediaPlayer.isPlaying())//不为空，并且不在播放
        {
            if (pausePosition == 0)//从头开始播放
            {
                try {
                    mediaPlayer.prepare();//准备
                    mediaPlayer.start();//播放
                    addTimer();
                    animator.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else//从暂停开始播放
            {
                mediaPlayer.seekTo(pausePosition);
                mediaPlayer.start();
            }

        }

    }
    //停止音乐播放
    public void stopMusic() {
        if(mediaPlayer != null)
        {
            pausePosition = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
        }
    }

    public void seekTo(int progress) {
        mediaPlayer.seekTo(progress);//设置音乐的播放位置
    }

    public void addTimer() {        //添加计时器用于设置音乐播放器中的播放进度条
        if (timer == null) {
            timer = new Timer();     //创建计时器对象
            task = new TimerTask() {
                @Override
                public void run() {
                    if (mediaPlayer == null) return;
                    int duration = mediaPlayer.getDuration();                //获取歌曲总时长
                    int currentPosition = mediaPlayer.getCurrentPosition();//获取播放进度
                    Message msg = MusicActivity.handler.obtainMessage();//创建消息对象
                    //将音乐的总时长和播放进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    MusicActivity.handler.sendMessage(msg);
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒执行一次
            timer.schedule(task, 5, 500);
        }

    }
    public void lastnextmusic(Musicmes musicmes)
    {
        musicmes = list.get(playposition);
        stopMusic();
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicmes.getPath());
            playMusic();
            animator.start();               //播放动画
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
        task.cancel();
    }
    public void showToast(String message)
    {
        Toast.makeText(MusicActivity.this,message,Toast.LENGTH_LONG).show();
    }
}
