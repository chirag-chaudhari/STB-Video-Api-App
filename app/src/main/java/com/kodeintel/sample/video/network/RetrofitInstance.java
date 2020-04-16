package com.kodeintel.sample.video.network;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
  
  private static Retrofit retrofit = null;
  private static final String BASE_URL = "https://storage.googleapis.com";
  
  public static ServiceInterface getService() {
    
    if (retrofit == null) {
      /*
       * OkHttp Logging Interceptor
       * */
      HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
      httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      
      OkHttpClient okHttpClient = new OkHttpClient.Builder()
          .addInterceptor(httpLoggingInterceptor)
          .connectTimeout(1, TimeUnit.MINUTES)
          .readTimeout(30, TimeUnit.SECONDS)
          .writeTimeout(15, TimeUnit.SECONDS)
          .retryOnConnectionFailure(true)
          .build();
      
      retrofit = new Retrofit
          .Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          //.client(okHttpClient)
          .build();
    }
    
    return retrofit.create(ServiceInterface.class);
  }
}