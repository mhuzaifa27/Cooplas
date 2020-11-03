package com.example.cooplas.activities.Travel.Customer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cooplas.R;
import com.example.cooplas.utils.NetworkManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainCustomerActivity.class.getSimpleName();
    private Context context = MainCustomerActivity.this;
    private Activity activity = MainCustomerActivity.this;

    private DrawerLayout drawer_customer;
    private MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<MarkerOptions> optionsList = new ArrayList<>();

    private Marker marker;
    private String id;
    private Bitmap smallMarker;

    private LinearLayout ll_offline_header, ll_btns_accept_ignore, ll_btn_start_ride;
    private RelativeLayout rl_driver_info;

    private RelativeLayout rl_bottom_float_buttons, rl_customer_info;
    private CardView card_pick_up,card_address;
    private Switch swch_online_ofline;
    private Button btn_ignore_ride, btn_accept_ride, btn_start_ride;
    private TextView tv_my_wallet, tv_rides_history, tv_promo_codes, tv_add_address, tv_notifications,tv_invite_friends, tv_help_and_support, tv_log_out;

    private EditText et_address;
    private ImageView img_discover,img_menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        initComponents();
        getNearDarzi();

        card_address.setOnClickListener(this::onClick);

        et_address.setOnClickListener(this::onClick);

        tv_my_wallet.setOnClickListener(this::onClick);
        tv_rides_history.setOnClickListener(this::onClick);
        tv_add_address.setOnClickListener(this::onClick);
        tv_promo_codes.setOnClickListener(this::onClick);
        tv_notifications.setOnClickListener(this::onClick);
        tv_invite_friends.setOnClickListener(this::onClick);
        tv_help_and_support.setOnClickListener(this::onClick);
        tv_log_out.setOnClickListener(this::onClick);
        img_menu.setOnClickListener(this::onClick);
        img_discover.setOnClickListener(this::onClick);

       /* btn_accept_ride.setOnClickListener(this::onClick);
        btn_ignore_ride.setOnClickListener(this::onClick);
        btn_start_ride.setOnClickListener(this::onClick);
        tv_legal_documents.setOnClickListener(this::onClick);
        tv_manage_vehicles.setOnClickListener(this::onClick);*/

        /*swch_online_ofline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ll_offline_header.setVisibility(View.GONE);
                    rl_customer_info.setVisibility(View.VISIBLE);
                    rl_driver_info.setVisibility(View.GONE);
                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            googleMap = mMap;

                            // For showing a move to my location button
                            //googleMap.setMyLocationEnabled(true);

                            // For dropping a marker at a point on the Map
                            LatLng sydney = new LatLng(Double.parseDouble("-33.865143"), Double.parseDouble("151.209900"));
                            googleMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_customer_marker));
                            // For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    });
                } else {
                    ll_offline_header.setVisibility(View.VISIBLE);
                    rl_customer_info.setVisibility(View.GONE);
                    rl_driver_info.setVisibility(View.VISIBLE);
                    rl_bottom_float_buttons.setVisibility(View.GONE);
                    mMapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap mMap) {
                            googleMap = mMap;

                            // For showing a move to my location button
                            //googleMap.setMyLocationEnabled(true);

                            // For dropping a marker at a point on the Map
               *//* LatLng sydney = new LatLng(Double.parseDouble("-33.865143"), Double.parseDouble("151.209900"));
                googleMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_driver_marker));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*//*
                        }
                    });
                }
            }
        });*/

        int height = 150;
        int width = 100;
       /* BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_driver_marker);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);*/

        mMapView = (MapView) findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        //markers = new Hashtable<String, Darzi>();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(Double.parseDouble("-33.865143"), Double.parseDouble("151.209900"));
                googleMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_customer_marker));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    private void initComponents() {
        drawer_customer=findViewById(R.id.drawer_customer);

        et_address=findViewById(R.id.et_address);

        card_address=findViewById(R.id.card_address);

        tv_my_wallet=findViewById(R.id.tv_my_wallet);
        tv_rides_history=findViewById(R.id.tv_rides_history);
        tv_promo_codes=findViewById(R.id.tv_promo_codes);
        tv_notifications=findViewById(R.id.tv_notifications);
        tv_add_address=findViewById(R.id.tv_add_address);
        tv_invite_friends=findViewById(R.id.tv_invite_friends);
        tv_help_and_support=findViewById(R.id.tv_help_and_support);
        tv_log_out=findViewById(R.id.tv_log_out);

        img_menu=findViewById(R.id.img_menu);
        img_discover=findViewById(R.id.img_discover);

    }

    private void getNearDarzi() {
      /*  darziArrayList=new ArrayList<>();
        darziArrayList.add(new Darzi("Awais Aslam","-33.865149","151.209910"));
        darziArrayList.add(new Darzi("Huzaifa Sohail","-33.865153","151.210000"));
        darziArrayList.add(new Darzi("Jerry Kazz","-33.865243","151.209700"));
        darziArrayList.add(new Darzi("Steve Smith","-33.865173","151.209300"));*/
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (NetworkManager.isConnectToInternet(this)) {
          /*  parms.put(Consts.LATITUDE, prefrence.getValue(Consts.LATITUDE));
            parms.put(Consts.LONGITUDE, prefrence.getValue(Consts.LONGITUDE));*/
            // getArtist();
            setmap();

        } else {
            Toast.makeText(this, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void setmap() {
       /* for (int i = 0; i < darziArrayList.size(); i++) {

            optionsList.add(new MarkerOptions().position(
                    new LatLng(Double.parseDouble(darziArrayList.get(i).getLat()),
                            Double.parseDouble(darziArrayList.get(i).getLng()))).title(darziArrayList.get(i).getName()).
                    snippet("1"));

        }*/
        //    mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(MainCustomerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainCustomerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(false);

                // For dropping a marker at a point on the Map

                            /*    for (LatLng point : latlngs) {
                                    options.position(point);
                                    options.title("SAMYOTECH");
                                    options.snippet("SAMYOTECH");
                                    googleMap.addMarker(options);
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                }*/
                for (MarkerOptions options : optionsList) {

                    options.position(options.getPosition());
                    options.title(options.getTitle());
                    options.snippet(options.getSnippet());
                    options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    final Marker hamburg = googleMap.addMarker(options);

                    //                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(options.getPosition()).zoom(12).build();
//                                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                   /* for (int i = 0; i < darziArrayList.size(); i++) {

                        // if (darziArrayList.get(i).getUser_id().equalsIgnoreCase(options.getSnippet()))

                        markers.put(hamburg.getId(), darziArrayList.get(i));


                    }*/

                    //googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());


                }


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {
                        //startActivity(new Intent(NearByDarziActivity.this,EMasterDetailActivity.class));
                        /*Intent intent = new Intent(getBaseContext(), NearByDarziActivity.class);
                        intent.putExtra(Consts.ARTIST_ID, arg0.getSnippet());
                        // Starting the Place Details Activity
                        startActivity(intent);*/
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.btn_accept_ride:
                ll_btns_accept_ignore.setVisibility(View.GONE);
                ll_btn_start_ride.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_ignore_ride:
                break;*/
            case R.id.tv_my_wallet:
                drawer_customer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.tv_rides_history:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context,RidesHistoryActivity.class));
                break;
            case R.id.tv_promo_codes:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context,PromoCodeActivity.class));
                break;
            case R.id.tv_notifications:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context,NotificationActivity.class));
                break;
            case R.id.tv_add_address:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context,AddAddressActivity.class));
                break;
            case R.id.tv_invite_friends:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context,InviteFriendsActivity.class));
                break;
            case R.id.tv_help_and_support:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context,SupportActivity.class));
                break;
            case R.id.tv_log_out:
                break;
            case R.id.img_menu:
                drawer_customer.openDrawer(Gravity.LEFT);
                break;
            case R.id.img_discover:
                onBackPressed();
                break;
            case R.id.card_address:
                startActivity(new Intent(context,SetAddressActivity.class));
                break;
            case R.id.et_address:
                startActivity(new Intent(context,SetAddressActivity.class));
                break;
        }
    }


//    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
//
//        private View view;
//
//        public CustomInfoWindowAdapter() {
//            view = getLayoutInflater().inflate(R.layout.custom_info_window,
//                    null);
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//
//            if (NearByDarziActivity.this.marker != null
//                    && NearByDarziActivity.this.marker.isInfoWindowShown()) {
//                NearByDarziActivity.this.marker.hideInfoWindow();
//                NearByDarziActivity.this.marker.showInfoWindow();
//            }
//            return null;
//        }
//
//        @Override
//        public View getInfoWindow(final Marker marker) {
//            NearByDarziActivity.this.marker = marker;
//
//            String url = null;
//            String name = null;
//            String id = null;
//            String category = null;
//
//            if (marker.getId() != null && markers != null && markers.size() > 0) {
//                if (markers.get(marker.getId()) != null &&
//                        markers.get(marker.getId()) != null) {
//                   /* url = markers.get(marker.getId()).getImage();
//                    name = markers.get(marker.getId()).getName();
//                    id = markers.get(marker.getId()).getUser_id();
//                    category = markers.get(marker.getId()).getCategory_name();*/
//                }
//            }
//            //final ImageView image = ((ImageView) view.findViewById(R.id.badge));
//
//            if (url != null && !url.equalsIgnoreCase("null")
//                    && !url.equalsIgnoreCase("")) {
//                /*Glide.with(getApplicationContext()).
//                        load(url)
//                        .placeholder(R.drawable.ic_dummy_user)
//                        .dontAnimate()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .into(image);*/
//
//            } else {
//                // image.setImageResource(R.mipmap.ic_launcher);
//            }
//
//            /*//final String title = marker.getTitle();
//            final CustomTextViewBold titleUi = ((CustomTextViewBold) view.findViewById(R.id.title));
//            if (name != null) {
//                titleUi.setText(name);
//            } else {
//                titleUi.setText("");
//            }
//
//            //   final String snippet = marker.getSnippet();
//            final CustomTextView snippetUi = ((CustomTextView) view
//                    .findViewById(R.id.snippet));
//            if (category != null) {
//                snippetUi.setText(category);
//            } else {
//                snippetUi.setText("");
//            }*/
//
//            return view;
//        }
//    }
}