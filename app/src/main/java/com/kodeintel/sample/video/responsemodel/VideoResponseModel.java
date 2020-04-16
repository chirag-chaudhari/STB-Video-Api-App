package com.kodeintel.sample.video.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VideoResponseModel {
  
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("sources")
  @Expose
  private List<String> sources = null;
  @SerializedName("card")
  @Expose
  private String card;
  @SerializedName("background")
  @Expose
  private String background;
  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("studio")
  @Expose
  private String studio;
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public List<String> getSources() {
    return sources;
  }
  
  public String getCard() {
    return card;
  }
  
  public String getBackground() {
    return background;
  }
  
  public void setBackground(String background) {
    this.background = background;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getStudio() {
    return studio;
  }
  
}