package cn.daixiaodong.myapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.adapter.DreamAdapter;
import cn.daixiaodong.myapp.fragment.common.SwipeRefreshBaseFragment;

/**
 * 所有的Dream列表
 */
public class DreamListFragment extends SwipeRefreshBaseFragment implements DreamAdapter.OnItemClickListener{

    private RecyclerView mDreamRecyclerView;
    private List<AVObject> mData;
    DreamAdapter mAdapter;
    private Date mLastCreateAtDate;
    private boolean isFirstRefresh = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init(View view) {
        mData = new ArrayList<>();
        mDreamRecyclerView = (RecyclerView) view.findViewById(R.id.id_rv_dream_recycler_view);
        mDreamRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setupRecyclerView();
        // loadData();
    }

    private void loadData(int start,int offset) {
        AVQuery<AVObject> query = new AVQuery<>("Post");
        query.setSkip(start);
        // 根据 createdAt 字段降序显示数据
        query.orderByDescending("createdAt");
      /*  if (isFirstRefresh) {
            isFirstRefresh = false;
        } else {
            query.whereGreaterThan("createAt", mLastCreateAtDate);
        }*/
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
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_dream_list;
    }

    @Override
    public int getSwipeRefreshLayoutResId() {
        return R.id.id_srl_refresh_data;
    }

    @Override
    protected void refreshData() {
        super.refreshData();
        loadData(0,10);

    }

    private void setupRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDreamRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DreamAdapter(getActivity(), mData);
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
        loadData(0,10);
    }

    @Override
    public void onItemClick(DreamAdapter.MyViewHolder viewHolder, int pos) {
        Log.i("tag","点击"+pos);
    }
}
