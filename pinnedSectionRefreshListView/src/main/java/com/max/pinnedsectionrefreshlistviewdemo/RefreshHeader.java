package com.max.pinnedsectionrefreshlistviewdemo;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RefreshHeader extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	
	/**
	 * @param context
	 */
	private int mState = STATE_NORMAL;
	private Animation mRotateAnim;
	private Animation mRotateDownAnim;
	
	private final int ROATE_ANIM_DURATION = 180;
	
	

	public RefreshHeader(Context context) {
		super(context);
		initView(context);
	}


	/**
	 * @param context
	 */
	private void initView(Context context) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LinearLayout.inflate(context, R.layout.refresh_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);
		mArrowImageView = (ImageView) findViewById(R.id.refresh_header_arrow);
		mProgressBar = (ProgressBar) findViewById(R.id.refresh_header_progressbar);
		mHintTextView = (TextView) findViewById(R.id.refresh_header_hint_textview);
		
		mRotateAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnim.setDuration(ROATE_ANIM_DURATION);
		mRotateAnim.setFillAfter(true);
		
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROATE_ANIM_DURATION);
		mRotateAnim.setFillAfter(true);
	}
	
	public void setState(int state){
		if (state == mState) {
			return;
		}
		if (state == STATE_REFRESHING) {
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText("正在刷新");
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.setAnimation(mRotateAnim);
				mHintTextView.setText("松开刷新数据");
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText("正在努力加载...");
			break;

		default:
			break;
		}
		mState = state;
	}

	public void setVisiableHeight(int height){
		if (height < 0) {
			height = 0;
		}
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}
	
	//��ÿ��Ӹ߶�
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}
}
