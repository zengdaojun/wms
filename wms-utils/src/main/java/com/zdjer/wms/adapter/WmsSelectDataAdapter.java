package com.zdjer.wms.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.bean.DataItemBO;

/**
 * 选择数据项适配器
 * @author zdj
 */
public class WmsSelectDataAdapter extends BaseListAdapter<DataItemBO> {

    /**
     * View持有者
     */
    static class ViewHolder{
        TextView tvDataValue;
        //LinearLayout llDataItem;

        /**
         * 构造函数，初始化需要使用的控件
         * @param view
         */
        public ViewHolder(View view){
            tvDataValue = (TextView) view
                    .findViewById(R.id.tv_wms_select_data_datavalue);
            //llDataItem = (LinearLayout)view.findViewById(R.id.ll_wms_item_select_data_item);
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
                    R.layout.wms_item_select_data, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DataItemBO dataItem = lstData.get(position);
        viewHolder.tvDataValue.setText(dataItem.getDataValue());
        return convertView;
    }
}
