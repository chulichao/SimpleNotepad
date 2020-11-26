package com.clc.note.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.clc.note.R;
import com.clc.note.util.AppContext;
import com.clc.note.util.RecordDao;

public class AddRecordActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private EditText mContentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        mRadioGroup = findViewById(R.id.add_radios);
        mContentET = findViewById(R.id.editText_content);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //返回
                if (!"".equals(mContentET.getText().toString())){
                    showDialog(R.string.tip,R.string.you_not_save,R.string.direct_quit,R.string.continue_edit);
                }else {
                    finish();
                }
                break;
            case R.id.action_bar_save:
                //保存
                if ("".equals(mContentET.getText().toString())){
                    showDialog(R.string.tip,R.string.you_not_edit,R.string.direct_quit,R.string.continue_edit);
                }else {
                    saveRecord();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!"".equals(mContentET.getText().toString())){
            showDialog(R.string.tip,R.string.you_not_save,R.string.direct_quit,R.string.continue_edit);
        }else {
            super.onBackPressed();
        }
    }

    private void saveRecord() {
        //获取类型id
        int typeId = 0;
        switch (mRadioGroup.getCheckedRadioButtonId()){
            case R.id.radioBtn1:
                typeId = 1;
                break;
            case R.id.radioBtn2:
                typeId = 2;
                break;
        }

        //获取标题和内容
        EditText titleET = findViewById(R.id.editText_title);
        String title = titleET.getText().toString();
        String content = mContentET.getText().toString();

        new RecordDao().save(title,content,typeId);

        Intent intent1 = new Intent(this, MainActivity.class);
        AppContext.getInstance().typeIdAfterSave = typeId;
        startActivity(intent1);
    }

    private void showDialog(int title,int msg,int pBtnStr,int nBtnStr){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(pBtnStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(nBtnStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        return;
                    }
                })
                .create().show();
    }
}
