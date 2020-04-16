package com.kodeintel.sample.video.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GoogleVideo {
  
  @SerializedName("category")
  @Expose
  private String category;
  @SerializedName("videos")
  @Expose
  private List<VideoResponseModel> videos = null;
  
  public String getCategory() {
    return category;
  }
  
  public List<VideoResponseModel> getVideos() {
    return videos;
  }
  
}