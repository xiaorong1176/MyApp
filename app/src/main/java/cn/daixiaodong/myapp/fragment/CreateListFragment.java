package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

/**
 *  用户创建的Dream列表
 */
public class CreateListFragment extends BaseFragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_list,container,false);
    }
}
