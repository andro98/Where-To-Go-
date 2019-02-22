package com.example.wheretogo;
//to be same package to main activity

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//for asynctask
//to download dataa from web page

///////
public class WeatherMap extends AsyncTask<String,Void,String> {
    String result = "";
    URL url;
    HttpURLConnection urlConnection =null;

    @Override
    protected String doInBackground(String... urls) {

        try {
            //url with uaer input (paramters)
            url =new URL (urls[0]);
            urlConnection =(HttpURLConnection)url.openConnection();
            //input stream from url to http connection
            InputStream in=urlConnection.getInputStream();
            //input stram to read the input stram
            InputStreamReader reader=new InputStreamReader(in);
            int data =reader.read();
            while(data != -1){
                char current = (char)data;
                result+=current;
                data= reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute (String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONObject weatherdata=new JSONObject(jsonObject.getString("main"));
            //from json mainn every thing between {}
            Double tempreture =Double.parseDouble(weatherdata.getString("temo"));
            // bigeeb al temp mn al weather data b3d ma 7t kol 2le fe al main fe al weather w b3d kada bi5od al temp lo7do
            int temperatureint =(int)(tempreture*1.8-459.67);
            String placename=jsonObject.getString("name");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
