package com.example.testme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.testme.adapters.bookmarkAdapter;
import com.example.testme.models.questionsModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.testme.QuestionsActivity.FILE_NAME;
import static com.example.testme.QuestionsActivity.KEY_NAME;

public class BookmarksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<questionsModel> BookmarksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        loadAds();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.bookmark_rv);

        preferences = getSharedPreferences(FILE_NAME , Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        bookmarkAdapter adapter = new bookmarkAdapter(BookmarksList);
        recyclerView.setAdapter(adapter);


    }
    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void getBookmarks(){
        String json = preferences.getString(KEY_NAME , "");

        Type type = new TypeToken<List<questionsModel>>(){}.getType();
        BookmarksList = gson.fromJson(json , type);

        if (BookmarksList == null){
            BookmarksList = new ArrayList<>();
        }
    }
    private  void storeBookmarks(){
        String json = gson.toJson(BookmarksList);
        editor.putString(KEY_NAME , json);
        editor.commit();

    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}