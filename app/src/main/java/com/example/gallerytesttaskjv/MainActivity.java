package com.example.gallerytesttaskjv;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallerytesttaskjv.api.ApiUtilities;
import com.example.gallerytesttaskjv.model.Image;
import com.example.gallerytesttaskjv.model.Search;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Image> list;
    private GridLayoutManager manager;
    private ImageAdapter adapter;
    private int page = 1;
    private ProgressDialog dialog;
    private int pageSize = 30;
    private boolean isLoading;
    private boolean isLastPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.imageRecycle);
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list);
        manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPos = manager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPos >= totalItem)
                            && firstVisibleItemPos >= 0
                            && totalItem >= pageSize) {
                        page++;
                        getData();
                    }
                }
            }
        });
    }

    private void getData() {
        isLoading = true;
        ApiUtilities.getApiUtility()
                .getImages(page, 30)
                .enqueue(new Callback<List<Image>>() {

                    @Override
                    public void onResponse(
                            Call<List<Image>> call, Response<List<Image>> response) {
                        if (response.body() != null) {
                            list.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }

                        isLoading = false;
                        dialog.dismiss();

                        if (list.size() > 0) {
                            isLastPage = list.size() < pageSize;
                        } else {
                            isLastPage = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Image>> call, Throwable throwable) {
                        dialog.dismiss();
                        Toast.makeText(
                                MainActivity.this,
                                "Error : " + throwable.getMessage(),
                                Toast.LENGTH_SHORT);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.show();
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void searchData(String query) {
        dialog.dismiss();

        ApiUtilities.getApiUtility().searchImage(query).enqueue(new Callback<Search>() {

            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                list.clear();
                list.addAll(response.body().getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {

            }
        });
    }
}