package com.hgad.warehousemanager.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;

/**
 * Created by Administrator on 2017/6/26.
 */
public abstract class BaseFragment extends Fragment implements Callback<BaseResponse>, View.OnClickListener {

    protected Context mContext;
    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    // 统一加载布局文件
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = getChildViewLayout(inflater);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();


    public abstract View getChildViewLayout(LayoutInflater inflater);

    public void sendRequest(BaseRequest request, Class<? extends BaseResponse> responseClass) {

        NetUtil.sendRequest(request, responseClass, this);
    }

    public abstract <Res extends BaseResponse> void onSuccessResult(BaseRequest request, Res response);

    @Override
    public void onSuccess(BaseRequest request, BaseResponse response) {

        onSuccessResult(request, response);
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {
    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }
}
