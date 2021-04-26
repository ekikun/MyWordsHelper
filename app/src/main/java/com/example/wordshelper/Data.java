package com.example.wordshelper;

import java.util.List;

public class Data {
    private int code;
    String msg;
    List<NewsList> newslist;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setNewlist(List<NewsList> newslist) {
        this.newslist = newslist;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<NewsList> getNewslist() {
        return newslist;
    }
}
