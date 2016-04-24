package com.zdjer.wms.view.helper;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zdjer.wms.utils.R;

public class ActivityHelper {

	/**
	 * 聚焦控件
	 * 
	 * @param view
	 *            控件对象
	 */
	public static void Focus(View view) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
		view.requestFocusFromTouch();
	}

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
	
	/**
	 * 创建访问对话框
	 * @param context 上下文
	 * @param tip 提示
	 * @return
	 */
	public static Dialog createRequestDialog(final Context context, String tip){
		
		final Dialog dialog = new Dialog(context, R.style.dialog);	
		//dialog.setContentView(R.wms_activity_login.dialog_layout);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();	
		int width = getScreenWidth(context);	
		lp.width = (int)(0.6 * width);	
		
		TextView titleTxtv = null;//(TextView) dialog.findViewById(R.id.tvLoad);
		if (tip == null || tip.length() == 0)
		{
			titleTxtv.setText(R.string.common_wait);	
		}else{
			titleTxtv.setText(tip);	
		}		
		return dialog;
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
	public static float getScreenDensity(Context context) {
    	try {
    		DisplayMetrics dm = new DisplayMetrics();
	    	WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	    	manager.getDefaultDisplay().getMetrics(dm);
	    	return dm.density;
    	} catch(Exception ex) {
    	
    	}
    	return 1.0f;
    }
}
