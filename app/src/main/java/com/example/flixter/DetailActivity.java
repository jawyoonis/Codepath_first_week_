package com.example.flixter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.Objects;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String YOUTUBE_API="AIzaSyBu2sqCR1_NprpNdIB6s_1eZp4rv_Xd3_Y";
    private static final String Video_URL="https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvTitle= findViewById(R.id.tvTitle);
        tvOverview= findViewById(R.id.tvOverview);
        ratingBar= findViewById(R.id.ratingBar);
        youTubePlayerView= findViewById(R.id.player);
        Movie movie= Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(Objects.requireNonNull(movie).getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(Video_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try{
                    JSONArray results =json.jsonObject.getJSONArray("results");
                    if(results.length()==0){
                       return;

                    }
                    String YOUTUBE_KEY= results.getJSONObject(0).getString("key");
                    Log.d("DetialActivity",YOUTUBE_KEY);
                    initializeYoutube(YOUTUBE_KEY);
                }
                catch (JSONException e){
                    Log.e("DatialActivity", "failed to parse json object", e);
                    e.printStackTrace();

                }



            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }
    private  void initializeYoutube(final String YOUTUBE_KEY){
        youTubePlayerView.initialize(YOUTUBE_API, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailActivity", "OnInitailizationSuccess");
                youTubePlayer.cueVideo(YOUTUBE_KEY);

            }

            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("DetailActivity", "onInitializationFailure");

            }
        });

    }
}
