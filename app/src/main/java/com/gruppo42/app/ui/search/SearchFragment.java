package com.gruppo42.app.ui.search;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.widget.SearchView;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.ViewModelProviders;
        import com.gruppo42.app.R;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.gruppo42.app.api.models.LanguagesDTO;
        import com.gruppo42.app.api.models.MovieItem;
        import com.gruppo42.app.api.models.RegionsDTO;
        import com.gruppo42.app.ui.dialogs.ChangeListener;
        import com.gruppo42.app.ui.dialogs.ItemPickDialog;
        import com.gruppo42.app.ui.dialogs.YearPickDialog;
        import com.example.app2.utils.JSONResourceReader;
        import com.example.app2.utils.Utils;
        import com.google.android.material.chip.Chip;
        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;
        import com.gruppo42.app.utils.Constants;

        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import retrofit2.Retrofit;
        import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment
{
    private final String endpoint = "https://api.themoviedb.org/3/";
    private SearchViewModel searchViewModel;
    private TextView view2;
    private Retrofit api;
    private Map<String, String> params;
    private ArrayList<RegionsDTO.Region> regions;
    private ArrayList<LanguagesDTO.Language> languages;
    private Toolbar toolbar;
    private Chip regionChip;
    private Chip languageChip;
    private Chip yearChip;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.params = new HashMap<>();
        initApis();
        loadJSONObjects();
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        languageChip = root.findViewById(R.id.chipLanguage);
        regionChip = root.findViewById(R.id.chipRegion);
        yearChip = root.findViewById(R.id.chipYear);
        recyclerView = root.findViewById(R.id.searchRecycler);
        View noDataview = root.findViewById(R.id.textView4);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), recyclerView, noDataview, api);
        ArrayList<MovieItem> filmItems = new ArrayList<>();
        ArrayList<String> generes = new ArrayList<>();
        generes.add("Thriller");
        generes.add("Action");
        generes.add("Dark");
        setHasOptionsMenu(true);
        searchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        MenuItem s = toolbar.getMenu().getItem(0);
        SearchView s2 = (SearchView) s.getActionView();
        s2.setClickable(true);
        s2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                performQuery(query, adapter, noDataview);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noDataview.setVisibility(View.GONE);
                return false;
            }
        });
        regionChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regionChip.setCloseIconVisible(false);
                params.remove("region");
                regionChip.setText(R.string.region_select, TextView.BufferType.EDITABLE);
                performQuery(s2.getQuery().toString(), adapter, noDataview);
            }
        });
        regionChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPickDialog regionPickDialog = new ItemPickDialog(R.layout.region_picker_dialog,
                        getString(R.string.region_select),
                        regions,
                        new ChangeListener() {
                            @Override
                            public void onChange(Object object) {
                                RegionsDTO.Region region = (RegionsDTO.Region)object;;
                                regionChip.setText(object +" "+ Utils.codeToEmoji(region.getCode2()), TextView.BufferType.EDITABLE);
                                regionChip.setCloseIconVisible(true);
                                params.put("region", region.getCode2());
                                performQuery(s2.getQuery().toString(), adapter, noDataview);
                            }
                        });
                regionPickDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.region_select));
            }
        });
        languageChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageChip.setCloseIconVisible(false);
                params.remove("language");
                languageChip.setText(R.string.language_select, TextView.BufferType.EDITABLE);
                performQuery(s2.getQuery().toString(), adapter, noDataview);
            }
        });
        languageChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemPickDialog languagePickDialog = new ItemPickDialog(R.layout.language_picker_dialog,
                        getString(R.string.language_select),
                        languages,
                        new ChangeListener() {
                            @Override
                            public void onChange(Object object) {
                                LanguagesDTO.Language language = (LanguagesDTO.Language)object;
                                languageChip.setText(language.getName(), TextView.BufferType.EDITABLE);
                                languageChip.setCloseIconVisible(true);
                                params.put("language", language.getCode2());
                                performQuery(s2.getQuery().toString(), adapter, noDataview);
                            }
                        });
                languagePickDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.language_select));
            }
        });
        yearChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearChip.setCloseIconVisible(false);
                params.remove("year");
                yearChip.setText(R.string.year_select, TextView.BufferType.EDITABLE);
                performQuery(s2.getQuery().toString(), adapter, noDataview);
            }
        });
        yearChip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                YearPickDialog yearPickDialogs = new YearPickDialog(R.layout.year_picker_dialog,
                        1800, 3000,
                        new ChangeListener() {
                            @Override
                            public void onChange(Object object) {
                                int change = (int) object;
                                yearChip.setText(change+"", TextView.BufferType.EDITABLE);
                                yearChip.setCloseIconVisible(true);
                                params.put("year", change+"");
                                performQuery(s2.getQuery().toString(), adapter, noDataview);
                            }
                        });
                yearPickDialogs.show(getActivity().getSupportFragmentManager(), getString(R.string.year_select));
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    adapter.onReachedBottom();
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null)
        {
            Object regionChipText = savedInstanceState.get("regionChip");
            Object languageChipText = savedInstanceState.get("languageChip");
            Object yearChipText = savedInstanceState.get("yearChip");
            if(regionChipText!=null && !regionChipText.equals(getString(R.string.region_select)))
            {
                this.regionChip.setText(regionChipText.toString(), TextView.BufferType.EDITABLE);
                this.regionChip.setCloseIconVisible(true);
                this.params.put("region", regionChipText.toString());
            }
            if(languageChipText!=null && !languageChipText.equals(getString(R.string.language_select)))
            {
                this.languageChip.setText(languageChipText.toString(), TextView.BufferType.EDITABLE);
                this.languageChip.setCloseIconVisible(true);
                this.params.put("language", languageChipText.toString());
            }
            if(yearChipText!=null && !yearChipText.equals(getString(R.string.year_select)))
            {
                this.yearChip.setText(yearChipText.toString(), TextView.BufferType.EDITABLE);
                this.yearChip.setCloseIconVisible(true);
                this.params.put("year", yearChipText.toString());
            }
            SearchView sview = (SearchView) toolbar.getMenu().getItem(0).getActionView();
            String query = (String) savedInstanceState.get("query");
            boolean search = false;
            if(savedInstanceState.get("query")!=null)
                search = true;
            sview.setQuery(query, search);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        SearchView view = (SearchView) toolbar.getMenu().getItem(0).getActionView();
        outState.putString("query", view.getQuery().toString());
        if(regionChip.getText()!=null)
            outState.putString("regionChip", regionChip.getText().toString());
        if(languageChip.getText()!=null)
            outState.putString("languageChip", languageChip.getText().toString());
        if(yearChip.getText()!=null)
            outState.putString("yearChip", yearChip.getText().toString());
    }

    private void loadJSONObjects()
    {
        String regionsjson = JSONResourceReader.getJsonString(getResources(), R.raw.countries);
        String languagesjson = JSONResourceReader.getJsonString(getResources(), R.raw.languages);
        Type listType1 = new TypeToken<List<RegionsDTO.Region>>() {}.getType();
        Type listType2 = new TypeToken<List<LanguagesDTO.Language>>() {}.getType();
        this.regions = new Gson().fromJson(regionsjson, listType1);
        this.languages = new Gson().fromJson(languagesjson, listType2);
        Log.d("SIZE", this.regions.size()+"");
        Log.d("SIZE", this.languages.size()+"");
    }

    private void initApis() {
        this.api = new Retrofit.Builder()
                .baseUrl(this.endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private void performQuery(String query, RecyclerViewAdapter adapter, View noDataview)
    {
        Log.d("performQuery", "performing query with:" +
                                        "\nLanguage: "+params.get("language")+
                                        "\nQuery: " + query+
                                        "\nRegion: "+params.get("region")+
                                        "\nYear: "+params.get("year"));
        adapter.findMovieWith((Constants.MOVIES_API_KEY),
                params.get("language"),
                query,
                params.get("region"),
                params.get("year"));
        adapter.addEmptyListener(new EmptyListener() {
            @Override
            public void onEmptyResult() {
                recyclerView.setVisibility(View.GONE);
                noDataview.setVisibility(View.VISIBLE);
            }
            @Override
            public void onResult() {
                noDataview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }
}
