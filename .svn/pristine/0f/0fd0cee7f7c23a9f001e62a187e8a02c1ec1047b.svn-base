package com.dnc.loc.ui.scan;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.zxing.Result;

public class ImageScanningTask extends AsyncTask<Uri, Void, Result> {
    private Uri uri;
    private ImageScanningCallback callback;

    public ImageScanningTask(Uri uri, ImageScanningCallback callback) {
        this.uri = uri;
        this.callback = callback;
    }

    @Override
    protected Result doInBackground(Uri... params) {
        return CodeScanningUtil.scanImage(uri);
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        callback.onFinishScanning(result);
    }

    public interface ImageScanningCallback {
        void onFinishScanning(Result result);
    }
}
