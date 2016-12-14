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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LimJoowon on 2016. 11. 3..
 */
public class location extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    static final LatLng KAIST = new LatLng(36.371, 127.36);
    static final LatLng N1 = new LatLng(36.374517, 127.365347);
    static final LatLng E3 = new LatLng(36.368305, 127.364789);
    private GoogleMap googleMap;
    Marker myLocMarker;
    //초기화는 서버 연결 안 됐을 때 죽지 말라고
    double[][] groupLocMarkers = {{KAIST.latitude, KAIST.longitude}, {N1.latitude, N1.longitude}, {E3.latitude, E3.longitude}};
    long timeSent = 0;

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


        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
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

    public void onClick(View v) {
        //여기에 할 일을 적어주세요.
        switch(v.getId()){
            case R.id.set_my_location:
                if (myLocMarker == null) {
                    myLocMarker = googleMap.addMarker(new MarkerOptions()
                            .position(N1)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(5)));
                }
                else{
                    myLocMarker.remove();
                    myLocMarker = null;
                }
                break;
            case R.id.loc_refresh:
                try {
                    if (myLocMarker != null) {
                        timeSent = System.currentTimeMillis();
                        LocationManager.sendLocationInfo("abcd", UserConfig.getInstance().getUserId(),
                                myLocMarker.getPosition().latitude, myLocMarker.getPosition().longitude, onSendLocResponse);
                        Log.v("refresh",myLocMarker.getPosition().latitude+", "+myLocMarker.getPosition().longitude);
                    }
                    Thread.sleep(300);


                   break;
                } catch(InterruptedException e) {}
            case R.id.recommend:
                moveToMeetLocationList();
                /*
                Intent i = new Intent(location.this, MeetLocationListActivity.class);
                startActivity(i);
                */
                break;
        }
    }

    /**
     * 만남 장소 목록 화면으로 이동
     */
    private void moveToMeetLocationList() {
        Intent intent = new Intent(location.this, MeetLocationListActivity.class);
        int len;
        double N1diff = 0.0, E3diff = 0.0;

        len = groupLocMarkers.length;
        for(int i=0; i<len; i++) {
            LatLng currLatLng = new LatLng(groupLocMarkers[i][0], groupLocMarkers[i][1]);
            N1diff += getDist(currLatLng,N1);
            E3diff += getDist(currLatLng,E3);
        }

        String location = "N1";
        if (E3diff < N1diff) {
            location = "E3";
        }

        intent.putExtra("LocationOfBuilding", location);
        startActivity(intent);
    }

    /**
     * 두 점 사이의 거리를 구한다.
     */
    private double getDist(LatLng d1, LatLng d2) {
        double latDiff = Math.abs((d1.latitude*100) - (d2.latitude*100));
        double lngDiff = Math.abs((d1.longitude*100) - (d2.longitude*100));

        return Math.sqrt( Math.pow(latDiff,2) + Math.pow(lngDiff,2));
    }

    public void updateMarkers(){
        myLocMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(groupLocMarkers[0][0], groupLocMarkers[0][1]))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(5)));
        for (int i=1; i<groupLocMarkers.length; i++) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(groupLocMarkers[i][0], groupLocMarkers[i][1]))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(25)));
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
           debug = 1;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();
                JSONObject object = new JSONObject(result);
                LocationManager.getAllLocationInfo("abcd", onGetLocResponse);
            } catch(Exception e) {
            }
        }
    };

    private Callback onGetLocResponse = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            int debug;
            debug = 1;
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String jsonData = response.body().string();
                JSONArray Jarray = new JSONArray(jsonData);

                groupLocMarkers = new double[Jarray.length()][2];

                String name;
                double pos_lat;
                double pos_lon;
                for (int i=0; i<Jarray.length(); i++) {
                     Log.d("getLoc i", ""+i);
                     JSONObject object = Jarray.getJSONObject(i);
                     name = object.getString("user");
                     pos_lat = object.getDouble("pos_lat");
                     pos_lon = object.getDouble("pos_lon");

                    if (UserConfig.getInstance().getUserId().equals(name) && i != 0){
                            groupLocMarkers[i][0] = groupLocMarkers[0][0];
                            groupLocMarkers[i][1] = groupLocMarkers[0][1];
                            groupLocMarkers[0][0] = pos_lat;
                            groupLocMarkers[0][1] = pos_lon;
                    }
                    else{
                        groupLocMarkers[i][0] = pos_lat;
                        groupLocMarkers[i][1] = pos_lon;
                    }
                    Log.d("getLoc l", ""+Jarray.getJSONObject(0).getDouble("pos_lat"));
                    Log.d("getLoc m", ""+groupLocMarkers[0][0]);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("[Location API]",Long.toString(System.currentTimeMillis()-timeSent));
                        Toast.makeText(location.this, "내 장소를 업데이트 하였습니다", Toast.LENGTH_SHORT).show();
                        googleMap.clear();
                        updateMarkers();
                    }
                });

            } catch(Exception e) {
                Log.d("getLoc", e.getMessage());
            }
        }
    };
}
