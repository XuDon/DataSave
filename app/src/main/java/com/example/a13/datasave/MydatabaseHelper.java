package com.example.a13.datasave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 13 on 2016/11/2.
 */

public class MydatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Users";

    private Context context;
    public boolean userExists=false;//判断该用户是否存在
    public String result_userName,result_userPassword;//查询到的用户名与密码



    public MydatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        String CREATE_TABLE="create table userInfo("            //tableName
                +"user_id integer primary key autoincrement,"   //userId
                +"user_name,"                                   //userName
                +"user_password)";                              //userPassword

        //第一次使用时自动建表
        db.execSQL(CREATE_TABLE);

        //插入初始用户
        db.execSQL("insert into userInfo (user_name,user_password) values (123,123)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists userInfo");
        this.onCreate(db);
    }

    //*****************************************************************************
    // insert()-insert new user   queue()-queue the user is exists
    // delete()-delete user       update()-change user's password

    public final static String UserName = "user_name";
    public final static String UserPass ="user_password";


    public void insert(User user){

        //1.get db
        SQLiteDatabase database = this.getWritableDatabase();

        //2.create ContentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserName,user.getUserName());
        contentValues.put(UserPass,user.getUserPassword());
        //先查询用户是否存在
        if(!queue(user)){
            //3.insert to table
//            database.insert(TableName,null,contentValues);
            database.execSQL("insert into userInfo (user_name,user_password) values ("+user.getUserName()+","+user.getUserPassword()+")");

            //4.close database
            database.close();
        }
        else{
            Toast.makeText(context,"该用户已存在,可以直接登录哦",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean queue(User user){
        //1.get db
        SQLiteDatabase database = this.getReadableDatabase();

        //2.queue user
        Cursor cursor =
                database.rawQuery(
                        "select * from userInfo where user_name="+user.getUserName(),null);
        //如果查到该用户
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                result_userName=cursor.getString(1);
                result_userPassword=cursor.getString(2);
                userExists=true;
                Toast.makeText(context,
//                        "Cursor.getCount()="+cursor.getCount()  //返回游标查询到的行数
//                        +"cursor.getColumnCount()="+cursor.getColumnCount()     //返回游标查询到的行数
//                        +"cursor.getColumnIndex(\"user_name\")="+cursor.getColumnIndex("user_name")
//                        +"cursor.getColumnIndex(\"user_password\")="+cursor.getColumnIndex("user_password")
                        "该用户名为:"+result_userName+" 密码为:"+result_userPassword,Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            database.close();
            return true;
        }
        //如果该用户不存在
        else{
            userExists=false;
            Toast.makeText(context,"该用户不存在",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
