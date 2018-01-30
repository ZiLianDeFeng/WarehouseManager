package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.ReviewInfo;
import com.hgad.warehousemanager.bean.request.ForUpRequest;
import com.hgad.warehousemanager.bean.response.ErrorResponseInfo;
import com.hgad.warehousemanager.bean.response.ForUpResponse;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.Callback;
import com.hgad.warehousemanager.net.NetUtil;
import com.hgad.warehousemanager.ui.activity.HandwritingActivity;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;
import com.hgad.warehousemanager.view.WritePadDialog;
import com.max.pinnedsectionrefreshlistviewdemo.TimeManagement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/8/14.
 */
public class ReviewAdapter extends BaseAdapter implements Callback {
    private List<ReviewInfo> data;
    private Context context;
    private CallFreshListener callFreshListener;
    private Bitmap mSignBitmap;

    public void setCallFreshListener(CallFreshListener callFreshListener) {
        this.callFreshListener = callFreshListener;
    }

    public ReviewAdapter(List<ReviewInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
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
        final int index = position;
        CommonViewHolder holder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_review);
        ReviewInfo reviewInfo = data.get(position);
        final int orderId = reviewInfo.getOrderId();
        TextView tv_order_num = (TextView) holder.getView(R.id.tv_order_num);
        TextView tv_apply_time = (TextView) holder.getView(R.id.tv_apply_time);
        TextView tv_apply_people = (TextView) holder.getView(R.id.tv_apply_people);
        TextView tv_urgency = (TextView) holder.getView(R.id.tv_urgency);
        TextView tv_apply_reason = (TextView) holder.getView(R.id.tv_apply_reason);
        TextView tv_net_weight = (TextView) holder.getView(R.id.tv_net_weight);
        Button btn_handle = (Button) holder.getView(R.id.btn_handle);
        tv_order_num.setText(reviewInfo.getOrderNum());
        tv_apply_people.setText(reviewInfo.getApplyPeople());
        tv_apply_reason.setText(reviewInfo.getApplyReason());
        String applyTime = reviewInfo.getApplyTime();
        try {
            applyTime = TimeManagement.exchangeStringDate(applyTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_apply_time.setText(applyTime);
        tv_net_weight.setText(reviewInfo.getTotalWeight());
        String urgency = reviewInfo.getUrgency();
        if ("非常紧急".equals(urgency)) {
            tv_urgency.setTextColor(context.getResources().getColor(R.color.red));
        } else if ("一般".equals(urgency)) {
            tv_urgency.setTextColor(context.getResources().getColor(R.color.yellow));
        } else if ("不紧急".equals(urgency)) {
            tv_urgency.setTextColor(context.getResources().getColor(R.color.blue));
        }
        tv_urgency.setText(urgency);
        btn_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertView("申请处理", null, "取消", null, new String[]{"批准"}, context, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                showEditDialog(orderId);
                                break;
                        }
                    }
                }).setCancelable(false).show();
            }
        });
        return holder.convertView;
    }

    private void showEditDialog(final int orderId) {
        WritePadDialog writeTabletDialog = new WritePadDialog(
                context, new HandwritingActivity.DialogListener() {
            @Override
            public void refreshActivity(Object object) {

                mSignBitmap = (Bitmap) object;
//                signPath = createFile();
                final File handWriteFile = createFile();
                            /*BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 15;
                            options.inTempStorage = new byte[5 * 1024];
                            Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        commit(handWriteFile, orderId);
                    }
                }).start();
            }
        });
        writeTabletDialog.setCanceledOnTouchOutside(false);
        writeTabletDialog.show();
    }

    private void commit(File file, int orderId) {
//        UpLoadPicResponse upLoadPicResponse = upLoadSign(file, orderId);
//        handWritePath = upLoadPicResponse.getPath();
        ForUpRequest forUpRequest = new ForUpRequest(orderId, "4", null);
        NetUtil.sendRequest(forUpRequest, ForUpResponse.class, ReviewAdapter.this);
    }

    private OkHttpClient client = new OkHttpClient();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

//    private UpLoadPicResponse upLoadSign(File file, int id) throws IOException {
//        String ip = SPUtils.getString(context, SPConstants.IP);
//        String RequestURL = HttpConstants.HOST + ip;
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        builder.addFormDataPart("", id + "");
//        builder.addFormDataPart("imgFileName", file.getName());
//        builder.addFormDataPart("imgContentType", MEDIA_TYPE_PNG + "");
//        builder.addFormDataPart("img", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
//        MultipartBody body = builder.build();
//        Request request = new Request.Builder().url(RequestURL).post(body).build();
//        Response response = client.newCall(request).execute();
//        String responseStr = response.body().string();
//        UpLoadPicResponse upLoadPicResponse = null;
//        if (responseStr != null) {
//            upLoadPicResponse = new Gson().fromJson(responseStr, UpLoadPicResponse.class);
//        }
//        return upLoadPicResponse;
//    }

    /**
     * 创建手写签名文件
     *
     * @return
     */
    private File createFile() {
        ByteArrayOutputStream baos = null;
        File file = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.HANDWRITE_CACHE;
            _path = sign_dir + System.currentTimeMillis() + ".jpg";
            File dir = new File(sign_dir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                file = new File(_path);
                new FileOutputStream(file).write(photoBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    @Override
    public void onSuccess(BaseRequest request, Object response) {
        if (request instanceof ForUpRequest) {
            ForUpResponse forUpResponse = (ForUpResponse) response;
            if (forUpResponse.getResponseCode().getCode() == 200) {
                CommonUtils.showToast(context, forUpResponse.getErrorMsg());
                if ("请求成功".equals(forUpResponse.getErrorMsg())) {
                    if (callFreshListener != null) {
                        callFreshListener.callFresh();
                    }
                }
            }
        }
    }

    @Override
    public void onOther(BaseRequest request, ErrorResponseInfo errorResponseInfo) {

    }

    @Override
    public void onError(BaseRequest request, Exception e) {

    }

    public interface CallFreshListener {
        void callFresh();
    }
}
