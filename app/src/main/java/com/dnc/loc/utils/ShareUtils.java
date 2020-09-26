package com.dnc.loc.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.dnc.loc.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by zhaoshuang on 16/8/29.
 * 弹出动画的popupwindow
 */
public class ShareUtils {
//    private static final String TAG = "_TAG_Sharetils";

    private Bitmap mBitmap;
    private Canvas mCanvas;


    public ShareUtils(){
    }

    /**
     * 创建文件
     */
    private void createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                file.createNewFile();
            } else {
                //创建目录之后再创建文件
                createDir(new File(file.getParentFile().getAbsolutePath()));
                file.createNewFile();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 创建目录
     */
    private void createDir(File file) {
        //因为文件夹可能有多层，比如:  a/b/c/ff.txt  需要先创建a文件夹，然后b文件夹然后...
        try {
            if (file.getParentFile().exists()) {
                file.mkdir();
            } else {
                createDir(new File(file.getParentFile().getAbsolutePath()));
                file.mkdir();
            }
        } catch (Exception e) {
        }
    }


    /**
     * 保存图片到图库
     * @param bmp
     */
    private String saveImageToGallery(Context context, Bitmap bmp, String prefix, Boolean isToast ) {

        // 首先保存图片
        String packageName = context.getPackageName().replace(".test", "");
        File appDir = new File(Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_PICTURES + "/" + packageName );
        if (!appDir.exists()) {
            createDir( appDir );
        }
        String fileName = prefix + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //bmp.compress(Bitmap.CompressFormat.PNG, 50, fos);
            fos.flush();
            fos.close();
            if(isToast)
                Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            if(isToast)
                Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            if(isToast)
                Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show();
            try {
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e1) {
            }
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), "image: " + file.getName());
//            if(isToast)
//                Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            if(isToast)
//                Toast.makeText(context, R.string.save_fail, Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }

        // 最后通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
        } else {
            //4.4开始不允许发送"Intent.ACTION_MEDIA_MOUNTED"广播, 否则会出现:
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }

        return appDir.getAbsolutePath() + "/" + fileName;
    }


    /**
     * 截屏
     */
    public ShareUtils captureWindow(View mView) {
        if(mView != null) {
            mBitmap = Bitmap.createBitmap(
                    mView.getMeasuredWidth(),
                    mView.getMeasuredHeight(),
//                    Bitmap.Config.ARGB_8888
                    Bitmap.Config.RGB_565
            );
            mCanvas = new Canvas(mBitmap);
        }

        if(mCanvas != null) {
            mView.draw(mCanvas);
            mCanvas.save();
            mCanvas.restore();
        }

        return this;
    }

    /**
     * 保存照片
     */
    public String savePic(Context context, String prefix, Boolean isToast) {
        String picPath = "";
        try {
            if(mBitmap != null) {
                // 保存图片到图库
                picPath = saveImageToGallery(context, mBitmap, prefix, isToast);
                //LogUtil.i(TAG, "picPath " + picPath)
                mBitmap.recycle();
            }
        } catch (Exception e) {
        }
        return picPath;
    }

    /**
     * 分享照片
     */
    public Intent sharePhoto(Context context, String picPath) {
        File file = new File(picPath);
        Uri uri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 当前包名可能是测试包名(如com.dnc.loc.test)
                String packageName = context.getPackageName().replace(".test", "");
                uri = FileProvider.getUriForFile( context, packageName + ".fileprovider", file );
            } else {
                uri = Uri.fromFile(file);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.putExtra(Intent.EXTRA_OUTPUT, url)
        shareIntent.setType("image/jpeg");
        return shareIntent;
    }

    /**
     * 分享文本
     */
//    public Intent shareText(String msg )  {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, msg);
//        return shareIntent;
//   }

 }


