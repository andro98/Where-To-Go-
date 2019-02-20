package com.example.wheretogo;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wheretogo.WeatherMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK()){
            Button  BtnMap = findViewById(R.id.btnMap);
            BtnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapActivity = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(mapActivity);
                }
            });
        }
    }
    public void getWeather(double latitude,double longitude ){
        double lat=latitude;
        double lon=longitude;
        WeatherMap obj=new WeatherMap();
        obj.execute("api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lat) + "&appid=5fd2eb6d64688fd32b0bde7ecf10ab65&lon="+ String.valueOf(lon) + "");



    }

    private void init(){

    }

    public boolean isServicesOK(){
        //Log.d(TAG, "is")

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
