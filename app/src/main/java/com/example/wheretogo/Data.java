package com.example.wheretogo;

public class Data {
    private String address, temp, newsH, news, id;

    public Data(String address, String temp, String newsH, String news) {
        this.address = address;
        this.temp = temp;
        this.newsH = newsH;
        this.news = news;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getTemp() {
        return temp;
    }

    public String getNewsH() {
        return newsH;
    }

    public String getNews() {
        return news;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setNewsH(String newsH) {
        this.newsH = newsH;
    }

    public void setNews(String news) {
        this.news = news;
    }
}
