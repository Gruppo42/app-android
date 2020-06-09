package com.gruppo42.app.ui.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gruppo42.app.R;
import com.gruppo42.app.api.models.MovieApi;
import com.gruppo42.app.api.models.MovieItem;
import com.gruppo42.app.api.models.QueryResultDTO;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FilmViewHolder>
{
    private List<MovieItem> filmItemList;
    private Context context;
    private Calendar calendar;
    private MovieApi api;
    private int maxPages;
    private int currentPage;
    private Map<String, Object> queryOptions;
    private RecyclerView recyclerView;
    private EmptyListener listener;
    //Current query track
    private String api_key;
    private String language;
    private String query;
    private String region;
    private String year;
    /////////////////
    private View noDataFound;

    public RecyclerViewAdapter(List<MovieItem> filmItemList,
                               Context context,
                               RecyclerView recyclerView,
                               View noDataFound,
                               Retrofit api) {
        this.filmItemList = filmItemList;
        this.api = api.create(MovieApi.class);
        this.context = context;
        this.recyclerView = recyclerView;
        calendar = new GregorianCalendar();
        this.maxPages = -1;
        this.currentPage = -1;
        query = "";
        queryOptions = new HashMap<String, Object>();
        this.noDataFound = noDataFound;
        String api_key = null;
        String language = null;
        String query = null;
        String region = null;
        String year = null;
    }

    public RecyclerViewAdapter(Context context,
                               RecyclerView recyclerView,
                               View noDataFound,
                               Retrofit api) {
        this.maxPages = -1;
        this.currentPage = -1;
        this.filmItemList = new ArrayList<>();
        this.api = api.create(MovieApi.class);
        this.context = context;
        this.recyclerView = recyclerView;
        calendar = new GregorianCalendar();
        this.filmItemList = new ArrayList<>();
        query = "";
        queryOptions = new HashMap<String, Object>();
        this.noDataFound = noDataFound;
        String api_key = null;
        String language = null;
        String query = null;
        String region = null;
        String year = null;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.alt_list_layout, parent, false);
        return new FilmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        MovieItem item = filmItemList.get(position);
        Glide
                .with(this.context)
                .load("https://image.tmdb.org/t/p/w94_and_h141_bestv2/"+item.getImageUrl())
                .fallback(R.drawable.ic_placeholder_err)
                .error(R.drawable.ic_placeholder_err)
                .transition(GenericTransitionOptions.with(R.transition.zoomin))
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if(isFirstResource) {
                            holder.shimmerFrameLayout.stopShimmer();
                            holder.shimmerFrameLayout.setVisibility(View.GONE);
                        }
                        holder.imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.imageView);
        holder.title.setText(item.getTitle());
        holder.year.setText(item.getYear());

    }

    @Override
    public int getItemCount() {
        return filmItemList.size();
    }

    public Call<QueryResultDTO> findMovieWith(String api_key,
                                              String language,
                                              String query,
                                              String region,
                                              String year) {

        Call<QueryResultDTO> res = api.findMovieWith(api_key, language, "1", query, region, year);
        this.query = query;
        this.api_key = api_key;
        this.query = query;
        this.region = region;
        this.year = year;
        Log.d("CALLPARAM", "API KEY: " + api_key +
                                    "\nLANGUAGE: " + language +
                                    "\nPAGE: " + 1 +
                                    "\nQUERY: " + query +
                                    "\nREGION: " + region +
                                    "\nYEAR: " + year);
        res.enqueue(new Callback<QueryResultDTO>() {
            @Override
            public void onResponse(Call<QueryResultDTO> call, Response<QueryResultDTO> response) {
                QueryResultDTO results = (QueryResultDTO) response.body();
                if(results==null) {
                    listener.onEmptyResult();
                    return;
                }
                currentPage = results.getPage();
                maxPages = results.getTotal_pages();
                filmItemList.clear();
                if(results.getTotal_results()==0)
                    listener.onEmptyResult();
                else
                    listener.onResult();
                for(QueryResultDTO.ResultDTO res : results.getResults())
                    filmItemList.add(new MovieItem(res));
                notifyDataSetChanged();
                Log.d("SEARCH", "Found " + results.getTotal_results() + " results");
            }

            @Override
            public void onFailure(Call<QueryResultDTO> call, Throwable t) {
                Log.e("SEARCH", t.toString());
            }
        });
        return res;
    }

    public void onReachedBottom()
    {
        nextPage();
        Log.d("ADPATERREC", "REACHED BOTTOM");
    }

    private Call<QueryResultDTO> nextPage()
    {
        if(this.currentPage==this.maxPages)
            return null;
        else
        {
            Log.d("nextPage", "performing nextpage");
            Call<QueryResultDTO> res = api.findMovieWith(this.api_key, this.language, ""+(this.currentPage+1), this.query, this.region, this.year);
            res.enqueue(new Callback<QueryResultDTO>() {
                @Override
                public void onResponse(Call<QueryResultDTO> call, Response<QueryResultDTO> response) {
                    QueryResultDTO results = (QueryResultDTO) response.body();
                    currentPage = results.getPage();
                    maxPages = results.getTotal_pages();
                    if(results==null || results.getTotal_results()==0) {
                        listener.onEmptyResult();
                        return;
                    }
                    else
                        listener.onResult();
                    for(QueryResultDTO.ResultDTO res : results.getResults())
                        filmItemList.add(new MovieItem(res));
                    notifyItemRangeInserted(filmItemList.size(), results.getResults().size());
                    Log.d("SEARCHr", "Found " + results.getTotal_results() + " results");
                }

                @Override
                public void onFailure(Call<QueryResultDTO> call, Throwable t) {
                    Log.e("SEARCHr", t.toString());
                }
            });
            return res;
        }
    }

    public void addEmptyListener(EmptyListener listener)
    {
        this.listener = listener;
    }

    public class FilmViewHolder extends RecyclerView.ViewHolder
    {
        ShimmerFrameLayout shimmerFrameLayout;
        ImageView imageView;
        TextView title;
        TextView year;
        ChipGroup chipGroup;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.movieTitle);
            year = itemView.findViewById(R.id.movieYear);
            chipGroup = itemView.findViewById(R.id.movieGenreChips);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_view_container);
            imageView.setClipToOutline(true);
        }


    }

    public List<MovieItem> getFilmItemList() {
        return filmItemList;
    }
}
