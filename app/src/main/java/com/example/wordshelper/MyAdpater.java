package com.example.wordshelper;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;



import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyAdpater extends BaseQuickAdapter<Word, BaseViewHolder> {
    MyViewModel viewModel;
    public MyAdpater(int layoutResId, @Nullable List<Word> data,MyViewModel viewModel){
        super(layoutResId, data);
        this.viewModel = viewModel;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Word word) {
        baseViewHolder.setText(R.id.text_id, String.valueOf(word.getId()));
        baseViewHolder.setText(R.id.text_english,word.getEnglish());
        baseViewHolder.setText(R.id.text_chinese,word.getChinese());
        Switch switchs = baseViewHolder.findView(R.id.switch1);
        switchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.d("调用","1");
                    word.setInv_chinese(false);
                    baseViewHolder.setGone(R.id.text_chinese,true);
                    viewModel.update(word);
                }else{
                    Log.d("调用","2");
                    baseViewHolder.setGone(R.id.text_chinese,false);
                    baseViewHolder.setText(R.id.text_chinese,word.getChinese());
                    word.setInv_chinese(true);
                    viewModel.update(word);
                }
            }
        });
    }
}
