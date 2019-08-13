package com.panda.littlesquirrel.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;
import com.panda.littlesquirrel.base.BaseActivity;
import com.panda.littlesquirrel.base.BaseDialogFragment;
import com.panda.littlesquirrel.config.Constant;
import com.panda.littlesquirrel.entity.SelcetInfo;
import com.panda.littlesquirrel.utils.CornerTransform;
import com.panda.littlesquirrel.utils.DefaultExceptionHandler;
import com.panda.littlesquirrel.utils.ForbiddenSysKeyBoardUtils;
import com.panda.littlesquirrel.utils.PreferencesUtil;
import com.panda.littlesquirrel.utils.ScreenUtil;
import com.panda.littlesquirrel.utils.SoundPlayUtil;

import com.panda.littlesquirrel.utils.StringUtil;
import com.panda.littlesquirrel.utils.Utils;
import com.panda.littlesquirrel.view.BackAndTimerView;
import com.panda.littlesquirrel.view.DigitalKeyboard;
import com.panda.littlesquirrel.view.ErrorStatusDialog;
import com.panda.littlesquirrel.view.UserDigitalKeyboard;
import com.panda.littlesquirrel.view.UserProtolDialog;
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
import butterknife.OnClick;

public class UserTeleActivity extends BaseActivity {

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
    @Bind(R.id.tv_line_01)
    TextView tvLine01;
    @Bind(R.id.tv_tip_01)
    TextView tvTip01;
    @Bind(R.id.tv_line_02)
    TextView tvLine02;
    @Bind(R.id.tv_tip_02)
    TextView tvTip02;
    @Bind(R.id.ed_tel)
    EditText edTel;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.tv_tip_03)
    TextView tvTip03;
    @Bind(R.id.check_protocol)
    CheckBox checkProtocol;
    @Bind(R.id.tv_tip_04)
    TextView tvTip04;
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;
    @Bind(R.id.tv_scan)
    TextView tvScan;
    @Bind(R.id.tv_switch_scan)
    TextView tvSwitchScan;
    @Bind(R.id.user_digital_keyboard)
    UserDigitalKeyboard userDigitalKeyboard;
    @Bind(R.id.ll_mid)
    FrameLayout llMid;
    @Bind(R.id.banner_buttom)
    Banner bannerButtom;
    @Bind(R.id.ll_buttom)
    FrameLayout llButtom;
    @Bind(R.id.backAndTime)
    BackAndTimerView backAndTime;
    @Bind(R.id.activity_user_tele)
    RelativeLayout activityUserTele;
    private UserTelRightDialog dialog = new UserTelRightDialog();
    private ErrorStatusDialog errorStatusDialog = new ErrorStatusDialog();
    private UserProtolDialog protolDialog = new UserProtolDialog();
    private CountDownTimer timer;
    private PreferencesUtil prf;
    // private SelcetInfo info;
    private ArrayList<Integer> images;
    // 特殊下标位置
    private static final int PHONE_INDEX_3 = 3;
    private static final int PHONE_INDEX_4 = 4;
    private static final int PHONE_INDEX_8 = 8;
    private static final int PHONE_INDEX_9 = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tele);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        //  System.loadLibrary("serial_port");
//        serialPort = serialPortUtils.openSerialPort();
//        setListener();
        prf = new PreferencesUtil(this);
        initData();

    }

    private void initData() {
        sendTimerBoaadCastReceiver(this);
        initBanner();
        tvDeviceNum.setText("设备编号:" + prf.readPrefs(Constant.DEVICEID));
        ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(UserTeleActivity.this, edTel);
        // ForbiddenSysKeyBoardUtils.bannedSysKeyBoard(UserTeleActivity.this,);
        btnMyRecycler.setVisibility(View.GONE);
        edTel.post(new Runnable() {
            @Override
            public void run() {
                edTel.getText().clear();
            }
        });
        edTel.post(new Runnable() {
            @Override
            public void run() {
                SoundPlayUtil.enablePlay = true;
                edTel.requestFocus();
            }
        });
        checkProtocol.post(new Runnable() {
            @Override
            public void run() {
                checkProtocol.setChecked(true);
            }
        });

        edTel.addTextChangedListener(new TextWatcher() {
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

                    edTel.setText(sb.toString());
                    edTel.setSelection(index);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 13) {
                    btnOk.setEnabled(false);
                    btnOk.setTextColor(Color.parseColor("#999999"));
                    return;
                }

                if (checkProtocol.isChecked() == false) {
                    btnOk.setEnabled(false);
                    btnOk.setTextColor(Color.parseColor("#999999"));
                    return;
                }

                btnOk.setEnabled(true);
                btnOk.setTextColor(Color.parseColor("#FFFFFF"));

            }
        });
        userDigitalKeyboard.setEditText(edTel);
        checkProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if ((isChecked) && (edTel.getText().length() == 13)) {
                    btnOk.setEnabled(true);
                    btnOk.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    btnOk.setEnabled(false);
                    btnOk.setTextColor(Color.parseColor("#999999"));
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @OnClick({R.id.tv_switch_scan, R.id.tv_scan, R.id.btn_ok, R.id.tv_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_switch_scan:
                openActivity(UserLoginActivity.class);
                finish();
                break;
            case R.id.tv_scan:
                openActivity(UserLoginActivity.class);
                finish();
                break;
            case R.id.btn_ok:
                if (timer != null) {
                    timer.cancel();
                }
                if (backAndTime != null) {
                    backAndTime.stop();
                }
                Logger.e("mobile--->" + edTel.getText().toString().trim().replaceAll(" ", ""));
                if (!StringUtil.isMobileExact(edTel.getText().toString().trim().replaceAll(" ", ""))) {
                    //SoundPlayUtils.StartMusic(18);
                    SoundPlayUtil.play(12);
                    errorStatusDialog.setContent("您输入的手机号有误请重新输入");
                    errorStatusDialog.setImage(R.drawable.error);
                    errorStatusDialog.setOnConfirmClickListener(new ErrorStatusDialog.ConfirmCallBack() {
                        @Override
                        public void onConfirm() {
                            backAndTime.stop();
                            if (timer != null) {
                                timer.cancel();
                            }
                            edTel.setText("");
                            errorStatusDialog.dismiss();

                        }
                    });
                    errorStatusDialog.show(getFragmentManager(), "error_dialog");
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
                            edTel.setText("");
                            timer.cancel();
                            backAndTime.setTimer(backAndTime.getCurrentTime());
                            backAndTime.start();

                        }
                    }.start();
                } else {
                    dialog.setContent(StringUtil.getPhoneText(edTel));
                    dialog.setOnConfirmClickListener(new UserTelRightDialog.ConfirmCallBack() {
                        @Override
                        public void onConfirm() {
                            backAndTime.stop();
                            dialog.dismiss();
                            if (timer != null) {
                                timer.cancel();
                            }
                            UserLogin();

                        }
                    });
                    dialog.setOnCloseClickListener(new UserTelRightDialog.CloseCallBack() {
                        @Override
                        public void onClose() {
                            if (timer != null) {
                                timer.cancel();
                            }
                            dialog.dismiss();
                            backAndTime.setTimer(backAndTime.getCurrentTime());
                            backAndTime.start();
                        }
                    });
                    dialog.show(getFragmentManager(), "tele_config");
                    //倒计时
                    timer = new CountDownTimer(1000 * 20, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                            if (secondsRemaining > 0) {

                            }
                        }

                        @Override
                        public void onFinish() {
                            if (dialog != null) {
                                dialog.dismiss();
                            }

                            timer.cancel();
                            backAndTime.setTimer(backAndTime.getCurrentTime());
                            backAndTime.start();

                        }
                    }.start();
                }


                break;
            case R.id.tv_protocol:
                if (backAndTime != null) {
                    backAndTime.stop();
                }
                protolDialog.setOnCloseClickListener(new UserProtolDialog.CloseCallBack() {
                    @Override
                    public void onClose() {
                        protolDialog.dismiss();
                        backAndTime.setTimer(backAndTime.getCurrentTime());
                        backAndTime.start();
                    }
                });
//                UserProtolDialog.getInstance().setOnCloseClickListener(new OnCloseDialogListener() {
//                    @Override
//                    public void onCloseDialog() {
//                        backAndTime.setTimer(backAndTime.getCurrentTime());
//                        backAndTime.start();
//                    }
//                });
                protolDialog.show(getFragmentManager(), "user_protocol");
                timer = new CountDownTimer(1000 * 20, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                        if (secondsRemaining > 0) {

                        }
                    }

                    @Override
                    public void onFinish() {
                        if (protolDialog != null) {
                            protolDialog.dismiss();
                        }

                        timer.cancel();
                        backAndTime.setTimer(backAndTime.getCurrentTime());
                        backAndTime.start();

                    }
                }.start();

                break;

        }
    }

    private void UserLogin() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceID", prf.readPrefs(Constant.DEVICEID));
            jsonObject.put("teleNum", edTel.getText().toString().trim().replaceAll(" ",""));
            addSubscription(Constant.HTTP_URL + "machine/verification/userLoginByPhoneNum", jsonObject.toString(), new CallBack<String>() {
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
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(s);
                    String stateCode = jsonObject.getString("stateCode");
                    if (stateCode.equals("1")) {
                        prf.writePrefs(Constant.LOGIN_STATUS, "1");
                        prf.writePrefs(Constant.USER_MOBILE,edTel.getText().toString().trim().replaceAll(" ",""));
                        openActivity(UserTypeSelectActivity.class);
                        finish();

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
        if (backAndTime != null) {
            backAndTime.stop();
        }

        if (timer != null) {
            timer.cancel();
        }
        //userDigitalKeyboard.release();

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
