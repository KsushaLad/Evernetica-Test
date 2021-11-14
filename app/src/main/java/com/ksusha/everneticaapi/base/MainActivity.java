package com.ksusha.everneticaapi.base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import com.ksusha.everneticaapi.base.adapter.Adapter;
import com.ksusha.everneticaapi.R;
import com.ksusha.everneticaapi.feature.model.ImageModel;
import com.ksusha.everneticaapi.feature.model.SearchModel;
import com.ksusha.everneticaapi.feature.trending.muitithreading.AppExecutor;
import com.ksusha.everneticaapi.feature.trending.muitithreading.OnDataReceived;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnDataReceived {

    private ArrayList<ImageModel> modelArrayList;
    private int page = 1;
    private RecyclerView recyclerView;
    Adapter adapter;
    CardView d_3, textures, nature, food, travel, animals;
    private boolean isLoading;
    private boolean isLastPage;
    private int pageSize = 30;
    GridLayoutManager gridLayoutManager;
    SearchView searchView;
    private  long backPressedTime;
    private  Toast backToast;
    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter(getApplicationContext(), modelArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem = gridLayoutManager.getChildCount();
                int totalItem = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItem >= totalItem) && firstVisibleItem >= 0 && totalItem >= pageSize) {
                        page++;
                        findPhotos();
                    }
                }
            }
        });
        findPhotos();

        d_3.setOnClickListener(view -> {
            String q = "3d";
            getSearchImage(q);
        });

        textures.setOnClickListener(view -> {
            String q = "textures";
            getSearchImage(q);
        });

        nature.setOnClickListener(view -> {
            String q = "nature";
            getSearchImage(q);
        });

        food.setOnClickListener(view -> {
            String q = "food";
            getSearchImage(q);
        });

        travel.setOnClickListener(view -> {
            String q = "travel";
            getSearchImage(q);
        });

        animals.setOnClickListener(view -> {
            String q = "animals";
            getSearchImage(q);
        });
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recycler_view);
        d_3 = findViewById(R.id.nature);
        textures = findViewById(R.id.bus);
        nature = findViewById(R.id.car);
        food = findViewById(R.id.train);
        travel = findViewById(R.id.trending);
        animals = findViewById(R.id.animals);
        modelArrayList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optin_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AppExecutor.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        getSearchImage(query);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    modelArrayList.clear();
                    findPhotos();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isIconified()) {
            searchView.setIconified(true);
            findPhotos();
        }
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        }else{
            backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void getSearchImage(String query) {
//        isLoading = true;
//        ApiUtilities.getApiInterphase().searchImages(query, 100000).enqueue(new Callback<SearchModel>() { //Executor
//            @Override
//            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
//                //dialog.show();
//                AppExecutor.getInstance().getMainIO().execute(() -> {
//                    modelArrayList.clear();
//                    modelArrayList.addAll(response.body().getResults());
//                    adapter.notifyDataSetChanged();
//                    isLoading = false;
//                    if (modelArrayList.size() > 0) {
//                        isLastPage = modelArrayList.size() < pageSize;
//                    } else isLastPage = true;
//                });
//            }
//
//            @Override
//            public void onFailure(Call<SearchModel> call, Throwable t) {
//            }
//        });
        isLoading = true;
        App app = (App) getApplication();
        disposable.add(app.getUnsplashService().getApiIntephase().searchImages(query, 10000) //RxJava
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BiConsumer<SearchModel, Throwable>() {
                    @Override
                    public void accept(SearchModel searchModel, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Toast.makeText(getApplicationContext(), "Not able to get", Toast.LENGTH_SHORT).show();
                        } else {
                            modelArrayList.clear();
                            modelArrayList.addAll(searchModel.getResults());
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (modelArrayList.size() > 0) {
                            isLastPage = modelArrayList.size() < pageSize;
                        } else isLastPage = true;
                    }
                }));
    }

    private void findPhotos() {
        isLoading = true;
//        ApiUtilities.getApiInterphase().getImages(page, 100000).enqueue(new Callback<List<ImageModel>>() { //Executor
//            @Override
//            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) { //response - отклик
//                if (response.body() != null) {
//                    modelArrayList.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Not able to get", Toast.LENGTH_SHORT).show();
//                }
//                isLoading = false;
//                if (modelArrayList.size() > 0) {
//                    isLastPage = modelArrayList.size() < pageSize;
//                } else isLastPage = true;
//            }
//
//            @Override
//            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
//            }
//        });
        App app = (App) getApplication();
        disposable.add(app.getUnsplashService().getApiIntephase().getImages(page, 100000) //RxJava
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BiConsumer<List<ImageModel>, Throwable>() {
                    @Override
                    public void accept(List<ImageModel> imageModels, Throwable throwable) throws Exception {
                        if (throwable != null) {
                            Toast.makeText(getApplicationContext(), "Not able to get", Toast.LENGTH_SHORT).show();
                        } else {
                            modelArrayList.addAll(imageModels);
                            adapter.notifyDataSetChanged();
                        }
                        isLoading = false;
                        if (modelArrayList.size() > 0) {
                            isLastPage = modelArrayList.size() < pageSize;
                        } else isLastPage = true;
                    }
                }));
    }

    @Override
    public void onReceived(List<ImageModel> list) {
        AppExecutor.getInstance().getMainIO().execute(() -> adapter.updateAdapter(list));
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }
}