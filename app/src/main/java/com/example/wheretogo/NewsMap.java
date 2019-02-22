package com.example.wheretogo;

import android.content.Context;
import android.view.textclassifier.TextLinks;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class NewsMap {
    private TextView mNewsTitle;
    private TextView mNews;
    private String country;
    private RequestQueue mQueue;
    String url;

    public NewsMap(Context context ,TextView mNews,TextView mNewsTitle,String country)
    {
     mQueue= Volley.newRequestQueue(context);
     this.mNews=mNews;
     this.mNewsTitle=mNewsTitle;
     this.country=country;
        url="https://newsapi.org/v2/top-headlines?source=bbc-news&apiKey=2e4e38c75219400488421f12215f43b6&country="+country;
        jsonParse();


    }


    private void jsonParse() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");
                            JSONObject articles = jsonArray.getJSONObject(0);
                            mNewsTitle.setText(articles.get("title").toString());
                            mNews.setText(articles.get("description").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
}
