package com.dnc.loc.ui.scan;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.blankj.utilcode.util.LogUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zlw.base.ui.utils.LifecycleCallbacks;

import java.io.IOException;

public class CodeScanningUtil {
    private static final String TAG = "_TAG_CodeScanningUtil";
    
    public static Result scanImage(Uri uri) {
        Result result = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(LifecycleCallbacks.currentActivity().getApplication().getContentResolver(), uri);
            bitmap = getSmallerBitmap(bitmap);
            if (bitmap != null) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                Reader reader = new QRCodeReader();
                try {
                    result = reader.decode(binaryBitmap);
                } catch (NotFoundException | ChecksumException | com.google.zxing.FormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //LogUtils.e(TAG, "scanImage: "+ result);
        return result;
    }

    private static Bitmap getSmallerBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int size = bitmap.getWidth() * bitmap.getHeight() / 160000;
            if (size <= 1) {
                return bitmap;
            } else {
                Matrix matrix = new Matrix();
                matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        }
        return null;
    }
}
