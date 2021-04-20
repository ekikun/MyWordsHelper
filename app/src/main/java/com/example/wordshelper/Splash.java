package com.example.wordshelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class Splash extends AppCompatActivity {

    MyViewModel viewModel;

    Word todayWord;

    TextView textView;

    LiveData<List<Word>> list;

    Random r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        r = new Random();
        viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);
        viewModel.init(this);
        list = viewModel.getList();
        list.observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> wordList) {
                if(wordList.size()!=0){
                    int flag = r.nextInt(wordList.size()-1);
                    textView.setText(wordList.get(flag).getEnglish()+"\n\n"+wordList.get(flag).getChinese());
                }else{
                    textView.setText("Keep\n\nLearning");
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Window window =getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        textView = findViewById(R.id.textView);
        AssetManager manager = getAssets();
        Typeface typeface = Typeface.createFromAsset(manager,"fonts/muyao.TTF");
        textView.setTypeface(typeface);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}