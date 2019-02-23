package com.example.wheretogo;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wheretogo.WeatherMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private ListView mList;
    private ArrayList<Data> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mList = findViewById(R.id.saved_list);
        if(!isLogged()){
            openLogin();
        }

        if(isServicesOK()){
            FloatingActionButton BtnMap = findViewById(R.id.btnMap);
            BtnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mapActivity = new Intent(MainActivity.this, MapActivity.class);
                    startActivity(mapActivity);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        data.clear();
        getAllNews();
    }

    private void openLogin(){
        startActivity(new Intent(this, Login.class));
        finish();
    }

    private Boolean isLogged(){
        return (mAuth.getCurrentUser() != null);
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


    private void getAllNews (){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    for(int i =0; i<myListOfDocuments.size(); i++){
                        Data d = new Data (myListOfDocuments.get(i).get("Address").toString(),
                                myListOfDocuments.get(i).get("temp").toString(),
                                myListOfDocuments.get(i).get("title").toString(),
                                myListOfDocuments.get(i).get("news").toString());
                        d.setId(myListOfDocuments.get(i).getId());
                        data.add(d);
                    }
                    mList.setAdapter(new CustomAdapter());
                }
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.news_custom_adapter, null);

            TextView Address = convertView.findViewById(R.id.mName_list);
            TextView temp = convertView.findViewById(R.id.mWeather_list);
            TextView title = convertView.findViewById(R.id.mNewsTitle_list);
            TextView news = convertView.findViewById(R.id.mNews_list);
            ImageView delete = convertView.findViewById(R.id.ic_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(position);
                }
            });

            Address.setText(data.get(position).getAddress());
            temp.setText(data.get(position).getTemp());
            title.setText(data.get(position).getNewsH());
            news.setText(data.get(position).getNews());

            return convertView;
        }

        public void delete(int i){

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection(mAuth.getCurrentUser().getUid())
                    .document(data.get(i).getId())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                data.clear();
                                getAllNews();
                            }else{
                                Toast.makeText(MainActivity.this, "Error just happened", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}
