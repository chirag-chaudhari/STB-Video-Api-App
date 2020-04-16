package com.kodeintel.sample.video.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetVideoResponseData {
  
  @SerializedName("googlevideos")
  @Expose
  private List<GoogleVideo> googleVideos = null;
  
  public List<GoogleVideo> getGoogleVideos() {
    return googleVideos;
  }
}