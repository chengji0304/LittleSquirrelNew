package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.entity.RecordBean;
import com.panda.littlesquirrel.entity.SaveDeliveryRecord;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.ErrorStatusDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectorPayActivity extends BaseActivity {


    @Bind(R.id.image_service_tel)
    ImageView imageServiceTel;
    @Bind(R.id.image_device_num)
    ImageView imageDeviceNum;
    @Bind(R.id.tv_service_tel)
    TextView tvServiceTel;
    @Bind(R.id.tv_device_num)
    TextView tvDeviceNum;
    @Bind(R.id.btn_my_recycler)
    Button btnMyRecycler;

    @Bind(R.id.tv_operate_tip)
    TextView tvOperateTip;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.tv_select)
    TextView tvSelect;
    @Bind(R.id.tv_pay)
    TextView tvPay;
    @Bind(R.id.tv_open)
    TextView tvOpen;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.tv_num_01)
    TextView tvNum01;
    @Bind(R.id.tv_line_02)
    TextView tvLine02;
    @Bind(R.id.tv_line_07)
    TextView tvLine07;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_line_08)
    TextView tvLine08;
    @Bind(R.id.tv_full_state)
    TextView tvFullState;
    @Bind(R.id.tv_full_value)
    TextView tvFullValue;
    @Bind(R.id.tv_garbage)
    TextView tvGarbage;
    @Bind(R.id.tv_garbage_value)
    TextView tvGarbageValue;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_price_value)
    TextView tvPriceValue;
    @Bind(R.id.tv_tip_05)
    TextView tvTip05;
    @Bind(R.id.tv_tip_06)
    TextView tvTip06;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;

    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_collector_pay)
    RelativeLayout activityCollectorPay;
    @Bind(R.id.tv_harmandglass)
    TextView tvHarmandglass;
    private GarbageParam garbageParam;
    private PreferencesUtil prf;
    private ArrayList<GarbageParam> prfList;
    private String balance;
  //  private String quantity;
    private String category;
    private ErrorStatusDialog dialog=new ErrorStatusDialog();
    private ArrayList<Integer> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_collector_pay);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf = new PreferencesUtil(this);
        prfList = prf.getDataList(Constant.DELIVER_LIST);
        initData();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        getBlance();
        garbageParam = getIntent().getBundleExtra("message").getParcelable("garbage");
        switch (garbageParam.getCategory()) {
            case "饮料瓶":
                category = "6";
                break;
            case "纸类1箱":
            case "纸类2箱":
                category = "3";
                break;
            case "书籍":
                category = "4";
                break;
            case "塑料":
                category = "1";
                break;
            case "纺织物1箱":
            case "纺织物2箱":
                category = "0";
                break;
            case "金属":
                category = "2";
                break;
            case "玻璃":
                category = "5";
                break;
            case "有害垃圾":
                category = "5";
                break;
        }
        tvCategory.setText(garbageParam.getCategory() + "回收物");
        if (!StringUtil.isEmpty(garbageParam.getFullState())) {
            if (Integer.valueOf(garbageParam.getFullState()) >= 80 & Integer.valueOf(garbageParam.getFullState()) < 100) {
                tvFullValue.setTextColor(Color.parseColor("#ff333333"));
                tvFullValue.setText("80%");

            } else if (Integer.valueOf(garbageParam.getFullState()) >= 100) {
                tvFullValue.setTextColor(Color.parseColor("#f6546c"));
                tvFullValue.setText("满箱");
            } else {
                tvFullValue.setTextColor(Color.parseColor("#ff333333"));
                tvFullValue.setText("未满箱");
            }
        }
        if (garbageParam.getCategory().equals("饮料瓶")) {
            tvGarbageValue.setText(garbageParam.getQuantity() + "个");
        } else {
            tvGarbageValue.setText(StringUtil.getTotalWeight(garbageParam.getQuantity()) + "公斤");
        }


        if (garbageParam.getCategory().equals("玻璃") | garbageParam.getCategory().equals("有害垃圾")) {
            tvHarmandglass.setVisibility(View.VISIBLE);
            tvPriceValue.setText("0.00元");
        } else {
            tvHarmandglass.setVisibility(View.GONE);
            tvPriceValue.setText(StringUtil.getTotalPrice(garbageParam.getMoney(), garbageParam.getQuantity(),category) + "元");
        }
        btnMyRecycler.setVisibility(View.GONE);

    }

    private void getBlance() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("account", prf.readPrefs(Constant.COLLECTOR_MOBILE));
            addSubscription(Constant.HTTP_URL + "machine/recycling/getRecyclingBalance", jsonObject.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {

                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s--->" + s);
                    //{"stateCode":1,"errorMessage":"处理成功","result":{"balance":500.00}}
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSON.parseObject(jsonObject.getString("result"));
                        balance = obj.getString("balance");
                        tvTip06.setText(balance + "元");

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setBackVisableStatue(true);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                openActivity(RecylerSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                openActivity(RecylerSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        upOrder();

    }

    private void upOrder() {
        backAndTime.setBackEnable(false);
        backAndTime.stop();
        btnOk.setText("正在支付");
        btnOk.setEnabled(false);
        try {
            String serialNum = StringUtil.getSerialNumber();
            SaveDeliveryRecord record=new SaveDeliveryRecord();
            record.setDeviceID(prf.readPrefs(Constant.DEVICEID));
            record.setSerialNum(serialNum);
            record.setAccount(prf.readPrefs(Constant.COLLECTOR_MOBILE));
            RecordBean bean=new RecordBean();
            switch (garbageParam.getCategory()) {
                case "饮料瓶":
                    category = "1";
                    break;
                case "纸类1箱":
                case "纸类2箱":
                    category = "2";
                    break;
                case "书籍":
                    category = "3";
                    break;
                case "塑料":
                    category = "4";
                    break;
                case "纺织物1箱":
                case "纺织物2箱":
                    category = "5";
                    break;
                case "金属":
                    category = "6";
                    break;
                case "玻璃":
                    category = "7";
                    break;
                case "有害垃圾":
                    category = "8";
                    break;
            }
            bean.setCategory(Integer.valueOf(category));
            if(category.equals("1")){
                bean.setCount(Integer.valueOf(garbageParam.getQuantity()));
                bean.setWeight(0);
            }else {
                bean.setCount(0);
                bean.setWeight(Double.valueOf(StringUtil.getTotalWeight(garbageParam.getQuantity())));
            }
            record.getRecord().add(bean);
            final String jsonString= com.alibaba.fastjson.JSON.toJSONString(record);
            Logger.e("s--->"+jsonString);
            addSubscription(Constant.HTTP_URL + "machine/recycling/saveRecord", jsonString, new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {
                    backAndTime.setBackEnable(true);
                    backAndTime.setTimer(backAndTime.getCurrentTime());
                    backAndTime.start();
                    btnOk.setText("确认支付");
                    btnOk.setEnabled(true);
                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s--->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("garbage", garbageParam);
                        openActivity(CollectorTakeGoodsActivity.class, bundle);
                        finish();
                    } else {
                        if(dialog!=null){
                            dialog.dismiss();
                        }
                        dialog.setContent(jsonObject.getString("errorMessage"));
                        dialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
                            @Override
                            public void onConfirm() {
                                dialog.dismiss();
                                backAndTime.setBackEnable(true);
                                backAndTime.setTimer(backAndTime.getCurrentTime());
                                backAndTime.start();
                                btnOk.setText("确认支付");
                                btnOk.setEnabled(true);
                            }
                        });
                        dialog.show(getFragmentManager(),"error_dialog");

                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
    }

    @Override
    public void getFindData(String reciveData) {

    }
    private void initBanner() {
        images = new ArrayList<>();
        images.add(R.drawable.banner111);
        images.add(R.drawable.banner222);
        images.add(R.drawable.banner333);
        //设置banner样式(显示圆形指示器)
        bannerButtom.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（指示器居右）
        bannerButtom.setIndicatorGravity(BannerConfig.RIGHT);
        //设置图片加载器
        bannerButtom.setImageLoader(new GlideImageLoader());
        //设置图片集合
        bannerButtom.setImages(images);
        bannerButtom.isAutoPlay(true);
        //设置轮播时间
        bannerButtom.setDelayTime(4000);
        //banner设置方法全部调用完毕时最后调用
        bannerButtom.setOnBannerListener(new OnBannerListener() {

            @Override
            public void OnBannerClick(int position) {
                Logger.e("position" + position);

            }
        });
        bannerButtom.start();
    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(final Context context, Object path, final ImageView imageView) {
            CornerTransform transformation = new CornerTransform(context, ScreenUtil.dip2px(context, 30));
            //只是绘制左上角和右上角圆角
            transformation.setExceptCorner(false, false, false, false);
            Glide.with(context)
                    .load(path)
                    .asBitmap()
                    .skipMemoryCache(true)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                    .transform(transformation)
                    .into(imageView);

        }

    }

}
