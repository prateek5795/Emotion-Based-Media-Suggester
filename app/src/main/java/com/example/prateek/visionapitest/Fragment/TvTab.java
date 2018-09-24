package com.example.prateek.visionapitest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prateek.visionapitest.Adapter.TvAdapter;
import com.example.prateek.visionapitest.Listener.RecyclerTouchListener;
import com.example.prateek.visionapitest.Model.Tv;
import com.example.prateek.visionapitest.Model.TvResponse;
import com.example.prateek.visionapitest.R;
import com.example.prateek.visionapitest.Rest.ApiClient;
import com.example.prateek.visionapitest.Rest.ApiInterface;
import com.example.prateek.visionapitest.UI.MovieInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvTab extends Fragment {

    private final static String API_KEY = "eda6b16ebbfe1bb908ee2cf30ac8a979";
    String url = "https://www.themoviedb.org/tv/";
    String genre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_tv, container, false);

        final RecyclerView tvRecyclerView = (RecyclerView) rootView.findViewById(R.id.tv_recycler_view);
        tvRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras.getString("mood").equals("joy")) {
            genre = "35";
        } else if (extras.getString("mood").equals("anger")) {
            genre = "10759";
        } else if (extras.getString("mood").equals("sorrow")) {
            genre = "18";
        } else if (extras.getString("mood").equals("surprise")) {
            genre = "10765";
        } else {
            genre = "";
        }

        Call<TvResponse> call = apiService.getTvAccGenre(API_KEY, genre);
        call.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                final List<Tv> tv = response.body().getResults();
                tvRecyclerView.setAdapter(new TvAdapter(tv, R.layout.list_item_tv, getContext()));

                tvRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), tvRecyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Tv tv1 = tv.get(position);
                        Intent infoIntent = new Intent(getContext(), MovieInfo.class);
                        infoIntent.putExtra("url", url + tv1.getId());
                        startActivity(infoIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {

            }
        });

        return rootView;
    }
}
