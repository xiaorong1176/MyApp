package cn.daixiaodong.myapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.CreateDreamActivity_;
import cn.daixiaodong.myapp.activity.SignInActivity_;
import cn.daixiaodong.myapp.activity.TopicActivity_;
import cn.daixiaodong.myapp.adapter.DreamAdapter;
import cn.daixiaodong.myapp.fragment.common.BaseFragment;
import cn.daixiaodong.myapp.utils.NetworkUtil;
import cn.daixiaodong.myapp.view.FixedRecyclerView;

/**
 * 所有的Dream列表
 */
public class DreamListFragment extends BaseFragment {

    private View mView;
    private FixedRecyclerView mDreamRecyclerView;
    private List<AVObject> mData;
    DreamAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private int mLastVisibleItem;

    private Date mLastDate;

    private Date mMinDate;
    private int mPager = 1;

    private boolean isFirstEnter = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dream_list, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRecyclerView();
        setUpSwipeRefreshLayout();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadData(true);
            }
        });
        /*if(NetworkUtil.isNetworkConnected(getActivity())){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadData();
                }
            });
        }else{
            final AVQuery<AVObject> query = new AVQuery<>("dream");
            query.orderByDescending("createAt");
            query.setLimit(10);
            query.setSkip(0);
            query.include("user");
            boolean b = query.hasCachedResult();
            if(b )
        }*/


    }

    private void setUpSwipeRefreshLayout() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_srl_refresh_data);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });
    }

    private void loadData(final boolean isRefresh) {
        final AVQuery<AVObject> query = new AVQuery<>("dream");
        query.orderByDescending("createdAt");
        query.setLimit(1);

        query.include("user");
        if (isRefresh) {
            if (!isFirstEnter) {
                query.whereGreaterThan("createdAt", mLastDate);
            }
            query.setSkip(0);
        } else {
            query.whereLessThan("createdAt", mMinDate);
            query.setSkip(0);
        }
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e == null) {
                    isFirstEnter = false;
                    if (!list.isEmpty()) {
                        Log.i("list size", list.size() + "");
                        if (isRefresh) {
                            AVObject object0 = list.get(0);

                            mLastDate = object0.getCreatedAt();
                            Log.i("mLastDate", mLastDate.toString());
                        }
                        AVObject objectlast = list.get(list.size() - 1);
                        mMinDate = objectlast.getCreatedAt();
                        Log.i("mMinDate", mMinDate.toString());
                        for (AVObject object : list) {
                            AVUser user = object.getAVUser("user");

                            AVQuery<AVObject> query1 = new AVQuery<>("user_join");
                            query1.whereEqualTo("user", AVUser.getCurrentUser());

                            query1.whereEqualTo("dream", object);

                            query1.countInBackground(new CountCallback() {
                                @Override
                                public void done(int i, AVException e) {
                                    Log.i("count", i + "");
                                }
                            });
                        }
                        if (isRefresh) {
                            mData.clear();
                            mData.addAll(0, list);
                        }else{
                            mData.addAll(list);
                            mPager++;
                        }
                        mAdapter.notifyDataSetChanged();
                        if (!NetworkUtil.isNetworkConnected(getActivity())) {
                            showToast(getActivity(), "网络错误");
                        }
                    } else {
                        showToast(getActivity(), "没有更多数据了");
                    }
                } else {
                    e.printStackTrace();
                    if (e.getCode() == AVException.CACHE_MISS) {

                    }
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mDreamRecyclerView =
                (FixedRecyclerView) mView.findViewById(R.id.id_rv_dream_recycler_view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mDreamRecyclerView.setLayoutManager(linearLayoutManager);
        mData = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        float scale = getResources().getDisplayMetrics().density;


        mAdapter = new DreamAdapter(getActivity(), mData);
        mAdapter.setImageSize(width, scale);
        mDreamRecyclerView.setHasFixedSize(false);
        mDreamRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDreamRecyclerView.setAdapter(mAdapter);
        mDreamRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItem + 1 == mAdapter.getItemCount()) {
                    //  mSwipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    // showToast(getActivity(), "刷新");
                    mSwipeRefreshLayout.setRefreshing(true);
                    loadData(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.id_fab_floating_action_button);
        fab.attachToRecyclerView(mDreamRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AVAnalytics.onEvent(getActivity(), "create_dream");
                if (AVUser.getCurrentUser() == null) {
                    SignInActivity_.intent(getActivity()).extra("log_in_toward", 0).start();
                } else {
                    CreateDreamActivity_.intent(getActivity()).start();
                }
            }
        });
        mAdapter.setOnItemClickListener(new DreamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DreamAdapter.MyViewHolder viewHolder, int pos) {

                String objectId = mData.get(pos).getObjectId();
                Intent intent = new Intent(getActivity(), TopicActivity_.class);
                intent.putExtra("objectId", objectId);
                startActivity(intent);
            }
        });
    }
















/*
    private void init(View view) {
        mData = new ArrayList<>();
        mDreamRecyclerView = (RecyclerView) view.findViewById(R.id.id_rv_dream_recycler_view);
        mDreamRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setupRecyclerView();
        // loadData();
    }

    private void loadData(int start, int offset) {
        AVQuery<AVObject> query = new AVQuery<>("Post");
        query.setSkip(start);
        // 根据 createdAt 字段降序显示数据
        query.orderByDescending("createdAt");
      *//*  if (isFirstRefresh) {
            isFirstRefresh = false;
        } else {
            query.whereGreaterThan("createAt", mLastCreateAtDate);
        }*//*
        query.setLimit(offset);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                    if (avObjects.size() > 0) {
                        mLastCreateAtDate = avObjects.get(0).getDate("createAt");
                        mAdapter.addData(avObjects);
                        //mAdapter.notifyDataSetChanged();
                    } else {
                        showToast(getActivity(), "木有更多了....");
                    }
                    setRefreshing(false);
                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
    }*/



    /*private void setupRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDreamRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DreamAdapter(getActivity(), mData);
        mAdapter.setOnItemClickListener(this);
        mDreamRecyclerView.setAdapter(mAdapter);
        mDreamRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastCompletelyVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPosition();
                Log.i("lastCompletely", "" + lastCompletelyVisibleItemPos);

                int lastItemPos = layoutManager.findLastVisibleItemPosition();
                Log.i("lastVisibleItemPos:", "" + lastItemPos);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Log.i("newState---->", "dragging");
                        if ((lastItemPos + 1) == layoutManager.getItemCount()) {
                            mSwipeRefreshLayout.setRefreshing(true);
                            loadMoreData();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.i("newState---->", "idle");

                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Log.i("newState---->", "settling");

                        break;
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("dx----->", dx + "");
                Log.i("dy----->", dy + "");
            }
        });


    }

    private void loadMoreData() {
        loadData(0, 10);
    }

    @Override
    public void onItemClick(DreamAdapter.MyViewHolder viewHolder, int pos) {
        Log.i("tag", "点击" + pos);
        String objectId = mData.get(pos).getObjectId();

        Intent intent = new Intent(getActivity(), DreamDetailActivity_.class);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
    }*/
}
