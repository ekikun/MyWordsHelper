package com.example.wordshelper;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MyViewModel  extends ViewModel {

    private LiveData<List<Word>> liveWordlist;

    private LiveData<Word> liveWord;

    private MutableLiveData<Boolean> isRecyclerview;

    private WordDepository depository;

       void init(Context context){
           depository = new WordDepository(context);
           liveWordlist = depository.queryAll();
           isRecyclerview = new MutableLiveData<>();
           isRecyclerview.setValue(false);
       }

       void insert(Word word){
           depository.insert(word);
       }

       void update(Word word){
           depository.update(word);
       }

       void delete(Word word){
           depository.delete(word);
       }

       void deleteAll(){
           depository.deleteAll();
       }

       LiveData<List<Word>> getList(){
           return liveWordlist;
       }

       LiveData<Boolean> getIsRecyclerview(){return isRecyclerview;}

       void setIsRecyclerview(boolean b){
           isRecyclerview.setValue(b);
       }
}
