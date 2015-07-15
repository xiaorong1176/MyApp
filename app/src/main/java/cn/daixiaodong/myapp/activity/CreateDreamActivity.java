package cn.daixiaodong.myapp.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.activity.common.BaseActivity;

/**
 * Created by daixiaodong on 15/7/15.
 */
@EActivity(R.layout.activity_create_dream)
public class CreateDreamActivity extends BaseActivity {

    @ViewById(R.id.id_tb_toolbar)
    Toolbar mViewToolbar;


    @AfterViews
    void init() {
        initToolbar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_dream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_done:

                break;
         /*   case R.id.home:
                finish();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(mViewToolbar);
        mViewToolbar.setTitle("创建");
        mViewToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
        mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.home) {
                    finish();
                }
            }
        });
        mViewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();

            }
        });
        mViewToolbar.setTitleTextColor(Color.WHITE);
    }

}
