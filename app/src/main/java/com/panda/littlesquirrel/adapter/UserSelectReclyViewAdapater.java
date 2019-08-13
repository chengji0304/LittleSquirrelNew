package com.panda.littlesquirrel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.PriceInfo;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by jinjing on 2019/4/1.
 */

public class UserSelectReclyViewAdapater extends RecyclerView.Adapter<UserSelectReclyViewAdapater.UserSelectViewHolder> {
    private Context mcontext;
    private ArrayList<SelcetInfo> mdata;
    private OnItemClickListener mItemClickListener;
    private PreferencesUtil preferencesUtil;
    private ArrayList<PriceInfo> list;
    //第一步：自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void onItemClick(View v, int position);

    }

    //第三步：定义方法并暴露给外面的调用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public UserSelectReclyViewAdapater(Context context, ArrayList<SelcetInfo> list) {
        preferencesUtil=new PreferencesUtil(context) ;
        this.mcontext = context;
        this.mdata = list;

    }

    @Override
    public UserSelectReclyViewAdapater.UserSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getView(R.layout.ry_item_bargae, parent);
        UserSelectViewHolder selectViewHolder = new UserSelectViewHolder(view);
        selectViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        return selectViewHolder;
    }


    @Override
    public void onBindViewHolder(UserSelectReclyViewAdapater.UserSelectViewHolder holder, int position) {
        SelcetInfo info = mdata.get(position);
        list=preferencesUtil.getPriceList(Constant.PRICE_LIST);

        holder.itemView.setTag(position);
        Glide.with(mcontext)
                .load(info.getImage())
                .into(holder.iv);
        holder.tv_type.setText(info.getTypeName());
        holder.iv_icon.setVisibility(View.GONE);
        holder.tv_perprice.setText(info.getPerprice()+"元/公斤");
//        if(info.getTyep().equals("书籍")){
//          holder.right.setVisibility(View.VISIBLE);
//        }

        if(info.getTypeName().equals("饮料瓶")){
          //  Logger.e("price-->"+list.get(0).getUserPrice());
            holder.tv_perprice.setText(info.getPerprice()+"元/个");
          //  holder.left.setVisibility(View.VISIBLE);
        }
        if (info.getTypeName().equals("玻璃") ) {
            holder.iv_icon.setVisibility(View.VISIBLE);
            holder.tv_perprice.setText("1积分/次");
        }
        if (info.getTypeName().equals("有害垃圾") ) {
            holder.iv_icon.setVisibility(View.VISIBLE);
            holder.tv_perprice.setText("1积分/次");
        }
        if(info.getTypeName().equals("饮料瓶")){
            if(Integer.valueOf(info.getQuantity())>250) {
                holder.tv_full.setVisibility(View.VISIBLE);
                holder.tv_full.setText("满箱");
                holder.tv_full.setBackgroundResource(R.drawable.full_warning);
            }
        }else{
            if(info.getQuantity().equals("")){

            }else {
                String quantity= String.valueOf(Integer.valueOf(info.getQuantity())*10);
                BigDecimal bigDecimal=new BigDecimal(quantity);
                BigDecimal bc=new BigDecimal("1000");
                if(Double.valueOf(bigDecimal.divide(bc,2,BigDecimal.ROUND_HALF_UP).toString())>=40.00) {
                    holder.tv_full.setVisibility(View.VISIBLE);
                    holder.tv_full.setText("满箱");
                    holder.tv_full.setBackgroundResource(R.drawable.full_warning);
                }
                if(!StringUtil.isEmpty(info.getFullstatus())){
//            if(Integer.valueOf(info.getFullstatus())>=80&Integer.valueOf(info.getFullstatus())<100){
//                holder.tv_full.setVisibility(View.VISIBLE);
//                holder.tv_full.setText("80%");
//                holder.tv_full.setBackgroundResource(R.drawable.full_warning_80);
//            }else
                    if(Integer.valueOf(info.getFullstatus())>=100){
                        holder.tv_full.setVisibility(View.VISIBLE);
                        holder.tv_full.setText("满箱");
                        holder.tv_full.setBackgroundResource(R.drawable.full_warning);
                    }
                }
            }

        }


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    /**
     * 用来引入布局的方法
     */
    private View getView(int view, ViewGroup parent) {
        View view1 = LayoutInflater.from(mcontext).inflate(view, parent, false);
        return view1;
    }

    public class UserSelectViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv_perprice;
        private TextView tv_type;
        private TextView tv_full;
        private ImageView iv_icon;
        private View left;
        private View right;

        public UserSelectViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_iv);
            tv_full = itemView.findViewById(R.id.full_warning);
            tv_type = itemView.findViewById(R.id.item_tv_type);
            tv_perprice = itemView.findViewById(R.id.tv_item_perprice);
            iv_icon = itemView.findViewById(R.id.item_iv_fen);
            left=itemView.findViewById(R.id.view_left);
            right=itemView.findViewById(R.id.view_right);

        }
    }
}
