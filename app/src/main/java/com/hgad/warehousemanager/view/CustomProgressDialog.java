package com.hgad.warehousemanager.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;


/**
 * Created by Administrator on 2017/3/8.
 */


public class CustomProgressDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private Context mContext;
    private ImageView mImageView;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private int count = 0;
    private String oldLoadingTip;
    private Animation operatingAnim;

    public CustomProgressDialog(Context context, String content) {
        super(context, ProgressDialog.THEME_HOLO_LIGHT);
        this.mContext = context;
        this.mLoadingTip = content;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void show() {
        super.show();
    }


    private void initData() {
        // 通过ImageView对象拿到背景显示的AnimationDrawable
//        mAnimation = (AnimationDrawable) mImageView.getBackground();
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        // 为了防止在onCreate方法中只显示第一帧的解决方案之一
        mImageView.post(new Runnable() {
            @Override
            public void run() {
//                mAnimation.start();
                if (operatingAnim != null) {
                    mImageView.startAnimation(operatingAnim);
                }
            }
        });
        mLoadingTv.setText(mLoadingTip);
        Point size = new Point();
        this.getWindow().getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.alpha = 1f;
        params.height = height / 6;
        params.width = 4 * width / 5;
        params.dimAmount = 0.1f;
        this.getWindow().setAttributes(params);
    }

    public void setContent(String str) {
        mLoadingTv.setText(str);
    }

    private void initView() {
        setContentView(R.layout.dialog_frame);
        mLoadingTv = (TextView) findViewById(R.id.loadingTv);
        mImageView = (ImageView) findViewById(R.id.loadingIv);
    }

	/*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
		mAnimation.start();
		super.onWindowFocusChanged(hasFocus);
	}*/
}