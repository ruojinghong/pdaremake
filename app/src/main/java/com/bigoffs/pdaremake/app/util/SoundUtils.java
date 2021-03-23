package com.bigoffs.pdaremake.app.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;


import com.bigoffs.pdaremake.R;

import java.util.HashMap;
import java.util.Map;


public class SoundUtils {
    public static SoundPool sp;
    public static Map<Integer, Integer> suondMap;
    public static Context context;
    public static int Volume = 0;
    private static AudioManager mAudioManager;
    private static int max = 0;
    //初始化声音池
    public static void initSoundPool(Context context) {
        SoundUtils.context = context;
        if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入最多播放音频数量,
            builder.setMaxStreams(10);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            //加载一个AudioAttributes
            builder.setAudioAttributes(attrBuilder.build());
            sp = builder.build();
        } else {
            /**
             * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
             * 第二个参数：int streamType：AudioManager中描述的音频流类型
             *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
             */
            sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        suondMap = new HashMap<Integer, Integer>();
        suondMap.put(1, sp.load(context, R.raw.msg, 1));
        suondMap.put(2, sp.load(context, R.raw.scan_error, 1));
        sp.load("/etc/Scan_new.ogg", 1);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

            }
        });
        Volume = (int) SPUtils.get(context, "volume", 0);
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
    }

    //播放声音池声音
    public static void play(int sound) {
        sp.play(
                suondMap.get(sound), //播放的音乐Id
                1, //左声道音量
                1, //右声道音量
                1, //优先级，0为最低
                0, //循环次数，0无不循环，-1无永远循环
                1);//回放速度，值在0.5-2.0之间，1为正常速度

    }

    public static void playErrorSound() {
        sp.play(

                suondMap.get(2), //播放的音乐Id
                15, //左声道音量
                15, //右声道音量
                1, //优先级，0为最低
                0, //循环次数，0无不循环，-1无永远循环
                1);//回放速度，值在0.5-2.0之间，1为正常速度

    }

    public static void playByVolume(int sound, float volume) {
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
        sp.play(
                2,
                volume, //左声道音量
                volume, //右声道音量
                1, //优先级，0为最低
                0, //循环次数，0无不循环，-1无永远循环
                1);//回放速度，值在0.5-2.0之间，1为正常速度

    }

    public static void setVolume(int volume) {
        Volume = volume;
        SPUtils.put(context, "volume", volume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        SoundUtils.play(1);
    }
}