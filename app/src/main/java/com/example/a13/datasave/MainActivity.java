package com.example.a13.datasave;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//定义SharedPreferences 及其 Editor 对象
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //用户名  密码 输入框
    EditText userName,userPass;
    // 登录  注册 按钮
    Button btn_login,btn_register;
    //保存获取到的 用户名 密码
    String userNameInfo="",userPassInfo="";
    //获取查询类和添加类
    QueueInfo queueInfo;
    InsertInfo insertInfo;
    //获取SQLiteOpenHelper类
    MydatabaseHelper helper;

    public SQLiteDatabase database;
    //数据库路径
    String databasePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        databasePath=this.getFilesDir().toString()+"/userInformations.db3";
//        database=SQLiteDatabase.openDatabase(databasePath,null,0);
        initView();

    }

    @Override
    protected void onPause() {
        //清空输入框
        userName.setText("");
        userPass.setText("");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(helper!=null){
            helper.close();
        }
    }

    //初始化视图
    private void initView() {
        preferences=getPreferences(Context.MODE_PRIVATE);
        editor=preferences.edit();

        userName = (EditText) findViewById(R.id.userName);
        userPass = (EditText) findViewById(R.id.userPass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        helper = new MydatabaseHelper(MainActivity.this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

//***********************************点击登录（开辟新线程进行验证操作,防止UI线程ANR）****************************************

            case R.id.btn_login:
            /*
            *1.验证用户名是否存在数据库中
            *   2.若不存在：Toast"还没有账号哦，请先注册"
            *    若存在：验证密码是否正确
            *   3.若密码不正确：(1)Toast"密码不正确，请重新输入" (2)清空密码输入框
            *    若密码正确：跳转Activity
            */
                userNameInfo=userName.getText().toString();
                userPassInfo=userPass.getText().toString();

                //输入框的值不能为空
                if(!Objects.equals(userNameInfo, "") && !Objects.equals(userPassInfo, "")){
                    User user_login = new User();
                    user_login.setUserName(userNameInfo);
                    user_login.setUserPassword(userPassInfo);
                    //1.查询该用户名
                    helper.queue(user_login);
                    //2.若用户存在
                    if(helper.userExists){
                        Toast.makeText(MainActivity.this,"该用户存在",Toast.LENGTH_SHORT).show();
                        //3.密码验证成功
                        if(Objects.equals(helper.result_userPassword, userPassInfo)){
                            Intent intent_success = new Intent(MainActivity.this,SuccessActivity.class);
                            startActivity(intent_success);
                        }
                        //3.密码验证失败
                        else{
                            Toast.makeText(MainActivity.this,"密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
                            userPass.setText("");
                        }
                        helper.userExists=false;//初始赋值
                    }
                    //2.若不存在
                    else{
                        Toast.makeText(MainActivity.this,"该用户不存在，请先注册哦",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"输入数据不能为空",Toast.LENGTH_SHORT).show();
                }

                break;

//**********************************************点击注册*************************************************************

            case R.id.btn_register:
                /*
                *1.获取用户名查询数据库是否已存在该用户
                *   2.若已存在：Toast"该用户已存在，请直接登录吧"
                *    若不存在：获取用户名及密码存入数据库
                *       3.存入成功：Toast"已注册成功，点击登录吧"
                */
                userNameInfo=userName.getText().toString();
                userPassInfo=userPass.getText().toString();

                //输入框的值不能为空
                if(!Objects.equals(userNameInfo,"") && !Objects.equals(userPassInfo,"")){
                    User user_register = new User();
                    user_register.setUserName(userNameInfo);
                    user_register.setUserPassword(userPassInfo);

                    //查询该用户是否存在
                    helper.queue(user_register);
                    //用户不存在，允许注册
                    if(!helper.userExists){
                        helper.insert(user_register);
                        Toast.makeText(MainActivity.this,"注册成功，可以登录了哦",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"注册失败，该用户已存在",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"输入数据不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
