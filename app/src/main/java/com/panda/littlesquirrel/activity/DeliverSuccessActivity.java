package com.panda.littlesquirrel.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.GarbageParam;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SoundPlayUtil;

import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliverSuccessActivity extends BaseActivity {

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
    @Bind(R.id.ll_top)
    FrameLayout llTop;
    @Bind(R.id.rel_none)
    LinearLayout relNone;
    @Bind(R.id.ll_money)
    LinearLayout llMoney;
    @Bind(R.id.tv_small_routine)
    TextView tvSmallRoutine;
    @Bind(R.id.img_small_routine)
    ImageView imgSmallRoutine;
    @Bind(R.id.btn_over)
    Button btnOver;
    @Bind(R.id.btn_look)
    Button btnLook;
    @Bind(R.id.ll_none)
    LinearLayout llNone;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_deliver_success)
    RelativeLayout activityDeliverSuccess;
    @Bind(R.id.tv_jifen_value)
    TextView tvJifenValue;
    @Bind(R.id.tv_money_value)
    TextView tvMoneyValue;
    @Bind(R.id.tv_shouyi)
    TextView tvShouyi;
    @Bind(R.id.tv_describe_tip)
    TextView tvDescribeTip;
    private AlertDialog mMyDialog;
    private CountDownTimer timer;
    private String money;
    private String jifen;
    private PreferencesUtil prf;
    private ArrayList<GarbageParam> list;
    private SpannableString spannableString;
    private GarbageParam garbageParam;
    private ArrayList<Integer> images;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this, getApplication(), 360);
        setContentView(R.layout.activity_deliver_success);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf = new PreferencesUtil(this);
        handler = new Handler();
        initData();
    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        money = getIntent().getStringExtra("money");
        jifen = getIntent().getStringExtra("jifen");
        Logger.e("money-->" + money + "jifen-->" + jifen);
        if (StringUtil.isEmpty(money) & StringUtil.isEmpty(jifen)) {
            tvShouyi.setVisibility(View.INVISIBLE);
            tvMoneyValue.setVisibility(View.VISIBLE);
            tvMoneyValue.setText("感谢投递");
            tvDescribeTip.setText("小松鼠与您一起守护美丽家园!");
            tvMoneyValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, 60);

        }
        if (!StringUtil.isEmpty(money) & !StringUtil.isEmpty(jifen)) {
            tvJifenValue.setVisibility(View.VISIBLE);
            tvMoneyValue.setVisibility(View.VISIBLE);
            String strJ = " 环保积分";
            String strM = " 环保金";
            SpannableString jifenStr = new SpannableString(jifen + strJ);
            jifenStr.setSpan(new RelativeSizeSpan(0.8F), 0, jifen.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvJifenValue.setText(jifenStr);
            SpannableString moneyStr = new SpannableString(money + strM);
            // moneyStr.setSpan(new RelativeSizeSpan(0.8F),jifen.length(),str.length()+jifen.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moneyStr.setSpan(new RelativeSizeSpan(0.8F), 0, jifen.length() + money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMoneyValue.setText(moneyStr);
        }

        if (!StringUtil.isEmpty(money)) {
            tvMoneyValue.setVisibility(View.VISIBLE);
            spannableString = new SpannableString(money + " 环保金");
            spannableString.setSpan(new RelativeSizeSpan(0.8F), money.length(), spannableString.length(), 17);
            tvMoneyValue.setText(spannableString);
        }

        if (!StringUtil.isEmpty(jifen)) {
            tvJifenValue.setVisibility(View.VISIBLE);
            spannableString = new SpannableString(jifen + " 环保积分");
            spannableString.setSpan(new RelativeSizeSpan(0.8F), jifen.length(), spannableString.length(), 17);
            tvJifenValue.setText(spannableString);
        }

        btnMyRecycler.setVisibility(View.GONE);
        //clearStatus();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
        SoundPlayUtil.play(16);
        //SoundPlayUtils.StartMusic(17);
    }

    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.setBackVisableStatue(false);
        backAndTime.start();

        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                backAndTime.stop();
                btnOver.setEnabled(false);
                clearStatus();

            }
        });
    }

    @OnClick({R.id.btn_over, R.id.btn_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_over:
                backAndTime.stop();
                btnOver.setEnabled(false);
                clearStatus();


//                prf.deletPrefs(Constant.LOGIN_STATUS);
//                prf.deletPrefs(Constant.USER_MOBILE);
//                prf.deletPrefs(Constant.DELIVER_LIST);
//                openActivity(UserSelectActivity.class);
//                finish();
                break;
            case R.id.btn_look:
                backAndTime.stop();
                final Dialog dialog = new Dialog(this, R.style.NobackDialog);
                dialog.setCancelable(true);
                View viewlook = getLayoutInflater().inflate(R.layout.user_waste_direction, null);
                Button confirm = viewlook.findViewById(R.id.btn_ok_look);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (timer != null) {
                            timer.cancel();
                        }
                        dialog.dismiss();
                        backAndTime.setTimer(backAndTime.getCurrentTime());
                        backAndTime.start();
                    }
                });
                dialog.setContentView(viewlook);
                Window window = dialog.getWindow();
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.height = (int) (d.getHeight() * 1.2f); // 高度设置为屏幕的0.5
                lp.width = (int) (d.getWidth() * 1f); // 宽度设置为屏幕的0.6
                window.setAttributes(lp);
                dialog.show();
                //倒计时
                timer = new CountDownTimer(1000 * 10, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                        if (secondsRemaining > 0) {

                        }
                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                        backAndTime.setTimer(backAndTime.getCurrentTime());
                        backAndTime.start();

                    }
                }.start();

        }
    }

    private void clearStatus() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            jsonObject.put("teleNum", prf.readPrefs(Constant.USER_MOBILE));
            addSubscription(Constant.HTTP_URL + "machine/delivery/clearStatus", jsonObject.toString(), new CallBack<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {
                    backAndTime.setTimer(backAndTime.getCurrentTime());
                    backAndTime.start();
                    prf.deletPrefs(Constant.LOGIN_STATUS);
                    prf.deletPrefs(Constant.USER_MOBILE);
                    prf.deletPrefs(Constant.DELIVER_LIST);
                    openActivity(UserSelectActivity.class);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s--->" + s);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        prf.deletPrefs(Constant.LOGIN_STATUS);
                        prf.deletPrefs(Constant.USER_MOBILE);
                        prf.deletPrefs(Constant.DELIVER_LIST);
                        openActivity(UserSelectActivity.class);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                    } else {
                        prf.deletPrefs(Constant.LOGIN_STATUS);
                        prf.deletPrefs(Constant.USER_MOBILE);
                        prf.deletPrefs(Constant.DELIVER_LIST);
                        openActivity(UserSelectActivity.class);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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
        if (mStartHandler != null) {
            mStartHandler.removeCallbacksAndMessages(null);
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        // serialPortUtils.closeSerialPort();

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
        bannerButtom.setDelayTime(1000 * 30);
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
