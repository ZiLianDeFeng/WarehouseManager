package com.hgad.warehousemanager.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.view.WritePadDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HandwritingActivity extends AppCompatActivity {

    private Bitmap mSignBitmap;
    private String signPath;
    private ImageView ivSign;
    private TextView tvSign;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting);
        setTitle("欢迎使用手写签名");
        ivSign =(ImageView)findViewById(R.id.iv_sign);
        tvSign = (TextView)findViewById(R.id.tv_sign);

        ivSign.setOnClickListener(signListener);
        tvSign.setOnClickListener(signListener);
    }
    public interface DialogListener {

        public void refreshActivity(Object object);

    }

    private View.OnClickListener signListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            WritePadDialog writeTabletDialog = new WritePadDialog(
                    HandwritingActivity.this, new DialogListener() {
                @Override
                public void refreshActivity(Object object) {

                    mSignBitmap = (Bitmap) object;
                    signPath = createFile();
                            /*BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 15;
                            options.inTempStorage = new byte[5 * 1024];
                            Bitmap zoombm = BitmapFactory.decodeFile(signPath, options);*/
                    ivSign.setImageBitmap(mSignBitmap);
                    tvSign.setVisibility(View.GONE);
                }
            });
            writeTabletDialog.show();
        }
    };

    /**
     * 创建手写签名文件
     *
     * @return
     */
    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory() + File.separator;
            _path = sign_dir + System.currentTimeMillis() + ".jpg";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
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
        return _path;
    }
}
