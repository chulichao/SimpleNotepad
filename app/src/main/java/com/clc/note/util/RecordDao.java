package com.clc.note.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaCrypto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordDao {
    private SQLiteDatabase mReadableDB = DBHelper.getInstance().getReadableDatabase();
    private SQLiteDatabase mWritableDB = DBHelper.getInstance().getWritableDatabase();
    private Cursor mCursor;
    private final String mTableName = "tb_record";

    /**
     * @Description:按照类型查询记录
     * @param typeId:
     * @return: android.database.Cursor
     */
    public Cursor queryList(int typeId){
        String[] columns = {"_id","title","content","typeId","addTime"};
        mCursor = mReadableDB.query(mTableName,columns,"typeId = ? and isExist = 1",new String[]{typeId+""},
                null,null,"_id desc",null);
        return mCursor;
    }

    public void save(String title,String content,int typeId){
        ContentValues values = new ContentValues();
        if (!"".equals(title)){
            values.put("title",title);
        }
        values.put("content",content);
        values.put("typeId",typeId);
        values.put("addTime",getCurrTimeStr());

        try {
            mWritableDB.insert(mTableName,null,values);
        }finally {
            mWritableDB.close();
        }
    }

    public void update(String title,String content,int typeId,int id){
        ContentValues values = new ContentValues();

        values.put("title","".equals(title)?"no title":title);
        values.put("content",content);
        values.put("typeId",typeId);
        values.put("addTime",getCurrTimeStr());

        try {
            mWritableDB.update(mTableName,values,"_id = "+id,null);
        }finally {
            mWritableDB.close();
        }
    }

    //获取当前时间字符串
    public String getCurrTimeStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    //假删除
    public void deleteById(int id) {
        ContentValues values = new ContentValues();
        values.put("isExist",0);

        try {
            mWritableDB.update(mTableName,values,"_id = "+id,null);
        }finally {
            mWritableDB.close();
        }
    }

    public Cursor queryInTrash(){
        String[] columns = {"_id","title","content","typeId","addTime"};
        mCursor = mReadableDB.query(mTableName,columns,"isExist = 0",null,
                    null,null,"_id desc",null);
        return mCursor;
    }

    public void deleteOneInTrash(int id){
        try {
            mWritableDB.delete(mTableName,"_id = "+id,null);
        }finally {
            mWritableDB.close();
        }
    }

    public void deleteAllInTrash(){
        try {
            mWritableDB.delete(mTableName,"isExist = 0",null);
        }finally {
            mWritableDB.close();
        }
    }

    public void restoreById(int id) {
        ContentValues values = new ContentValues();
        values.put("isExist",1);

        try {
            mWritableDB.update(mTableName,values,"_id = "+id,null);
        }finally {
            mWritableDB.close();
        }
    }

    public void closeAll(){
        if (mCursor != null){
            mCursor.close();
        }

        mReadableDB.close();
    }
}
