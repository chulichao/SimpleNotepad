package com.clc.note.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.clc.note.activity.TrashListActivity;
import com.clc.note.util.AppContext;
import com.clc.note.util.RecordDao;

public class TrashRecyclerAdapter extends RecyclerView.Adapter {
    private AppContext mAppContext = AppContext.getInstance();
    private Cursor mCursor;

    public TrashRecyclerAdapter(Cursor mCursor) {
        this.mCursor = mCursor;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trash_recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        mCursor.moveToPosition(position);

        int id = mCursor.getInt(mCursor.getColumnIndex("_id"));
        String title = mCursor.getString(mCursor.getColumnIndex("title"));
        String content = mCursor.getString(mCursor.getColumnIndex("content"));
        String addTime = mCursor.getString(mCursor.getColumnIndex("addTime"));
        int typeId = mCursor.getInt(mCursor.getColumnIndex("typeId"));
        int typeNameResId = R.string.record_type0;
        switch (typeId){
            case 1:
                typeNameResId = R.string.record_type1;
                break;
            case 2:
                typeNameResId = R.string.record_type2;
                break;
            default:
        }

        myViewHolder.titleItemTV.setText(title);
        myViewHolder.contentItemTV.setText(content);
        myViewHolder.timeItemTV.setText(addTime);
        myViewHolder.idItemTV.setText(id+"");
        myViewHolder.typeNameItemTV.setText(typeNameResId);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleItemTV,contentItemTV,timeItemTV,idItemTV,deleteItemIV,typeNameItemTV,restoreItemTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleItemTV = itemView.findViewById(R.id.item_view_title);
            contentItemTV = itemView.findViewById(R.id.item_view_content);
            timeItemTV = itemView.findViewById(R.id.item_view_time);
            idItemTV = itemView.findViewById(R.id.item_view_id);
            deleteItemIV = itemView.findViewById(R.id.item_view_delete);
            typeNameItemTV = itemView.findViewById(R.id.item_view_typeName);
            restoreItemTV = itemView.findViewById(R.id.item_view_restore);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteItemIV.setVisibility(View.VISIBLE);
                    restoreItemTV.setVisibility(View.VISIBLE);

                    deleteItemIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogOnDelete();
                        }
                    });
                    restoreItemTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new RecordDao().restoreById(Integer.parseInt(idItemTV.getText().toString()));

                            Intent intent = new Intent(mAppContext, TrashListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mAppContext.startActivity(intent);

                            Toast.makeText(mAppContext,R.string.has_restore,Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemIV.setVisibility(View.INVISIBLE);
                    restoreItemTV.setVisibility(View.INVISIBLE);
                }
            });
        }

        private void dialogOnDelete() {
            //Builder()需要特定的父容器对象，不可以用全局的application对象
            new AlertDialog.Builder(TrashListActivity.getCurrAct())
                    .setTitle(R.string.tip)
                    .setMessage(R.string.are_you_remove_this)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new RecordDao().deleteOneInTrash(Integer.parseInt(idItemTV.getText().toString()));

                            Intent intent = new Intent(mAppContext, TrashListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mAppContext.startActivity(intent);

                            Toast.makeText(mAppContext,R.string.delete_forever,Toast.LENGTH_SHORT).show();
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
    }
}
