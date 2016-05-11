package com.zdjer.win.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.win.R;
import com.zdjer.win.bean.RecordBO;

/**
 * Created by zdj on 4/20/16.
 */
public class SearchThdNumAdapter extends BaseListAdapter<RecordBO> {
    /**
     * View持有者
     */
    static class ViewHolder{
        TextView tvTHDNum;
        TextView tvCount;



        /**
         * 构造函数，初始化需要使用的控件
         * @param view
         */
        public ViewHolder(View view){
            tvTHDNum = (TextView) view
                    .findViewById(R.id.tv_item_thdnum_thdnum);
            tvCount = (TextView) view
                    .findViewById(R.id.tv_item_thdnum_count);
        }
    }

    /**
     * 获取真正的视图
     * @param position
     * @param convertView
     * @param parent
     * @return View
     */
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.item_thdnum, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RecordBO recordBO = lstData.get(position);


        viewHolder.tvTHDNum.setText(recordBO.getThdNum());

        viewHolder.tvCount.setText(String.valueOf(recordBO.getBarCodeCount()));

        return convertView;
    }
}
