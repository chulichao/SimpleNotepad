package com.clc.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.clc.note.R;
import com.clc.note.util.AppContext;
import com.clc.note.util.SharedPreferencesUtil;

public class PwdActivity extends AppCompatActivity {
    private String TAG = "PwdActivity------";
    private final String mAppPwdKey = "app_pwd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView msgTV = findViewById(R.id.text_checkMsg);
        CheckBox checkBox = findViewById(R.id.checkbox_open_pwd);
        final Group groupSet = findViewById(R.id.group_set_pwd);
        Button btnSet = findViewById(R.id.btn_confirm);
        final TextView updatePwdTV = findViewById(R.id.text_update_pwd);
        final Group groupUpdate = findViewById(R.id.group_update_pwd);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    //!!!
                    groupSet.setWillNotDraw(true);
                    groupSet.setVisibility(View.VISIBLE);
                }else {
                    //!!!
                    groupSet.setWillNotDraw(false);
                    groupSet.setVisibility(View.INVISIBLE);
                    groupUpdate.setWillNotDraw(false);
                    groupUpdate.setVisibility(View.INVISIBLE);
                    updatePwdTV.setVisibility(View.INVISIBLE);

                    new SharedPreferencesUtil().removeKey(mAppPwdKey);
                }
            }
        });

        final String app_pwd = new SharedPreferencesUtil().getValue(mAppPwdKey);
        if ("-1".equals(app_pwd)){
            checkBox.setChecked(false);
        }else {
            checkBox.setChecked(true);
            groupSet.setWillNotDraw(false);
            groupSet.setVisibility(View.INVISIBLE);
            updatePwdTV.setVisibility(View.VISIBLE);
        }

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pwdET = findViewById(R.id.editText_pwd);
                EditText repwdET = findViewById(R.id.editText_repwd);
                String pwd = pwdET.getText().toString();
                String repwd = repwdET.getText().toString();
                if ("".equals(pwd)||"".equals(repwd)){
                    msgTV.setText(R.string.please_input_all);
                    return;
                }
                if (!pwd.equals(repwd)){
                    msgTV.setText(R.string.two_pwd_not_same);
                    return;
                }
                new SharedPreferencesUtil().setKey(mAppPwdKey,pwd);

                groupSet.setWillNotDraw(false);
                groupSet.setVisibility(View.INVISIBLE);

                updatePwdTV.setVisibility(View.VISIBLE);

                msgTV.setText("");
                startActivity(new Intent(AppContext.getInstance(),LoginActivity.class));
                finish();
            }
        });

        updatePwdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupUpdate.setWillNotDraw(true);
                groupUpdate.setVisibility(View.VISIBLE);

                updatePwdTV.setVisibility(View.INVISIBLE);

                final EditText oldPwdET = findViewById(R.id.editText_old_pwd);
                final EditText newPwdET = findViewById(R.id.editText_new_pwd);
                final EditText reNewPwdET = findViewById(R.id.editText_new_repwd);

                findViewById(R.id.btn_update_pwd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldPwd = oldPwdET.getText().toString();
                        String newPwd = newPwdET.getText().toString();
                        String reNewPws = reNewPwdET.getText().toString();

                        if ("".equals(oldPwd)||"".equals(newPwd)||"".equals(reNewPws)){
                            msgTV.setText(R.string.please_input_all);
                            return;
                        }

                        if (!app_pwd.equals(oldPwd)){
                            msgTV.setText(R.string.wrong_old_pwd);
                            return;
                        }
                        if (!newPwd.equals(reNewPws)){
                            msgTV.setText(R.string.two_pwd_not_same);
                            return;
                        }

                        new SharedPreferencesUtil().setKey(mAppPwdKey,newPwd);

                        groupUpdate.setWillNotDraw(false);
                        groupUpdate.setVisibility(View.INVISIBLE);

                        updatePwdTV.setVisibility(View.VISIBLE);

                        msgTV.setText("");
                        startActivity(new Intent(AppContext.getInstance(),LoginActivity.class));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.getInstance().finish();
    }
}
