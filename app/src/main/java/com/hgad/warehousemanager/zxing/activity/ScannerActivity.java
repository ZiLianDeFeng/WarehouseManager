package com.hgad.warehousemanager.zxing.activity;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/8/25.
 */
public class ScannerActivity extends AppCompatActivity{
//public class ScannerActivity extends AppCompatActivity implements QRCodeView.Delegate {
//
//    private static final int REQUEST_CODE_CAMERA = 999;
//    private static final String TAG = ScannerActivity.class.getSimpleName();
//    private String type;
//    private List<WareInfo> data;
//    private OrderInfo orderInfo;
//
//    private QRCodeView mQRCodeView;
//    private MediaPlayer mediaPlayer;
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scanner);
//        Intent intent = getIntent();
//        type = intent.getStringExtra(Constants.TYPE);
//        orderInfo = (OrderInfo) intent.getSerializableExtra(Constants.ORDER_INFO);
//        data = (List<WareInfo>) intent.getSerializableExtra(Constants.LIST_DATA);
//        mQRCodeView = (ZBarView) findViewById(R.id.zbarview);
//        mQRCodeView.setDelegate(this);
//        btnLight = (Button) findViewById(R.id.btn_light);
//        findViewById(R.id.tv_left_title).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        initBeepSound();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mQRCodeView.startCamera();
//        mQRCodeView.showScanRect();
////        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
//        mQRCodeView.startSpot();
//
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mQRCodeView.stopCamera();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mQRCodeView.onDestroy();
//        ReleasePlayer();
//    }
//
//    /**
//     * 释放播放器资源
//     */
//    private void ReleasePlayer() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//
//    }
//
//    //震动器
//    private void vibrate() {
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(100);
//    }
//
//    /**
//     * 初始化 报警音频
//     */
//    private void initBeepSound() {
//        if (mediaPlayer == null) {
//            // 在stream_system音量不可调的，用户发现它太大声，所以我们现在播放的音乐流。
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            // 初始化 媒体播放器
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            /**
//             *
//             当媒体源的结束时调用一个回调函数 已达到在播放。
//             *
//             * @param监听器回调将运行
//             */
//            mediaPlayer.setOnCompletionListener(null);
//            /**
//             * 在资源管理入口文件描述符。这提供你自己的
//             *
//             * 打开FileDescriptor，可以用来读取数据，以及
//             *
//             * 该项数据在文件中的偏移和长度。
//             */
//            AssetFileDescriptor file = getResources().openRawResourceFd(
//                    R.raw.scan_success);
//            try {
//                /**
//                 * file.getFileDescriptor() 返回FileDescriptor，可以用来读取的数据文件。
//                 *
//                 * setDataSource() 设置数据源（FileDescriptor）使用。这是来电者的责任
//                 *
//                 * 关闭文件描述符。这是安全的，这样做，只要这个呼叫返回。
//                 */
//                mediaPlayer.setDataSource(file.getFileDescriptor(),
//                        file.getStartOffset(), file.getLength());
//
//                // 关闭 资源文件管理器
//                file.close();
//                mediaPlayer.setVolume(30f, 30f);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null; // 异常 释放播放器对象
//            }
//        }
//    }
//
//    /**
//     * 打声音和振动
//     */
//    private void playBeepSoundAndVibrate() {
//        boolean playMusic = SPUtils.getBoolean(this, SPConstants.PLAY_MUSIC);
//        if (mediaPlayer != null && playMusic) {
//            mediaPlayer.start();
//        }
//        boolean isVibrator = SPUtils.getBoolean(this, SPConstants.VIBRATOR);
//        if (isVibrator) {
//            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            vibrator.vibrate(Constants.VIBRATE_DURATION);
//        }
//
//    }
//
//    @Override
//    public void onScanQRCodeSuccess(String result) {
//        playBeepSoundAndVibrate();
//        mQRCodeView.stopSpot();
//        if (!TextUtils.isEmpty(result)) {
//            mQRCodeView.stopCamera();
//            mQRCodeView.onDestroy();
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putString("result", result);
//            resultIntent.putExtras(bundle);
//            this.setResult(RESULT_OK, resultIntent);
//            finish();
//        } else {
//            Toast.makeText(this, "链接无效,请重新扫描", Toast.LENGTH_SHORT).show();
//            mQRCodeView.startSpot();
//        }
//    }
//
//    @Override
//    public void onScanQRCodeOpenCameraError() {
//        Log.e(TAG, "无相机权限,打开相机出错");
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
//    }
//
//    public void pickPictureFromAblum(View v) {
////        Intent mIntent = new Intent(
////                Intent.ACTION_PICK,
////                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////
////        startActivityForResult(mIntent, 1);
//        Intent intent = null;
//        if (Constants.IN_WARE.equals(type)) {
//            intent = new Intent(this, HandOperateActivity.class);
//            intent.putExtra(Constants.TYPE, type);
//        } else {
//            if (Constants.CHECK.equals(type)) {
//                setResult(Constants.HAND);
//                finish();
//                return;
//            }
//            intent = new Intent(this, InWareByHandActivity.class);
//            intent.putExtra(Constants.TYPE, type);
//            intent.putExtra(Constants.ORDER_INFO, orderInfo);
//            intent.putExtra(Constants.LIST_DATA, ((Serializable) data));
//        }
//        startActivity(intent);
//        finish();
//    }
//
//    private int ifOpenLight = 0;//判断是否开启闪光灯
//    private Button btnLight;
//
//    // 是否开启闪光灯
//    public void IfOpenLight(View v) {
//        ifOpenLight++;
//
//        switch (ifOpenLight % 2) {
//            case 0:
//                //关闪光灯
//                mQRCodeView.closeFlashlight();
//                btnLight.setText(getString(R.string.str_open_light));
//                break;
//            case 1:
//                //开闪光灯
//                mQRCodeView.openFlashlight();
//                btnLight.setText(getString(R.string.str_close_light));
//                break;
//            default:
//                break;
//        }
//    }
}