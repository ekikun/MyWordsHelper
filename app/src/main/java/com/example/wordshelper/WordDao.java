package com.example.wordshelper;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    public void insert(Word word);

    @Update
    public void update(Word word);

    @Delete
    public void delete(Word word);

    @Query("DELETE FROM Word;")
    public void deleteAll();

    @Query("SELECT * FROM Word")
    public LiveData<List<Word>> queryAll();


    @Query("SELECT * FROM Word WHERE id = :id")
    public LiveData<Word> queryByid(int id);

    @Query("DELETE FROM sqlite_sequence")
    public void clear();

    @Query("SELECT * FROM Word WHERE English LIKE :english")
    LiveData<List<Word>> queryEnglish(String english);

    @Query("SELECT * FROM Word WHERE Chinese LIKE :chinese")
    LiveData<List<Word>> queryChinese(String chinese);
}
