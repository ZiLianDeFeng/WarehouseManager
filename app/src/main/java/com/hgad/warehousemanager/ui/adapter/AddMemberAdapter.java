package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.util.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public class AddMemberAdapter extends BaseAdapter {

    private List<UserInfo> userInfoList;
    private Context context;

    public AddMemberAdapter(List<UserInfo> userInfoList, Context context) {
        this.userInfoList = userInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userInfoList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserInfo userInfo = userInfoList.get(position);
        CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_add_member);
        CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.cb);
        TextView tv_name = (TextView) viewHolder.getView(R.id.tv_name);
        checkBox.setChecked(userInfo.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userInfo.setChecked(isChecked);
            }
        });
        tv_name.setText(userInfo.getRealName());

        return viewHolder.convertView;
    }
}
