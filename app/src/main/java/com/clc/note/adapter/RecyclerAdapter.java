package com.clc.note.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clc.note.R;
import com.clc.note.activity.DetailRecordActivity;
import com.clc.note.activity.MainActivity;
import com.clc.note.util.AppContext;
import com.clc.note.util.RecordDao;

public class RecyclerAdapter extends RecyclerView.Adapter {
    private Cursor mCursor;

    private int mTypeId;
    private AppContext mAppContext = AppContext.getInstance();
    private String TAG = "RecyclerAdapter------";

    public RecyclerAdapter(Cursor mCursor,int mTypeId) {
        this.mCursor = mCursor;
        this.mTypeId = mTypeId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        mCursor.moveToPosition(position);

        int id = mCursor.getInt(mCursor.getColumnIndex("_id"));
        String title = mCursor.getString(mCursor.getColumnIndex("title"));
        String content = mCursor.getString(mCursor.getColumnIndex("content"));
        String addTime = mCursor.getString(mCursor.getColumnIndex("addTime"));

        myViewHolder.titleItemTV.setText(title);
        myViewHolder.contentItemTV.setText(content);
        myViewHolder.timeItemTV.setText(addTime);
        myViewHolder.idItemTV.setText(id+"");
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleItemTV,contentItemTV,timeItemTV,idItemTV,deleteItemIV;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            titleItemTV = itemView.findViewById(R.id.item_view_title);
            contentItemTV = itemView.findViewById(R.id.item_view_content);
            timeItemTV = itemView.findViewById(R.id.item_view_time);
            idItemTV = itemView.findViewById(R.id.item_view_id);
            deleteItemIV = itemView.findViewById(R.id.item_view_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                private AppContext mAppContext = AppContext.getInstance();
                @Override
                public void onClick(View view) {
                    if (deleteItemIV.getVisibility() == View.VISIBLE){
                        deleteItemIV.setVisibility(View.INVISIBLE);
                        return;
                    }

                    Intent intent = new Intent(mAppContext, DetailRecordActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("typeId",mTypeId);
                    intent.putExtra("title",titleItemTV.getText().toString());
                    intent.putExtra("content",contentItemTV.getText().toString());
                    intent.putExtra("addTime",timeItemTV.getText().toString());
                    //不同组件之间传递值时，注意变量类型一致
                    intent.putExtra("id",Integer.parseInt(idItemTV.getText().toString()));

                    mAppContext.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteItemIV.setVisibility(View.VISIBLE);
                    deleteItemIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //删除
                            new RecordDao().deleteById(Integer.parseInt(idItemTV.getText().toString()));

                            Intent intent1 = new Intent(mAppContext, MainActivity.class);
                            mAppContext.startActivity(intent1);

                            mAppContext.typeIdAfterSave = mTypeId;

                            Toast.makeText(mAppContext,R.string.has_to_trash,Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });
        }
    }
}
