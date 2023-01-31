package com.example.todolist.data;

import android.os.StrictMode;

import com.example.todolist.data.OwOService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSingleton {
    private static Retrofit retrofit = null;
    private static OwOService service = null;
    private static String baseURL = "https://data.mongodb-api.com/app/data-metzp/endpoint/data/v1/";

    public static Retrofit getInstance()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        if(retrofit == null)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit =  new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            service = retrofit.create(OwOService.class);
        }
        return retrofit;
    }

    public static OwOService getService()
    {
        if(retrofit == null)
        {
            getInstance();
        }
        return service;
    }
}
