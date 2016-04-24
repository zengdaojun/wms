package com.zdjer.utils.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * Created by zdj on 16/4/19.
 */
public class AudioHelper {

    // 打开扬声器
    public static void openSpeaker(Context context, int resid) {
        try {
            // 判断扬声器是否在打开
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.RINGER_MODE_NORMAL);
            if(!audioManager.isSpeakerphoneOn()){
                audioManager.setSpeakerphoneOn(true);
                audioManager.setStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            }
            MediaPlayer mediaPlayer = MediaPlayer.create(context, resid);
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始震动
     *
     * @param context
     */
    public static void startVibrator(Context context) {
        int vibrateTime = 1000;
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateTime);
    }
}
