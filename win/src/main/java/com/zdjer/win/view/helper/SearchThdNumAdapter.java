package com.zdjer.win.view.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.win.R;
import com.zdjer.win.bean.RecordBO;

import java.util.List;

/**
 * 入库/出库记录列表行适配器
 */
public class SearchThdNumAdapter extends BaseAdapter {

	private Context context = null;
	private List<RecordBO> lstRecord = null;
	private TextView tvAdded = null;

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param lstRecord
	 *            记录集合
	 * @param tvAdded
	 *            添加记录的标签
	 */
	public SearchThdNumAdapter(Context context, List<RecordBO> lstRecord,
			TextView tvAdded) {
		this.context = context;
		this.lstRecord = lstRecord;
		this.tvAdded = tvAdded;
	}

	@Override
	public int getCount() {
		return lstRecord.size();
	}

	@Override
	public Object getItem(int position) {
		return lstRecord.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.item_wbarcode_delete, null);
			}
			/*final TextView lblBarCode = (TextView) view
					.findViewById(R.id.itemdelete_tb_barcode);
			lblBarCode.setText(lstRecord.get(position).getBarCode());

			final ImageView imgDeleteBarCode = (ImageView) view
					.findViewById(R.id.item_iv_delete);
			imgDeleteBarCode.setBackgroundResource(R.drawable.delete);
			imgDeleteBarCode.setTag(position);

			imgDeleteBarCode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 1 确认删除 TODO

					// 2 删除选中的记录
					long recordId = lstRecord.get(index).getRecordId();
					if (recordBlo == null) {
						recordBlo = new RecordBLO();
					}
					if (recordBlo.deleteRecord(recordId)) {
						Toast.makeText(context, R.string.common_delete_success,
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(context, R.string.common_delete_faild,
								Toast.LENGTH_LONG).show();
					}

					// 2 根据提货单号查询条码，重新更新列表中的数据
					lstRecord.remove(index);
					notifyDataSetChanged();
					setAddCount();
				}
			});*/
			return view;
		} catch (Exception e) {
			Toast.makeText(context, R.string.wms_common_exception,
					Toast.LENGTH_LONG).show();
		}
		return null;
	}

	private void setAddCount() {
		if (lstRecord.size() == 0) {
			tvAdded.setVisibility(View.INVISIBLE);
		} else {
			tvAdded.setVisibility(View.VISIBLE);
			tvAdded.setText("已添加条码 " + lstRecord.size() + " 个：");
		}
	}
}