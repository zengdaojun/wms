package com.zdjer.utils.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zdjer.utils.R;
import com.zdjer.utils.StringHelper;
import com.zdjer.utils.view.DeviceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配基类
 * @param <T>
 */
public class BaseListAdapter<T> extends BaseAdapter {
    public static final int STATE_EMPTY_ITEM = 0;//空
    public static final int STATE_LOAD_MORE = 1;//加载更多
    public static final int STATE_NO_MORE = 2;//无更多
    public static final int STATE_NO_DATA = 3;//无数据
    public static final int STATE_LESS_ONE_PAGE = 4;//少于1页
    public static final int STATE_NETWORK_ERROR = 5;//网络错误
    //public static final int STATE_OTHER = 6;

    protected int state = STATE_LESS_ONE_PAGE;

    protected int loadMoreText;//加载更多
    protected int loadedAll;//已加载全部
    protected int noData;//无数据
    protected int mScreenWidth;

    private LayoutInflater layoutInflater;
    private LinearLayout llFooterView;

    protected ArrayList<T> lstData = new ArrayList<T>();

    /**
     * 构造函数
     */
    public BaseListAdapter() {

        loadMoreText = R.string.zdjer_loading;
        loadedAll = R.string.zdjer_loaded_all;
        noData = R.string.zdjer_no_data;
    }

    /**
     * 获得布局
     * @param context
     * @return
     */
    protected LayoutInflater getLayoutInflater(Context context) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return layoutInflater;
    }

    /**
     * 设置状态
     * @param state 状态
     */
    public void setState(int state) {

        this.state = state;
    }

    /**
     * 获取状态
     * @return
     */
    public int getState() {

        return this.state;
    }

    /**
     * 设置加载更多的信息
     * @param loadmoreText
     */
    public void setLoadmoreText(int loadmoreText) {

        loadMoreText = loadmoreText;
    }

    /**
     * 设置完成的信息
     * @param loadFinishText
     */
    public void setLoadFinishText(int loadFinishText) {

        loadedAll = loadFinishText;
    }

    /**
     * 设置无数据的信息
     * @param noDataText
     */
    public void setNoDataText(int noDataText) {

        noData = noDataText;
    }

    /**
     * 获得数量
     * @return
     */
    @Override
    public int getCount() {
        switch (getState()) {
            case STATE_EMPTY_ITEM:
                return getDataSizePlus1();
            case STATE_NETWORK_ERROR:
            case STATE_LOAD_MORE:
                return getDataSizePlus1();
            case STATE_NO_DATA:
                return 1;
            case STATE_NO_MORE:
                return getDataSizePlus1();
            case STATE_LESS_ONE_PAGE:
                return getDataSize();
            default:
                break;
        }
        return getDataSize();
    }

    /**
     *
     * @return
     */
    public int getDataSizePlus1() {
        if (hasFooterView()) {
            return getDataSize() + 1;
        }
        return getDataSize();
    }

    /**
     *
     * @return
     */
    public int getDataSize() {

        return lstData.size();
    }

    @Override
    public T getItem(int arg0) {
        if (lstData.size() > arg0) {
            return lstData.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(ArrayList<T> data) {
        lstData = data;
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     * @return
     */
    public ArrayList<T> getData() {
        if(lstData == null) {
            lstData = new ArrayList<T>();
        }
        return lstData;
    }

    /**
     * 添加数据
     * @param data
     */
    public void addData(List<T> data) {

        if (lstData != null && data != null && !data.isEmpty()) {
            lstData.addAll(data);
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param obj
     */
    public void addItem(T obj) {

        if (lstData != null) {
            lstData.add(obj);
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param pos
     * @param obj
     */
    public void addItem(int pos, T obj) {

        if (lstData != null) {
            lstData.add(pos, obj);
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @param obj
     */
    public void removeItem(Object obj) {

        lstData.remove(obj);
        notifyDataSetChanged();
    }

    /**
     *
     */
    public void clear() {

        lstData.clear();
        notifyDataSetChanged();
    }

    /**
     *
     * @return
     */
    protected boolean loadMoreHasBg() {

        return true;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1 && hasFooterView()) {
            // 最后一条
            if (state == STATE_LOAD_MORE
                    || state == STATE_NO_MORE
                    || state == STATE_EMPTY_ITEM
                    || state == STATE_NETWORK_ERROR) {
                this.llFooterView = (LinearLayout) LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.zdjer_list_item_footer,
                        null);
                if (!loadMoreHasBg()) {
                    llFooterView.setBackgroundDrawable(null);
                }
                ProgressBar progress = (ProgressBar) llFooterView
                        .findViewById(R.id.pb_zdjer_list_item_footer_progressbar);
                TextView text = (TextView) llFooterView.findViewById(R.id.tv_zdjer_list_item_footer_message);
                switch (getState()) {
                    case STATE_LOAD_MORE:
                        setFooterViewLoading();
                        break;
                    case STATE_NO_MORE:
                        llFooterView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        text.setText(loadedAll);
                        break;
                    case STATE_EMPTY_ITEM:
                        progress.setVisibility(View.GONE);
                        llFooterView.setVisibility(View.VISIBLE);
                        text.setText(noData);
                        break;
                    case STATE_NETWORK_ERROR:
                        llFooterView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        text.setVisibility(View.VISIBLE);
                        if (DeviceHelper.hasInternet()) {
                            text.setText("加载出错了");
                        } else {
                            text.setText("没有可用的网络");
                        }
                        break;
                    default:
                        progress.setVisibility(View.GONE);
                        llFooterView.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                        break;
                }
                return llFooterView;
            }
        }
        if (position < 0) {
            position = 0; // 若列表没有数据，是没有footview/headview的
        }
        return getRealView(position, convertView, parent);
    }

    protected View getRealView(int position, View convertView, ViewGroup parent) {
        return null;
    }



    protected boolean hasFooterView() {
        return true;
    }

    public View getFooterView() {
        return this.llFooterView;
    }

    public void setFooterViewLoading(String loadMsg) {
        ProgressBar progress = (ProgressBar) llFooterView
                .findViewById(R.id.pb_zdjer_list_item_footer_progressbar);
        TextView text = (TextView) llFooterView.findViewById(R.id.tv_zdjer_list_item_footer_message);
        llFooterView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        if (StringHelper.isEmpty(loadMsg)) {
            text.setText(loadMoreText);
        } else {
            text.setText(loadMsg);
        }
    }

    public void setFooterViewLoading() {
        setFooterViewLoading("");
    }

    public void setFooterViewText(String msg) {
        ProgressBar progress = (ProgressBar) llFooterView
                .findViewById(R.id.pb_zdjer_list_item_footer_progressbar);
        TextView text = (TextView) llFooterView.findViewById(R.id.tv_zdjer_list_item_footer_message);
        llFooterView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
        text.setText(msg);
    }

   /* protected void setContent(TweetTextView contentView, String content) {
        contentView.setMovementMethod(MyLinkMovementMethod.a());
        contentView.setFocusable(false);
        contentView.setDispatchToParent(true);
        contentView.setLongClickable(false);
        Spanned span = Html.fromHtml(TweetTextView.modifyPath(content));
        span = InputHelper.displayEmoji(contentView.getResources(),
                span.toString());
        contentView.setText(span);
        MyURLSpan.parseLinkText(contentView, span);
    }*/

    protected void setText(TextView textView, String text, boolean needGone) {
        if (text == null || TextUtils.isEmpty(text)) {
            if (needGone) {
                textView.setVisibility(View.GONE);
            }
        } else {
            textView.setText(text);
        }
    }

    protected void setText(TextView textView, String text) {

        setText(textView, text, false);
    }
}
