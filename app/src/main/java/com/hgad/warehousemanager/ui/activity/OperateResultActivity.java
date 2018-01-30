package com.hgad.warehousemanager.ui.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
public class OperateResultActivity extends BaseActivity {

    private static final int IN_SCAN = 199;
    private String type;
    private OrderInfo orderInfo;
    private List<WareInfo> data;
    private String address;
    private TextView tv_people;
    private TextView tv_time;
    private MediaPlayer mediaPlayer;
    private LinearLayout ll_address;
    private TextView tv_address;
    private Button btn_back;
    private Button btn_continue;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_operate_result);
    }

    @Override
    protected void initData() {
        playBeepSoundAndVibrate();
        String userName = SPUtils.getString(this, SPConstants.USER_NAME);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        if (Constants.IN_WARE.equals(type)) {
            initHeader("入库结果");
        } else if (Constants.OUT_TYPE.equals(type)) {
            initHeader("出库结果");
        } else if (Constants.REVIEW_TYPE.equals(type)) {
            initHeader("复核结果");
        } else if (Constants.CHANGE_WARE.equals(type)) {
            initHeader("移位结果");
        } else if (Constants.CHECK.equals(type)) {
            initHeader("盘点结果");
            address = intent.getStringExtra(Constants.ADDRESS);
            ll_address.setVisibility(View.VISIBLE);
            String formatAddress = CommonUtils.formatAddress(address);
            tv_address.setText(formatAddress);
            btn_back.setText("调整货位");
            btn_back.setTextColor(Color.WHITE);
            btn_back.setBackgroundColor(getResources().getColor(R.color.dark));
            btn_continue.setText("扫描下一号");
        }
        orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
        tv_people.setText(userName);
        tv_time.setText(CommonUtils.getCurrentTime());
    }

    @Override
    protected void initView() {
        initBeepSound();
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(backListener);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(continueListener);
        tv_people = (TextView) findViewById(R.id.tv_opera_people);
        tv_time = (TextView) findViewById(R.id.tv_opera_time);
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        tv_address = (TextView) findViewById(R.id.tv_opera_address);
    }

    /**
     * 打声音和振动
     */
    private void playBeepSoundAndVibrate() {
        boolean playMusic = SPUtils.getBoolean(this, SPConstants.PLAY_MUSIC);
        if (mediaPlayer != null && playMusic) {
            mediaPlayer.start();
        }
    }

    /**
     * 初始化 报警音频
     */
    private void initBeepSound() {
        if (mediaPlayer == null) {
            // 在stream_system音量不可调的，用户发现它太大声，所以我们现在播放的音乐流。
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            // 初始化 媒体播放器
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(null);
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.ope_success);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());

                // 关闭 资源文件管理器
                file.close();
                mediaPlayer.setVolume(30f, 30f);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null; // 异常 释放播放器对象
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReleasePlayer();
    }

    /**
     * 释放播放器资源
     */
    private void ReleasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Constants.OVER);
            sendBroadcast(intent);
            finish();
        }
    };

    private View.OnClickListener continueListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Constants.CHECK.equals(type)) {
                Intent intent = new Intent(Constants.NEXT);
                sendBroadcast(intent);
                finish();
                return;
            }
//            Intent intent = new Intent(OperateResultActivity.this, ScannerActivity.class);
//            intent.putExtra(Constants.TYPE, type);
//            if (orderInfo != null && data != null) {
//                intent.putExtra(Constants.ORDER_INFO, orderInfo);
//                intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
//            }
//            if (address != null) {
//                intent.putExtra(Constants.ADDRESS, address);
//            }
//            startActivity(intent);
//            finish();
            Intent intent = new Intent();
            intent.putExtra(Constants.TYPE, type);
            if (orderInfo != null && data != null) {
                intent.putExtra(Constants.ORDER_INFO, orderInfo);
                intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
            }
            if (address != null) {
                intent.putExtra(Constants.ADDRESS, address);
            }
            setResult(Constants.RESULT_OK, intent);
            finish();
        }
    };

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {

    }
}
