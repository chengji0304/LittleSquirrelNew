package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.fragment.ADBannerActivity;
import com.panda.littlesquirrel.fragment.UserSelectFragment;

import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.SerialPortUtils;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.UerTimerDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import android_serialport_api.SerialPort;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {
    public static SerialPortUtils serialPortUtils = new SerialPortUtils( "/dev/ttyS4",57600);
    public static SerialPort serialPort;
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
    @Bind(R.id.tv_select_type)
    TextView tvSelectType;
    @Bind(R.id.btn_big_piece)
    Button btnBigPiece;
    @Bind(R.id.btn_guide)
    Button btnGuide;
    @Bind(R.id.tv_line)
    TextView tvLine;
    @Bind(R.id.imBtn_bottle)
    ImageButton imBtnBottle;
    @Bind(R.id.tv_bottle)
    TextView tvBottle;
    @Bind(R.id.tv_bottle_price)
    TextView tvBottlePrice;
    @Bind(R.id.imBtn_paper)
    ImageButton imBtnPaper;
    @Bind(R.id.tv_paper)
    TextView tvPaper;
    @Bind(R.id.tv_paper_price)
    TextView tvPaperPrice;
    @Bind(R.id.imBtn_book)
    ImageButton imBtnBook;
    @Bind(R.id.tv_book)
    TextView tvBook;
    @Bind(R.id.tv_book_price)
    TextView tvBookPrice;
    @Bind(R.id.imBtn_plastic)
    ImageButton imBtnPlastic;
    @Bind(R.id.tv_plastic)
    TextView tvPlastic;
    @Bind(R.id.tv_plastic_price)
    TextView tvPlasticPrice;
    @Bind(R.id.imBtn_fabric)
    ImageButton imBtnFabric;
    @Bind(R.id.tv_fabric)
    TextView tvFabric;
    @Bind(R.id.tv_fabric_price)
    TextView tvFabricPrice;
    @Bind(R.id.imBtn_metal)
    ImageButton imBtnMetal;
    @Bind(R.id.tv_metal)
    TextView tvMetal;
    @Bind(R.id.tv_metal_price)
    TextView tvMetalPrice;
    @Bind(R.id.imBtn_glass)
    ImageButton imBtnGlass;
    @Bind(R.id.tv_glass)
    TextView tvGlass;
    @Bind(R.id.img_public)
    ImageView imgPublic;
    @Bind(R.id.tv_glass_price)
    TextView tvGlassPrice;
    @Bind(R.id.imBtn_harm)
    ImageButton imBtnHarm;
    @Bind(R.id.tv_harm)
    TextView tvHarm;
    @Bind(R.id.img_public_02)
    ImageView imgPublic02;
    @Bind(R.id.tv_harm_price)
    TextView tvHarmPrice;
    @Bind(R.id.tip_01)
    TextView tip01;
    @Bind(R.id.img_public_03)
    ImageView imgPublic03;
    @Bind(R.id.tip_02)
    TextView tip02;
    @Bind(R.id.tip_03)
    TextView tip03;
    @Bind(R.id.tip_04)
    TextView tip04;
    @Bind(R.id.tip_05)
    TextView tip05;
    @Bind(R.id.weixin)
    ImageView weixin;
    @Bind(R.id.full_warning_01)
    TextView fullWarning01;
    @Bind(R.id.full_warning_02)
    TextView fullWarning02;
    @Bind(R.id.full_warning_03)
    TextView fullWarning03;
    @Bind(R.id.full_warning_04)
    TextView fullWarning04;
    @Bind(R.id.full_warning_05)
    TextView fullWarning05;
    @Bind(R.id.full_warning_06)
    TextView fullWarning06;
    @Bind(R.id.full_warning_07)
    TextView fullWarning07;
    @Bind(R.id.full_warning_08)
    TextView fullWarning08;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_home)
    RelativeLayout activityHome;
    private CountTimer countTimerView;
    ArrayList<Integer> images;
    private String loginStatue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        initData();

    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        backAndTime.setVisableStatue(Boolean.valueOf(false));
        countTimerView=new CountTimer(60000,1000,HomeActivity.this);
        initBanner();

    }

    private void initBanner() {
        images=new ArrayList<>();
        images.add(R.drawable.buttombanner2);
        images.add(R.drawable.buttombanner3);
        images.add(R.drawable.bottom_adv_banner);
        //设置banner样式(显示圆形指示器)
        bannerButtom.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置指示器位置（指示器居右）
       // bannerButtom.setIndicatorGravity(BannerConfig.RIGHT);
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


    public void getFindData(String reciveData) {

    }

    class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);

        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        timeStart();
    }
    private void timeStart(){
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                countTimerView.start();
                break;
            default:
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }

    public class CountTimer extends CountDownTimer {
        private Context context;

        /**
         * 参数 millisInFuture       倒计时总时间（如60S，120s等）
         * 参数 countDownInterval    渐变时间（每次倒计1s）
         */
        public CountTimer(long millisInFuture, long countDownInterval,Context context) {
            super(millisInFuture, countDownInterval);
            this.context=context;
        }
        // 计时完毕时触发
        @Override
        public void onFinish() {
            //  UIHelper.showMainActivity((Activity) context);
            Intent intent=new Intent(HomeActivity.this, ADBannerActivity.class);
            startActivity(intent);

        }
        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {
        }
    }


    @OnClick({R.id.btn_my_recycler, R.id.btn_big_piece, R.id.btn_guide, R.id.imBtn_bottle, R.id.imBtn_paper, R.id.imBtn_book, R.id.imBtn_plastic, R.id.imBtn_fabric, R.id.imBtn_metal, R.id.imBtn_glass, R.id.imBtn_harm})
    public void onViewClicked(View view) {

        if(Utils.hourMinuteBetween(new SimpleDateFormat("HH:mm").format(new Date()),"8:30","21:00")){
            switch (view.getId()) {
                case R.id.btn_my_recycler:
                    if(countTimerView!=null){
                        countTimerView.cancel();
                    }
                    openActivity(CollectLoginActivity.class);
                    break;
                case R.id.btn_big_piece:
                    openActivity(BigRecoveryActivity.class);
                    break;
                case R.id.btn_guide:
                    openActivity(QuickGuideActivity.class);
                    break;
                case R.id.imBtn_bottle:
                      openActivity(UserLoginActivity.class);

                    break;
                case R.id.imBtn_paper:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
                case R.id.imBtn_book:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
                case R.id.imBtn_plastic:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
                case R.id.imBtn_fabric:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
                case R.id.imBtn_metal:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
                case R.id.imBtn_glass:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
                case R.id.imBtn_harm:
                    if(StringUtil.isEmpty(loginStatue)){

                    }else {

                    }
                    break;
            }
        }else {
            UerTimerDialog.getInstance().show(getFragmentManager(),"user_time_tip");
        }

    }
}
