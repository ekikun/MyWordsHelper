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
    public MyAdpater(int layoutResId, @Nullable List<Word> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Word word) {
        baseViewHolder.setText(R.id.text_id, String.valueOf(word.getId()));
        baseViewHolder.setText(R.id.text_english,word.getEnglish());
        if(word.isInv_chinese()){
            baseViewHolder.setText(R.id.text_chinese,word.getChinese());
        }else{
            baseViewHolder.setText(R.id.text_chinese,"");
        }
    }
}
