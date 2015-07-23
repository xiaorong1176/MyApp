package cn.daixiaodong.myapp.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;
import cn.daixiaodong.myapp.adapter.TopicAdapter;



@EActivity
public class TopicActivity extends BaseActivity implements TopicAdapter.OnItemClickListener {

    private List<AVObject> mData;
    private TopicAdapter mAdapter;


    @ViewById(R.id.id_rv_recycler_view)
    RecyclerView mRecyclerView;


    @AfterViews
    void init() {
        setUpRecyclerView();
        loadData();
    }

    private void loadData() {
        AVQuery<AVObject> query = new AVQuery<>("association");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null){
                    mData.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mData = new ArrayList<>();
        mAdapter = new TopicAdapter(this, mData);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(TopicAdapter.MyViewHolder viewHolder, int pos) {
        AssociationActivity_.intent(this).start();
    }

    @Override
    public void onItemJoinClick(TopicAdapter.MyViewHolder viewHolder, int pos) {
        RegistrationInformationActivity_.intent(this).start();
    }
}
