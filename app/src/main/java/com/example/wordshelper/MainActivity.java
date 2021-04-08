package com.example.wordshelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    LinearLayoutManager manager1;

    GridLayoutManager manager2;

    MyAdpater adpater;

    boolean change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        change = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        manager1 = new LinearLayoutManager(this);
        manager2 = new GridLayoutManager(this,1);
        MyViewModel viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(MyViewModel.class);
        viewModel.init(this);
        viewModel.deleteAll();
        Button btn_insert = findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0;i<20;i++){
                    Word word = new Word();
                    word.setChinese("奥特曼");
                    word.setEnglish("Urutoraman");
                    viewModel.insert(word);
                }
                change = !change;
            }
        });
        viewModel.getList().observe(MainActivity.this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                List<Word> wordList = viewModel.getList().getValue();
                if(!change){
                    adpater = new MyAdpater(R.layout.word_item,wordList);
                    adpater.setOnItemClickListener((adapter, view, position) -> {
                        if (view.getId() == R.id.recycler) {
                            System.out.println("被点击");
                            Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q="+wordList.get(position).getEnglish());
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adpater);
                    recyclerView.setLayoutManager(manager1);
                }else {
                    adpater = new MyAdpater(R.layout.card_word_item,wordList);
                    adpater.setOnItemClickListener((adapter, view, position) -> {
                        System.out.println("被点击");
                        Uri uri = Uri.parse("https://m.youdao.com/dict?le=eng&q="+wordList.get(position).getEnglish());
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(uri);
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adpater);
                    recyclerView.setLayoutManager(manager2);
                }
            }
        });
    }
}