package com.max.pinnedsectionrefreshlistviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView.PinnedSectionListAdapter;

import java.util.ArrayList;
public class WarnDetailAdapter extends BaseAdapter implements
		PinnedSectionListAdapter {
	private ArrayList<PinnedSectionBean> list;
	private Context mContext;

	public ArrayList<PinnedSectionBean> getList() {
		return list;
	}

	public void setList(ArrayList<PinnedSectionBean> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<PinnedSectionBean>();
		}
	}

	public WarnDetailAdapter(ArrayList<PinnedSectionBean> list, Context mContext) {
		super();
		this.setList(list);
		this.mContext = mContext;
	}

	final static class ViewHolder {
		TextView item_date, item_content;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public PinnedSectionBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_list_layout, null);
			viewHolder.item_date = (TextView) convertView
					.findViewById(R.id.item_date);
			viewHolder.item_content = (TextView) convertView
					.findViewById(R.id.item_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PinnedSectionBean bean = getItem(position);
		if (bean.type == PinnedSectionBean.SECTION) {
			viewHolder.item_date.setText(list.get(position).getMessages()
					.getTime());
		}
		else {
			viewHolder.item_date.setText(list.get(position).getMessages()
					.getTime());
			viewHolder.item_content.setText(list.get(position).getMessages()
					.getContent());
		}
		return convertView;

	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == PinnedSectionBean.SECTION;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return ((PinnedSectionBean) getItem(position)).type;
	}
}
