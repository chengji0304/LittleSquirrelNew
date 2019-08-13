package com.panda.littlesquirrel.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by jinjing on 2019/4/10.
 */

public class DeliverReclyViewAdapter extends RecyclerView.Adapter<DeliverReclyViewAdapter.DeliverViewHolder> {
    private Context mcontext;
    private ArrayList<GarbageParam> mdata;


    public DeliverReclyViewAdapter(Context context, ArrayList<GarbageParam> data) {
        this.mcontext = context;
        this.mdata = data;

    }

    @Override
    public DeliverReclyViewAdapter.DeliverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getView(R.layout.deliver_list_item, parent);
        DeliverViewHolder deliverViewHolder = new DeliverViewHolder(view);

        return deliverViewHolder;
    }

    /**
     * 用来引入布局的方法
     */
    private View getView(int view, ViewGroup parent) {
        View view1 = LayoutInflater.from(mcontext).inflate(view, parent, false);
        return view1;
    }

    @Override
    public void onBindViewHolder(DeliverReclyViewAdapter.DeliverViewHolder holder, int position) {
           GarbageParam garbageParam=mdata.get(position);

          switch (garbageParam.getCategory()){
              case "1":
                  holder.imageView.setImageResource(R.drawable.bottle);
                  holder.typeName.setText("饮料瓶");

                  holder.quantity.setText(garbageParam.getQuantity()+" "+"个");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"6"));
                  break;
              case "2":

                  holder.typeName.setText("纸类");
                  holder.imageView.setImageResource(R.drawable.paper);
                  Logger.e("weight--->"+StringUtil.getTotalWeight(garbageParam.getQuantity()));
                  Logger.e("momey--->"+garbageParam.getMoney());
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));
                  break;
              case "纸类1箱":
                  holder.typeName.setText("纸类");
                  holder.imageView.setImageResource(R.drawable.paper);
                  Logger.e("weight--->"+StringUtil.getTotalWeight(garbageParam.getQuantity()));
                  Logger.e("momey--->"+garbageParam.getMoney());
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));
                  break;
              case "纸类2箱":
                  holder.typeName.setText("纸类");
                  holder.imageView.setImageResource(R.drawable.paper);
                  Logger.e("weight--->"+StringUtil.getTotalWeight(garbageParam.getQuantity()));
                  Logger.e("momey--->"+garbageParam.getMoney());
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));
                  break;
              case "3":
                  holder.typeName.setText("书籍");
                  holder.imageView.setImageResource(R.drawable.book);
                  Logger.e("weight--->"+StringUtil.getTotalWeight(garbageParam.getQuantity()));
                  Logger.e("momey--->"+garbageParam.getMoney());
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));

                  break;
              case "4":
                  holder.typeName.setText("塑料");
                  holder.imageView.setImageResource(R.drawable.plastic);
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));

                  break;
              case "5":
                  holder.typeName.setText("纺织物");
                  holder.imageView.setImageResource(R.drawable.fabric);
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));

                  break;
              case "纺织物1箱":
                  holder.typeName.setText("纺织物");
                  holder.imageView.setImageResource(R.drawable.fabric);
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));

                  break;
              case "纺织物2箱":
                  holder.typeName.setText("纺织物");
                  holder.imageView.setImageResource(R.drawable.fabric);
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));

                  break;
              case "6":
                  holder.typeName.setText("金属");
                  holder.imageView.setImageResource(R.drawable.metal);
                  holder.quantity.setText(StringUtil.getTotalWeight(garbageParam.getQuantity())+" "+"公斤");
                  holder.value.setText(StringUtil.getTotalPrice(garbageParam.getMoney(),garbageParam.getQuantity(),"1"));

                  break;
              case "7":
                  holder.typeName.setText("玻璃");
                  holder.imageView.setImageResource(R.drawable.glass);
                  holder.quantity.setText(garbageParam.getMoney()+" "+"次");
                  holder.value.setText(garbageParam.getMoney());

                  holder.jifen.setVisibility(View.VISIBLE);
                  break;
              case "8":
                  holder.typeName.setText("有害垃圾");
                  holder.imageView.setImageResource(R.drawable.harm);
                  holder.quantity.setText(garbageParam.getMoney()+" "+"次");
                  holder.value.setText(garbageParam.getMoney());

                  holder.jifen.setVisibility(View.VISIBLE);
                  break;



          }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class DeliverViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView typeName;
        private TextView quantity;
        private TextView value;
        private ImageView jifen;
        public DeliverViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_garbage_icon);
            typeName=itemView.findViewById(R.id.tv_garbage__name);
            quantity=itemView.findViewById(R.id.tv_garbage_quantity);
            value=itemView.findViewById(R.id.tv_garbage_value);
            jifen=itemView.findViewById(R.id.iv_garbage_jifen);


        }
    }
}
