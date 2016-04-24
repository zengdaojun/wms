package com.zdjer.win.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.win.R;
import com.zdjer.win.adapter.SearchThdNumAdapter;
import com.zdjer.win.bean.RecordBO;
import com.zdjer.win.model.RecordBLO;
import com.zdjer.win.view.helper.ScanActivity;
import com.zdjer.wms.utils.BroadcastReceiverHelper;

import java.util.List;

import butterknife.Bind;

/**
 * Activity:历史记录
 */
public class SearchThdNumActivity extends BaseListActivity<RecordBO>
		implements OnScrollListener,OnQueryTextListener {
	private TextView tvBack = null; // 返回

	@Bind(R.id.sv_search_thdnum_barcode)
	protected SearchView svSearch = null;// 查询
	private ImageView imgScan = null;//扫描
	private ListView lvHistory = null; // 历史记录列表
	private View vwLoadMore; //点击加载更多   
	private Button btnLoadMore;//加载更多 
	
	private int currentPage=1;
	private int totalCount=0;
	private int pageCount=0;
	
	private String qureyText="";//查询字符串
	
	private Handler handler = new Handler(); 	
	private RecordBLO recordBLO = new RecordBLO();

	/*//扫描
	private String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";//接受广播
	private String START_SCAN_ACTION = "com.geomobile.se4500barcode";//调用扫描广播
	private String STOP_SCAN_ACTION = "com.geomobile.se4500barcode.poweroff";//结束扫描
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context,
							  Intent intent) {
			String action = intent.getAction();
			if (action.equals(RECE_DATA_ACTION)) {
				String barcode = intent.getStringExtra("se4500");
				hardScanBarCode(barcode);
			}
		}

	};*/

	private BroadcastReceiverHelper broadcastReceiverHelper;
	private Boolean isAllowScan = true;

	@Override
	protected int getLayoutId() {

		return R.layout.activity_search_thdnum;
	}

	@Override
	protected SwipeRefreshLayout getSwipeRefreshLayout() {

		return (SwipeRefreshLayout) findViewById(R.id.srl_win_barcode);
	}

	@Override
	protected ListView getListView() {

		return (ListView) findViewById(R.id.lv_win_barcode);
	}

	@Override
	protected BaseListAdapter<RecordBO> getListAdapter() {

		return new SearchThdNumAdapter();
	}

	@Override
	protected List<RecordBO> getListData() {

		return recordBLO.getRecordsLikeThdNum(qureyText,currentPage,getPageSize());
	}


	/**
	 * 开始
	 * 注册扫描
	 */
	@Override
	protected void onStart() {
		super.onStart();
		//注册广播接收器
		this.broadcastReceiverHelper = new BroadcastReceiverHelper(this) {
			@Override
			public void onReceive(Context context,
								  Intent intent) {
				super.onReceive(context, intent);
				if (broadcastReceiverHelper.getIsScanBarCode() && isAllowScan) {
					String barcode = intent.getStringExtra("se4500");
					handleScanBarCode(barcode);
				}
			}
		};
		broadcastReceiverHelper.registerAction();
	}

	/**
	 * 处理扫描的条码
	 */
	private void handleScanBarCode(String barCode) {
		if (barCode != null) {
			this.svSearch.setQuery(barCode,false);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.broadcastReceiverHelper.stopScan();
	}

	/**
	 * 按键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case 134://中间
			case 135://左边
			case 136: {
				if (isAllowScan) {
					this.broadcastReceiverHelper.startScan();
				}
				break;
			}
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_HOME: {
				//返回
				//this.back(null);
			}
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**  
     * 初始化ListView的适配器  
	 * @throws Exception 
     *//*
    private void initializeAdapter(String like) throws Exception{
    	this.currentPage=1;
    	if(recordBlo==null){
    		recordBlo=new RecordBLO();
    	}    			
    	this.totalCount= recordBlo.getRecordsTotalCountLikeThdNum("");
    	*//*this.pageCount=this.totalCount% ConstCore.PAGESIZE==0?
    			this.totalCount/ConstCore.PAGESIZE:this.totalCount/ConstCore.PAGESIZE+1;
    	List<RecordBO> lstRecord = recordBlo.getRecordsLikeThdNum(like,currentPage,ConstCore.PAGESIZE);
        adapter = new SearchThdnumAdapter(this,lstRecord); 
		lvHistory.setAdapter(adapter);
		
		if(this.totalCount==0){
			lvHistory.setVisibility(View.INVISIBLE);
		}else{
			lvHistory.setVisibility(View.VISIBLE);
		}*//*
    }*/

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 返回
	 * 
	 * @author bipolor
	 * 
	 */
	private class BackClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				SearchThdNumActivity.this.finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
			} catch (Exception e) {
				Toast.makeText(SearchThdNumActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * 扫条码
	 * @author bipolor
	 *
	 */
	private class ScanClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent();
				intent.setClass(SearchThdNumActivity.this,ScanActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 10);
			
			} catch (Exception e) {
				Toast.makeText(SearchThdNumActivity.this, R.string.wms_common_exception,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		try {
			qureyText=newText;
			loadData();
			return true;
		} catch (Exception e) {
			Toast.makeText(SearchThdNumActivity.this,
					R.string.wms_common_exception, Toast.LENGTH_LONG).show();
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (svSearch != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager != null) {
				inputMethodManager.hideSoftInputFromWindow(
						svSearch.getWindowToken(), 0);
			}
			svSearch.clearFocus();
		}
		return true;
	}
    
    /**
	 * 返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		//扫描条码
		if(resultCode == 10){
			Bundle bundle = data.getExtras();
			//显示扫描到的内容
			svSearch.setQuery(bundle.getString("result"), false);
			this.loadData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*private class SearchThdnumAdapter extends BaseAdapter {
    	
		private Context context;
    	private List<RecordBO> lstRecord = null;
    	
    	*//**
    	 * 构造函数
    	 * @param lstRecord 记录集合
    	 *//*
    	public SearchThdnumAdapter(Context context,List<RecordBO> lstRecord){
    		this.context=context;
    		this.lstRecord=lstRecord;
    	}
    	

    	@Override
    	public int getCount() {
    		return this.lstRecord.size();
    	}

    	@Override
    	public Object getItem(int position) {
    		return this.lstRecord.get(position);
    	}

    	@Override
    	public long getItemId(int position) {
    		return position;
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		final int index = position;
    		final RecordBO record=lstRecord.get(index);
    		View view = convertView;
    		if(view==null){
    			view = getLayoutInflater().inflate(R.layout.item_thdnum, null);
    		} 
    		TextView tvThdnum=(TextView)view.findViewById(R.id.searchthdnum_item_tv_thdnum);
    		tvThdnum.setText(record.getThdNum());
    		
    		TextView tvBarcodeCount=(TextView)view.findViewById(R.id.searchthdnum_item_tv_barcodecount);
    		tvBarcodeCount.setText(String.valueOf(record.getBarCodeCount()));    		
    		
    		//整行确认选择
			LinearLayout llSearchthdnum=(LinearLayout)view
					.findViewById(R.id.searchthdnum_select_ll_item);				
    		if (lstRecord.size() == 1) {
    			llSearchthdnum
						.setBackgroundResource(R.drawable.bg_row_single_selector);
			} else {
				if (position == 0) {
					llSearchthdnum
							.setBackgroundResource(R.drawable.bg_row_top_selector);
				} else if (position == lstRecord.size() - 1) {
					llSearchthdnum
							.setBackgroundResource(R.drawable.bg_row_bot_selector);
				}
			}
    		
    		//查看详细
    		llSearchthdnum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						// 绑定参数，跳转页面
						Intent intent = new Intent(SearchThdNumActivity.this,
								SearchThdNumRecordActivity.class);

						intent.putExtra("ThdNum", record.getThdNum());
						SearchThdNumActivity.this.startActivity(intent);
													
					} catch (Exception e) {
						Toast.makeText(context, R.string.wms_common_exception,
								Toast.LENGTH_LONG).show();
					}
				}
			});
    		
    		return view;
    	}
    	
    	*//**
         * 添加数据列表项  
         * @param lstRecord
         *//*
        public void addRecordItem(List<RecordBO> lstRecord){  
            this.lstRecord.addAll(lstRecord);  
        }  
    }*/
}
