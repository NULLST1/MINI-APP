package com.example.potplayer.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.oneapp.R;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;





import com.example.potplayer.database.DBinformation;
import com.example.potplayer.database.SQLiteHelper;



public class NoteActivity extends AppCompatActivity implements View.OnClickListener {




    TextView note_time;
    EditText content;
    ImageView delete;
    ImageView note_save;
    SQLiteHelper mSQLiteHelper;
    SQLiteHelper.Note note =  new SQLiteHelper.Note();
    TextView noteName;

    String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);





        note_time = (TextView) findViewById(R.id.tv_time);
        content = (EditText) findViewById(R.id.note_content);
        delete = (ImageView) findViewById(R.id.delete);
        note_save = (ImageView) findViewById(R.id.note_save);
        noteName = (TextView) findViewById(R.id.note_name);

        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);
        initData();

    }
    protected void initData()
    {
        mSQLiteHelper = new SQLiteHelper(this);
        noteName.setText("添加记录");
        Intent intent = getIntent();
        if(intent != null)
        {
            id = intent.getStringExtra("id");
            if(id !=  null)
            {
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.delete:
                content.setText("");
                break;
            case R.id.note_save:
                String noteContent = content.getText().toString().trim();
                if(id != null)//修改数据
                {
                    if(noteContent.length()>0)
                    {
                        if(note.updateData(id,noteContent, DBinformation.getTime()))
                        {
                            showToast("修改成功");
                            setResult(2);
                            finish();
                        }
                        else
                        {
                            showToast("修改失败");
                        }
                    }
                    else
                    {
                        showToast("修改内容不能为空！");
                    }
                }
                else //添加数据
                {
                    if(noteContent.length()>0)
                    {
                        if(note.insertData(noteContent,DBinformation.getTime()))
                        {
                            showToast("保存成功");
                            setResult(2);
                            finish();
                        }
                        else
                        {
                            showToast("保存失败");
                        }
                    }
                    else
                    {
                        showToast("修改内容不能为空");
                    }
                }
                break;
        }
    }

    public void showToast(String message)
    {
        Toast.makeText(NoteActivity.this,message,Toast.LENGTH_LONG).show();
    }


}