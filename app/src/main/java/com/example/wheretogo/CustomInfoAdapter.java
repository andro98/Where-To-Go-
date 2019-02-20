package com.example.wheretogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter{

    private final View Window;
    private Context Context;

    public CustomInfoAdapter(Context context) {
        Context = context;
        Window = LayoutInflater.from(Context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = view.findViewById(R.id.title);

        if(!title.equals("")){
            tvTitle.setText(title);
        }

        String snippet = marker.getTitle();
        TextView tvSnippet= view.findViewById(R.id.snippet);
        if(!title.equals("")){
            tvSnippet.setText(snippet);
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
}
