package com.example.potplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.potplayer.Bean.Musicmes;
import com.example.oneapp.R;

import java.util.ArrayList;
import java.util.List;

public class WebActivity extends AppCompatActivity implements View.OnClickListener{
    String  name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        name = intent.getStringExtra("username");

        WebView webView = (WebView) findViewById(R.id.web);
        //设置允许javascript执行
        webView.getSettings().setJavaScriptEnabled(true);
        //当需要跳转到一个网页是， 目标网页依然在webView中
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://music.163.com") ;
       // webView.loadUrl("https://y.qq.com/?ADTAG=myqq#type=index") ;
//系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });

        findViewById(R.id.im_a).setOnClickListener(this);
        findViewById(R.id.im_f).setOnClickListener(this);
        findViewById(R.id.im_o).setOnClickListener(this);
        findViewById(R.id.im_q).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_f:
                Intent intentf = new Intent(WebActivity.this, FirstActivity.class);

                intentf.putExtra("username",name);

                startActivity(intentf);
                break;
            case R.id.im_q:
                Intent intentb = new Intent(WebActivity.this, MusicMainActivity.class);

                intentb.putExtra("username",name);

                startActivity(intentb);
                break;
            case R.id.im_a:
                Intent intenta = new Intent(WebActivity.this, TweetActivity.class);

                intenta.putExtra("username",name);

                startActivity(intenta);
                break;
            case R.id.im_o:
                Intent intento = new Intent(WebActivity.this, OwnActivity.class);

                intento.putExtra("username",name);

                startActivity(intento);
                break;
        }
    }
}