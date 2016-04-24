package com.zdjer.utils.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.R;

/**
 * Created by zdj on 16/4/12.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ToastHelper {

    private static String lastToastMessage = "";
    private static long lastToastTime;

    private static Context context;

    /**
     * 设置上下文
     *
     * @param context
     */
    public static void setContext(Context context) {
        ToastHelper.context = context;
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     */
    public static void showShortToast(int messageResId) {
        showToast(messageResId, Toast.LENGTH_SHORT, 0);
    }

    /**
     * 显示Toast
     *
     * @param message 消息
     */
    public static void showShortToast(String message) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     * @param args         参数
     */
    public static void showShortToast(int messageResId, Object... args) {
        showToast(messageResId, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM, args);
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     */
    public static void showToast(int messageResId) {
        showToast(messageResId, Toast.LENGTH_LONG, 0);
    }

    /**
     * 显示Toast
     *
     * @param message 消息
     */
    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     * @param iconResId    图标资源Id
     */
    public static void showToast(int messageResId, int iconResId) {
        showToast(messageResId, Toast.LENGTH_LONG, iconResId);
    }

    /**
     * 显示Toast
     *
     * @param message   消息
     * @param iconResId 图标资源Id
     */
    public static void showToast(String message, int iconResId) {
        showToast(message, Toast.LENGTH_LONG, iconResId, Gravity.BOTTOM);
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     * @param duration     持续时间
     * @param iconResId    图标资源Id
     */
    public static void showToast(int messageResId, int duration, int iconResId) {
        showToast(messageResId, duration, iconResId, Gravity.BOTTOM);
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     * @param duration     持续时间
     * @param iconResId    图标资源Id
     * @param gravity      比重
     */
    public static void showToast(int messageResId, int duration, int iconResId, int gravity) {
        showToast(context.getString(messageResId), duration, iconResId, gravity);
    }

    /**
     * 显示Toast
     *
     * @param messageResId 消息资源Id
     * @param duration     持续时间
     * @param iconResId    图标资源Id
     * @param gravity      比重
     * @param args         参数
     */
    public static void showToast(int messageResId, int duration, int iconResId, int gravity, Object... args) {
        showToast(context.getString(messageResId, args), duration, iconResId, gravity);
    }


    /**
     * 显示Toast
     *
     * @param message   消息
     * @param duration
     * @param iconResId
     * @param gravity
     */
    public static void showToast(String message, int duration, int iconResId, int gravity) {
        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToastMessage)
                    || Math.abs(time - lastToastTime) > 2000) {
                View view = LayoutInflater.from(context).inflate(R.layout.zdjer_view_toast, null);
                TextView tvMessage = (TextView) view.findViewById(R.id.tv_zdjer_view_toast_message);
                tvMessage.setText(message);

                if (iconResId != 0) {
                    ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_zdjer_view_toast_icon);
                    iv_icon.setImageResource(iconResId);
                    iv_icon.setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(context);
                toast.setView(view);
                if (gravity == Gravity.CENTER) {
                    toast.setGravity(gravity, 0, 0);
                } else {
                    toast.setGravity(gravity, 0, 35);
                }
                toast.setDuration(duration);
                toast.show();
                lastToastMessage = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }

    /*private ToastView toastView;
    private long duration = 3500l;

    public ToastHelper(Activity activity) {
        init(activity);
    }

    private void init(Activity activity){
        toastView = new ToastView(activity);
        setLayoutGravity(81);
    }

    public void setLayoutGravity(int gravity){
        if(gravity!=0){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2,-2);
            layoutParams.gravity = gravity;
            int pixel = (int) DeviceHelper.dpToPixel(16F);
            layoutParams.setMargins(pixel,pixel,pixel,pixel);
            toastView.setLayoutParams(layoutParams);
        }
    }

    public long getDuration(){
        return duration;
    }

    public void setDuration(long duration){
        this.duration = duration;
    }

    public void setIcon(int iconResId){
        toastView.ivIcon.setImageResource(iconResId);
    }

    public void setMessage(String message){
        toastView.tvMessage.setText(message);
    }

    public void show(){
        final ViewGroup viewGroup = (ViewGroup)((Activity)toastView.getContext())
                .findViewById(android.R.id.content);
        if(viewGroup!=null){
            ObjectAnimator.ofFloat(toastView,"alpha",0.0F).setDuration(0L).start();
            viewGroup.addView(toastView);
            ObjectAnimator.ofFloat(toastView,"alpha",0.0F,1.0F).setDuration(167L).start();
            toastView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(toastView, "alpha", 1.0F, 0.0F);
                    objectAnimator.setDuration(100L);
                    objectAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewGroup.removeView(toastView);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    objectAnimator.start();
                }
            },duration);
        }else{

        }
    }

    private class ToastView extends FrameLayout {
        public ImageView ivIcon;
        public TextView tvMessage;

        public ToastView(Context context) {
            this(context, null);
        }

        public ToastView(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public ToastView(Context context, AttributeSet attributeSet, int defStyleAttr) {
            super(context, attributeSet, defStyleAttr);
            init();
        }

        private void init() {
            LayoutInflater.from(this.getContext()).inflate(
                    R.zdjer_list_item_footer.zdjer_view_base_toast, this, true);
            ivIcon = (ImageView)this.findViewById(R.id.iv_zdjer_view_base_toast_action);
            tvMessage = (TextView)this.findViewById(R.id.tv_zdjer_view_base_toast_title);

        }


    }*/
}

