package com.hgad.warehousemanager.ui.activity;

import android.view.View;
import android.widget.CompoundButton;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.base.BaseActivity;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.net.BaseRequest;
import com.hgad.warehousemanager.net.BaseResponse;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.SPUtils;
import com.hgad.warehousemanager.view.UISwitchButton;

/**
 * Created by Administrator on 2017/8/7.
 */
public class SystemSettingActivity extends BaseActivity {

    private UISwitchButton voiceButton;
    private UISwitchButton vibratorButton;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_system_setting);
    }

    @Override
    protected void initData() {
        initHeader("系统设置");
        boolean playMusic = SPUtils.getBoolean(this, SPConstants.PLAY_MUSIC);
        boolean vibrator = SPUtils.getBoolean(this, SPConstants.VIBRATOR);
        voiceButton.setChecked(playMusic);
        vibratorButton.setChecked(vibrator);
    }

    @Override
    protected void initView() {
        findViewById(R.id.rl_text_size).setOnClickListener(this);
        voiceButton = (UISwitchButton) findViewById(R.id.sb_voice);
        voiceButton.setOnCheckedChangeListener(onCheckedChangeListener);
        vibratorButton = (UISwitchButton) findViewById(R.id.sb_vibrator);
        vibratorButton.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.sb_voice:
                    SPUtils.put(SystemSettingActivity.this, SPConstants.PLAY_MUSIC, isChecked);
                    break;
                case R.id.sb_vibrator:
                    SPUtils.put(SystemSettingActivity.this, SPConstants.VIBRATOR, isChecked);
                    break;
            }
        }
    };

    @Override
    public void onSuccessResult(BaseRequest request, BaseResponse response) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_text_size:
                showChoose();
                break;
        }
    }


    private void showChoose() {
        new AlertView(null, null, null, null, new String[]{"小", "中", "大"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        setSize(1);
                        break;
                    case 1:
                        setSize(2);
                        break;
                    case 2:
                        setSize(3);
                        break;
                }
            }
        }).setCancelable(true).show();
    }

    private void setSize(int i) {
        SPUtils.put(this, SPConstants.TEXT_SIZE, i);
        CommonUtils.showToast(this, "设置完成后需重启应用才能生效");
    }
}
