package com.hgad.warehousemanager.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseFragment;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.db.dao.BaseDaoImpl;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.ui.activity.ChangeWareActivity;
import com.hgad.warehousemanager.ui.activity.CheckRecordActivity;
import com.hgad.warehousemanager.ui.activity.InWareChooseActivity;
import com.hgad.warehousemanager.ui.activity.NotificationActivity;
import com.hgad.warehousemanager.ui.activity.OutWareActivity;
import com.hgad.warehousemanager.ui.activity.ScanResultActivity;
import com.hgad.warehousemanager.zxing.activity.CaptureActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */
public class HomeFragment extends BaseFragment {
    private static final int SCAN = 199;

    private View mView;
    //    private UserDao userDao;
    private BaseDaoImpl<UserInfo, Integer> userDao;
    private ConvenientBanner<String> convenientBanner;
    private List<String> mImageList = new ArrayList<>();

    @Override
    protected void initData() {
//        userDao = new UserDao(mContext);
        userDao = new BaseDaoImpl<>(mContext, UserInfo.class);
    }

    @Override
    protected void initView() {
        mImageList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1225040570,1155990249&fm=23&gp=0.jpg");
        mImageList.add("http://d.hiphotos.baidu.com/zhidao/pic/item/241f95cad1c8a7862239b60f6109c93d70cf50a4.jpg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174359&di=24c744dd93e649d57138a099e1a147c6&imgtype=0&src=http%3A%2F%2Fimgb.mumayi.com%2Fandroid%2Fwallpaper%2F2012%2F01%2F03%2Fsl_600_2012010302213667522752.jpg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174359&di=b1d83951268801db17ea1a50e64da0f4&imgtype=0&src=http%3A%2F%2Ffile.mumayi.com%2Fforum%2F201305%2F16%2F171333grx16e26je1q1m1e.jpg");
        mImageList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496227174367&di=d0ac3c5b319e5d5ae540efc980188606&imgtype=0&src=http%3A%2F%2Fimg1.pconline.com.cn%2Fpiclib%2F200906%2F16%2Fbatch%2F1%2F35412%2F1245119293386tv7sapi8x4.jpg");
        mView.findViewById(R.id.tv_in_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_change_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_out_ware).setOnClickListener(this);
        mView.findViewById(R.id.tv_code_scanning).setOnClickListener(this);
        mView.findViewById(R.id.tv_check).setOnClickListener(this);
        mView.findViewById(R.id.tv_task_notification).setOnClickListener(this);
        mView.findViewById(R.id.iv_scan).setOnClickListener(this);
        convenientBanner = (ConvenientBanner<String>) mView.findViewById(R.id.convenientBanner);
        convenientBanner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
            @Override
            public ImageViewHolder createHolder() {
                return new ImageViewHolder();
            }
        },mImageList)
                .setPageIndicator(new int[]  {R.drawable.shape_oval_gray,R.drawable.shape_oval_white}) //设置两个点作为指示器
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL); //设置指示器的方向水平  居中
        convenientBanner.setCanLoop(true);
    }

    public class ImageViewHolder implements Holder<String> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
//            imageView.setImageResource(data);
            Picasso.with(mContext).load(data).into(imageView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(2000);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public View getChildViewLayout(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home, null);
        return mView;
    }

    @Override
    public <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == SCAN) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                Intent intent = new Intent(mContext, ScanResultActivity.class);
                intent.putExtra(Constants.SCAN_RESULT, result);
                intent.putExtra(Constants.TYPE, Constants.SCAN_RESULT);
                startActivity(intent);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_in_ware:
                go2InWare();
                break;
            case R.id.tv_change_ware:
                go2ChangeWare();
                break;
            case R.id.tv_out_ware:
                go2OutWare();
                break;
            case R.id.tv_code_scanning:
                go2Scan();
                break;
            case R.id.tv_check:
                go2Check();
                break;
            case R.id.tv_task_notification:
                go2Notification();
                break;
            case R.id.iv_scan:
                go2Scan();
                break;
        }
    }

    private void go2Notification() {
        Intent intent = new Intent(mContext, NotificationActivity.class);
        startActivity(intent);
    }

    private void go2Scan() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, SCAN);
    }

    private void go2ChangeWare() {
        Intent intent = new Intent(mContext, ChangeWareActivity.class);
        startActivity(intent);
    }

    private void go2Check() {
        Intent intent = new Intent(mContext, CheckRecordActivity.class);
        startActivity(intent);
    }

    private void go2OutWare() {
        Intent intent = new Intent(mContext, OutWareActivity.class);
        startActivity(intent);
    }

    private void go2InWare() {
        Intent intent = new Intent(mContext, InWareChooseActivity.class);
        startActivity(intent);
    }
}
