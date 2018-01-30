package com.hgad.warehousemanager.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
public class GuideActivity extends BaseActivity {

    /**
     * 是否显示引导界面
     */
    boolean isShow = false;
    /**
     * ViewPager对象
     */
    private ViewPager mViewPager;
    /**
     * 装载小圆圈的LinearLayout
     */
    private LinearLayout indicatorLayout;
    /**
     * ViewPager的每个页面集合
     */
    private List<View> views;
    /**
     * ViewPager下面的小圆圈
     */
    private ImageView[] mImageViews;
    /**
     * PagerAdapter对象
     */
    private GuidePagerAdapter guidePagerAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_viewPager);
        indicatorLayout = (LinearLayout) findViewById(R.id.linearlayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.pager_layout1, null));
        views.add(inflater.inflate(R.layout.pager_layout2, null));
        views.add(inflater.inflate(R.layout.pager_layout3, null));
        guidePagerAdapter = new GuidePagerAdapter(this, views);
        mImageViews = new ImageView[views.size()];
        drawCircl();
        mViewPager.setAdapter(guidePagerAdapter);
        mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
    }

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 画圆圈
     */
    private void drawCircl() {
        int num = views.size();
        for (int i = 0; i < num; i++) {
            //实例化每一个mImageViews[i]
            mImageViews[i] = new ImageView(this);
            if (i == 0) {
                // 默认选中第一张照片，所以将第一个小圆圈变为icon_carousel_02
                mImageViews[i].setImageResource(R.drawable.shape_oval_white);
            } else {
                mImageViews[i].setImageResource(R.drawable.shape_oval_tran);
            }
            // 给每个小圆圈都设置间隔
            mImageViews[i].setPadding(10, 10, 10, 10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            // 让每一个小圆圈都在LinearLayout的CENTER_VERTICAL（中间垂直）
            indicatorLayout.addView(mImageViews[i], params);
        }
    }

    /**
     * @author Harry 页面改变监听事件
     */
    private class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        /**
         * 页面有所改变，如果是当前页面，将小圆圈改为icon_carousel_02，其他页面则改为icon_carousel_01
         */
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mImageViews.length; i++) {
                if (arg0 != i) {
                    mImageViews[i]
                            .setImageResource(R.drawable.shape_oval_tran);
                } else {
                    mImageViews[arg0]
                            .setImageResource(R.drawable.shape_oval_white);
                }
            }
        }
    }

    class GuidePagerAdapter extends PagerAdapter {
        private List<View> mViews;
        private Activity mContext;

        public GuidePagerAdapter(Activity context, List<View> views) {
            this.mViews = views;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mViews.get(arg1));
        }

        /**
         * 实例化页卡，如果变为最后一页，则获取它的button并且添加点击事件
         */
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mViews.get(arg1), 0);
            if (arg1 == mViews.size() - 1) {
                TextView enterBtn = (TextView) arg0
                        .findViewById(R.id.guide_enter);
                enterBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SPUtils.put(GuideActivity.this, SPConstants.NOT_FRIST, true);
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            return mViews.get(arg1);
        }
    }
}
