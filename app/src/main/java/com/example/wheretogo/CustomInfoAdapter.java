package com.example.wheretogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter{

    private final View Window;
    private Context Context;
    private TextView mNews;
    private TextView mNewsTitle;
    private TextView mWeather;
    private TextView mTitle;

    public CustomInfoAdapter(Context context) {
        Context = context;
        Window = LayoutInflater.from(Context).inflate(R.layout.custom_info_window, null);
        mNews = Window.findViewById(R.id.mNews);
        mNewsTitle = Window.findViewById(R.id.mNewsTitle);
        mTitle = Window.findViewById(R.id.mName);
        mWeather = Window.findViewById(R.id.mWeather);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.mName);

        if(!title.equals("")){
            tvTitle.setText(title);
        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, Window);
        return Window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, Window);
        return Window;
    }

    public TextView getmNews() {
        return mNews;
    }

    public TextView getmNewsTitle() {
        return mNewsTitle;
    }

    public TextView getmWeather() {
        return mWeather;
    }

    public TextView getmTitle() {
        return mTitle;
    }
}
