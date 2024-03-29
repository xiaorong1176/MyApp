package cn.daixiaodong.myapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.adapter.CollectAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;

import static android.support.v7.widget.RecyclerView.OnScrollListener;


/**
 * 收藏
 */
public class CollectFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private View mRootView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CollectAdapter mAdapter;
    private List<AVObject> mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView
                = inflater.inflate(R.layout.fragment_collect, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.id_srl_refresh_data);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.id_rv_collect_recycler_view);
        setUpRecyclerView();
        setRefreshLayout();
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                mRefreshLayout.setRefreshing(true);
                loadData(true);
            }
        });
    }

    private void setRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mData = new ArrayList<>();
        mAdapter = new CollectAdapter(getActivity(), mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onRefresh() {



            loadData(true);


    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {

            AVQuery<AVObject> query = new AVQuery<>("user_collect");
            query.whereEqualTo("user", AVUser.getCurrentUser());
            query.include("event");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    mRefreshLayout.setRefreshing(false);
                    if (e == null) {
                        mData.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        e.printStackTrace();

                    }
                }
            });

        }
    }
}
