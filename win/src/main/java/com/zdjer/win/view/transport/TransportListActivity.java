package com.zdjer.win.view.transport;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zdjer.utils.view.ToastHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.win.R;
import com.zdjer.win.bean.TransportBO;
import com.zdjer.win.model.TransportBLO;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Activity:出库运输信息选择
 */
public class TransportListActivity extends BaseListActivity<TransportBO> {

	private ListView lvTransport = null; // 可选物流信息
	private TransportBLO transportBlo = new TransportBLO();// 选项业务逻辑类

	@Override
	protected int getLayoutId() {

		return R.layout.activity_transport_list;
	}

	@Override
	protected SwipeRefreshLayout getSwipeRefreshLayout() {

		return (SwipeRefreshLayout) findViewById(R.id.srl_transport);
	}

	@Override
	protected ListView getListView() {

		return (ListView) findViewById(R.id.lv_transport);
	}

	@Override
	protected BaseListAdapter<TransportBO> getListAdapter() {
		return new TransportAdapter(this);
	}

	@Override
	protected List<TransportBO> getListData() {
		try {
			return transportBlo.getTransportsLike("", currentPage, getPageSize());
		}catch (Exception e){
			ToastHelper.showToast(R.string.wms_common_exception);
		}
		return new ArrayList<TransportBO>();
	}

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void onClick(View v) {

	}

	@OnClick(R.id.tv_transport_list_back)
	public void onBack(View v) {
		try {
			TransportListActivity.this.finish();
			overridePendingTransition(R.anim.in_from_left,
					R.anim.out_to_right);
		} catch (Exception e) {
			Toast.makeText(TransportListActivity.this, R.string.wms_common_exception,
					Toast.LENGTH_LONG).show();
		}
	}
	
	// 新建
	@OnClick(R.id.tv_transport_list_new)
	public void onNew(View v) {
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

	/**
	 * 选择后返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			// 新建后
			if (requestCode == 1 && resultCode == 1) {
				currentPage = 0;
				loadData();
			}
			// 编辑后
			if (requestCode == 2 && resultCode == 1) {
				currentPage = 0;
				loadData();
			}
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception e) {
			Toast.makeText(TransportListActivity.this,
					R.string.wms_common_exception, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 物流信息选择适配器
	 * @author bipolor
	 *
	 */
	private class TransportAdapter extends BaseListAdapter<TransportBO> {
		private Context context = null;

		/**
		 * 构造函数
		 *
		 * @param context
		 *            上下文
		 *            物流信息集合
		 */
		public TransportAdapter(Context context) {
			super();
			this.context = context;
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
			try {
				if (convertView == null || convertView.getTag() == null) {
					convertView = getLayoutInflater(parent.getContext()).inflate(
							R.layout.item_transport_select, null);
					//viewHolder = new ViewHolder(convertView);
					//convertView.setTag(viewHolder);
				}

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
							ToastHelper.showToast(R.string.wms_common_exception);
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
	}
}
