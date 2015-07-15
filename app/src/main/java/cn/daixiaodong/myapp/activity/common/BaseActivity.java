package cn.daixiaodong.myapp.activity.common;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by daixiaodong on 15/7/11.
 */
public class BaseActivity extends AppCompatActivity {


    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
