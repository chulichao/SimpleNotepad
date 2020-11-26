package com.clc.note.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clc.note.R;
import com.clc.note.adapter.TrashRecyclerAdapter;
import com.clc.note.util.AppContext;
import com.clc.note.util.RecordDao;

public class TrashListActivity extends AppCompatActivity {
    private static TrashListActivity sCurrAct;
    private RecordDao mRecordDao;

    public static TrashListActivity getCurrAct() {
        return sCurrAct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_list);

        sCurrAct = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trash_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_remove_all:
                dialogOnRemoveAll();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.trash_recyclerView);

        mRecordDao = new RecordDao();
        TrashRecyclerAdapter adapter = new TrashRecyclerAdapter(mRecordDao.queryInTrash());
        recyclerView.setLayoutManager(new LinearLayoutManager(AppContext.getInstance()));
        recyclerView.setAdapter(adapter);
    }

    private void dialogOnRemoveAll() {
        //Builder()需要特定的父容器对象，不可以用全局的application对象
        new AlertDialog.Builder(this)
                .setTitle(R.string.tip)
                .setMessage(R.string.are_you_remove_all)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RecordDao().deleteAllInTrash();

                        Intent intent = new Intent(AppContext.getInstance(), TrashListActivity.class);
                        startActivity(intent);

                        Toast.makeText(AppContext.getInstance(),R.string.has_remove_all_trash,Toast.LENGTH_SHORT);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRecordDao.closeAll();
    }
}
