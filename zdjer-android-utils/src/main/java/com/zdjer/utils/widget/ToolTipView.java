package com.zdjer.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zdjer.utils.R;
import com.zdjer.utils.view.DeviceHelper;

/**
 * Created by zdj on 16/4/14.
 */
public class ToolTipView extends LinearLayout implements View.OnClickListener {

    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1;

    /**
     * 网络加载中
     */
    public static final int NETWORK_LOADING = 2;

    /**
     * 加载中
     */
    public static final int LOADING = 3;

    /**
     * 无数据
     */
    public static final int NODATA = 4;

    /**
     * 隐藏
     */
    public static final int HIDE = 5;

    /**
     *
     */
    public static final int NODATA_ENABLE_CLICK = 6;

    private ProgressBar pbProgress;
    private boolean isClickIconEnable = true;
    private final Context context;
    public ImageView ivIcon;
    private View.OnClickListener listener;
    private int toopTipState;
    private RelativeLayout rlToopTip;
    private String noDataContent = "";
    private TextView tvMessage;

    public ToolTipView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ToolTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        try{
        View vToopTip = View.inflate(context, R.layout.zdjer_view_tooltip, null);
        ivIcon = (ImageView) vToopTip.findViewById(R.id.iv_zdjer_view_tooptip_icon);
        tvMessage = (TextView) vToopTip.findViewById(R.id.tv_zdj_view_tooltip_message);
        rlToopTip = (RelativeLayout) vToopTip.findViewById(R.id.rl_zdjer_view_tooltip);
        pbProgress = (ProgressBar) vToopTip.findViewById(R.id.pb_zdjer_view_tooptip_progress);
        setBackgroundColor(-1);
        setOnClickListener(this);
        ivIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isClickIconEnable) {
                    // setErrorType(NETWORK_LOADING);
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        addView(vToopTip);
        changeErrorLayoutBgMode(context);
        }catch (Exception e){
            Log.e("1",e.getMessage());
        }
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        toopTipState = HIDE;
        setVisibility(View.GONE);
    }

    public int getToopTipState() {
        return toopTipState;
    }

    public boolean isLoadError() {
        return toopTipState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return toopTipState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v) {
        if (isClickIconEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null)
                listener.onClick(v);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // MyApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void onSkinChanged() {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setDayNight(boolean flag) {
    }

    public void setErrorMessage(String msg) {
        tvMessage.setText(msg);
    }

    /**
     * 新添设置背景
     *
     * @author 火蚁 2015-1-27 下午2:14:00
     */
    public void setErrorImag(int imgResource) {
        try {
            ivIcon.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setTipType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                toopTipState = NETWORK_ERROR;
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"pagefailed_bg"));
                if (DeviceHelper.hasInternet()) {
                    tvMessage.setText(R.string.zdjer_load_error_click_to_refresh);
                    ivIcon.setBackgroundResource(R.drawable.zdjer_page_error);
                } else {
                    tvMessage.setText(R.string.zdjer_network_error_click_to_refresh);
                    ivIcon.setBackgroundResource(R.drawable.zdjer_network_error);
                }
                ivIcon.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                isClickIconEnable = true;
                break;
            case NETWORK_LOADING:
                toopTipState = NETWORK_LOADING;
                // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
                pbProgress.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.GONE);
                tvMessage.setText(R.string.zdjer_loading);
                isClickIconEnable = false;
                break;
            case LOADING:
                //加载中
                toopTipState = LOADING;
                // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
                pbProgress.setVisibility(View.VISIBLE);
                ivIcon.setVisibility(View.GONE);
                tvMessage.setText(R.string.zdjer_loading);
                isClickIconEnable = false;
                break;
            case NODATA:
                toopTipState = NODATA;
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                ivIcon.setBackgroundResource(R.drawable.zdjer_page_empty);
                ivIcon.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                isClickIconEnable = true;
                break;
            case HIDE:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                toopTipState = NODATA_ENABLE_CLICK;
                ivIcon.setBackgroundResource(R.drawable.zdjer_page_empty);
                ivIcon.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                isClickIconEnable = true;
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        noDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (!noDataContent.equals("")) {
            tvMessage.setText(noDataContent);
        } else {
            tvMessage.setText(R.string.zdjer_no_data);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            toopTipState = HIDE;
        }
        super.setVisibility(visibility);
    }
}
