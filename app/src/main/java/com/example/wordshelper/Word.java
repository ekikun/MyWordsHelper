package com.example.wordshelper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "English")
    String english;

    @ColumnInfo(name = "Chinese")
    String chinese;

    @ColumnInfo(name = "Chinese_Invisble")
    boolean inv_chinese;

    public int getId() {
        return id;
    }

    public String getEnglish() {
        return english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public boolean isInv_chinese() {
        return inv_chinese;
    }

    public void setInv_chinese(boolean inv_chinese) {
        this.inv_chinese = inv_chinese;
    }
}
