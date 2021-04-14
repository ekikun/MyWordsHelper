package com.example.wordshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    LinearLayoutManager manager1;

    GridLayoutManager manager2;

    MyAdpater adpater_r;

    MyAdpater adpater_c;

    boolean change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        change = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        manager1 = new LinearLayoutManager(this);
        manager2 = new GridLayoutManager(this,1);
        adpater_r = new MyAdpater(R.layout.word_item, new ArrayList<Word>());
        adpater_c = new MyAdpater(R.layout.card_word_item, new ArrayList<Word>());
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
                    word.setInv_chinese(true);
                    viewModel.insert(word);
                }
                change = !change;
            }
        });
        adpater_r.addChildClickViewIds(R.id.switch1);

        adpater_r.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if(view.getId()==R.id.switch1){
                    Log.d("点击了", String.valueOf(position)+" swtich");
                    Switch switch1 = (Switch) view;
                    switch1.setChecked(false);
                    Word word = (Word) adapter.getItem(position);
                    switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                               word.setInv_chinese(false);
                               Log.d("设置为不可视",String.valueOf(word.isInv_chinese()));
                                viewModel.update(word);
                            }else{
                                word.setInv_chinese(true);
                                Log.d("设置为可视",String.valueOf(word.isInv_chinese()));
                                viewModel.update(word);
                            }
                        }
                    });
                }
            }
        });
        adpater_c.addChildClickViewIds(R.id.switch1);
        adpater_c.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if(view.getId()==R.id.switch1){
                    Log.d("点击了", String.valueOf(position)+" swtich");
                    Switch switch1 = (Switch) view;
                    switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Word word = (Word) adapter.getItem(position);
                                word.setInv_chinese(true);
                                viewModel.update(word);
                            }else{
                                Word word = (Word) adapter.getItem(position);
                                word.setInv_chinese(true);
                                viewModel.update(word);
                            }
                        }
                    });
                }
            }
        });
        viewModel.getList().observe(MainActivity.this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                List<Word> wordList = viewModel.getList().getValue();
                if(!change){
                    MyAdpater adpater = adpater_r;
                    adpater.setList(wordList);
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
                    MyAdpater adpater = adpater_c;
                    adpater.setList(wordList);
                    adpater.setOnItemClickListener((adapter, view, position) -> {
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