package com.example.testme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ScoreActivity extends AppCompatActivity {
    private TextView totalScored , outOf;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        loadAds();

        totalScored = findViewById(R.id.scored);
        outOf = findViewById(R.id.total);
        doneBtn = findViewById(R.id.done_btn);

        totalScored.setText(String.valueOf(getIntent().getIntExtra("score" , 0)));
        outOf.setText("OUT OF " + String.valueOf(getIntent().getIntExtra("total" , 0)));

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}