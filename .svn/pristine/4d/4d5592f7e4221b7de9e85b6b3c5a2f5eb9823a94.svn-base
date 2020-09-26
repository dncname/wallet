package com.zlw.base.ui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.yanzhenjie.permission.AndPermission;
import com.zlw.base.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/12/27 0027.
 */
public class PhotosHelper {
    public static final int ACTION_PHOTOGRAPHVIEW_CHOOSE = 1;
    public static final int ACTION_ALBUMVIEW_CHOOSE = 2;
    public static final int ACTION_CROP = 3;
    private static final int REQUEST_CODE_CHOOSE = 11;

    private Context c;
    private Dialog dialog;
    private View.OnClickListener clickPhotograph;
    private View.OnClickListener clickAlbum;
    public File tempFile;
    public List<String> imageIds = new ArrayList<>();

    public PhotosHelper(Context c, View.OnClickListener clickPhotograph, View.OnClickListener clickAlbum) {
        this.c = c;
        this.clickPhotograph = clickPhotograph;
        this.clickAlbum = clickAlbum;
        tempFile = new File(c.getExternalCacheDir() + "/temp.jpg");
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
//        AndPermission.with(c)
//                .runtime()
//                .permission(Permission.Group.STORAGE)
//                .onGranted(permissions -> {
//                    // Storage permission are allowed.
//                })
//                .onDenied(permissions -> {
//                    // Storage permission are not allowed.
//                })
//                .start();
    }

    public void showSelect() {
        if (dialog == null) {
            dialog = new Dialog(c, R.style.dialog_card);
            dialog.setCanceledOnTouchOutside(true);
//            CardViewBindingAdapter
            CardView cardView = new CardView(c);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout layout = new LinearLayout(c);
            layout.setLayoutParams(new LinearLayout.LayoutParams(SizeUtils.dp2px(280), ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView txtPhotograph = new TextView(c);
            txtPhotograph.setLayoutParams(lp);
            txtPhotograph.setPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), 0, SizeUtils.dp2px(15));
            txtPhotograph.setText(R.string.photograph);
            txtPhotograph.setBackground(c.getResources().getDrawable(R.drawable.selector_btn_white0dp));
            txtPhotograph.setTextSize(SizeUtils.px2dp(36));
            txtPhotograph.setTextColor(c.getResources().getColor(R.color.text_black_222222));
            txtPhotograph.setClickable(true);
            txtPhotograph.setOnClickListener(clickPhotograph);
            layout.addView(txtPhotograph);

            View line = new View(c);
            line.setLayoutParams(new ViewGroup.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)));
            line.setBackgroundColor(c.getResources().getColor(R.color.day_white));
            layout.addView(line);

            TextView txtFromAlbum = new TextView(c);
            txtFromAlbum.setLayoutParams(lp);
            txtFromAlbum.setPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), 0, SizeUtils.dp2px(15));
            txtFromAlbum.setText(R.string.Select_from_the_album);
            txtFromAlbum.setBackground(c.getResources().getDrawable(R.drawable.selector_btn_white0dp));
            txtFromAlbum.setTextSize(SizeUtils.px2dp(36));
            txtFromAlbum.setTextColor(c.getResources().getColor(R.color.text_black_222222));
            txtFromAlbum.setClickable(true);
            txtFromAlbum.setOnClickListener(clickAlbum);
            layout.addView(txtFromAlbum);
            cardView.addView(layout);
            dialog.setContentView(cardView);
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissSelect() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void openCamera(Activity aty) {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, AndPermission.getFileUri(aty, new File(c.getExternalCacheDir() + "/temp.jpg")));
        aty.startActivityForResult(takeIntent, ACTION_PHOTOGRAPHVIEW_CHOOSE);
        dismissSelect();
    }

    public void openAlbum(Activity aty) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        aty.startActivityForResult(intent, ACTION_ALBUMVIEW_CHOOSE);
        dismissSelect();
    }

    public void openCrop(Activity aty, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);

        intent .putExtra("scale", true);//黑边
        intent .putExtra("scaleUpIfNeeded", true);//黑边

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Uri outputUri = Uri.fromFile(new File(c.getExternalCacheDir() + "/avatar.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

//        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        aty.startActivityForResult(intent, ACTION_CROP);
    }

    public void onActivityResultWithCrop(Activity aty, int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ACTION_ALBUMVIEW_CHOOSE:
                if (data != null) {
                    openCrop(aty, data.getData());
                }
                break;
            case ACTION_PHOTOGRAPHVIEW_CHOOSE:
//                    File file = new File(c.getExternalCacheDir() + "/temp.jpg"); // 根据路径获取数据
                openCrop(aty, AndPermission.getFileUri(aty, new File(c.getExternalCacheDir() + "/temp.jpg")));
                break;
        }
    }

    public void postPhotos(File file) {
    }

    public void onDestroy() {
        c = null;
        dialog = null;
        clickAlbum = null;
        clickPhotograph = null;
        imageIds = null;
    }

    public static void compressBitmapFileToFile(File infile, File outFile) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(infile.getPath(), opts);
        compressBmpToFile(bitmap, outFile);
    }

    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);

        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            if (options <= 10) {
                bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
                break;
            }

            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
//            bmp.recycle();
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
//
        }

    }
}
