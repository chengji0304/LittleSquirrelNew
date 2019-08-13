package com.panda.littlesquirrel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.entity.Garbage;


import java.util.ArrayList;

/**
 * Created by jinjing on 2019/4/3.
 */

public class GarbageTypeReclyViewAdapter extends RecyclerView.Adapter<GarbageTypeReclyViewAdapter.GarbageTypeViewHolder> {
    private Context mcontext;
    private ArrayList<Garbage> mdata;

    public GarbageTypeReclyViewAdapter(Context context, ArrayList<Garbage> data) {
       this.mcontext=context;
        this.mdata=data;
    }

    /**
     * 用来引入布局的方法
     */
    private View getView(int view, ViewGroup parent) {
        View view1 = LayoutInflater.from(mcontext).inflate(view, parent, false);
        return view1;
    }

    @Override
    public GarbageTypeReclyViewAdapter.GarbageTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getView(R.layout.select_type, parent);
        GarbageTypeViewHolder garbageTypeViewHolder = new GarbageTypeViewHolder(view);
        return garbageTypeViewHolder;
    }

    @Override
    public void onBindViewHolder(GarbageTypeReclyViewAdapter.GarbageTypeViewHolder holder, int position) {
        Garbage garbage = mdata.get(position);
        holder.imageView.setImageResource(garbage.getImage());
        holder.tv.setText(garbage.getName());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class GarbageTypeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tv;

        public GarbageTypeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_type);
            tv = itemView.findViewById(R.id.tv_type);
        }
    }


}
