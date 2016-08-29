package com.baic.net.api.intf;

import com.baic.net.api.ApiConfig;
import com.baic.net.api.model.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by baic on 16/4/19.
 */
public interface ApiService {

    @GET("{pathname}")
    Call<Response> get(@Path("pathname") String pathname, @Query("j") String j);

    @GET("{pathname}")
    Call<Response> get(@Path("pathname") String pathname, @Query("j") String j, @Query("_t") String t, @Query("sign") String sign);

    @GET("{pathname}")
    Call<Response> get(@Path("pathname") String pathname, @Query("j") String j, @Query("_t") String t, @Query("sign") String sign , @Query("jdomain") String jdomain, @Query("japi") String japi);

    @POST("{pathname}")
    Call<Response> post(@Path("pathname") String pathname, @Query("j") String j);

    @POST("{pathname}")
    Call<Response> post(@Path("pathname") String pathname, @Query("j") String j, @Query("_t") String t, @Query("sign") String sign);

    @GET("{pathname}")
    Call<Response> post(@Path("pathname") String pathname, @Query("j") String j, @Query("_t") String t, @Query("sign") String sign , @Query("jdomain") String jdomain, @Query("japi") String japi);
}
