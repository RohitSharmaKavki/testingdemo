package com.web.ajmerainfotech;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    String Base_URL = "http://talentartist.in/API_KAVKI/";


    @FormUrlEncoded
    @POST("authoradd.php")
    Call<String> addauto(@Field("authorname") String authorname , @Field("createdate") String createdate);


    @GET("authorget.php")
    Call<List<AuthorResponse>> getauto();


    @FormUrlEncoded
    @POST("authordelete.php")
    Call<String> deleteauto(@Field("id") String id);


    @FormUrlEncoded
    @POST("authorupdate.php")
    Call<String> updateauto(@Field("id") String id, @Field("bookname") String bookname , @Field("bookprice") String bookprice);


}
