package com.panda.littlesquirrel.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;


import com.panda.littlesquirrel.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 播放提示音工具类
 */
public class SoundPlayUtil {

    private static SoundPool mSoundPool;
    private static SoundPlayUtil soundPlayUtils;
    /**
     * 将语音添加到Map中
     */
    private static Map<Integer, Integer> mSoundPoolMap = new HashMap<>();

    /**
     * 上一次播放的语音Map集合中的ID号
     */
    private static int lastMapSoundID = -1;

    /**
     * 上一次播放的语音play返回的ID号
     */
    private static int lastSoundID;

    /**
     * 是否允许语音提示
     */
    public static boolean enablePlay = true;

//    private SoundPlayUtil() {
//    }

    /**
     * 初始化SoundPool对象
     * */
//    static {
//        //Android5.0及之后
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            AudioAttributes audioAttributes = null;
//            audioAttributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build();
//
//            mSoundPool = new SoundPool.Builder()
//                    .setMaxStreams(16)
//                    .setAudioAttributes(audioAttributes)
//                    .build();
//        } else {
//            //Android5.0以前
//            mSoundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
//        }
//
//    }

    /**
     * 加载语音文件
     *
     * @param context
     */
    public static SoundPlayUtil init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtil();
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
        mSoundPoolMap.put(0, mSoundPool.load(context, R.raw.digital_0, 1));
        mSoundPoolMap.put(1, mSoundPool.load(context, R.raw.digital_1, 1));
        mSoundPoolMap.put(2, mSoundPool.load(context, R.raw.digital_2, 1));
        mSoundPoolMap.put(3, mSoundPool.load(context, R.raw.digital_3, 1));
        mSoundPoolMap.put(4, mSoundPool.load(context, R.raw.digital_4, 1));
        mSoundPoolMap.put(5, mSoundPool.load(context, R.raw.digital_5, 1));
        mSoundPoolMap.put(6, mSoundPool.load(context, R.raw.digital_6, 1));
        mSoundPoolMap.put(7, mSoundPool.load(context, R.raw.digital_7, 1));
        mSoundPoolMap.put(8, mSoundPool.load(context, R.raw.digital_8, 1));
        mSoundPoolMap.put(9, mSoundPool.load(context, R.raw.digital_9, 1));
        mSoundPoolMap.put(10, mSoundPool.load(context, R.raw.welcome, 1));
        mSoundPoolMap.put(11, mSoundPool.load(context, R.raw.login, 1));
        mSoundPoolMap.put(12, mSoundPool.load(context, R.raw.tel_error, 1));
        mSoundPoolMap.put(13, mSoundPool.load(context, R.raw.box_opening, 1));
        mSoundPoolMap.put(14, mSoundPool.load(context, R.raw.deliver_finish, 1));
        mSoundPoolMap.put(15, mSoundPool.load(context, R.raw.select_type, 1));
        mSoundPoolMap.put(16, mSoundPool.load(context, R.raw.thanks, 1));
        mSoundPoolMap.put(17, mSoundPool.load(context, R.raw.deliver_bottle, 1));
        mSoundPoolMap.put(18, mSoundPool.load(context, R.raw.weight, 1));
        mSoundPoolMap.put(19, mSoundPool.load(context, R.raw.closebox, 1));
        return soundPlayUtils;
    }

    /**
     * 播放语音(注：播放数字语音时可以连续播放，播放其它语音时一次只能播放一个)
     *
     * @param mapSoundID
     */
    public static void play(int mapSoundID) {
        if (enablePlay) {
//            if (lastMapSoundID != -1 && (mapSoundID > 9 || mapSoundID <= 9 && lastMapSoundID > 9)) {
//                mSoundPool.stop(lastSoundID);
//            }
            lastSoundID = mSoundPool.play(mSoundPoolMap.get(mapSoundID), 1.0f, 1.0f, 0, 0, 1);
            lastMapSoundID = mapSoundID;
        }
//        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                mSoundPool.play(mSoundPoolMap.get(soundID),1,1,0,0,1);
//            }
//        });
    }

}
