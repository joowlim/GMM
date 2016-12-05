package com.example.limjoowon.gmm;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.limjoowon.gmm.config.UserConfig;
import com.example.limjoowon.gmm.module.LocationManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LimJoowon on 2016. 11. 3..
 */
public class location extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener  {
    static final LatLng KAIST = new LatLng(36.37, 127.36);
    //36.374517, 127.365347 N1
    //36.368305, 127.364789 E3
    private GoogleMap googleMap;
    Marker myLocMarker;
    double[][] groupLocMarkers = {{36.374517, 127.365347}, {36.368305, 127.364789}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("장소");

        Button set_my_location =(Button)findViewById(R.id.set_my_location);
        set_my_location.setOnClickListener(this);
        Button refresh =(Button)findViewById(R.id.loc_refresh);
        refresh.setOnClickListener(this);
        Button recommend =(Button)findViewById(R.id.recommend);
        recommend.setOnClickListener(this);

    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        //(location, zoop level)으로 카메라 이동
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KAIST, 15));


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)

        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);
    }
    public void meeting_time(View view){

        Intent i = new Intent(location.this, MeetingTime.class);
        startActivity(i);
    }

    public void onClick(View v) {
        //여기에 할 일을 적어주세요.
        switch(v.getId()){
            case R.id.set_my_location:
                if (myLocMarker == null) {
                    myLocMarker = googleMap.addMarker(new MarkerOptions()
                            .position(KAIST)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(10)));
                }
                else{
                    myLocMarker.remove();
                    myLocMarker = null;
                }
                break;
            case R.id.loc_refresh:
                if (myLocMarker != null) {
                    LocationManager.sendLocationInfo("abcd", UserConfig.getInstance().getUserId(),
                            myLocMarker.getPosition().latitude, myLocMarker.getPosition().longitude, onSendLocResponse);
                }
                Toast.makeText(location.this, "hi", Toast.LENGTH_SHORT).show();
                //LocationManager.getAllLocationInfo("abcd", onGetLocResponse);
                updateMarkers();
                break;
        }

    }
    public void updateMarkers(){
        for (int i=0; i<groupLocMarkers.length; i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(groupLocMarkers[i][0], groupLocMarkers[i][1]))
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(20)));
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private Callback onSendLocResponse = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            int debug;
            Log.e("send", "hi",e);
            debug = 1;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                Log.v("send", "bb");
                String result = response.body().string();
                JSONObject object = new JSONObject(result);
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(location.this, "내 장소를 업데이트 하였습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
            } catch(Exception e) {
            }
        }
    };
    private Callback onGetLocResponse = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            int debug;
            Log.v("get", e.getMessage());
            debug = 1;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();
                JSONObject object = new JSONObject(result);
                final String roomId = object.getString("room_id");
                final String name = object.getString("user_id");
                final double pos_lat = object.getDouble("pos_lat");
                final double pos_lon = object.getDouble("pos_lon");


                if (roomId == null) {
                } else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(location.this, "hahaahahA", Toast.LENGTH_SHORT).show();

                        }
                    });
            } catch(Exception e) {
            }
        }
    };
}
