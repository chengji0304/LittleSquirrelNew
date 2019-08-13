package com.panda.littlesquirrel.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.orhanobut.logger.Logger;
import com.panda.littlesquirrel.R;

/**
 * Created by jinjing on 2019/3/25.
 */

public class SoundPlayUtils {
    public static SoundPool mSoundPool;
    public static SoundPlayUtils soundPlayUtils;
    public static boolean enablePaly=true ;
    static Context mContext;

    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }
        AudioAttributes audioAttributes = null;
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        mContext = context;
        initMusic(mContext);
        return soundPlayUtils;

    }

    /**
     * 初始化音频文件
     */
    private static void initMusic(Context context) {
        //分别加入到SoundPool中
        mSoundPool.load(context, R.raw.digital_1, 1);// 1
        mSoundPool.load(context, R.raw.digital_2, 1);// 2
        mSoundPool.load(context, R.raw.digital_3, 1);// 3
        mSoundPool.load(context, R.raw.digital_4, 1);// 4
        mSoundPool.load(context, R.raw.digital_5, 1);// 5
        mSoundPool.load(context, R.raw.digital_6, 1);// 6
        mSoundPool.load(context, R.raw.digital_7, 1);// 7
        mSoundPool.load(context, R.raw.digital_8, 1);// 8
        mSoundPool.load(context, R.raw.digital_9, 1);// 9
        mSoundPool.load(context, R.raw.digital_0, 1);// 10

        mSoundPool.load(context, R.raw.welcome, 1);// 11
        mSoundPool.load(context, R.raw.box_opening, 1);// 12
        mSoundPool.load(context, R.raw.deliver_bottle, 1);// 13
        mSoundPool.load(context, R.raw.deliver_finish, 1);// 14
        mSoundPool.load(context, R.raw.login, 1);// 15
        mSoundPool.load(context, R.raw.select_type, 1);// 16
        mSoundPool.load(context, R.raw.thanks, 1);// 17
        mSoundPool.load(context, R.raw.tel_error, 1);// 18
        mSoundPool.load(context, R.raw.weight, 1);// 19
        mSoundPool.load(context, R.raw.closebox, 1);// 20

    }

    /**
     * 播放MP3资源
     *
     * @param resId 资源ID
     */
    public static void StartMusic( int resId) {
        /**
         * 第一个参数为播放音频ID
         * 第二个 第三个为音量
         * 第四个为优先级
         * 第五个为是否循环播放
         * 第六个设置播放速度
         * 返回值 不为0即代表成功
         */

        if (enablePaly) {
            int type = mSoundPool.play(resId, 0.50f, 0.50f, 0, 0, 1);
            Logger.e("type--->"+type);
        }

    }
    public static void StartMusic1( int resId) {
        /**
         * 第一个参数为播放音频ID
         * 第二个 第三个为音量
         * 第四个为优先级
         * 第五个为是否循环播放
         * 第六个设置播放速度
         * 返回值 不为0即代表成功
         */

        if (enablePaly) {
            int type = mSoundPool.play(resId, 0.95f, 0.95f, 0, 0, 1);
        }

    }
    /**
     * 释放资源
     */
    public static void release() {
        if (mSoundPool!=null){
            mSoundPool.release();
        }

    }

}
