package com.dnc.loc.ui.aty;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dnc.loc.BuildConfig;
import com.dnc.loc.R;
import com.dnc.loc.constant.RouteConst;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.databinding.ActivityMainBinding;
import com.dnc.loc.ui.dialog.PromptDialog;
import com.dnc.loc.ui.fm.create.WalletFm;
import com.dnc.loc.utils.RxUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.zlw.base.ui.aty.BaseActivity;
import com.zlw.base.ui.vm.BaseViewModel;

import java.io.File;

@Route(path = RouteConst.AV_MAIN)
public class MainActivity extends BaseActivity<BaseViewModel, ActivityMainBinding> {

    private static final String TAG = "_TAG_MainActivity";

    //用withString传参可能收不到
    public static String from;

    private DownloadManager manager;
    private long downLoadId;
    private int requestPermissionsTime;
    private PromptDialog warnPermissionsDialog;
    private PromptDialog updateDialog;
    private PromptDialog navDialog;

    private FragmentTransaction transaction;
    private CompleteReceiver completeReceiver;

    private WalletFm walletFm;
    private WalletFm indexFm;
    private WalletFm appFm;
    private WalletFm balanceFm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initView();

        completeReceiver = new CompleteReceiver();
        /** register download success broadcast **/
        registerReceiver(completeReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        getVersion();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        walletFm = new WalletFm();
        FragmentUtils.add(getSupportFragmentManager(), walletFm, R.id.container);
        binding.bottomView.check(binding.navigation1.getId());

        binding.bottomView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = getSupportFragmentManager().beginTransaction();
                switch(checkedId) {
                    case R.id.navigation_1:
                        hideOtherFra(transaction);
                        if (null == walletFm) {
                            walletFm = new WalletFm();
                            transaction.add(R.id.container, walletFm);
                        }
                        transaction.show(walletFm).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_2:
                        hideOtherFra(transaction);
                        if (null == indexFm) {
                            indexFm = new WalletFm();
                            transaction.add(R.id.container, indexFm);
                        }
                        transaction.show(walletFm).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_3:
                        hideOtherFra(transaction);
                        if (null == appFm) {
                            appFm = new WalletFm();
                            transaction.add(R.id.container, appFm);
                        }
                        transaction.show(appFm).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_4:
                        hideOtherFra(transaction);
                        if (null == balanceFm) {
                            balanceFm = new WalletFm();
                            transaction.add(R.id.container, balanceFm);
                        }
                        transaction.show(balanceFm).commitAllowingStateLoss();
                        break;
                }
            }
        });

        binding.navigation2.setOnTouchListener((v, event) -> {
            navDialog();
            return true;
        });
        binding.navigation3.setOnTouchListener((v, event) -> {
            navDialog();
            return true;
        });
        binding.navigation4.setOnTouchListener((v, event) -> {
            navDialog();
            return true;
        });

//        if (checkPermission(this, false)) {
//        } else {
//            checkUpdate()
//        }
    }

    /**
     * 初始化 Fragment
     */
    private void initFragments() {
    }

    private void hideOtherFra(FragmentTransaction tran) {

        if (walletFm != null) {
            tran.hide(walletFm);
        }
        if (indexFm != null) {
            tran.hide(indexFm);
        }
        if (appFm != null) {
            tran.hide(appFm);
        }
        if (balanceFm != null) {
            tran.hide(balanceFm);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(completeReceiver);
    }

    @Override
    public boolean canBeSlideBack() {
        return false;
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }
    
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        initData();
//        initView();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(int indext=0;indext<fragmentManager.getFragments().size();indext++)
        {
            Fragment fragment=fragmentManager.getFragments().get(indext); //找到第一层Fragment
            if(fragment==null)
                Log.w(TAG, "Activity result no fragment exists for index: 0x"
                        + Integer.toHexString(requestCode));
            else
                handleResult(fragment,requestCode,resultCode,data);
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment fragment,int requestCode,int resultCode,Intent data) {
        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
//        Log.e(TAG, "MyBaseFragmentActivity");
//        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
//        if(childFragment!=null)
//            for(Fragment f:childFragment)
//                if(f != null){
//                    handleResult(f, requestCode, resultCode, data);
//                }
//        if(childFragment==null)
//            Log.e(TAG, "MyBaseFragmentActivity1111");
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Long clickTime = 0L; //记录第一次点击的时间

    private void exit() {
        if (System.currentTimeMillis() - clickTime > 2000) {
            ToastUtils.showShort("再次点击退出");
            clickTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void navDialog() {
        if (navDialog != null){
            navDialog.dismiss();
        }
        navDialog = new PromptDialog(this, R.style.prompt_dialog);
        navDialog.showTextDialog(getString(R.string.prompt_title), getString(R.string.prompt_jqqd));
    }

    @SuppressLint("CheckResult")
    private void getVersion() {
        if (updateDialog != null){
            updateDialog.dismiss();
        }
        updateDialog = new PromptDialog(this, R.style.prompt_dialog);

        EosHttp.app.getVersion(1, 5, "version", "").compose(RxUtils.observableAsync()).subscribe(version -> {
            if (version != null && version.version != null && version.url != null && version.info != null) {
                int s2 = 0;
                if (version.version.contains(".")) {
                    s2 = Integer.parseInt(version.version.replace(".", ""));
                } else {
                    s2 = Integer.parseInt(version.version);
                }
                int s1 = Integer.parseInt(BuildConfig.VERSION_NAME.replace(".", ""));
                if (s1 < s2) {
                    String content = "当前版本V" + BuildConfig.VERSION_NAME + "，检查到新版本V" + version.version + "，是否更新为最新版本";
                    updateDialog.showTextDialog(getString(R.string.check_update), content,
                            getString(R.string.update), getString(R.string.update_cancel),
                            v -> {
                                updateDialog.dismiss();
                            }, v1 -> {
                                updateDialog.dismiss();
                                downLoadApk(this, version.url, getResources().getString(R.string.app_name) + getString(R.string.update_download), getResources().getString(R.string.app_name) + getString(R.string.update_download));
                            });
                }
            }
            //onCompleted();
        }, e -> {
            //onCompleted();
        });
    }


    /**
     * 该方法是调用了系统的下载管理器
     */
    public void downLoadApk(Context c, String url, String title, String des) {
        /**
         * 在这里返回的 reference 变量是系统为当前的下载请求分配的一个唯一的ID，
         * 我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作
         * 或者查询下载的状态以及取消下载等等
         */            //启动下载,该方法返回系统为当前下载请求分配的一个唯一的ID
        AndPermission.with(c)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    File apkFile = new File(Environment.getExternalStorageDirectory() + "/dnc.apk");
                    if (apkFile.exists()) apkFile.delete();
                    Uri uri = Uri.parse(url);        //下载连接
                    manager = (DownloadManager) c.getSystemService(c.DOWNLOAD_SERVICE);  //得到系统的下载管理
                    DownloadManager.Request requestApk = new DownloadManager.Request(uri);  //得到连接请求对象
                    requestApk.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);   //指定在什么网络下进行下载，这里我指定了WIFI网络
                    requestApk.setDestinationInExternalPublicDir("", "dnc.apk");  //制定下载文件的保存路径，我这里保存到根目录
                    requestApk.setVisibleInDownloadsUi(true);  //设置显示下载界面
                    requestApk.allowScanningByMediaScanner();  //表示允许MediaScanner扫描到这个文件，默认不允许。
                    requestApk.setTitle(title);      //设置下载中通知栏的提示消息
                    requestApk.setDescription(des);//设置设置下载中通知栏提示的介绍
                    downLoadId = manager.enqueue(requestApk);
                }).onDenied(permissions -> {
            requestPermissionsTime++;
            if (requestPermissionsTime < 3) {
                downLoadApk(c, url, title, des);
            } else {
                if (warnPermissionsDialog == null) {
                    warnPermissionsDialog = new PromptDialog(this, R.style.prompt_dialog);
                }
                warnPermissionsDialog.showTextDialog(c.getString(R.string.request_permission_prompt), c.getString(R.string.to_update_you_need_to_use), v -> {
                    warnPermissionsDialog.dismiss();
                }, v1 -> {
                    downLoadApk(c, url, title, des);
                    warnPermissionsDialog.dismiss();
                });
            }
        }).start();
    }

    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downLoadId == completeDownloadId) {
                if (manager != null) {
//                    Uri uri = manager.getUriForDownloadedFile(completeDownloadId);
//                    getActivity().startActivity(IntentUtils.getInstallAppIntent(uri.getPath()));
                    File apkFile = new File(Environment.getExternalStorageDirectory() + "/dnc.apk");
                    if (apkFile.exists()) {
                        startActivity(IntentUtils.getInstallAppIntent(apkFile));
                    } else {
                        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    }
                }
            }
        }
    }
}