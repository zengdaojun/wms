package com.zdjer.win.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.win.R;
import com.zdjer.win.adapter.RecordAdapter;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.bean.RecordType;
import com.zdjer.win.model.RecordBLO;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Activity:出库记录
 */
public class SearchThdNumRecordActivity extends BaseListActivity<RecordBO> {

	@Bind(R.id.tv_search_thdnum_record_title)
	protected TextView tvTitle = null;// 标题

	@Bind(R.id.tv_search_thdnum_record_thdnum)
	protected TextView tvThdNum = null;// 单号标签

	@Bind(R.id.tv_search_thdnum_record_thdnum_value)
	protected TextView tvTHDNumValue = null;// 单号值

	@Bind(R.id.tv_search_thdnum_record_total_count)
	protected TextView tvTotalCount;// 已添加条码

	private String thdNum = "";
	private RecordBLO recordBLO = new RecordBLO();

	@Override
	protected int getLayoutId() {

		return R.layout.activity_search_thdnum_record;
	}

	@Override
	public void initView() {
		super.initView();
		Intent intent = getIntent();
		if (intent != null) {
			// 获得提货单号
			thdNum = intent.getStringExtra("ThdNum");
		}
	}

	//@Override
	/*public void initData(){
		if(currentPage == 0){

		}

		super.initData();

		*//*Intent intent = getIntent();
		if (intent != null) {
			// 获得提货单号
			String thdNum = intent.getStringExtra("ThdNum");
			RecordBLO recordBlo = new RecordBLO();
			lstRecord = recordBlo.getRecords(thdNum);
			if(lstRecord.size()==0){
				lvBarCode.setVisibility(View.GONE);
			}else{
				lvBarCode.setVisibility(View.VISIBLE);
			}
			if (lstRecord.size() > 0) {
				RecordBO recordTemp = lstRecord.get(0);
				if (recordTemp.getRecordType() == RecordType.in) {
					tvTitle.setText(R.string.record_in);
					tvThdNum.setText(R.string.record_in_thdnum);
				} else if (recordTemp.getRecordType() == RecordType.out) {
					tvTitle.setText(R.string.record_out);
					tvThdNum.setText(R.string.record_out_thdnum);
				}
				tvTHDNumValue.setText(recordTemp.getThdNum());
				bindData();
			}
		}*//*
	}*/

	@Override
	protected SwipeRefreshLayout getSwipeRefreshLayout() {

		return (SwipeRefreshLayout) findViewById(R.id.srl_search_thdnum_record_barcode);
	}

	@Override
	protected ListView getListView() {

		return (ListView) findViewById(R.id.lv_search_thdnum_record_barCode);
	}

	@Override
	protected BaseListAdapter getListAdapter() {

		return new RecordAdapter();
	}

	@Override
	protected List getListData() {
		if(currentPage == 0){
			setTotalCount();
		}
		List<RecordBO> lstRecord = new ArrayList<RecordBO>();
		Intent intent = getIntent();
		if (intent != null) {
			// 获得提货单号
			String thdNum = intent.getStringExtra("ThdNum");
			RecordBLO recordBlo = new RecordBLO();
			lstRecord = recordBLO.getRecords(thdNum, currentPage, getPageSize());
			if (lstRecord.size() > 0) {
				RecordBO recordTemp = lstRecord.get(0);
				if (recordTemp.getRecordType() == RecordType.in) {
					tvTitle.setText(R.string.record_in);
					tvThdNum.setText(R.string.record_in_thdnum);
				} else if (recordTemp.getRecordType() == RecordType.out) {
					tvTitle.setText(R.string.record_out);
					tvThdNum.setText(R.string.record_out_thdnum);
				}
				tvTHDNumValue.setText(recordTemp.getThdNum());
			}
		}
		return lstRecord;
	}

	/**
	 * 初始化组件
	 *//*
	private void initializeComponent() throws Exception {
		tvBack = (TextView) findViewById(R.id.searchthdnumbrowse_tv_back);
		tvTitle = (TextView) findViewById(R.id.searchthdnumbrowse_tv_title);
		tvThdNum = (TextView) findViewById(R.id.searchthdnumbrowse_tv_thdnum);
		tvTHDNumValue = (TextView) findViewById(R.id.searchthdnumbrowse_tv_thdnumvalue);

		tvAdded = (TextView) findViewById(R.id.searchthdnumbrowse_tv_added);
		lvBarCode = (ListView) findViewById(R.id.searchthdnumbrowse_lv_barCode);

		tvBack.setOnClickListener(new BackClickListenerImpl());

		Intent intent = getIntent();
		if (intent != null) {
			// 获得提货单号
			String thdNum = intent.getStringExtra("ThdNum");
			RecordBLO recordBlo = new RecordBLO();
			lstRecord = recordBlo.getRecords(thdNum);
			if(lstRecord.size()==0){
				lvBarCode.setVisibility(View.GONE);
			}else{
				lvBarCode.setVisibility(View.VISIBLE);
			}
			if (lstRecord.size() > 0) {
				RecordBO recordTemp = lstRecord.get(0);
				if (recordTemp.getRecordType() == RecordType.in) {
					tvTitle.setText(R.string.record_in);
					tvThdNum.setText(R.string.record_in_thdnum);
				} else if (recordTemp.getRecordType() == RecordType.out) {
					tvTitle.setText(R.string.record_out);
					tvThdNum.setText(R.string.record_out_thdnum);
				}
				tvTHDNumValue.setText(recordTemp.getThdNum());
				bindData();
			}
		}
	}*/

	// 绑定数据
	/*private void bindData() {
		// 获得适配器数据
		List<Map<String, Object>> lstData = new ArrayList<Map<String, Object>>();
		for (RecordBO recordBo : lstRecord) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("BarCode", recordBo.getBarCode());
			map.put("WareHouse", recordBo.getWarehouse());
			lstData.add(map);
		}
		if(lstData.size()==0){
			lvBarCode.setVisibility(View.GONE);
		}else{
			lvBarCode.setVisibility(View.VISIBLE);
		}
		// 实例化SimpleAdapter适配器,并绑定
		SimpleAdapter adapter = new SimpleAdapter(this, lstData,
				R.layout.item_thdnum_barcode, new String[] { "BarCode","WareHouse" },
				new int[] {
						R.id.wbarcode_item_tv_barcode,
						R.id.wbarcode_item_tv_warehouse});
		lvBarCode.setAdapter(adapter);
		setAddCount();
	}*/

	private void setTotalCount() {
		int totalCount = recordBLO.getRecordsTotalCount(thdNum);

		if (totalCount==0) {
			tvTotalCount.setVisibility(View.VISIBLE);
			tvTotalCount.setText("已添加条码 " + totalCount + " 个：");
		} else {
			tvTotalCount.setVisibility(View.INVISIBLE);
		}
	}

	@OnClick(R.id.tv_search_thdnum_record_back)
	public void back(View v) {
		try {
			SearchThdNumRecordActivity.this.finish();
			overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
		} catch (Exception e) {
			Toast.makeText(SearchThdNumRecordActivity.this,
					R.string.wms_common_exception, Toast.LENGTH_LONG).show();
		}
	}
}
