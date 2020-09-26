package com.dnc.loc.data.http;


import com.dnc.loc.data.eos.model.ChainRecord;
import com.dnc.loc.data.eos.model.VersionRecord;
import com.google.gson.JsonObject;
import com.dnc.loc.data.eos.model.ChainBlock;
import com.dnc.loc.data.eos.model.ChainInfo;
import com.dnc.loc.data.eos.model.RequireFee;
import com.dnc.loc.data.eos.model.UserAvailableList;
import com.dnc.loc.data.eos.model.UserTokenAvailableList;
import com.dnc.loc.data.model.BaseUserInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EosApi {


//    @FormUrlEncoded
//    @POST("api/api_app.aspx?action=login")
//    Observable<Response<UserInfo>> accountlogin(@Field("telephone") String telephone, @Field("password") String password, @Field("loginType") int loginType);
//
//
//    @GET
//    Observable<Response<Object>> newAccount(@Url String url, @Query("new_username") String newUsername, @Query("pub_key") String pubKey, @Query("sign") String sign, @Header("x-author") String author, @Header("x-key") String xkey);


    @FormUrlEncoded
    @POST(EosConst.get_key_accounts)
    Observable<BaseUserInfo> getKeyAccounts(@Field("public_key") String publicKey);


//    @FormUrlEncoded
    @POST(EosConst.get_abi_json_to_bin)
    Observable<JsonObject> json2bean(@Body JsonObject body);

    @FormUrlEncoded
    @POST(EosConst.get_table_rows)
    Observable<UserAvailableList> getTableRows(@Field("scope") String scope, @Field("code") String code, @Field("table") String table, @Field("table_key") String table_key, @Field("limit") int limit, @Field("json") boolean json);

    @FormUrlEncoded
    @POST(EosConst.get_table_rows)
    Observable<UserTokenAvailableList> getTokenBalance(@Field("scope") String scope, @Field("code") String code, @Field("table") String table,@Field("key") String key, @Field("limit") int limit, @Field("json") boolean json);


    @GET(EosConst.get_chain_info)
    Observable<ChainInfo> getChainInfo();

    @FormUrlEncoded
    @POST(EosConst.get_chain_block)
    Observable<ChainBlock> getChainBlock(@Field("block_num_or_id") String id);

    @POST(EosConst.get_required_fee)
    Observable<RequireFee> getRequiredFee(@Body JsonObject body);

    @POST(EosConst.push_transaction)
    Observable<JsonObject> pushTransaction(@Body JsonObject body);

    @FormUrlEncoded
    @POST(EosConst.get_trans_record)
    Observable<ChainRecord> getTransRecord(@Field("pageno") int scope, @Field("pagesize") int code, @Field("table") String table, @Field("user") String key);

    @FormUrlEncoded
    @POST(EosConst.get_trans_record)
    Observable<VersionRecord> getVersion(@Field("pageno") int scope, @Field("pagesize") int code, @Field("table") String table, @Field("user") String key);

}