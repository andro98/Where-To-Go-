package com.example.wheretogo;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class newweather {

    private TextView mWeather ;
    private RequestQueue mQueue;

    private String url;

    public newweather(Context context, TextView mWeather, String lat, String lon){
        mQueue = Volley.newRequestQueue(context);
        this.mWeather = mWeather;

        url = "api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lat)  + "&appid=5fd2eb6d64688fd32b0bde7ecf10ab65&lon="+ String.valueOf(lon) + "";
        jsonParse();
    }

    private void jsonParse(){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse (JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("main");
                            JSONObject weather = jsonArray.getJSONObject(0);
                            //from json mainn every thing between {}
                            Double tempreture =Double.parseDouble(weather.getString("temp"));
                            // bigeeb al temp mn al weather data b3d ma 7t kol 2le fe al main fe al weather w b3d kada bi5od al temp lo7do
                            int temperatureint =(int)(tempreture*1.8-459.67);

                            mWeather.setText(temperatureint);

                        }catch (JSONException e) {
                            e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener(){
                        public void onErrorResponse(VolleyError error){
                            error.printStackTrace();

                        }
                    }
                );
        mQueue.add(request);
    }
}


