package com.zdjer.wms.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.bean.RecordGatherBO;

/**
 * 记录汇总适配器
 *
 * @author zdj
 */
public class RecordGatherAdapter extends BaseListAdapter<RecordGatherBO> {
    /**
     * View持有者
     */
    static class ViewHolder{
        TextView tvSerNo;
        TextView tvCount;


        /**
         * 构造函数，初始化需要使用的控件
         * @param view
         */
        public ViewHolder(View view){
            tvSerNo = (TextView) view
                    .findViewById(R.id.tv_wms_record_gather_serno);
            tvCount = (TextView) view
                    .findViewById(R.id.tv_wms_record_gather_count);

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
                    R.layout.wms_item_record_gather, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RecordGatherBO recordGatherBO = lstData.get(position);
        //设置显示的数据
        viewHolder.tvSerNo.setText(recordGatherBO.getSerNo());
        viewHolder.tvCount.setText(String.valueOf(recordGatherBO.getCount()));
        return convertView;
    }
}