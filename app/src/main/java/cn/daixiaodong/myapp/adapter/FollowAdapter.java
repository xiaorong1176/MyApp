package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;

import java.util.List;

import cn.daixiaodong.myapp.R;


public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;

    public FollowAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public FollowAdapter(Context context, List<AVObject> data) {
        this.mContext = context;
        this.mDataSet = data;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setDataSet(List<AVObject> data) {
        this.mDataSet = data;
        notifyDataSetChanged();
    }

    public void addData(List<AVObject> data) {
        this.mDataSet.addAll(0, data);
        this.notifyItemInserted(1);
    }

    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_dream, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FollowAdapter.MyViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });

        viewHolder.title.setText(mDataSet.get(i).getString("username"));
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null) {
            return 0;
        }
        return mDataSet.size();
    }


    public interface OnItemClickListener {
        void onItemClick(MyViewHolder viewHolder, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.id_tv_title);
        }
    }


}
