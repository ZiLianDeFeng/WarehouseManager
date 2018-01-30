package com.max.pinnedsectionrefreshlistviewdemo;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RefreshFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;
	public final static int STATE_FINISH = 3;
	
	private Context mContext;
	
	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;

	public RefreshFooter(Context context) {
		super(context);
		initView(context);
	}
	
	
	
	public RefreshFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}



	//��ʼ���ؼ�
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.refresh_footer, null);
		addView(moreView);
		//�趨���
		moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = moreView.findViewById(R.id.refresh_footer_content);
		mProgressBar = moreView.findViewById(R.id.refresh_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.refresh_footer_hint_textview);
	}
	
	//��ͬ״̬�µ���ʾ������
	public void setState(int state){
		//ȫ����ʧ
		mHintView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		//׼������
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText("");
		} 
		//���ڼ���
		else if (state == STATE_LOADING) {
			//��ʾ������
			mProgressBar.setVisibility(View.VISIBLE);
		}else if (state == STATE_NORMAL) {
			//ԭʼ״̬,����ʾ�鿴����
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText("");
		}else if (state == STATE_FINISH) {
			//ԭʼ״̬,����ʾ�鿴����
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText("");
		}
	}
	
	/**
	 * ���õײ��ľ���
	 * @param height
	 */
	public void setBottomMargin(int height){
		if (height < 0) {
			return;
		}
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = height;
		
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * ��ȡ���ײ��ľ���
	 */
	public int getBottomMargin(){
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	/**
	 * ��ʼ״̬��ʾ����
	 */
	public void normal(){
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	/**
	 * ����״̬,��ʾ������
	 */
	public void loading(){
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * ���صײ�
	 */
	public void hide(){
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mHintView.setLayoutParams(lp);
	}
	
	/**
	 * ��ʾ�ײ�
	 */
	public void show(){
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
}
