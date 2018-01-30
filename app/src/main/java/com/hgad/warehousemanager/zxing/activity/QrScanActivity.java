package com.hgad.warehousemanager.zxing.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.OrderInfo;
import com.hgad.warehousemanager.bean.WareInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.ui.activity.HandOperateActivity;
import com.hgad.warehousemanager.ui.activity.InWareByHandActivity;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.CustomProgressDialog;
import com.kaola.qrcodescanner.qrcode.QrCodeActivity;
import com.kaola.qrcodescanner.qrcode.camera.CameraManager;
import com.kaola.qrcodescanner.qrcode.decode.CaptureActivityHandler;
import com.kaola.qrcodescanner.qrcode.decode.DecodeManager;
import com.kaola.qrcodescanner.qrcode.decode.InactivityTimer;
import com.kaola.qrcodescanner.qrcode.view.QrCodeFinderView;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 二维码扫描类。
 */
public class QrScanActivity extends QrCodeActivity implements Callback, OnClickListener {

    public static final String INTENT_OUT_STRING_SCAN_RESULT = "scan_result";
    private static final String INTENT_IN_INT_SUPPORT_TYPE = "support_type";
    private static final int REQUEST_PERMISSIONS = 1;
    private CaptureActivityHandler mCaptureActivityHandler;
    private boolean mHasSurface;
    private InactivityTimer mInactivityTimer;
    private QrCodeFinderView mQrCodeFinderView;
    private SurfaceView mSurfaceView;
    private View mLlFlashLight;
    private boolean mNeedFlashLightOpen = true;
    private ImageView mIvFlashLight;
    private TextView mTvFlashLightText;
    private ViewStub mSurfaceViewStub;
    private DecodeManager mDecodeManager = new DecodeManager();
    private String type;
    private List<WareInfo> data;
    private OrderInfo orderInfo;
    private MediaPlayer mediaPlayer;
    private String scanState;
    private RadioGroup rg;
    private TextView tv_address;
    private String checkTaskId;
    private ScreenListener screenListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.qr_code_header_black_pic).setOnClickListener(this);
        mIvFlashLight = (ImageView) findViewById(R.id.qr_code_iv_flash_light);
        mTvFlashLightText = (TextView) findViewById(R.id.qr_code_tv_flash_light);
        mQrCodeFinderView = (QrCodeFinderView) findViewById(R.id.qr_code_view_finder);
        mLlFlashLight = findViewById(R.id.qr_code_ll_flash_light);
        mLlFlashLight.setOnClickListener(this);
        mSurfaceViewStub = (ViewStub) findViewById(R.id.qr_code_view_stub);
        findViewById(R.id.qr_code_ll_hand).setOnClickListener(this);
        rg = (RadioGroup) findViewById(R.id.rg_scan);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_scan_tape:
                        scanState = Constants.NO_PLATES;
                        SPUtils.put(QrScanActivity.this, SPConstants.IS_SCAN_PLATE, false);
                        break;
                    case R.id.rb_scan_plate:
                        scanState = Constants.PLATES;
                        SPUtils.put(QrScanActivity.this, SPConstants.IS_SCAN_PLATE, true);
                        break;
                }
            }
        });
//        TextView tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText("扫一扫");
        mHasSurface = false;
        findViewById(R.id.tv_left_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_address = (TextView) findViewById(R.id.tv_cur_address);
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIvFlashLight.setOnClickListener(QrScanActivity.this);
//                tvPic.setOnClickListener(QrScanActivity.this);
            }
        }, 1000);
        initBeepSound();
    }

    private void initData() {
        boolean isScanPlate = SPUtils.getBoolean(this, SPConstants.IS_SCAN_PLATE);
        if (!isScanPlate) {
            ((RadioButton) rg.getChildAt(0)).setChecked(true);
        } else {
            ((RadioButton) rg.getChildAt(1)).setChecked(true);
        }
        CameraManager.init(this);
        mInactivityTimer = new InactivityTimer(this);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constants.TYPE);
        orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
        data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
        String address = intent.getStringExtra(Constants.ADDRESS);
        if (Constants.CHECK.equals(type) && address != null) {
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(CommonUtils.formatAddress(address));
        }
        if (screenListener == null) {
            screenListener = new ScreenListener(this);
        }
        screenListener.register(screenStateLisener);
    }

    private ScreenStateListener screenStateLisener = new ScreenStateListener() {
        @Override
        public void onScreenOn() {

        }

        @Override
        public void onScreenOff() {

        }

        @Override
        public void onUserPresent() {
            surfaceCreated(mSurfaceView.getHolder());
        }
    };

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
            /**
             *
             当媒体源的结束时调用一个回调函数 已达到在播放。
             *
             * @param监听器回调将运行
             */
            mediaPlayer.setOnCompletionListener(null);
            /**
             * 在资源管理入口文件描述符。这提供你自己的
             *
             * 打开FileDescriptor，可以用来读取数据，以及
             *
             * 该项数据在文件中的偏移和长度。
             */
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.scan_success);
            try {
                /**
                 * file.getFileDescriptor() 返回FileDescriptor，可以用来读取的数据文件。
                 *
                 * setDataSource() 设置数据源（FileDescriptor）使用。这是来电者的责任
                 *
                 * 关闭文件描述符。这是安全的，这样做，只要这个呼叫返回。
                 */
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
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS);
        }
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

    private void initCamera() {
        if (null == mSurfaceView) {
            mSurfaceViewStub.setLayoutResource(R.layout.layout_surface_view);
            mSurfaceView = (SurfaceView) mSurfaceViewStub.inflate();
        }
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCaptureActivityHandler != null) {
            try {
                mCaptureActivityHandler.quitSynchronously();
                mCaptureActivityHandler = null;
                mHasSurface = false;
                if (null != mSurfaceView) {
                    mSurfaceView.getHolder().removeCallback(this);
                }
                CameraManager.get().closeDriver();

            } catch (Exception e) {
                // 关闭摄像头失败的情况下,最好退出该Activity,否则下次初始化的时候会显示摄像头已占用.
                finish();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        findViewById(R.id.qr_code_view_background).setVisibility(View.VISIBLE);
        mQrCodeFinderView.setVisibility(View.GONE);
        mDecodeManager.showPermissionDeniedDialog(this);
    }

    @Override
    protected void onDestroy() {
        if (null != mInactivityTimer) {
            mInactivityTimer.shutdown();
        }
        super.onDestroy();
        ReleasePlayer();
        screenListener.unregister();
    }

    /**
     * Handler scan result
     *
     * @param result
     */
    public void handleDecode(Result result) {
        mInactivityTimer.onActivity();
        if (null == result) {
            mDecodeManager.showCouldNotReadQrCodeFromScanner(this, new DecodeManager.OnRefreshCameraListener() {
                @Override
                public void refresh() {
                    restartPreview();
                }
            });
        } else {
            String type = null;
            BarcodeFormat barcodeFormat = result.getBarcodeFormat();
            if (barcodeFormat == BarcodeFormat.QR_CODE) {
                type = Constants.QR;
            } else {
                type = Constants.CODE;
            }
            String resultString = result.getText();
            handleResult(resultString, type);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            if (!CameraManager.get().openDriver(surfaceHolder)) {
                showPermissionDeniedDialog();
                return;
            }
        } catch (IOException e) {
            // 基本不会出现相机不存在的情况
            Toast.makeText(this, getString(R.string.qr_code_camera_not_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        } catch (RuntimeException re) {
            re.printStackTrace();
            showPermissionDeniedDialog();
            return;
        }
        mQrCodeFinderView.setVisibility(View.VISIBLE);
        mLlFlashLight.setVisibility(View.VISIBLE);
        findViewById(R.id.qr_code_view_background).setVisibility(View.GONE);
        turnFlashLightOff();
        if (mCaptureActivityHandler == null) {
            mCaptureActivityHandler = new CaptureActivityHandler(QrScanActivity.this);
        }
    }

    /**
     * 打声音和振动
     */
    private void playBeepSoundAndVibrate() {
        boolean playMusic = SPUtils.getBoolean(this, SPConstants.PLAY_MUSIC);
        if (mediaPlayer != null && playMusic) {
            mediaPlayer.start();
        }
        boolean isVibrator = SPUtils.getBoolean(this, SPConstants.VIBRATOR);
        if (isVibrator) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(Constants.VIBRATE_DURATION);
        }

    }

    private void restartPreview() {
        if (null != mCaptureActivityHandler) {
            try {
                mCaptureActivityHandler.restartPreviewAndDecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }

    public Handler getCaptureActivityHandler() {
        return mCaptureActivityHandler;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.qr_code_ll_flash_light) {
            if (mNeedFlashLightOpen) {
                turnFlashlightOn();
            } else {
                turnFlashLightOff();
            }
        }
        if (i == R.id.qr_code_header_black_pic) {
            showDialog("切换中");
            scanState = Constants.PLATES;
        }
        if (i == R.id.qr_code_ll_hand) {
            Intent intent = null;
            if (Constants.IN_WARE.equals(type)) {
                intent = new Intent(this, HandOperateActivity.class);
                intent.putExtra(Constants.TYPE, type);
            } else {
                if (Constants.CHECK.equals(type)) {
                    setResult(Constants.HAND);
                    finish();
                    return;
                }
                intent = new Intent(this, InWareByHandActivity.class);
                intent.putExtra(Constants.TYPE, type);
                intent.putExtra(Constants.ORDER_INFO, orderInfo);
                intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
            }
            startActivity(intent);
            finish();
        }
    }

    private void showDialog(String content) {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this, content);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null) {
                    customProgressDialog.dismiss();
                }
            }
        }, 1000);
    }

    private void turnFlashlightOn() {
        try {
            CameraManager.get().setFlashLight(true);
            mNeedFlashLightOpen = false;
            mTvFlashLightText.setText(getString(R.string.qr_code_close_flash_light));
            mTvFlashLightText.setTextColor(Color.WHITE);
//            mIvFlashLight.setBackgroundResource(R.drawable.flashlight_turn_off);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void turnFlashLightOff() {
        try {
            CameraManager.get().setFlashLight(false);
            mNeedFlashLightOpen = true;
            mTvFlashLightText.setText(getString(R.string.qr_code_open_flash_light));
            mTvFlashLightText.setTextColor(getResources().getColor(R.color.no_net));
//            mIvFlashLight.setBackgroundResource(R.drawable.flashlight_turn_on);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            int cameraPermission = grantResults[0];
            if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
                initCamera();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSIONS);
            }
        }
    }

    private void handleResult(String resultString, String type) {
        if (TextUtils.isEmpty(resultString)) {
            mDecodeManager.showCouldNotReadQrCodeFromScanner(this, new DecodeManager.OnRefreshCameraListener() {
                @Override
                public void refresh() {
                    restartPreview();
                }
            });
        } else {
            playBeepSoundAndVibrate();
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.RESULT, resultString);
            bundle.putString(Constants.CODE_TYPE, type);
            bundle.putString(Constants.SCAN_STATE, scanState);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    public class ScreenListener {
        private Context mContext;
        private ScreenBroadcastReceiver receiver;
        private ScreenStateListener mScreenStateListener;

        public ScreenListener(Context context) {
            mContext = context;
            receiver = new ScreenBroadcastReceiver();
        }

        public void register(ScreenStateListener screenStateListener) {
            if (screenStateListener != null) {
                mScreenStateListener = screenStateListener;
            }
            if (receiver != null) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_USER_PRESENT);
                mContext.registerReceiver(receiver, filter);
            }
        }

        public void unregister() {
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);
            }
        }


        private class ScreenBroadcastReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (Intent.ACTION_SCREEN_ON.equals(action)) {
                        if (mScreenStateListener != null) {
                            Log.e("zhang", "ScreenBroadcastReceiver --> ACTION_SCREEN_ON");
                            mScreenStateListener.onScreenOn();
                        }
                    } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                        if (mScreenStateListener != null) {
                            Log.e("zhang", "ScreenBroadcastReceiver --> ACTION_SCREEN_OFF");
                            mScreenStateListener.onScreenOff();
                        }
                    } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                        if (mScreenStateListener != null) {
                            Log.e("zhang", "ScreenBroadcastReceiver --> ACTION_USER_PRESENT");
                            mScreenStateListener.onUserPresent();
                        }
                    }
                }
            }
        }
    }

    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();

        void onUserPresent();
    }
}