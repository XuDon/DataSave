package com.example.a13.datasave;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Objects;

/**
 * Created by 13 on 2016/11/2.
 */

//信息保存类
public class InsertInfo {
    private MydatabaseHelper baseHelper; //数据库
    private SQLiteDatabase database;     //Helper
    private String userName;             //用户名
    private String userPassword;         //用户密码
    private QueueInfo queueInfo;
    Context context;

    InsertInfo(Context context,SQLiteOpenHelper baseHelper,SQLiteDatabase database){
        this.context=context;
        this.database=database;

    }
    public void insert(String userName,String userPassword){
        this.userName=userName;
        this.userPassword=userPassword;
 //获取查询类实例
        queueInfo=new QueueInfo(context,baseHelper,new MainActivity().database);
 //判断是否已经存在该用户名
        queueInfo.queue(userName);
        //如果该用户名已经存在
        if(queueInfo.resultName!=null && !Objects.equals(queueInfo.resultName, "")) {
            Toast.makeText(context, "该用户名已存在", Toast.LENGTH_SHORT).show();
        }
//执行插入操作
        else {
            //database.execSQL("insert into userInfo (user_name,user_password) values(" + userName + "," + userPassword + ")");
            ContentValues contentValues = new ContentValues();
            contentValues.put("user_name",userName);
            contentValues.put("user_password",userPassword);
            database.insert("userInfo",null,contentValues);
//插入数据后查询判断是否已录入数据库成功
            queueInfo.queue(userName);
//            if (queueInfo.resultName != null && queueInfo.resultName != "") {
//                Toast.makeText(context, "数据存储成功", Toast.LENGTH_SHORT).show();
//            }
            Toast.makeText(context, "添加用户成功", Toast.LENGTH_SHORT).show();
        }
    }
}
