package com.example.wordshelper;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.ListIterator;

public class WordDepository {

    private LiveData<List<Word>> wordList;

    private WordDao dao;

    private WordDataBase dataBase;

    WordDepository(Context context){
        dataBase = WordDataBase.getDatabase(context);
        dao = dataBase.getDao();
        wordList = dao.queryAll();
    }

    void insert(Word word){
        new Thread(new InsertThread(word)).start();
    }

    void update(Word word){
        new Thread(new UpdateThread(word)).start();
    }

    void delete(Word word){
        new Thread(new DeleteThread(word)).start();
    }

    void deleteAll(){
        new Thread(new DeleteAllThread()).start();
    }

    LiveData<Word> query(int id){
        return dao.queryByid(id);
    }

    LiveData<List<Word>> queryAll(){
        wordList = dao.queryAll();
        return wordList;
    }


    class InsertThread implements Runnable{

        Word word;

        InsertThread(Word word){
            this.word = word;
        }

        @Override
        public void run() {
            dao.insert(word);
        }
    }

    class UpdateThread implements Runnable{

        Word word;

        UpdateThread(Word word){
            this.word = word;
        }

        @Override
        public void run() {
            dao.update(word);
        }
    }

    class DeleteThread implements Runnable{

        Word word;

        DeleteThread(Word word){
            this.word = word;
        }

        @Override
        public void run() {
            dao.delete(word);
        }
    }

    class DeleteAllThread implements Runnable{

        @Override
        public void run() {
            dao.deleteAll();
            dao.clear();
        }
    }



}
