package com.example.potplayer.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oneapp.R;
import com.example.potplayer.Bean.Musicmes;
import com.example.potplayer.*;
import com.example.potplayer.database.MD5Utils;
import com.example.potplayer.database.SQLiteHelper;
import com.example.potplayer.Bean.Loginuser;

import java.util.ArrayList;
import java.util.List;

public class ChangePwdActivity extends AppCompatActivity {

    private EditText name,pwd,num;
    private Button btnxg,btnfinish;
    private SQLiteHelper sqLiteHelper;
    SQLiteHelper.User userSql = new SQLiteHelper.User();
    private ImageView back;
    String namess;
    private MD5Utils md5Utils ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        sqLiteHelper = new SQLiteHelper(this);
        name = (EditText) findViewById(R.id.e_namesc);
        pwd = (EditText) findViewById(R.id.e_pwdsc);
        btnxg = (Button)findViewById(R.id.btn_c);
        btnfinish = (Button) findViewById(R.id.btn_f);
        back = (ImageView)findViewById(R.id.back);
        num = (EditText)findViewById(R.id.e_lxfs);

        Intent intent = getIntent();
        namess = intent.getStringExtra("username");

        btnxg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String names = name.getText().toString();
                String pwds = md5Utils.md5Password(pwd.getText().toString());
                String nums = num.getText().toString();
               if(names.length() <= 0 )
               {
                   showToast("账号为空！！！请重新输入");
               }
               if(pwds.length() <= 0 )
               {
                   showToast("密码为空！！！请重新输入");
               }
               if(nums.isEmpty())
               {
                   showToast("联系方式为空！！！请重新输入");
               }
               else if(userSql.userquery(names) == null)
               {
                   showToast("账号不存在！！！检查账号是否正确！");
               }
               else if(userSql.userquery(names) != null)
               {
                   Loginuser loginuser = userSql.userquery(names);
                   String id = loginuser.getId();
                   if(userSql.updateData(id,names,pwds) && loginuser.getNum().equals(nums))
                   {
                       showToast("密码修改成功！！！");


                       if(namess == null)
                       {
                           Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                           finish();
                           startActivity(intent);
                       }
                       else
                       {
                           Intent intent = new Intent(ChangePwdActivity.this, MusicMainActivity.class);

                           intent.putExtra("username",namess);

                           finish();
                           startActivity(intent);
                       }
                   }
                   else if(loginuser.getNum() !=  nums)
                   {
                      showToast("输入联系方式错误！！！请重新输入！！！");
                   }
               }
            }
        });

        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(namess == null)
                {
                    Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(ChangePwdActivity.this,OwnActivity.class);
                    finish();

                    intent.putExtra("username",namess);

                    startActivity(intent);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(namess == null)
                {
                    Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(ChangePwdActivity.this,OwnActivity.class);
                    finish();

                    intent.putExtra("username",namess);

                    startActivity(intent);
                }
            }
        });
    }
    public void showToast(String message)
    {
        Toast.makeText(ChangePwdActivity.this,message,Toast.LENGTH_LONG).show();
    }
}