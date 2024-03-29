package cn.daixiaodong.myapp.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.utils.NetworkUtil;

/**
 *  登录界面
 */
@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity {

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


    @ViewById(R.id.id_et_phone_number)
    EditText mViewPhoneNumber;

    @ViewById(R.id.id_et_password)
    EditText mViewPassword;

    @ViewById(R.id.id_btn_sign_in)
    Button mViewSignIn;

    @ViewById(R.id.id_btn_sign_up_now)
    Button mViewSignUp;


    @ViewById(R.id.id_tv_forgot_password)
    TextView mViewForgotPassword;


    @Extra("log_in_toward")
    int LogInToward;

    @Click(R.id.id_btn_sign_up_now)
    void signUp() {
        SignUpFirstStepActivity_.intent(this).start();
    }

    @AfterViews
    void init(){
        initToolbar();
    }

    @Click(R.id.id_btn_sign_in)
    void signIn() {


        String phoneNumber = mViewPhoneNumber.getText().toString();
        String password = mViewPassword.getText().toString();
        if(!checkData(phoneNumber,password)){
            return;
        }
        if(!NetworkUtil.isNetworkConnected(this)){
            showToast("请检查网络是否连接");
            return;
        }
        signInByPhoneNumber(phoneNumber, password);
    }


    @Click(R.id.id_tv_forgot_password)
    void forgotPassword(){
        PasswordResetActivity_.intent(this).start();
    }


    /**
     * 通过手机号码登录
     * @param phoneNumber 手机号
     * @param password 密码
     */
    private void signInByPhoneNumber(String phoneNumber, String password) {
        AVUser.loginByMobilePhoneNumberInBackground(phoneNumber, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    showToast("登录成功");

                    switch (LogInToward){
                        case 0:
                            CreateDreamActivity_.intent(SignInActivity.this).start();
                            break;
                    }
                    finish();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 校验用户输入
     * @param phoneNumber 手机号
     * @param password 密码
     */
    private boolean checkData(String phoneNumber, String password) {
        if (phoneNumber.isEmpty()) {
            showToast("请输入手机号码");
            return false;
        }
        if (phoneNumber.length() != 11) {
            showToast("请输入正确的手机号码");
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     *  设置Toolbar，设置标题，设置Drawer导航
     */
    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitleTextColor(Color.WHITE);
        mViewToolbar.setTitle("登录");
        mViewToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
        mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
