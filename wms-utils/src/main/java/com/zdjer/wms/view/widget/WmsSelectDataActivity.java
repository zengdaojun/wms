package com.zdjer.wms.view.widget;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zdjer.utils.view.AppHelper;
import com.zdjer.utils.view.adapter.BaseListAdapter;
import com.zdjer.utils.view.base.BaseListActivity;
import com.zdjer.wms.utils.R;
import com.zdjer.wms.adapter.WmsSelectDataAdapter;
import com.zdjer.wms.bean.DataItemBO;
import com.zdjer.wms.bean.DataType;
import com.zdjer.wms.model.DataItemBLO;

import java.util.List;

/**
 * Activity:基础数据选择
 *
 * @author zdj
 */
public class WmsSelectDataActivity extends BaseListActivity<DataItemBO> implements TextWatcher {

    private TextView tvBack = null; // 返回
    private TextView tvTitle = null; // 标题
    private EditText editinput = null;//模糊搜索

    private DataType dataType = DataType.wareHouse;
    private long parentId = 0;
    private DataItemBLO dataItemBlo = new DataItemBLO();// 选项业务逻辑类
    private String search = "";

    private int resultCode = 1;

    /**
     * 获得布局的Id
     *
     * @return
     */
    protected int getLayoutId() {

        return R.layout.wms_activity_select_data;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {

        super.initView();

        tvBack = (TextView) findViewById(R.id.tv_wms_select_data_back);
        tvBack.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tv_wms_select_data_title);

        editinput = (EditText) findViewById(R.id.et_wms_search_keyword);
        editinput.addTextChangedListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            int dataTypeInt = intent.getIntExtra("dataType", 0);
            dataType = DataType.value(dataTypeInt);
            parentId = intent.getLongExtra("parentId", 0);
            // 获得标题
            String dataTitle = intent.getStringExtra("dataTitle");
            tvTitle.setText(dataTitle);
        }
    }

    /**
     * 获得刷新布局
     *
     * @return
     */
    @Override
   protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return (SwipeRefreshLayout) findViewById(R.id.srl_zdjer_activity_listview);
    }

    /**
     * 获得列表控件
     *
     * @return
     */
    @Override
    protected ListView getListView() {
        return (ListView) findViewById(R.id.lv_wms_select_data);
    }

    /**
     * 获得列表适配器
     *
     * @return
     */
    @Override
    protected BaseListAdapter getListAdapter() {
        return new WmsSelectDataAdapter();
    }

    /**
     * 获取数据
     *
     * @return
     */
    @Override
    public List<DataItemBO> getListData() {
        return dataItemBlo.getDataItems(dataType, parentId,search, currentPage, getPageSize());
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();
        if (viewId == R.id.tv_wms_select_data_back) {
            finish();
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_to_right);
        }
    }

    /**
     * 列表项点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DataItemBO dataItemBO = (DataItemBO) baseListAdapter.getItem(position);
        if(dataItemBO!=null) {
            Intent data = new Intent();
            data.putExtra("dataId", dataItemBO.getDataId());
            data.putExtra("dataValue", dataItemBO.getDataValue());
            setResult(resultCode, data);
            overridePendingTransition(R.anim.in_from_left,
                    R.anim.out_to_right);
            AppHelper.finishActivity();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
            search = s.toString();
            loadData();
    }

    @Override
    public void afterTextChanged(Editable arg0) {

    }


}