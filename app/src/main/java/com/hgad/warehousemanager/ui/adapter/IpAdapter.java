package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.IpInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.ui.activity.AddIpActivity;
import com.hgad.warehousemanager.util.CommonViewHolder;
import com.hgad.warehousemanager.util.SPUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */
public class IpAdapter extends BaseAdapter {
    private List<IpInfo> datas;
    private Context context;
    private BaseDaoImpl<IpInfo, Integer> ipInfoDao;
    private CallFreshListener callFreshListener;

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
    }

    public IpAdapter(List<IpInfo> datas, Context context) {
        this.datas = datas;
        this.context = context;
        ipInfoDao = new BaseDaoImpl<>(context, IpInfo.class);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_ip);
        TextView tv_name = (TextView) holder.getView(R.id.tv_name);
        TextView tv_address = (TextView) holder.getView(R.id.tv_address);
        CheckBox cb_set = (CheckBox) holder.getView(R.id.cb_set);
        TextView tv_edit = (TextView) holder.getView(R.id.tv_edit);
        TextView tv_delete = (TextView) holder.getView(R.id.tv_delete);
        final IpInfo ipInfo = datas.get(position);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ipInfoDao.delete(ipInfo);
                    if (callFreshListener != null) {
                        callFreshListener.callFresh();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddIpActivity.class);
                intent.putExtra(Constants.IP_INFO, ipInfo);
                context.startActivity(intent);
            }
        });
        tv_name.setText(ipInfo.getName());
        tv_address.setText(ipInfo.getIp());
        cb_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (IpInfo data : datas) {
                        if (data.getId() != ipInfo.getId()) {
                            data.setDefault(false);
                        } else {
                            data.setDefault(true);
                            SPUtils.put(context, SPConstants.IP, data.getIp());
                        }
                    }
                } else {
                    for (IpInfo data : datas) {
                        if (data.getId() == ipInfo.getId()) {
                            data.setDefault(false);
                        }
                    }
                }
                try {
                    ipInfoDao.saveOrUpdate(datas);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (callFreshListener != null) {
                    callFreshListener.callFresh();
                }

            }
        });
        cb_set.setChecked(ipInfo.isDefault());
        return holder.convertView;
    }

    public interface CallFreshListener {
        void callFresh();
    }
}
