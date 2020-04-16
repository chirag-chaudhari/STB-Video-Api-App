/*
 * Copyright (c) 2014 The Android Open Source Project
 * Modifications Copyright (C) 2020 <your KodeIntel
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kodeintel.sample.video.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.CursorObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.loader.app.LoaderManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kodeintel.sample.video.R;
import com.kodeintel.sample.video.model.Video;
import com.kodeintel.sample.video.network.RetrofitInstance;
import com.kodeintel.sample.video.network.ServiceInterface;
import com.kodeintel.sample.video.presenter.CardPresenter;
import com.kodeintel.sample.video.presenter.GridItemPresenter;
import com.kodeintel.sample.video.presenter.IconHeaderItemPresenter;
import com.kodeintel.sample.video.recommendation.UpdateRecommendationsService;
import com.kodeintel.sample.video.responsemodel.GetVideoResponseData;
import com.kodeintel.sample.video.responsemodel.GoogleVideo;
import com.kodeintel.sample.video.responsemodel.VideoResponseModel;
import com.kodeintel.sample.video.ui.setting.SettingPage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Main class to show BrowseFragment with header and rows of videos
 */
public class MainFragment extends BrowseSupportFragment {
  
  private static final int BACKGROUND_UPDATE_DELAY = 300;
  private static final String sBgImageUrl = "https://promexpo.am/Catalogs/PAN/2017/files/extfiles/mainbgImgUrl.jpg";
  private static final String sStudio = "JOS";
  private final Handler mHandler = new Handler();
  private ArrayObjectAdapter mCategoryRowAdapter;
  private Drawable mDefaultBackground;
  private DisplayMetrics mMetrics;
  private Runnable mBackgroundTask;
  private Uri mBackgroundURI;
  private BackgroundManager mBackgroundManager;
  private LoaderManager mLoaderManager;
  private ArrayObjectAdapter mListRowAdapter = null;
  // Maps a Loader Id to its CursorObjectAdapter.
  private Map<Integer, CursorObjectAdapter> mVideoCursorAdapters;
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    
    // Create a list to contain all the CursorObjectAdapters.
    // Each adapter is used to render a specific row of videos in the MainFragment.
    mVideoCursorAdapters = new HashMap<>();
    
  }
  
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    // Final initialization, modifying UI elements.
    super.onActivityCreated(savedInstanceState);
    
    // Prepare the manager that maintains the same background image between activities.
    prepareBackgroundManager();
    
    setupUIElements();
    setupEventListeners();
    prepareEntranceTransition();
    
    updateRecommendations();
  }
  
  /**
   * Fetch data from api using Retrofit and store into db
   */
  private void fetchApiData() {
    ServiceInterface getCountryDataService = RetrofitInstance.getService();
    Call<GetVideoResponseData> call = getCountryDataService.getVideoData();
    call.enqueue(new Callback<GetVideoResponseData>() {
      @Override
      public void onResponse(Call<GetVideoResponseData> call,
          Response<GetVideoResponseData> response) {
        Log.e("data-->", "received");
        List<GoogleVideo> GoogleVideos = response.body().getGoogleVideos();
        int index = 0;
        for (GoogleVideo googleVideo : GoogleVideos) {
          // Create header for this category.
          HeaderItem header = new HeaderItem(googleVideo.getCategory());
          mListRowAdapter = new ArrayObjectAdapter(new CardPresenter());
          for (VideoResponseModel videoResponseModel : googleVideo.getVideos()) {
            if (videoResponseModel.getSources() == null
                || videoResponseModel.getSources().size() == 0) {
              continue;
            }
            Video video = new Video.VideoBuilder()
                .id(index)
                .title(videoResponseModel.getTitle())
                .category(googleVideo.getCategory())
                .description(videoResponseModel.getDescription())
                .videoUrl(videoResponseModel.getSources().get(0))
                .bgImageUrl(sBgImageUrl)
                .cardImageUrl(videoResponseModel.getCard())
                .studio(sStudio)
                .build();
            
            mListRowAdapter.add(video);
          }
          mCategoryRowAdapter.add(new ListRow(header, mListRowAdapter));
        }
        // Create a row for this special case with more samples.
        HeaderItem gridHeader = new HeaderItem(getString(R.string.more_samples));
        GridItemPresenter gridPresenter = new GridItemPresenter(MainFragment.this);
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(gridPresenter);
        gridRowAdapter.add(getString(R.string.grid_view));
        gridRowAdapter.add(getString(R.string.guidedstep_first_title));
        gridRowAdapter.add(getString(R.string.error_fragment));
        gridRowAdapter.add(getString(R.string.personal_settings));
        ListRow row = new ListRow(gridHeader, gridRowAdapter);
        mCategoryRowAdapter.add(row);
        startEntranceTransition(); // TODO: Move startEntranceTransition to after all
      }
      
      @Override
      public void onFailure(@NonNull Call<GetVideoResponseData> call, Throwable throwable) {
        Toast.makeText(getActivity(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
      }
    });
  }
  
  @Override
  public void onDestroy() {
    mHandler.removeCallbacks(mBackgroundTask);
    mBackgroundManager = null;
    super.onDestroy();
  }
  
  @Override
  public void onStop() {
    mBackgroundManager.release();
    super.onStop();
  }
  
  private void prepareBackgroundManager() {
    mBackgroundManager = BackgroundManager.getInstance(getActivity());
    mBackgroundManager.attach(getActivity().getWindow());
    mDefaultBackground = getResources().getDrawable(R.drawable.default_background, null);
    mBackgroundTask = new UpdateBackgroundTask();
    mMetrics = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
  }
  
  private void setupUIElements() {
    //setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.videos_by_google_banner, null));
    setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent over title
    setHeadersState(HEADERS_ENABLED);
    setHeadersTransitionOnBackEnabled(true);
    
    // Set fastLane (or headers) background color
    setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
    
    // Set search icon color.
    setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    
    setHeaderPresenterSelector(new PresenterSelector() {
      @Override
      public Presenter getPresenter(Object o) {
        return new IconHeaderItemPresenter();
      }
    });
    // Map category results from the database to ListRow objects.
    // This Adapter is used to render the MainFragment sidebar labels.
    mCategoryRowAdapter = new ArrayObjectAdapter(new ListRowPresenter());
    setAdapter(mCategoryRowAdapter);
    // Fetch data from api
    fetchApiData();
  }
  
  private void setupEventListeners() {
    setOnSearchClickedListener(new View.OnClickListener() {
      
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("flag", false);
        startActivity(intent);
      }
    });
    
    setOnItemViewClickedListener(new ItemViewClickedListener());
    setOnItemViewSelectedListener(new ItemViewSelectedListener());
  }
  
  private void updateBackground(String uri) {
    int width = mMetrics.widthPixels;
    int height = mMetrics.heightPixels;
    
    RequestOptions options = new RequestOptions()
        .centerCrop()
        .error(mDefaultBackground);
    
    Glide.with(this)
        .asBitmap()
        .load(uri)
        .apply(options)
        .into(new SimpleTarget<Bitmap>(width, height) {
          @Override
          public void onResourceReady(
              Bitmap resource,
              Transition<? super Bitmap> transition) {
            mBackgroundManager.setBitmap(resource);
          }
        });
  }
  
  private void startBackgroundTimer() {
    mHandler.removeCallbacks(mBackgroundTask);
    mHandler.postDelayed(mBackgroundTask, BACKGROUND_UPDATE_DELAY);
  }
  
  private void updateRecommendations() {
    Intent recommendationIntent = new Intent(getActivity(), UpdateRecommendationsService.class);
    getActivity().startService(recommendationIntent);
  }
  
  private class UpdateBackgroundTask implements Runnable {
    
    @Override
    public void run() {
      if (mBackgroundURI != null) {
        updateBackground(mBackgroundURI.toString());
      }
    }
  }
  
  private final class ItemViewClickedListener implements OnItemViewClickedListener {
    
    @Override
    public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
        RowPresenter.ViewHolder rowViewHolder, Row row) {
      
      if (item instanceof Video) {
        Video video = (Video) item;
        Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
        intent.putExtra(VideoDetailsActivity.VIDEO, video);
        
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
            getActivity(),
            ((ImageCardView) itemViewHolder.view).getMainImageView(),
            VideoDetailsActivity.SHARED_ELEMENT_NAME).toBundle();
        getActivity().startActivity(intent, bundle);
      } else if (item instanceof String) {
        if (((String) item).contains(getString(R.string.grid_view))) {
          Intent intent = new Intent(getActivity(), VerticalGridActivity.class);
          Bundle bundle =
              ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                  .toBundle();
          startActivity(intent, bundle);
        } else if (((String) item).contains(getString(R.string.guidedstep_first_title))) {
          Intent intent = new Intent(getActivity(), GuidedStepActivity.class);
          Bundle bundle =
              ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                  .toBundle();
          startActivity(intent, bundle);
        } else if (((String) item).contains(getString(R.string.error_fragment))) {
          BrowseErrorFragment errorFragment = new BrowseErrorFragment();
          getFragmentManager().beginTransaction().replace(R.id.main_frame, errorFragment)
              .addToBackStack(null).commit();
        } else if (((String) item).contains(getString(R.string.personal_settings))) {
          Intent intent = new Intent(getActivity(), SettingPage.class);
          Bundle bundle =
              ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity())
                  .toBundle();
          startActivity(intent, bundle);
        } else {
          Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
              .show();
        }
      }
    }
  }
  
  private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
    
    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
        RowPresenter.ViewHolder rowViewHolder, Row row) {
      if (item instanceof Video) {
        mBackgroundURI = Uri.parse(((Video) item).bgImageUrl);
        startBackgroundTimer();
      }
      
    }
  }
}
