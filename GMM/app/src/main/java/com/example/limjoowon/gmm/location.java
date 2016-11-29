package com.example.limjoowon.gmm;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by LimJoowon on 2016. 11. 3..
 */
public class location extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener  {
    static final LatLng KAIST = new LatLng(36.37, 127.36);
    private GoogleMap googleMap;
    Marker myLocMarker;
    Marker groupLocMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button set_my_location =(Button)findViewById(R.id.set_my_location);
        set_my_location.setOnClickListener(this);
        Button dont_know =(Button)findViewById(R.id.dont_know);
        dont_know.setOnClickListener(this);
        Button set_group_location =(Button)findViewById(R.id.set_group_location);
        set_group_location.setOnClickListener(this);
        Button remove_location =(Button)findViewById(R.id.remove_location);
        remove_location.setOnClickListener(this);
        Button absent =(Button)findViewById(R.id.absent);
        remove_location.setOnClickListener(this);
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
            case R.id.remove_location:
                finish();
                break;
            case R.id.set_my_location:
                if (myLocMarker == null) {
                    myLocMarker = googleMap.addMarker(new MarkerOptions()
                            .position(KAIST)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(20)));
                }
                else{
                    myLocMarker.remove();
                    myLocMarker = null;
                }
                break;
            case R.id.set_group_location:
                if (groupLocMarker == null) {
                    groupLocMarker = googleMap.addMarker(new MarkerOptions()
                            .position(KAIST)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.defaultMarker(80)));
                }
                else{
                    groupLocMarker.remove();
                    groupLocMarker = null;
                }
                break;
        }

    }
}
