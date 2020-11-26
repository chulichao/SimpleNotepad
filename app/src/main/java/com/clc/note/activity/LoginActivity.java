package com.clc.note.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.clc.note.R;
import com.clc.note.util.AppContext;
import com.clc.note.util.SharedPreferencesUtil;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        final EditText userPwdET = findViewById(R.id.editText_user_pwd);
        final TextView msgTV = findViewById(R.id.text_checkMsg);
        final String app_pwd = new SharedPreferencesUtil().getValue("app_pwd");

        findViewById(R.id.btn_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPwd = userPwdET.getText().toString();

                if ("".equals(userPwd)){
                    msgTV.setText(R.string.please_input_pwd);
                    return;
                }

                if (!app_pwd.equals(userPwd)){
                    msgTV.setText(R.string.pwd_wrong);
                    return;
                }

                startActivity(new Intent(AppContext.getInstance(),MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.getInstance().finish();
    }
}
