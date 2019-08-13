package com.panda.littlesquirrel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jinjing on 2019/4/2.
 */

public class CollectSelectReclyViewAdapater extends RecyclerView.Adapter<CollectSelectReclyViewAdapater.CollectorSelectViewHolder> {

    private Context mcontext;
    private ArrayList<SelcetInfo> mdata;
    private UserSelectReclyViewAdapater.OnItemClickListener mItemClickListener;

    //第一步：自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }

    //第三步：定义方法并暴露给外面的调用者
    public void setOnItemClickListener(UserSelectReclyViewAdapater.OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public CollectSelectReclyViewAdapater(Context context, ArrayList<SelcetInfo> list) {
        this.mcontext = context;
        this.mdata = list;

    }

    /**
     * 用来引入布局的方法
     */
    private View getView(int view, ViewGroup parent) {
        View view1 = LayoutInflater.from(mcontext).inflate(view, parent, false);
        return view1;
    }


    @Override
    public CollectSelectReclyViewAdapater.CollectorSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getView(R.layout.ry_item_bargae, parent);
        CollectorSelectViewHolder collectorViewHolder = new CollectorSelectViewHolder(view);
        collectorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        return collectorViewHolder;
    }

    @Override
    public void onBindViewHolder(CollectSelectReclyViewAdapater.CollectorSelectViewHolder holder, int position) {
        SelcetInfo info = mdata.get(position);
        holder.itemView.setTag(position);
        Glide.with(mcontext)
                .load(info.getImage())
                .into(holder.iv);
        holder.tv_type.setText(info.getTypeName());
        holder.iv_icon.setVisibility(View.GONE);
        holder.tv_quantity.setVisibility(View.VISIBLE);
        String quantity= String.valueOf(Integer.valueOf(info.getQuantity())*10);
        BigDecimal bigDecimal=new BigDecimal(quantity);
        BigDecimal bc=new BigDecimal("1000");
        holder.tv_quantity.setText("当前"+bigDecimal.divide(bc,2,BigDecimal.ROUND_HALF_UP)+"公斤");
        holder.tv_perprice.setText(info.getPerprice()+"元/公斤");
        if(info.getTypeName().equals("饮料瓶")){
            holder.tv_perprice.setText(info.getPerprice()+"元/个");
            holder.tv_quantity.setText("当前"+info.getQuantity()+"个");
        }
        if (info.getTypeName().equals("玻璃") ) {
            holder.iv_icon.setVisibility(View.VISIBLE);
           // holder.tv_perprice.setText("1积分/次");
            holder.tv_perprice.setVisibility(View.INVISIBLE);
           // holder.tv_quantity.setVisibility(View.INVISIBLE);
        }
        if (info.getTypeName().equals("有害垃圾") ) {
            holder.iv_icon.setVisibility(View.VISIBLE);
            holder.tv_perprice.setVisibility(View.INVISIBLE);

        }
        if(Double.valueOf(bigDecimal.divide(bc,2,BigDecimal.ROUND_HALF_UP).toString())>=40.00){
            holder.tv_full.setVisibility(View.VISIBLE);
            holder.tv_full.setText("满箱");
            holder.tv_full.setBackgroundResource(R.drawable.full_warning);
        }
        if(info.getTypeName().equals("玻璃")){
            if(Integer.valueOf(info.getQuantity())>250) {
                holder.tv_full.setVisibility(View.VISIBLE);
                holder.tv_full.setText("满箱");
                holder.tv_full.setBackgroundResource(R.drawable.full_warning);
            }
        }else{
            if(!StringUtil.isEmpty(info.getFullstatus())){
                if(Integer.valueOf(info.getFullstatus())>=80&Integer.valueOf(info.getFullstatus())<100){
                    holder.tv_full.setVisibility(View.VISIBLE);
                    holder.tv_full.setText("80%");
                    holder.tv_full.setBackgroundResource(R.drawable.full_warning_80);
                }else if(Integer.valueOf(info.getFullstatus())>=100){
                    holder.tv_full.setVisibility(View.VISIBLE);
                    holder.tv_full.setText("满箱");
                    holder.tv_full.setBackgroundResource(R.drawable.full_warning);
                }
            }
        }



//        switch (info.getFullstatus()) {
//            case "满箱":
//                holder.tv_full.setVisibility(View.VISIBLE);
//                holder.tv_full.setText("满箱");
//                holder.tv_full.setBackgroundResource(R.drawable.full_warning);
//                break;
//            case "80%":
//                holder.tv_full.setVisibility(View.VISIBLE);
//                holder.tv_full.setText("80%");
//                holder.tv_full.setBackgroundResource(R.drawable.full_warning_80);
//                break;
//        }


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public class CollectorSelectViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv_perprice;
        private TextView tv_type;
        private TextView tv_full;
        private ImageView iv_icon;
        private TextView tv_quantity;

        public CollectorSelectViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_iv);
            tv_full = itemView.findViewById(R.id.full_warning);
            tv_type = itemView.findViewById(R.id.item_tv_type);
            tv_perprice = itemView.findViewById(R.id.tv_item_perprice);
            iv_icon = itemView.findViewById(R.id.item_iv_fen);
            tv_quantity=itemView.findViewById(R.id.tv_item_data);

        }
    }
}
