package com.zdjer.win.view.transport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.win.R;
import com.zdjer.win.bean.TransportBO;
import com.zdjer.win.model.TransportBLO;

import java.util.List;

/**
 * Activity:出库运输信息选择
 */
public class TransportListActivity extends BaseListActivity<TransportBO> {

	private TextView tvBack = null; // 返回
	private TextView tvNew=null;//新建
	private ListView lvTransport = null; // 可选物流信息
	
	private TransportSelectAdapter adapter = null;// 选项列表适配器
	private TransportBLO transportBlo = null;// 选项业务逻辑类

	// 分页处理
	private int currentPage = 1;// 当前页
	private int totalCount = 0;// 总数据数
	private int pageCount = 0;// 总页数
	private View vwLoadMore; // 点击加载更多视图
	private Button btnLoadMore;// 加载更多按钮
	private Handler handler = new Handler();

	/**
	 * 创建
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// 1 加载布局
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_transport_list);			

			// 2 初始化组件
			initializeComponent();
			
		} catch (Exception e) {
			Toast.makeText(TransportListActivity.this, R.string.wms_common_exception,
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		try {
			super.onResume();

		} catch (Exception e) {
			Toast.makeText(TransportListActivity.this, R.string.wms_common_exception,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 初始化组件
	 * 
	 * @throws Exception
	 */
	private void initializeComponent() throws Exception {
		tvBack = (TextView) findViewById(R.id.transport_list_tv_back);
		tvBack.setOnClickListener(new BackClickListenerImpl());// 返回
				
		tvNew = (TextView) findViewById(R.id.transport_list_tv_new);
		tvNew.setOnClickListener(new NewClickListenerImpl());// 新建
		
		lvTransport = (ListView) findViewById(R.id.transport_list_lv_item);
		
		vwLoadMore = getLayoutInflater().inflate(R.layout.item_listview_footer,
				null);
		btnLoadMore = (Button) vwLoadMore.findViewById(R.id.loadMoreButton);
		btnLoadMore.setOnClickListener(new LoadMoreClickListenerImpl());

		initializeAdapter("");// 绑定数据		
		lvTransport.setOnScrollListener(this);// 处理滚动
	}

	/**
	 * 初始化数据适配器
	 * 
	 * @throws Exception
	 */
	private void initializeAdapter(String likeData) throws Exception {
		lvTransport.addFooterView(vwLoadMore);// 添加底部视图

		if (lvTransport.getFooterViewsCount() != 0) {
			for (int i = 0; i < lvTransport.getFooterViewsCount(); i++) {
				lvTransport.removeFooterView(vwLoadMore);
			}
		} else {
			lvTransport.addFooterView(vwLoadMore);// 添加底部视图
		}
		if(transportBlo==null){
			transportBlo=new TransportBLO();
		}
		this.currentPage=1;
		//符合条件的总数据数
		this.totalCount = transportBlo.getTransportsTotalCountLike(likeData);	
		if(this.totalCount==0){
			this.lvTransport.setVisibility(View.INVISIBLE);
		}else{
			this.lvTransport.setVisibility(View.VISIBLE);
		}
		//页数
		/*this.pageCount = this.totalCount % ConstCore.PAGESIZE == 0 ? this.totalCount
				/ ConstCore.PAGESIZE : this.totalCount / ConstCore.PAGESIZE + 1;
		List<TransportBO> lstTransport = transportBlo.getTransportsLike(likeData, currentPage, ConstCore.PAGESIZE);
		adapter = new TransportSelectAdapter(this, lstTransport);
		lvTransport.setAdapter(adapter);*/
	}

	@Override
	protected SwipeRefreshLayout getSwipeRefreshLayout() {
		return null;
	}

	@Override
	protected ListView getListView() {
		return null;
	}

	@Override
	protected BaseListAdapter<TransportBO> getListAdapter() {
		return null;
	}

	@Override
	protected List<TransportBO> getListData() {
		return null;
	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {

	}

	// 返回
	private class BackClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				TransportListActivity.this.finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
			} catch (Exception e) {
				Toast.makeText(TransportListActivity.this, R.string.wms_common_exception,
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	// 新建
	private class NewClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent(TransportListActivity.this,
						TransportNewActivity.class);
				TransportListActivity.this.startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			} catch (Exception e) {
				Toast.makeText(TransportListActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 加载更多
	 * 
	 * @author bipolor
	 * 
	 */
	private class LoadMoreClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				btnLoadMore.setText(R.string.common_loading); // 设置按钮文字
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {						
						try {
							TransportListActivity.this.currentPage++;
							loadMoreData();
						} catch (Exception e) {
							Toast.makeText(TransportListActivity.this,
									R.string.wms_common_exception,
									Toast.LENGTH_LONG).show();
						}						
						btnLoadMore.setText(R.string.common_click_to_load); // 恢复按钮文字
					}
				}, 0);
			} catch (Exception e) {
				Toast.makeText(TransportListActivity.this, R.string.wms_common_exception,
						Toast.LENGTH_LONG).show();
			}
		}
	}
	
	/**
	 * 加载查询更多数据
	 * 
	 * @throws Exception
	 */
	private void loadMoreData() throws Exception {
		/*if (this.currentPage <= this.pageCount) {
			if(transportBlo==null){
				transportBlo=new TransportBLO();
			}
			String likeData="";
			List<TransportBO> lstTransport = transportBlo.getTransportsLike(likeData,currentPage, ConstCore.PAGESIZE);
			adapter.addTransports(lstTransport);
			adapter.notifyDataSetChanged();
		}*/
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 如果所有的记录选项等于数据集的条数，则移除列表底部视图
		if (totalItemCount == totalCount + 1) {
			lvTransport.removeFooterView(vwLoadMore);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {		
	}

	/**
	 * 物流信息选择适配器
	 * @author bipolor
	 *
	 */
	private class TransportSelectAdapter extends BaseAdapter {
		private Context context = null;
		private List<TransportBO> lstTransport = null;

		/**
		 * 构造函数
		 * 
		 * @param context
		 *            上下文
		 * @param lstTransport
		 *            物流信息集合		
		 */
		public TransportSelectAdapter(Context context, List<TransportBO> lstTransport) {
			this.context = context;
			this.lstTransport = lstTransport;
		}

		@Override
		public int getCount() {
			return (lstTransport == null) ? 0 : lstTransport.size();
		}

		@Override
		public Object getItem(int position) {
			return lstTransport.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				final TransportBO transport = (TransportBO) getItem(position);
				if(transport==null){
					return null;
				}
				View view = convertView;
				if (view == null) {
					LayoutInflater inflater = LayoutInflater.from(context);
					view = inflater.inflate(R.layout.item_transport_select, null);
				}

				//运输信息
				TextView lblTransportInfo = (TextView) view
						.findViewById(R.id.transport_select_tv_transportinfo);
				lblTransportInfo.setText(transport.getTransportInfo().toString());
				
				//整行确认选择
				LinearLayout llTransport=(LinearLayout)view
						.findViewById(R.id.transport_select_ll_item);				
				if (lstTransport.size() == 1) {
					llTransport
							.setBackgroundResource(R.drawable.bg_row_single_selector);
				} else {
					if (position == 0) {
						llTransport
								.setBackgroundResource(R.drawable.bg_row_top_selector);
					} else if (position == lstTransport.size() - 1) {
						llTransport
								.setBackgroundResource(R.drawable.bg_row_bot_selector);
					}
				}
				
				//点击车牌号为选中
				lblTransportInfo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Intent intent=new Intent(); 
							intent.putExtra("tranId", transport.getTranId());
							intent.putExtra("transportInfo", transport.getTransportInfo());
							TransportListActivity.this.setResult(1, intent);
							TransportListActivity.this.finish();
							overridePendingTransition(R.anim.in_from_left,
									R.anim.out_to_right);							
						} catch (Exception e) {
							Toast.makeText(context, R.string.wms_common_exception,
									Toast.LENGTH_LONG).show();
						}
					}
				});
				
				//点击箭头编辑
				ImageView imgArrow=(ImageView)view.findViewById(R.id.transport_select_img_arrow);
				imgArrow.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Intent intent=new Intent(TransportListActivity.this,TransportEditActivity.class); 
							Log.i("===========", transport.getTransportId()+"");							
							intent.putExtra("transportId", transport.getTransportId());
							TransportListActivity.this.startActivityForResult(intent, 2);						
							overridePendingTransition(R.anim.in_from_left,
									R.anim.out_to_right);							
						} catch (Exception e) {
							Toast.makeText(context, R.string.wms_common_exception,
									Toast.LENGTH_LONG).show();
						}
					}
				});
				return view;
			} catch (Exception e) {
				Toast.makeText(context, R.string.wms_common_exception,
						Toast.LENGTH_LONG).show();
			}
			return null;
		}
		
		/**  
	     * 添加物流信息选项
	     * @param lstTransport  
	     */
		public void addTransports(List<TransportBO> lstTransport){
			this.lstTransport.addAll(lstTransport);
		}
	}
	
	/**
	 * 选择后返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// 新建后
			if (requestCode == 1 && resultCode == 1) {
				initializeAdapter("");
			}
			// 编辑后
			if (requestCode == 2 && resultCode == 1) {
				initializeAdapter("");
			}
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			Toast.makeText(TransportListActivity.this,
					R.string.wms_common_exception, Toast.LENGTH_LONG).show();
		}
	}
}
