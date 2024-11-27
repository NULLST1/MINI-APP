package com.example.potplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.example.potplayer.Activity.MusicMainActivity;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicplayService extends Service {
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private int playPosition = -1;//记录正在播放的歌曲在列表中的位置
    private int pausePosition = 0;//记录当前正在播放的歌曲时间位置
    public MusicplayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
       // list = intent.getParcelableArrayListExtra("musiclist");
        return (IBinder) new MusicControl();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();//创建音乐播放器对象
    }
    public void addTimer() {        //添加计时器用于设置音乐播放器中的播放进度条
        if (timer == null) {
            timer = new Timer();     //创建计时器对象
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (mediaPlayer == null) return;
                    int duration = mediaPlayer.getDuration();                //获取歌曲总时长
                    int currentPosition = mediaPlayer.getCurrentPosition();//获取播放进度
                    Message msg = MusicMainActivity.handler.obtainMessage();//创建消息对象
                    //将音乐的总时长和播放进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    MusicMainActivity.handler.sendMessage(msg);
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒执行一次
            timer.schedule(task, 5, 500);
        }
    }

    public class MusicControl extends Binder {

        //暂停音乐
        public void pauseMusic() {
            if(mediaPlayer != null && mediaPlayer.isPlaying())//不为空，并且在播放
            {
                pausePosition = mediaPlayer.getCurrentPosition();//播放位置
                mediaPlayer.pause();

            }
        }
        //播放音乐
        public void playItemMusic(String path) {

            mediaPlayer.reset();//重置
            try {
                mediaPlayer.setDataSource(path);
                try {
                    mediaPlayer.prepare();//准备
                    mediaPlayer.start();//播放
                    addTimer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else//从暂停开始播放
                    {
                        mediaPlayer.seekTo(pausePosition);
                        mediaPlayer.start();
                    }

                }

            //stop.setImageResource(R.drawable.back);
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
        public int positionMusic()
        {
            return playPosition;
        }

        public boolean isplay()
        {
            return mediaPlayer.isPlaying();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) mediaPlayer.stop();//停止播放音乐
        mediaPlayer.release();                         //释放占用的资源
        mediaPlayer = null;                            //将player置为空
    }
}

