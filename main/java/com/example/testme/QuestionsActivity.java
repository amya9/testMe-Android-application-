package com.example.testme;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorStateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testme.models.questionsModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    public static final String FILE_NAME = "TESTER";
    public static final String KEY_NAME = "QUESTIONS";

    private TextView questions , questionsNumber;
    private FloatingActionButton bookmarksBtn;
    private LinearLayout optionsContainer;
    private Button nextBtn , shareBtn;
    private  int position = 0;
    private List<questionsModel>list;
    private int COUNT  = 0;
    private int SCORE = 0;
    private Dialog loadingDialog;
    private  String  setId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private List<questionsModel> BookmarksList;
    private int  matchedQuestionPosition;
    private  DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadAds();

        questions = findViewById(R.id.questions);
        questionsNumber = findViewById(R.id.questions_number);
        bookmarksBtn = findViewById(R.id.bookmark_btn);
        optionsContainer = findViewById(R.id.que_options_container);
        nextBtn = findViewById(R.id.next_btn);
        shareBtn = findViewById(R.id.share_btn);

        reference = FirebaseDatabase.getInstance().getReference();

        preferences = getSharedPreferences(FILE_NAME , Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmarks();
        bookmarksBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (matchModel()){
                    BookmarksList.remove(matchedQuestionPosition);
                    bookmarksBtn.setImageDrawable(getDrawable(R.drawable.bookmarks));
                }else {
                    BookmarksList.add(list.get(position));
                    bookmarksBtn.setImageDrawable(getDrawable(R.drawable.bookmark_boarder));
                }
            }
        });

        setId = getIntent().getStringExtra("setId" );

        //loading Dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

         list = new ArrayList<>();
        loadingDialog.show();

        reference.child("SETS").child(setId).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    String id = dataSnapshot1.getKey();
                    String question = dataSnapshot1.child("question").getValue().toString();
                    String optionA = dataSnapshot1.child("optionA").getValue().toString();
                    String optionB = dataSnapshot1.child("optionB").getValue().toString();
                    String optionC = dataSnapshot1.child("optionC").getValue().toString();
                    String optionD = dataSnapshot1.child("optionD").getValue().toString();
                    String correctAns = dataSnapshot1.child("correctAnswer").getValue().toString();

                    list.add(new questionsModel(id , correctAns ,optionA , optionB ,optionC , optionD , question , setId));
                }
                if (list.size() >0){
                for (int i = 0; i < 4; i++) {
                    optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            checkAnswer((Button) v);
                        }
                    });
                }
                playAnime(questions, 0, list.get(position).getQuestion());
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        nextBtn.setEnabled(false);
                        nextBtn.setAlpha(0.7f);
                        enableOption(true);
                        position++;
                        if (position == list.size()) {
                            // score activity will show here
                            Intent scoreIntent = new Intent(QuestionsActivity.this, ScoreActivity.class);
                            scoreIntent.putExtra("score", SCORE);
                            scoreIntent.putExtra("total", list.size());
                            startActivity(scoreIntent);
                            finish();
                            return;
                        }
                        COUNT = 0;
                        playAnime(questions, 0, list.get(position).getQuestion());
                    }
                });
                shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String body = list.get(position).getQuestion() + "\n" +
                                       list.get(position).getOptionA() + "\n" +
                                         list.get(position).getOptionB() + "\n" +
                                          list.get(position).getOptionC() + "\n" +
                                           list.get(position).getOptionD() ;
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plane");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT , "Solve The Challenge");
                        shareIntent.putExtra(Intent.EXTRA_TEXT , body);
                        startActivity(Intent.createChooser(shareIntent , "Share Via"));
                    }
                });
            } else {
                    finish();
                    Toast.makeText(QuestionsActivity.this , "No Questions" , Toast.LENGTH_LONG).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
             Toast.makeText(QuestionsActivity.this , error.getMessage() , Toast.LENGTH_LONG).show();
             loadingDialog.dismiss();
             finish();
            }
        });

      }

    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void playAnime(final View view , final int value , final String data){
        for (int i = 0; i < 4; i++) {
            optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
//            optionsContainer.getChildAt(i).setBackgroundColor();
        }

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if(value == 0  && COUNT < 4){
                    String option = "";
                    if (COUNT == 0){
                        option = list.get(position).getOptionA();
                    }else if (COUNT == 1){
                        option = list.get(position).getOptionB();
                    }else if (COUNT == 2){
                        option = list.get(position).getOptionC();
                    }else if (COUNT == 3){
                        option = list.get(position).getOptionD();
                    }
                    playAnime(optionsContainer.getChildAt(COUNT) , 0 , option);
                    COUNT++;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animator animation) {
                if (value == 0){
                    try {
                        ((TextView)view).setText(data);
                        questionsNumber.setText(position+1+"/" + list.size());
                        if (matchModel()){
                            bookmarksBtn.setImageDrawable(getDrawable(R.drawable.bookmark_boarder));
                        }else {
                            bookmarksBtn.setImageDrawable(getDrawable(R.drawable.bookmarks));
                        }
                    }catch (ClassCastException ex){
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnime(view , 1 , data);
                }else {
                    enableOption(true);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private  void checkAnswer(Button selectedOption){
        enableOption(false);
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);
        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){
            //Correct answer
            SCORE++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
//            selectedOption.setBackgroundColor(Color.parseColor("#4CAF50"));

        }else{
            //incorrect answer
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
//            selectedOption.setBackgroundColor(Color.parseColor("#FF0000"));
            //show correct answer
//            Button correctOption = (Button) optionsContainer.findViewWithTag(list.get(position).getCorrectAnswer());
//            ViewCompat.setBackgroundTintList(correctOption , ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){
        for(int i =0 ;i<4 ;i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if (enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
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
    private boolean matchModel(){
        Boolean matched = false;
        int i =0;
        for(questionsModel model : BookmarksList){
            if (model.getQuestion().equals(list.get(position).getQuestion())
                  && model.getCorrectAnswer().equals(list.get(position).getCorrectAnswer())
                    && model.getSet().equals(list.get(position).getSet())){
                matched = true;
                matchedQuestionPosition = i ;
            }
            i++;
        }
        return matched;
    }

    private void loadAds(){
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}