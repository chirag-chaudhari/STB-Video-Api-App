package com.kodeintel.sample.video.network;

import com.kodeintel.sample.video.responsemodel.GetVideoResponseData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceInterface {
  
  @GET("/android-tv/android_tv_videos_new.json")
  Call<GetVideoResponseData> getVideoData();
}