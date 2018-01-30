package com.max.pinnedsectionrefreshlistviewdemo;

import android.app.Activity;
import android.os.Bundle;

import com.max.pinnedsectionrefreshlistviewdemo.PinnedSectionRefreshListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements IXListViewListener{
	private PinnedSectionRefreshListView mListView;
	private List<Messages> mData;
	private ArrayList<PinnedSectionBean> real_data;
	private WarnDetailAdapter mAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initData();
		show();
	}

	private void init() {
		mListView = (PinnedSectionRefreshListView) findViewById(R.id.list);
	}

	private void initData() {
		mData = new ArrayList<Messages>();
		for (int i = 0; i < 24; i++) {
			Messages messages = new Messages();
			if (i < 10) {
				messages.setTime("2015-10-11 0"+i+":00:00");
			}else{
				messages.setTime("2015-10-12 "+i+":00:00");
			}
			messages.setContent(""+i);
			mData.add(messages);
		}
		real_data = new ArrayList<PinnedSectionBean>();
		real_data = PinnedSectionBean.getData(mData);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
	}

	private void show() {
		mAdapter = new WarnDetailAdapter(real_data, this);
		mListView.setAdapter(mAdapter);
	}


	@Override
	public void onRefresh() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("");
	}


	@Override
	public void onLoadMore() {
		mListView.setLoadHide();
	}

}
