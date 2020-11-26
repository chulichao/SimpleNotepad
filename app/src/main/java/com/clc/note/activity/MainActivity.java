package com.clc.note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.clc.note.R;
import com.clc.note.fragment.MainFragment;
import com.clc.note.util.AppContext;
import com.clc.note.util.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {
    private static MainActivity sCurrAct;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton0;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private FragmentManager mFragmentMgr;
    FragmentTransaction mTransaction;
    private String TAG = "MainActivity------";

    public static MainActivity getInstance(){
        return sCurrAct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String app_pwd = new SharedPreferencesUtil().getValue("app_pwd");
        if (!"-1".equals(app_pwd)){
            startActivity(new Intent(AppContext.getInstance(),LoginActivity.class));
        }

        sCurrAct = this;

        mFragmentMgr = getSupportFragmentManager();
        mRadioButton0 = findViewById(R.id.radioBtn0);
        mRadioButton1 = findViewById(R.id.radioBtn1);
        mRadioButton2 = findViewById(R.id.radioBtn2);

        findViewById(R.id.btn_addrecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppContext.getInstance(),AddRecordActivity.class);
                startActivity(intent);
            }
        });

        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //一个事务只能提交一次
                mTransaction = mFragmentMgr.beginTransaction();
                switch (i){
                    case R.id.radioBtn0:
                        //可以直接执行replace，即使之前没有add
                        mTransaction.replace(R.id.framelayout_main,new MainFragment(0));
                        break;
                    case R.id.radioBtn1:
                        mTransaction.replace(R.id.framelayout_main,new MainFragment(1));
                        break;
                    case R.id.radioBtn2:
                        mTransaction.replace(R.id.framelayout_main,new MainFragment(2));
                        break;
                }
                mTransaction.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final AppContext appContext = AppContext.getInstance();

        if (!appContext.isBack){
            /*
            在重新选中某个radioButton之前，先清空任何的选中，
            才能保证onCheckedChanged()在每次选中时都会被触发，
            以达到及时刷新页面、显示刚加记录的预期效果
             */
            mRadioGroup.clearCheck();

            switch (AppContext.getInstance().typeIdAfterSave){
                case 0:
                    mRadioButton0.setChecked(true);
                    break;
                case 1:
                    mRadioButton1.setChecked(true);
                    break;
                case 2:
                    mRadioButton2.setChecked(true);
                    break;
                default:
            }
        }

        appContext.isBack = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.aciton_bar_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_trash:
                Intent intent1 = new Intent(MainActivity.this,TrashListActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_set_pwd:
                Intent intent2 = new Intent(MainActivity.this,PwdActivity.class);
                startActivity(intent2);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
