package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.daixiaodong.myapp.R;


public class DreamAdapter extends RecyclerView.Adapter<DreamAdapter.MyViewHolder> {

    private Context mContext;
    private List<AVObject> mDataSet;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    private int mWidth;
    private float mScale;

    public DreamAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public DreamAdapter(Context context, List<AVObject> data) {
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
    public DreamAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = mLayoutInflater.inflate(R.layout.item_dream_new, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DreamAdapter.MyViewHolder viewHolder, final int i) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(viewHolder, i);
                }
            }
        });

        viewHolder.title.setText(mDataSet.get(i).getString("title") + mDataSet.get(i).getAVUser("user").getUsername());
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.image.getLayoutParams();

        layoutParams.width = mWidth - (int) (16 * mScale + 0.5f);
        layoutParams.height = (int) ((9 * layoutParams.width) / 16.0f);
        viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.image.setLayoutParams(layoutParams);
        Log.i("tag", layoutParams.width + "");
        Log.i("tag", layoutParams.height + "");
        Picasso.with(mContext).load(mDataSet.get(i).getString("imgUrl")).into(viewHolder.image);
       // Picasso.with(mContext).load(mDataSet.get(i).getString("imgUrl")).resize()
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

    public void setImageSize(int width, float scale) {
        this.mWidth = width;
        this.mScale = scale;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.id_tv_title);
            image = (ImageView) itemView.findViewById(R.id.id_iv_img);

        }
    }


}
