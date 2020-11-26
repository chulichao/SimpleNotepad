package com.clc.note.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.clc.note.R;
import com.clc.note.util.AppContext;
import com.clc.note.util.RecordDao;

public class UpdateRecordActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private EditText mContentET;
    private EditText mTitleET;
    private RadioButton mRadioBtn0,mRadioBtn1,mRadioBtn2;
    private int mId,mTypeId;
    private String mTitle,mContent,mTypeName;
    private String TAG = "UpdateRecordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        mContent = intent.getStringExtra("content");
        mTypeId = intent.getIntExtra("typeId",-1);
        mId = intent.getIntExtra("id",-1);

        mRadioGroup = findViewById(R.id.add_radios);
        mContentET = findViewById(R.id.editText_content);
        mTitleET = findViewById(R.id.editText_title);
        mRadioBtn0 = findViewById(R.id.radioBtn0);
        mRadioBtn1 = findViewById(R.id.radioBtn1);
        mRadioBtn2 = findViewById(R.id.radioBtn2);

        mTitleET.setText(mTitle);
        mContentET.setText(mContent);
        switch (mTypeId){
            case 0:
                mRadioBtn0.setChecked(true);
                mTypeName = getString(R.string.record_type0);
                break;
            case 1:
                mRadioBtn1.setChecked(true);
                mTypeName = getString(R.string.record_type1);
                break;
            case 2:
                mRadioBtn2.setChecked(true);
                mTypeName = getString(R.string.record_type2);
                break;
        }

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
                if (isChange()){
                    showDialog(R.string.tip,R.string.you_not_save,R.string.direct_quit,R.string.continue_edit);
                }else {
                    finish();
                }
                break;
            case R.id.action_bar_save:
                //保存
                if (!isChange()){
                    showDialog(R.string.tip,R.string.you_not_update,R.string.direct_quit,R.string.continue_edit);
                }else if ("".equals(mContentET.getText().toString())){
                    showDialog(R.string.tip,R.string.you_not_edit,R.string.direct_quit,R.string.continue_edit);
                }else {
                    updateRecord();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isChange()){
            showDialog(R.string.tip,R.string.you_not_save,R.string.direct_quit,R.string.continue_edit);
        }else {
            super.onBackPressed();
        }
    }

    private void updateRecord() {
        //获取类型id
        int mTypeId = 0;
        switch (mRadioGroup.getCheckedRadioButtonId()){
            case R.id.radioBtn1:
                mTypeId = 1;
                break;
            case R.id.radioBtn2:
                mTypeId = 2;
                break;
        }

        //获取标题和内容
        String title = mTitleET.getText().toString();
        String content = mContentET.getText().toString();

        new RecordDao().update(title,content,mTypeId,mId);

        Intent intent1 = new Intent(this, MainActivity.class);
        AppContext.getInstance().typeIdAfterSave = mTypeId;
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

    private boolean isChange(){
        RadioButton rBtn = findViewById(mRadioGroup.getCheckedRadioButtonId());

        if (mTitle.equals(mTitleET.getText().toString())
            && mContent.equals(mContentET.getText().toString())
            && mTypeName.equals(rBtn.getText().toString())){

            return false;
        }
        return true;
    }
}
