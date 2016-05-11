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
public class SearchThdNumRecordAdapter extends BaseListAdapter<RecordBO> {
    /**
     * View持有者
     */
    static class ViewHolder{
        TextView tvBarCode;
        TextView tvWarehouse;



        /**
         * 构造函数，初始化需要使用的控件
         * @param view
         */
        public ViewHolder(View view){
            tvBarCode = (TextView) view
                    .findViewById(R.id.tv_item_thdnum_barcode_barcode);
            tvWarehouse = (TextView) view
                    .findViewById(R.id.tv_item_thdnum_barcode_warehouse);
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
                    R.layout.item_thdnum_barcode, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RecordBO recordBO = lstData.get(position);

        viewHolder.tvBarCode.setText(recordBO.getBarCode());

        //Thdnum
        String jxsNum = "";
        if (recordBO.getJxsNum().length() > 0) {
            jxsNum = String.format("(%s)", recordBO.getJxsNum());
        }
        viewHolder.tvBarCode.setText(recordBO.getBarCode() + jxsNum);

        viewHolder.tvWarehouse.setText(recordBO.getWarehouse());

        return convertView;
    }
}
