package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.ForbiddenSysKeyBoardUtils;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenAdaptUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;

import com.panda.littlesquirrel.utils.SoundPlayUtil;
import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.DigitalKeyboard;
import com.panda.littlesquirrel.view.ErrorStatusDialog;
import com.panda.littlesquirrel.view.UserTelRightDialog;
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

public class CollectTeleActivity extends BaseActivity {


    ArrayList<Integer> images;
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
    @Bind(R.id.tv_line_05)
    TextView tvLine05;
    @Bind(R.id.tv_tip_03)
    TextView tvTip03;
    @Bind(R.id.tv_line_06)
    TextView tvLine06;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.ed_account)
    EditText edAccount;
    @Bind(R.id.tv_password)
    TextView tvPassword;
    @Bind(R.id.ed_password)
    EditText edPassword;
    @Bind(R.id.recycler_digital_keyboard)
    DigitalKeyboard recyclerDigitalKeyboard;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;

    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_collect_login)
    RelativeLayout activityCollectLogin;
    private CountDownTimer timer;
    private UserTelRightDialog dialog = new UserTelRightDialog();
    private PreferencesUtil prf;
    private ErrorStatusDialog errorStatusDialog = new ErrorStatusDialog();
    // 特殊下标位置
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtil.setCustomDesity(this,getApplication(),360);
        setContentView(R.layout.activity_collect_tele);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        prf = new PreferencesUtil(this);
        ButterKnife.bind(this);
        initKeyBoard();
        initData();
        //initTimer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
    }

    private void initKeyBoard() {





    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        btnMyRecycler.setVisibility(View.GONE);
        //initBanner();
        ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(CollectTeleActivity.this,edAccount);
        ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(CollectTeleActivity.this,edPassword);
        edAccount.post(new Runnable() {
            @Override
            public void run() {
                edAccount.getText().clear();
              //  SoundPlayUtil.enablePlay = true;
                edAccount.requestFocus();

            }
        });
        edPassword.post(new Runnable() {
            @Override
            public void run() {
               // SoundPlayUtil.enablePlay = false;
                edPassword.getText().clear();
            }
        });

        edAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s == null || s.length() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != PHONE_INDEX_3 && i != PHONE_INDEX_8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == PHONE_INDEX_4 || sb.length() == PHONE_INDEX_9) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }

                    edAccount.setText(sb.toString());
                    edAccount.setSelection(index);
                }



            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
        edAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerDigitalKeyboard.setEditText(edAccount);

                }//   Logger.e("edAcount1--->"+edAccount.getText().length());

            }

        });
        edPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerDigitalKeyboard.setEditText(edPassword);
                    //SoundPlayUtils.enablePaly = false;
                   // SoundPlayUtil.enablePlay=false;
                }

            }
        });

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = recyclerDigitalKeyboard.getEditText();

                // Logger.e("s--->"+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerDigitalKeyboard.setOnConfirmListener(new DigitalKeyboard.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (timer != null) {
                    timer.cancel();
                }
                if (backAndTime != null) {
                    backAndTime.stop();
                }

                if(isNull()){
                    collectLogin();

                }

            }
        });
    }

    private void collectLogin() {
        backAndTime.setBackEnable(false);
        backAndTime.stop();
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("deviceID",prf.readPrefs(Constant.DEVICEID));
            jsonObject.put("account",edAccount.getText().toString().trim().replaceAll(" ", ""));
            jsonObject.put("password",edPassword.getText().toString().trim());
            addSubscription(Constant.HTTP_URL + "machine/verification/recyclerLoginByAccount", jsonObject.toString(), new CallBack<String>() {
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

                }

                @Override
                public void onSuccess(String s) {
                    Logger.e("s--->"+s);
                    backAndTime.setBackEnable(true);
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if(stateCode.equals("1")){
                        prf.writePrefs(Constant.COLLECTOR_MOBILE,edAccount.getText().toString().trim().replaceAll(" ",""));
                        openActivity(RecylerSelectActivity.class);

                        finish();
                    }else {
                        backAndTime.setBackEnable(true);
                        backAndTime.setTimer(backAndTime.getCurrentTime());
                        backAndTime.start();
                        showError("您输入的账号和密码有误请重新输入","3");

                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isNull() {
        if(StringUtil.isEmpty(edAccount.getText().toString().trim())){
            showError("手机号不能为空","");
            return false;
        }
        if(!StringUtil.isMobileExact(edAccount.getText().toString().trim().replaceAll(" ", ""))){
            showError("您输入的手机号有误请重新输入","1");
            return false;
        }

        if(StringUtil.isEmpty(edPassword.getText().toString().trim())){
            showError("密码不能为空","");
            return false;
        }
        if(edPassword.getText().toString().length()<6){
            showError("您输入的密码有误请重新输入","2");
            return false;
        }
        if(edPassword.getText().toString().length()<6||!StringUtil.isMobileExact(edAccount.getText().toString().trim().replaceAll(" ", ""))){
            showError("您输入的手机号密码有误请重新输入","3");
            return false;
        }

        return true;
    }
    public void  showError(String s, final String str){
        errorStatusDialog.setImage(R.drawable.error);
        errorStatusDialog.setContent(s);
        errorStatusDialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
            @Override
            public void onConfirm() {
                if(str.equals("1")){
                    edAccount.setText("");
                }
                if(str.equals("2")){
                    edPassword.setText("");
                }
                if(str.equals("3")){
                    edAccount.setText("");
                    edPassword.setText("");
                }
                errorStatusDialog.dismiss();
            }
        });
        errorStatusDialog.show(getFragmentManager(),"error_dialog");
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

                if (errorStatusDialog != null) {
                    errorStatusDialog.dismiss();
                }
                if(str.equals("1")){
                    edAccount.setText("");
                }
                if(str.equals("2")){
                    edPassword.setText("");
                }
                if(str.equals("3")){
                    edAccount.setText("");
                    edPassword.setText("");
                }
                timer.cancel();
                backAndTime.setTimer(backAndTime.getCurrentTime());
                backAndTime.start();

            }
        }.start();
    }


    private void initTimer() {
        backAndTime.setTimer(280);
        backAndTime.setBackVisableStatue(true);
        backAndTime.setVisableStatue(Boolean.valueOf(true));
        backAndTime.start();
        backAndTime.setOnBackListener(new BackAndTimerView.OnBackListener() {
            @Override
            public void onBack() {
                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        backAndTime.setOnTimerFinishListener(new BackAndTimerView.OnTimerFinishListener() {
            @Override
            public void onTimerFinish() {
                openActivity(UserSelectActivity.class);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backAndTime.stop();
      //  recyclerDigitalKeyboard.release();
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
        bannerButtom.setDelayTime(1000*30);
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
