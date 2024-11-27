package com.example.potplayer.Activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.potplayer.Bean.Musicmes;
import com.example.potplayer.Bean.NotepadBean;

import com.example.potplayer.Service.MyBroadcastReceiver;
import com.example.potplayer.adapter.NotepadAdapter;
import com.example.potplayer.database.SQLiteHelper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.oneapp.R;


public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    String name;
    ListView listView;
    List<NotepadBean> list1;
    SQLiteHelper mSQliteHelper;
    SQLiteHelper.Note note = new SQLiteHelper.Note();
    NotepadAdapter adapter;
    private TextView city;

    private LocationManager locationManager;

    private LocationListener locationListener;

    private MyBroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Intent intent = getIntent();
        name = intent.getStringExtra("username");

        mSQliteHelper = new SQLiteHelper(this);

        city = findViewById(R.id.f_city);

        receiver = new MyBroadcastReceiver();


        findViewById(R.id.im_q).setOnClickListener(this);
        findViewById(R.id.im_a).setOnClickListener(this);
        findViewById(R.id.im_o).setOnClickListener(this);
        findViewById(R.id.im_b).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listview);
        ImageView add = (ImageView) findViewById(R.id.noteadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent("com.example.CUSTOM_BROADCAST");
                intent1.putExtra("message", "记录下你的所思所想吧！");
                sendBroadcast(intent1);
                Intent intent = new Intent(FirstActivity.this, NoteActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        initData();

        //获取权限 获取城市信息

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        setupLocationListener();

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        IntentFilter filter = new IntentFilter("com.example.CUSTOM_BROADCAST");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }

        unregisterReceiver(receiver);
    }

    private void setupLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Geocoder geocoder = new Geocoder(FirstActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String cityname = addresses.get(0).getLocality();
                        runOnUiThread(() -> city.setText(cityname));
                    } else {
                        Log.e("Geocoder", "No addresses found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Geocoder", "Geocoder failed", e);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                Log.e("Permission", "Location permission denied");
            }
        }
    }


    public void showToast(String message)
    {
        Toast.makeText(FirstActivity.this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {


            case R.id.im_q:
                Intent intentq = new Intent(FirstActivity.this,MusicMainActivity.class);

                intentq.putExtra("username",name);

                startActivity(intentq);
                break;
            case R.id.im_b:
                Intent intentb = new Intent(FirstActivity.this, WebActivity.class);

                intentb.putExtra("username",name);

                startActivity(intentb);
                break;
            case R.id.im_a:
                Intent intenta = new Intent(FirstActivity.this, TweetActivity.class);

                intenta.putExtra("username",name);

                startActivity(intenta);
                break;
            case R.id.im_o:
                Intent intento = new Intent(FirstActivity.this,OwnActivity.class);

                intento.putExtra("username",name);

                startActivity(intento);
                break;
        }
    }
    //权限判断
    private void permissionVersion(){
        if(Build.VERSION.SDK_INT >= 23){//6.0或6.0以上
            //动态权限申请

        }else {//6.0以下
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            showToast("你的版本在Android6.0以下，不需要动态申请权限。");
        }
    }

    protected void initData()
    {
        mSQliteHelper = new SQLiteHelper(this);
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NotepadBean notepadBean = list1.get(position);
                Intent intent = new Intent(FirstActivity.this,NoteActivity.class);
                intent.putExtra("id",notepadBean.getId());
                intent.putExtra("content",notepadBean.getContent());
                intent.putExtra("time",notepadBean.getNotepadtime());
                FirstActivity.this.startActivityForResult(intent,1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                NotepadBean notepadBean = list1.get(position);
                                if(note.deleteData(notepadBean.getId()))
                                {
                                    list1.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(FirstActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }
    private void showQueryData()
    {
        if(list1 != null)
        {
            list1.clear();
        }
        list1 = note.query();
        adapter = new NotepadAdapter(this,list1);
        listView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 2)
        {
            showQueryData();
        }
    }
    
}
