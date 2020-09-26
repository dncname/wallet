package com.dnc.loc.ui.aty;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.journeyapps.barcodescanner.CaptureManager;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.databinding.ActivityScanBinding;
import com.dnc.loc.ui.scan.ImageScanningTask;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

@Route(path = RouteConst.AV_SCAN)
public class ScanAty extends BaseActivity<BaseViewModel, ActivityScanBinding> {
    private static final String TAG = "_TAG_ScanAty";

    private static final int REQUEST_CODE_PICK_IMAGE = 11;
    public static final int RESULT_CODE_PICK_IMAGE = 11;
    private CaptureManager capture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setAppBarView(binding.appbar);

        binding.ivBack.setOnClickListener(view -> onBackPressed());
        capture = new CaptureManager(this, binding.bv);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        binding.btnLocalQr.setOnClickListener(v -> pickImage());
//        binding.bv.setTorchOn();
//        binding.bv.setTorchOff();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return binding.bv.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (Activity.RESULT_OK == resultCode && null != data) {
                    Uri uri = data.getData();
                    ImageScanningTask scanningTask = new ImageScanningTask(uri,
                                   result -> {
                                       if (result != null) {
                                           Intent intent = new Intent();
                                           intent.putExtra("result", result.getText());
                                           setResult(RESULT_CODE_PICK_IMAGE, intent);
                                           //LogUtils.e(TAG, "识别成功: "+ result.getText());
                                           finish();
                                       } else {
                                           // 识别失败
                                            //LogUtils.e(TAG, "识别失败: " );
                                       }
                                   });
                    scanningTask.execute();
                } else {
                    // 什么也没有
                }
                break;
        }
    }
//    小结：
//    看到选中图片后会得到图片uri，塞给异步ImageScanningTask去解析。解析完之后回回调onFinishScanning()，将result传给启动扫描器的activity。
//
//            2.ImageScanningTask
//    它是一个AsyncTask，在doInBackground()中做从uri解析出二维码result出来的操作。


}
