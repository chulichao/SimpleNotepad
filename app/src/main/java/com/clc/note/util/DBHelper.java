package com.clc.note.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper sDBHelper;

    private DBHelper(Context context) {
        //强制子类调用父类的构造方法
        super(context, "note_v2.db", null, 1);
    }

    public static DBHelper getInstance(){
        if (sDBHelper == null){
            sDBHelper = new DBHelper(AppContext.getInstance());
        }
        return sDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        表：tb_record
        字段名	数据类型	说明
        _id	    integer	主键，自增
        title	text	一条记录的标题，默认值“no title ”
        content	text	一条记录的内容
        typeId	tinyint	记录的类型id (0:工作,1:学习,2:生活)
        addTime	text	记录添加的时间，存入相应格式的字符串
        isExist	tinyint	是否存在表中（0:不存在表中,在回收站中；1:在表中）,默认1
         */
        db.execSQL("create table tb_record(" +
                "_id integer primary key autoincrement," +
                "title text default 'no title'," +
                "content text," +
                "typeId tinyint," +
                "addTime datetime," +
                "isExist tinyint default 1" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
