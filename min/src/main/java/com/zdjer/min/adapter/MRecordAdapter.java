package com.zdjer.min.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdjer.min.R;
import com.zdjer.min.bean.MRecordBO;
import com.zdjer.utils.view.adapter.BaseListAdapter;

/**
 * Created by zdj on 16/4/19.
 */
public class MRecordAdapter extends BaseListAdapter<MRecordBO> {
    /**
     * View持有者
     */
    static class ViewHolder{
        TextView tvBarCode;


        /**
         * 构造函数，初始化需要使用的控件
         * @param view
         */
        public ViewHolder(View view){
            tvBarCode = (TextView) view
                    .findViewById(R.id.tv_mbarcode_barcode);

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
                    R.layout.item_mrecord, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MRecordBO mRecordBO = lstData.get(position);
        //设置显示的数据
        viewHolder.tvBarCode.setText(mRecordBO.getBarCode());
        return convertView;
    }
}