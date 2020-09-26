package com.dnc.loc.data.eos;

import android.annotation.SuppressLint;

import com.dnc.loc.blockchain.cypto.ec.EcDsa;
import com.dnc.loc.blockchain.cypto.ec.EcSignature;
import com.dnc.loc.blockchain.cypto.ec.EosPrivateKey;
import com.dnc.loc.blockchain.cypto.types.TypeChainId;
import com.dnc.loc.data.eos.model.ChainBlock;
import com.dnc.loc.data.eos.model.ChainInfo;
import com.dnc.loc.data.eos.model.TransactionPushRequest;
import com.dnc.loc.data.eos.model.TransactionRequest;
import com.dnc.loc.data.eos.model.TransferMessageInfo;
import com.dnc.loc.data.http.EosHttp;
import com.dnc.loc.utils.GsonUtils;
import com.dnc.loc.utils.RxUtils;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;
import com.zlw.base.ui.utils.LifecycleCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 一、序列化转账交易的 json
 * 二、获取 EOS 区块链的最新区块号
 * 三、获取最新区块的具体信息
 * 四、创建一个trasaction
 * 五、获取交易费用
 * 六、签名并推送交易
 */
public class EosTransferManger {
    private static final String TAG = "_TAG_EosTransferManger";

    private static EosTransferManger INSTANCE;

    public static EosTransferManger getInstance() {
        if (INSTANCE == null) INSTANCE = new EosTransferManger();
        return INSTANCE;
    }


    private String creator = "";
    private String creatorPriKey = "";

    public EosTransferManger setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public EosTransferManger setCreatorPriKey(String priKey) {
        this.creatorPriKey = priKey;
        return this;
    }

    //格式化数据
    @SuppressLint("CheckResult")
    public void getJson2Bin(String action, String memo, String to, String quantity, String from, Callback callback) {
        //Sample   mome = "测试转账"    to="eosio"   quantity="0.0100 EOS"  from="test"
        TransferMessageInfo info = new TransferMessageInfo(memo, to, quantity, from);
        String message = GsonUtils.getInstance().toJson(info);
        JsonObject obj = GsonUtils.getInstance().fromJson(message, JsonObject.class);
        JsonObject object = new JsonObject();
        object.addProperty("action", "transfer");
        object.addProperty("code", action);//.token
        object.add("args", obj);
//        LogUtils.e(TAG, " Param_1: " + object.toString() );
        EosHttp.app.json2bean(object).compose(RxUtils.observableAsync()).subscribe(o -> {
            if (o.has("binargs")) {
//                LogUtils.e(TAG, " Process_1: " + o.toString() );
                callback.onback(true, o.get("binargs").getAsString());
//                getChainInfo(action, o.get("binargs").getAsString());
            } else {
//                ToastUtils.showShort("序列化数据失败");
                callback.onback(false, "序列化数据失败");
            }
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "序列化数据失败" + throwable.getMessage());
            if (LifecycleCallbacks.currentActivity() != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("JsonObject", object.toString());
                map.put("Error", throwable.getMessage());
                MobclickAgent.onEvent(LifecycleCallbacks.currentActivity(), "GetJson2Bin", map);
            }
        });
    }

    //获取区块链状态
    @SuppressLint("CheckResult")
    private void getChainInfo(String action, String data, Callback callback) {
        EosHttp.app.getChainInfo().compose(RxUtils.observableAsync()).subscribe(o -> {
//            LogUtils.e(TAG, " Process_2: " + o.toString() );
            callback.onback(true, o);
//            getChainBlock(action, data, o);
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "获取最新区块号失败" + throwable.getMessage());

            if (LifecycleCallbacks.currentActivity() != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Error", throwable.getMessage());
                MobclickAgent.onEvent(LifecycleCallbacks.currentActivity(), "GetChainInfo", map);
            }
        });
    }

    //
    @SuppressLint("CheckResult")
    private void getChainBlock(String strAction, String data, ChainInfo info, Callback callback) {
//        LogUtils.e(TAG, " Param_3: " + String.valueOf(info.head_block_num) );
        EosHttp.app.getChainBlock(String.valueOf(info.head_block_num)).compose(RxUtils.observableAsync()).subscribe(o -> {
//            createTransactionRe(o, data);
//            LogUtils.e(TAG, " Process_3: " + o.toString() );
            callback.onback(true, createTransactionRe(strAction, o, data));
//            getRequiredFee(info, createTransactionRe(strAction, o, data));
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "最新区块的信息失败" + throwable.getMessage());

            if (LifecycleCallbacks.currentActivity() != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ChainInfo", info.toString());
                map.put("Error", throwable.getMessage());
                MobclickAgent.onEvent(LifecycleCallbacks.currentActivity(), "GetChainBlock", map);
            }

//            ToastUtils.showShort("最新区块的信息失败");
        });
    }

    @SuppressLint("CheckResult")
    private void getRequiredFee(ChainInfo info, TransactionRequest transactionRe, Callback callback) {
        JsonObject object = new JsonObject();
        object.add("transaction", GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(transactionRe), JsonObject.class));
//        LogUtils.e(TAG, " Param_4: " + object.toString() );
        EosHttp.app.getRequiredFee(object).compose(RxUtils.observableAsync()).subscribe(o -> {
//            createTransactionRe(o, data);
            transactionRe.fee = o.required_fee;

//            JsonObject oTest = new JsonObject();
//            oTest.add("oTest", GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(o), JsonObject.class));
//            LogUtils.e(TAG, " Process_4: " + oTest.toString() );

            callback.onback(true, transactionRe);
//            signAndPushTransaction(info, transactionRe);
        }, throwable -> {
            throwable.printStackTrace();
            callback.onback(false, "获取交易费用失败" + throwable.getMessage());
            if (LifecycleCallbacks.currentActivity() != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ChainInfo", info.toString());
                map.put("Error", throwable.getMessage());
                MobclickAgent.onEvent(LifecycleCallbacks.currentActivity(), "GetRequiredFee", map);
            }
//            ToastUtils.showShort("获取交易费用失败");
        });
    }


    @SuppressLint("CheckResult")
    private void signAndPushTransaction(String key, ChainInfo info, TransactionRequest transactionRe, Callback callback) {
        EcSignature sign = EcDsa.sign(transactionRe.getDigestForSignature(new TypeChainId(info.chain_id)), new EosPrivateKey(key));
        transactionRe.signatures.add(sign.toString());
        TransactionPushRequest request = new TransactionPushRequest();
        request.transaction = transactionRe;
        request.compression = "none";
        request.signatures = new ArrayList<>();
        request.signatures.add(sign.toString());
        JsonObject object = GsonUtils.getInstance().fromJson(GsonUtils.getInstance().toJson(request), JsonObject.class);
//        LogUtils.e(TAG, " Param_5: " + object.toString() );
        EosHttp.app.pushTransaction(object).compose(RxUtils.observableAsync()).subscribe(o -> {
            if (o.has("transaction_id")) {

//                LogUtils.e(TAG, " Process_5: " + o.toString() );
//                //LogUtils.e(TAG, " Process_5: " + o.toString().length() );
//                String sTest = o.toString();
//                if(sTest.length()<2000) {
//                }
//                else if(sTest.length()<4000) {
//                    LogUtils.e(TAG, " Process_5-1: " + sTest.substring(0,2000) );
//                    LogUtils.e(TAG, " Process_5-1: " + sTest.substring(2000,sTest.length()) );
//                }

                callback.onback(true, o.get("transaction_id").getAsString() + "time=" + o.getAsJsonObject("processed").get("block_time").getAsString());
            } else {
                callback.onback(false, "签名推送到链失败");
            }
        }, e -> {
            e.printStackTrace();
            callback.onback(false, "签名推送到链失败" + e.getMessage());
            if (LifecycleCallbacks.currentActivity() != null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ChainInfo", info.toString());
                map.put("Error", e.getMessage());
                MobclickAgent.onEvent(LifecycleCallbacks.currentActivity(), "SignAndPushTransaction", map);
            }
        });
    }

    private TransactionRequest createTransactionRe(String strAction, ChainBlock o, String data) {
        TransactionRequest request = new TransactionRequest();
        request.ctxFreeData = new ArrayList<>();
        request.signatures = new ArrayList<>();
        request.actions = new ArrayList<>();
        TransactionRequest.Action action = new TransactionRequest.Action();
        action.account = strAction;
        action.authorization = new ArrayList<>();
        TransactionRequest.Auth auth = new TransactionRequest.Auth();
        auth.actor = creator;
        auth.permission = "active";
        action.authorization.add(auth);
        action.data = data;
        action.name = "transfer";
        request.actions.add(action);

        request.context_free_actions = new ArrayList<>();
        request.transaction_extensions = new ArrayList<>();
        if (o != null) {
            request.expiration = o.getTimeAfterHeadBlockTime(3000000);
            request.ref_block_num = o.block_num;
            request.ref_block_prefix = o.ref_block_prefix;
        }
        return request;
    }

    public interface Callback {
        void onback(boolean isSuccess, Object o);
    }


    public void pay(String action, String memo, String to, String quantity, String from, Callback callback) {
        quantity = quantity.replace("DNC", "EOS");
        getJson2Bin(action, memo, to, quantity, from, (isSuccess, o) -> {
            if (isSuccess) {
                getChainInfo(action, (String) o, (isSuccess1, o1) -> {
                    if (isSuccess1) {
                        getChainBlock(action, (String) o, (ChainInfo) o1, (isSuccess2, o2) -> {
                            if (isSuccess2) {
                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2, (isSuccess3, o3) -> {
                                    if (isSuccess3) {
                                        signAndPushTransaction(creatorPriKey, (ChainInfo) o1, (TransactionRequest) o3, callback);
                                    } else {
                                        callback.onback(false, o3);
                                    }
                                });
                            } else {
                                callback.onback(false, o2);
                            }
                        });
                    } else {
                        callback.onback(false, o1);
                    }
                });
            } else {
                callback.onback(false, o);
            }
        });
    }

    // 单独获取手续费
//    public void getRequiredFee(String action, String memo, String to, String quantity, String from, Callback callback) {
//        getJson2Bin(action, memo, to, quantity, from, (isSuccess, o) -> {
//            if (isSuccess) {
//                getChainInfo(action, (String) o, (isSuccess1, o1) -> {
//                    if (isSuccess1) {
//                        getChainBlock(action, (String) o, (ChainInfo) o1, (isSuccess2, o2) -> {
//                            if (isSuccess2) {
//                                getRequiredFee((ChainInfo) o1, (TransactionRequest) o2, callback);
//                            } else {
//                                callback.onback(false, "");
//                            }
//                        });
//                    } else {
//                        callback.onback(false, "");
//                    }
//                });
//            } else {
//                callback.onback(false, "");
//            }
//        });
//    }


//    public static void UTCtoCST(String utc){//CST可视为美国、澳大利亚、古巴或中国的标准时间,北京时间与utc时间相差8小时
//        ZonedDateTime zdt  = ZonedDateTime.parse(utc);
//        LocalDateTime localDateTime = zdt.toLocalDateTime();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM HH:mm:ss");
//        System.out.println("北京时间："+formatter.format(localDateTime.plusHours(8)));
//    }"yyyy-MM-dd'T'HH:mm:ss"

}
