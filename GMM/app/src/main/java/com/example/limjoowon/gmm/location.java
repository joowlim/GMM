package com.example.limjoowon.gmm;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by LimJoowon on 2016. 11. 3..
 */
public class location extends FragmentActivity implements OnMapReadyCallback {
    static final LatLng KAIST = new LatLng(36.37, 127.36);
    private GoogleMap googleMap;
    private PopupWindow pwindo;
    private Button btnClosePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        //  LayoutInflater 객체와 시킴
        LayoutInflater inflater = (LayoutInflater) location.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.activity_time,
                (ViewGroup) findViewById(R.id.time_popup));
        pwindo = new PopupWindow(layout, 1200, 2000, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

        btnClosePopup = (Button) layout.findViewById(R.id.remove_time_popup);
        btnClosePopup.setOnClickListener(   new OnClickListener() {

            public void onClick(View v) {
                pwindo.dismiss();
            }
        });
    }

}
