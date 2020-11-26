package com.clc.note.fragment;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clc.note.R;
import com.clc.note.adapter.RecyclerAdapter;
import com.clc.note.util.AppContext;
import com.clc.note.util.RecordDao;

public class MainFragment extends Fragment {
    private AppContext mAppContext = AppContext.getInstance();
    private int mTypeId;
    private RecyclerView mRecyclerView;
    private RecordDao mRecordDao;

    private String TAG = "MainFragment------";

    public MainFragment(int mTypeId) {
        this.mTypeId = mTypeId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        mRecordDao = new RecordDao();

        RecyclerAdapter adapter = new RecyclerAdapter(mRecordDao.queryList(mTypeId),mTypeId);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAppContext));
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRecordDao.closeAll();
    }
}
