package com.example.a13.datasave;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 13 on 2016/11/2.
 */

//信息查询类
 class QueueInfo {
    private MydatabaseHelper baseHelper; //Helper
    private SQLiteDatabase database;     //数据库
    private String userName;             //用户名
    private String userPassword;         //用户密码
    public String resultName;           //数据库返回的用户名
    public String resultPassword;       //数据库返回的用户密码
    private Context context;

    public String CREATE_TABLE="create table userInfo("     //表名
            +"user_id integer primary key autoincrement,"   //用户ID
            +"user_name,"                                   //用户名
            +"user_password)";                              //用户密码

    QueueInfo(Context context,MydatabaseHelper baseHelper,SQLiteDatabase database){
        this.context = context;
        this.baseHelper=baseHelper;
        this.database=database;
    }
//根据用户名查询方法
    public void queue(String userName){

        this.userName=userName;
        //返回查询结果的游标
            Cursor cursor = database.rawQuery("select user_name from userInfo.db where user_name='"+userName+"'",null);
        //若查询到该数据
        if(cursor.getCount()!=0){
                resultName=cursor.getString(cursor.getColumnIndex("user_name"));
                resultPassword=cursor.getString(cursor.getColumnIndex("user_password"));
                Toast.makeText(context,"查询到的cursor(行数)大小为："+cursor.getCount()
                        +"用户名(getString(1))为"+resultName+" 密码(getString(2))为"+resultPassword,Toast.LENGTH_SHORT).show();
                cursor.close();
        }
        //查询不到该数据
        else{
                Toast.makeText(context,"数据查询失败",Toast.LENGTH_SHORT).show();
            }
    }
}
