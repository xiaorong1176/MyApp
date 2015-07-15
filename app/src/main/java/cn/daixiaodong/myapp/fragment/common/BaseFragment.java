package cn.daixiaodong.myapp.fragment.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;


public class BaseFragment extends Fragment {




    public void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
