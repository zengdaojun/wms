package com.zdjer.win.view.transport;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zdjer.utils.DateHelper;
import com.zdjer.utils.view.ViewHelper;
import com.zdjer.utils.view.base.BaseActivity;
import com.zdjer.win.R;
import com.zdjer.win.bean.TransportBO;
import com.zdjer.win.model.TransportBLO;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.view.widget.WmsSelectDataActivity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity:出库运输信息选择
 */
public class TransportEditActivity extends BaseActivity {

	private TextView tvBack = null; // 返回
	private TextView tvSave = null;// 保存
	
	private LinearLayout llWuLiu = null;// 物流
	private TextView tvWuLiu = null;// 物流
	
	private LinearLayout llCarNum = null;// 车牌号
	private TextView tvCarNum = null;// 车牌号
	
	private LinearLayout llDriver = null;// 司机
	private TextView tvDriver = null;// 司机
	
	private LinearLayout llSendDate = null;// 发车日期
	private TextView tvSendDate = null;// 发车日期
	
	private LinearLayout llSendTime = null;// 发车时间
	private TextView tvSendTime = null;// 发车时间
	private Calendar calendar = null;	
	private TransportBLO transportBlo=null;//运输信息业务逻辑
	private long transportId=0;

	/**
	 * 创建
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// 1 加载布局
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_transport_edit);

			// 2 初始化组件
			initializeComponent();

		} catch (Exception e) {
			Toast.makeText(TransportEditActivity.this,
					R.string.wms_common_exception, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 初始化组件
	 * 
	 * @throws Exception
	 */
	private void initializeComponent() throws Exception {
		tvBack = (TextView) findViewById(R.id.transport_edit_tv_back);// 返回
		tvBack.setOnClickListener(new BackClickListenerImpl());

		tvSave = (TextView) findViewById(R.id.transport_edit_tv_save);// 保存
		tvSave.setOnClickListener(new SaveClickListenerImpl());

		llWuLiu = (LinearLayout) findViewById(R.id.transport_edit_ll_wuliu);// 物流公司
		llWuLiu.setOnClickListener(new WuLiuClickListenerImpl());
		tvWuLiu = (TextView) findViewById(R.id.transport_edit_tv_wuliu);

		llCarNum = (LinearLayout) findViewById(R.id.transport_edit_ll_carnum);// 车牌号
		llCarNum.setOnClickListener(new CarNumClickListenerImpl());
		tvCarNum = (TextView) findViewById(R.id.transport_edit_tv_carNum);// 车牌号

		llDriver = (LinearLayout) findViewById(R.id.transport_edit_ll_driver);// 司机
		llDriver.setOnClickListener(new DriverClickListenerImpl());
		tvDriver = (TextView) findViewById(R.id.transport_edit_tv_driver);

		llSendDate = (LinearLayout) findViewById(R.id.transport_edit_ll_senddate);// 发车日期
		llSendDate.setOnClickListener(new SendDateClickListenerImpl());
		tvSendDate = (TextView) findViewById(R.id.transport_edit_tv_senddate);

		llSendTime = (LinearLayout) findViewById(R.id.transport_edit_ll_sendtime);// 发车时间
		llSendTime.setOnClickListener(new SendTimeClickListenerImpl());
		tvSendTime = (TextView) findViewById(R.id.transport_edit_tv_sendtime);

		initView();
	}
	


	/**
	 * 初始化控件
	 * @throws ParseException 
	 */
	public void initView()  {
		try {
			Intent intent = getIntent();
			if (intent != null) {
				transportId = intent.getLongExtra("transportId", 0);
				if (transportBlo == null) {
					transportBlo = new TransportBLO();
				}
				TransportBO transport = transportBlo.getTransportByTransportId(transportId);
				if (transport != null) {

					tvWuLiu.setTag(transport.getWuLiuId());
					tvWuLiu.setText(transport.getWuLiu());

					tvCarNum.setTag(transport.getCarNumId());
					tvCarNum.setText(transport.getCarNum());

					tvDriver.setTag(transport.getDriverId());
					tvDriver.setText(transport.getDriver());

					calendar = Calendar.getInstance();
					calendar.setTime(transport.getSendDate());
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);

					tvSendDate.setText(String.format("%d-%d-%d", year, month + 1, day));
					tvSendTime.setText(String.format("%d:%d", hourOfDay, minute));
				}
			}
		}catch (Exception e){

		}
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
				TransportEditActivity.this.finish();
				overridePendingTransition(R.anim.in_from_left,
						R.anim.out_to_right);
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}	

	/**
	 * 保存
	 * 
	 * @author bipolor
	 * 
	 */
	private class SaveClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (isValidate()) {
					long wuLiuId=Long.parseLong(String.valueOf(tvWuLiu.getTag()));
					String wuLiu=tvWuLiu.getText().toString();
					
					long carNumId=Long.parseLong(String.valueOf(tvCarNum.getTag()));
					String carNum=tvCarNum.getText().toString();

					long driverId=Long.parseLong(String.valueOf(tvDriver.getTag()));
					String driver=tvDriver.getText().toString();
					
					String sendDateString=tvSendDate.getText().toString().trim()+" "+
							 tvSendTime.getText().toString()+":0";
					Date sendDate= DateHelper.getDate(sendDateString);
					
//					TransportBO transport=new TransportBO();
//					transport.setTransportId(transportId);
					TransportBO transport=transportBlo.getTransportByTransportId(transportId);
					transport.setWuLiuId(wuLiuId);
					transport.setWuLiu(wuLiu);
					transport.setCarNumId(carNumId);
					transport.setCarNum(carNum);
					transport.setDriverId(driverId);
					transport.setDriver(driver);
					transport.setSendDate(sendDate);					
					TransportBLO transportBlo=new TransportBLO();
					SharedPreferences sharedPreferences = getSharedPreferences(
							"win", MODE_PRIVATE);
					String ip=sharedPreferences.getString("ip", "");
					String token= sharedPreferences.getString("currToken", "");
					String msg=new String("");
					if(!transportBlo.syncTransport(ip,transport,token,msg)){
						Toast.makeText(TransportEditActivity.this,
								msg, Toast.LENGTH_LONG).show();
						return;
					}
					if(transportBlo.editTransport(transport)){
						Toast.makeText(TransportEditActivity.this,
								R.string.wms_common_save_success, Toast.LENGTH_LONG).show();
					}else{						
						Toast.makeText(TransportEditActivity.this,
								R.string.wms_common_save_failed, Toast.LENGTH_LONG).show();
					}

					TransportEditActivity.this.setResult(1, null);
					TransportEditActivity.this.finish();
					overridePendingTransition(R.anim.in_from_left,
							R.anim.out_to_right);
				}
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 验证是否通过
	 * 
	 * @return
	 */
	private boolean isValidate() {
		// 1 物流
		String wuliu = tvWuLiu.getText().toString();
		if (wuliu.length() == 0) {
			Toast.makeText(TransportEditActivity.this,
					R.string.transport_wuliu_hint, Toast.LENGTH_LONG).show();
			ViewHelper.Focus(tvWuLiu);
			return false;
		}
		// 2 车牌号
		/*String carnum = tvCarNum.getText().toString();
		if (carnum.length() == 0) {
			Toast.makeText(TransportEditActivity.this,
					R.string.transport_carnum_hint, Toast.LENGTH_LONG).show();
			ActivityHelper.Focus(tvCarNum);
			return false;
		}
		// 3 司机
		String driver = tvDriver.getText().toString();
		if (driver.length() == 0) {
			Toast.makeText(TransportEditActivity.this,
					R.string.transport_driver_hint, Toast.LENGTH_LONG).show();
			ActivityHelper.Focus(tvDriver);
			return false;
		}*/
		// 4 发货日期
		String sendDate = tvSendDate.getText().toString();
		if (sendDate.length() == 0) {
			Toast.makeText(TransportEditActivity.this,
					R.string.transport_senddate_hint, Toast.LENGTH_LONG).show();
			ViewHelper.Focus(tvSendDate);
			return false;
		}
		// 5 发货时间
		String sendtime = tvSendTime.getText().toString();
		if (sendtime.length() == 0) {
			Toast.makeText(TransportEditActivity.this,
					R.string.transport_sendtime_hint, Toast.LENGTH_LONG).show();
			ViewHelper.Focus(tvSendTime);
			return false;
		}
		return true;
	}

	/**
	 * 选择物流公司
	 * 
	 * @author bipolor
	 * 
	 */
	private class WuLiuClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent(TransportEditActivity.this,
						WmsSelectDataActivity.class);
				intent.putExtra("dataType", DataType.wuLiu.getValue());
				intent.putExtra("dataTitle",
						getResources().getString(R.string.transport_wuliu));
				TransportEditActivity.this.startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 选择车牌号
	 * 
	 * @author bipolor
	 * 
	 */
	private class CarNumClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if(tvWuLiu.getTag()==null){
					Toast.makeText(TransportEditActivity.this,
							R.string.transport_wuliu_hint, Toast.LENGTH_LONG).show();
					return;
				}
				long wuLiuId=Long.parseLong(tvWuLiu.getTag().toString());
				Intent intent = new Intent(TransportEditActivity.this,
						WmsSelectDataActivity.class);
				intent.putExtra("parentId", wuLiuId);
				intent.putExtra("dataType", DataType.carNum.getValue());
				intent.putExtra("dataTitle",
						getResources().getString(R.string.transport_carnum));
				TransportEditActivity.this.startActivityForResult(intent, 2);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 选择司机信息
	 * 
	 * @author bipolor
	 * 
	 */
	private class DriverClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if(tvWuLiu.getTag()==null){
					Toast.makeText(TransportEditActivity.this,
							R.string.transport_wuliu_hint, Toast.LENGTH_LONG).show();
					return;
				}
				long wuLiuId=Long.parseLong(tvWuLiu.getTag().toString());				
				Intent intent = new Intent(TransportEditActivity.this,
						WmsSelectDataActivity.class);
				intent.putExtra("parentId", wuLiuId);
				intent.putExtra("dataType", DataType.driver.getValue());
				intent.putExtra("dataTitle",
						getResources().getString(R.string.transport_driver));
				TransportEditActivity.this.startActivityForResult(intent, 3);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 选择发车日期
	 * 
	 * @author bipolor
	 * 
	 */
	private class SendDateClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (calendar == null) {
					calendar = DateHelper.getCurrCalendar();
				}
				final int year = calendar.get(Calendar.YEAR);
				final int month = calendar.get(Calendar.MONTH);
				final int day = calendar.get(Calendar.DAY_OF_MONTH);
				Dialog dialog = new DatePickerDialog(TransportEditActivity.this,
						R.style.dialog, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {

								String sendDateString = String.format(
										"%d-%d-%d", year, monthOfYear + 1,
										dayOfMonth);

								tvSendDate.setText(sendDateString);
							}
						}, year, month, day);
				dialog.show();
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 选择发货时间
	 * 
	 * @author bipolor
	 * 
	 */
	private class SendTimeClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (calendar == null) {
					calendar = DateHelper.getCurrCalendar();
				}
				final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
				final int minute = calendar.get(Calendar.MINUTE);
				Dialog dialog = new TimePickerDialog(TransportEditActivity.this,
						R.style.dialog,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								String sendTimeString = String.format("%d:%d",
										hourOfDay, minute);

								tvSendTime.setText(sendTimeString);
							}
						}, hourOfDay, minute, true);
				dialog.show();
			} catch (Exception e) {
				Toast.makeText(TransportEditActivity.this,
						R.string.wms_common_exception, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 选择后返回结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		long dataId = data.getExtras().getLong("dataId");
		String dataValue = data.getExtras().getString("dataValue");
		// 物流选择返回结果
		if (requestCode == 1 && resultCode == 1) {
			tvWuLiu.setTag(dataId);
			tvWuLiu.setText(dataValue);
		}
		// 车牌号选择返回结果
		if (requestCode == 2 && resultCode == 1) {
			tvCarNum.setTag(dataId);
			tvCarNum.setText(dataValue);
		}
		// 司机选择返回结果
		if (requestCode == 3 && resultCode == 1) {
			tvDriver.setTag(dataId);			
			tvDriver.setText(dataValue);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
