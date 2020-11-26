package com.clc.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.clc.note.R;
import com.clc.note.util.AppContext;

public class DetailRecordActivity extends AppCompatActivity {
    private int mTypeId;
    private String mTitle;
    private String mContent;
    private String mAddTime;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        int typeNameResId = R.string.record_type0;
        mTypeId = intent.getIntExtra("typeId", -1);
        switch (mTypeId){
            case 1:
                typeNameResId = R.string.record_type1;
                break;
            case 2:
                typeNameResId = R.string.record_type2;
                break;
            default:
        }

        mId = intent.getIntExtra("id",-1);
        mTitle = intent.getStringExtra("title");
        mContent = intent.getStringExtra("content");
        mAddTime = intent.getStringExtra("addTime");

        TextView titleTV = findViewById(R.id.textView_title);
        TextView contentTV = findViewById(R.id.textView_content);
        TextView typeNameTV = findViewById(R.id.textView_typeName);
        TextView timeTV = findViewById(R.id.textView_time);
        titleTV.setText(mTitle);
        contentTV.setText(mContent);
        timeTV.setText(mAddTime);
        typeNameTV.setText(typeNameResId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_update,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                AppContext.getInstance().isBack = true;
                finish();
                break;
            //跳转到修改页面
            case R.id.action_bar_toUpdate:
                AppContext appContext = AppContext.getInstance();
                Intent intent = new Intent(appContext,UpdateRecordActivity.class);
                intent.putExtra("typeId",mTypeId);
                intent.putExtra("title",mTitle);
                intent.putExtra("content",mContent);
                intent.putExtra("id",mId);
                startActivity(intent);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AppContext.getInstance().isBack = true;
        super.onBackPressed();
    }
}
