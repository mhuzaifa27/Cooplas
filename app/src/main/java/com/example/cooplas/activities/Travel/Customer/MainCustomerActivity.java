package com.example.cooplas.activities.Travel.Customer;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cooplas.R;
import com.example.cooplas.models.Travel.Customer.CallbackAcceptRide.CallbackAcceptRide;
import com.example.cooplas.models.Travel.Customer.CallbackSearchForVehicle.CallbackSearchForVehicle;
import com.example.cooplas.models.Travel.Customer.CallbackUpdateVehicleType.CallbackUpdateVehicleType;
import com.example.cooplas.models.Travel.Customer.Callbacks.CallbackCreateRide;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.DirectionPointListener;
import com.example.cooplas.utils.GetPathFromLocation;
import com.example.cooplas.utils.NetworkManager;
import com.example.cooplas.utils.RippleEffectLoader;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainCustomerActivity extends Activity implements View.OnClickListener {

    private String TAG = MainCustomerActivity.class.getSimpleName();
    private Context context = MainCustomerActivity.this;
    private Activity activity = MainCustomerActivity.this;
    public static final int REQUEST_TO_SET_ADDRESSES = 2533;
    public static final int REQUEST_DRIVER_REACHED = 2531;
    public static final int REQUEST_RIDE_COMPLETE = 2521;
    public static final int REQUEST_TO_UPDATE_ADDRESSES = 2534;

    private DrawerLayout drawer_customer;
    private MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<MarkerOptions> optionsList = new ArrayList<>();

    private Marker marker;
    private String id;
    private Bitmap smallMarker;

    private LinearLayout ll_offline_header, ll_btns_accept_ignore, ll_btn_start_ride,
            ll_car_type_economy, ll_car_type_luxury, ll_car_type_family;
    private RelativeLayout rl_driver_info;

    private RelativeLayout rl_bottom_float_buttons, rl_customer_info, rl_car_types, rl_confirm, rl_filled_addresses;
    private CardView card_pick_up, card_address;
    private Switch swch_online_ofline;
    private Button btn_ignore_ride, btn_accept_ride, btn_start_ride;
    private TextView tv_my_wallet, tv_rides_history, tv_promo_codes, tv_add_address, tv_notifications, tv_invite_friends, tv_help_and_support, tv_log_out;

    private EditText et_address;
    private ImageView img_discover, img_menu;
    private TextView tv_calculated_fare, tv_pick_up_address, tv_destination_address, tv_title_text,
            tv_driver_name, tv_vehicle_maker, tv_vehicle_reg_num, tv_destination;
    private CircleImageView img_driver,img_user,img_user_menu;

    private CallbackCreateRide callbackCreateRide;
    private CallbackAcceptRide callbackAcceptRide;
    private KProgressHUD progressHUD;
    private RippleEffectLoader rippleEffectLoader;
    private Map<String, String> updateVehicleTypeParams = new HashMap<>();
    private Double lati = 31.465214, longi = 74.253296;
    private boolean isMarkerRotating = false;
    private Location targetLocation;
    private int loop = 0;
    private TextView tv_user_name_menu,tv_user_name_pet_menu;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        initComponents();
        getNearDarzi();

        card_address.setOnClickListener(this::onClick);
        et_address.setOnClickListener(this::onClick);
        ll_car_type_economy.setOnClickListener(this::onClick);
        ll_car_type_luxury.setOnClickListener(this::onClick);
        ll_car_type_family.setOnClickListener(this::onClick);
        rl_confirm.setOnClickListener(this::onClick);
        rl_filled_addresses.setOnClickListener(this::onClick);

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
                LatLng sydney = new LatLng(lati, longi);
                // Marker markerName=googleMap.addMarker(new MarkerOptions().position(sydney)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_customer_marker));
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    private void initComponents() {
        targetLocation = new Location("");
        progressHUD = KProgressHUD.create(activity);
        rippleEffectLoader = new RippleEffectLoader(activity, false);

        drawer_customer = findViewById(R.id.drawer_customer);

        et_address = findViewById(R.id.et_address);

        card_address = findViewById(R.id.card_address);

        tv_my_wallet = findViewById(R.id.tv_my_wallet);
        tv_rides_history = findViewById(R.id.tv_rides_history);
        tv_promo_codes = findViewById(R.id.tv_promo_codes);
        tv_notifications = findViewById(R.id.tv_notifications);
        tv_add_address = findViewById(R.id.tv_add_address);
        tv_invite_friends = findViewById(R.id.tv_invite_friends);
        tv_help_and_support = findViewById(R.id.tv_help_and_support);
        tv_log_out = findViewById(R.id.tv_log_out);
        tv_calculated_fare = findViewById(R.id.tv_calculated_fare);
        tv_pick_up_address = findViewById(R.id.tv_pick_up_address);
        tv_destination_address = findViewById(R.id.tv_destination_address);
        tv_title_text = findViewById(R.id.tv_title_text);
        tv_vehicle_maker = findViewById(R.id.tv_vehicle_maker);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_vehicle_reg_num = findViewById(R.id.tv_vehicle_reg_num);
        tv_destination = findViewById(R.id.tv_destination);
        tv_user_name_menu = findViewById(R.id.tv_user_name_menu);
        tv_user_name_pet_menu = findViewById(R.id.tv_user_name_pet_menu);

        img_menu = findViewById(R.id.img_menu);
        img_discover = findViewById(R.id.img_discover);
        img_driver = findViewById(R.id.img_driver);
        img_user=findViewById(R.id.img_user);
        img_user_menu=findViewById(R.id.img_user_menu);

        rl_car_types = findViewById(R.id.rl_car_types);
        rl_confirm = findViewById(R.id.rl_confirm);
        rl_filled_addresses = findViewById(R.id.rl_filled_addresses);
        rl_customer_info = findViewById(R.id.rl_customer_info);

        ll_car_type_economy = findViewById(R.id.ll_car_type_economy);
        ll_car_type_luxury = findViewById(R.id.ll_car_type_luxury);
        ll_car_type_family = findViewById(R.id.ll_car_type_family);

        Glide
                .with(context)
                .load(FunctionsKt.getImage(context))
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_dummy_user)
                .into(img_user);

        Glide
                .with(context)
                .load(FunctionsKt.getImage(context))
                .centerCrop()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_dummy_user)
                .into(img_user_menu);
        tv_user_name_menu.setText(FunctionsKt.getFirstName(context)+" "+FunctionsKt.getLastName(context));
        tv_user_name_pet_menu.setText("@"+FunctionsKt.getUserName(context));

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
                startActivity(new Intent(context, RidesHistoryActivity.class));
                break;
            case R.id.tv_promo_codes:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, PromoCodeActivity.class));
                break;
            case R.id.tv_notifications:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, NotificationActivity.class));
                break;
            case R.id.tv_add_address:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, AddAddressActivity.class));
                break;
            case R.id.tv_invite_friends:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, InviteFriendsActivity.class));
                break;
            case R.id.tv_help_and_support:
                drawer_customer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(context, SupportActivity.class));
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

                break;
            case R.id.et_address:
                startActivityForResult(new Intent(context, SetAddressActivity.class), REQUEST_TO_SET_ADDRESSES);
                break;
            case R.id.ll_car_type_economy:
                updateVehicleTypeParams.put(Constants.RIDE_ID, callbackCreateRide.getRide().getId().toString());
                updateVehicleTypeParams.put(Constants.VEHICLE_TYPE, getString(R.string.car_economy));
                selectCarType(1);
                break;
            case R.id.ll_car_type_luxury:
                updateVehicleTypeParams.put(Constants.RIDE_ID, callbackCreateRide.getRide().getId().toString());
                updateVehicleTypeParams.put(Constants.VEHICLE_TYPE, getString(R.string.car_luxury));
                selectCarType(2);
                break;
            case R.id.ll_car_type_family:
                updateVehicleTypeParams.put(Constants.RIDE_ID, callbackCreateRide.getRide().getId().toString());
                updateVehicleTypeParams.put(Constants.VEHICLE_TYPE, getString(R.string.car_family));
                selectCarType(3);
                break;
            case R.id.rl_confirm:
                searchForVehicle();
                break;
        }
    }

    private void searchForVehicle() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackSearchForVehicle> call = apiInterface.searchForVehicle("Bearer " + accessToken, callbackCreateRide.getRide().getId().toString());
        call.enqueue(new Callback<CallbackSearchForVehicle>() {
            @Override
            public void onResponse(Call<CallbackSearchForVehicle> call, Response<CallbackSearchForVehicle> response) {
                CallbackSearchForVehicle responseUpdateCarType = response.body();
                if (responseUpdateCarType != null) {
                    progressHUD.dismiss();
                    rl_filled_addresses.setVisibility(View.GONE);
                    rl_car_types.setVisibility(View.GONE);
                    tv_title_text.setText(R.string.searching_for_vehicle);
                    rippleEffectLoader.showIndicator();
                    new CountDownTimer(6000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onFinish() {
                            // TODO Auto-generated method stub
                            acceptRide();
                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(Call<CallbackSearchForVehicle> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void acceptRide() {
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackAcceptRide> call = apiInterface.acceptRide("Bearer EZ8OOnBW4JAvsCffaZBJKX9sygyMSH9V4xYAXvDQKj6A6sqXzBC3BbVD0mrH", callbackCreateRide.getRide().getId().toString());
        call.enqueue(new Callback<CallbackAcceptRide>() {
            @Override
            public void onResponse(Call<CallbackAcceptRide> call, Response<CallbackAcceptRide> response) {
                CallbackAcceptRide responseAcceptRide = response.body();
                if (responseAcceptRide != null) {
                    setDirectionsOnMap(responseAcceptRide);
                }
            }

            @Override
            public void onFailure(Call<CallbackAcceptRide> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    rippleEffectLoader.hideIndicator();
                }
            }
        });
    }

    private void setDirectionsOnMap(CallbackAcceptRide response) {
        callbackAcceptRide = response;
        rippleEffectLoader.hideIndicator();
        rl_customer_info.setVisibility(View.VISIBLE);
        rl_filled_addresses.setVisibility(View.GONE);
        tv_title_text.setText(R.string.vehicle_is_on_the_way);

        tv_driver_name.setText(response.getDriver().getFirstName() + " " + response.getDriver().getLastName());
        tv_vehicle_maker.setText(response.getVehicle().getMaker() + "-" + response.getVehicle().getModel());
        tv_vehicle_reg_num.setText(response.getVehicle().getRegistrationNumber());
        tv_destination.setText(response.getRide().getDestination().getName());

        if (response.getDriver().getProfilePic() != null) {
            Glide
                    .with(context)
                    .load(response.getDriver().getProfilePic())
                    .centerCrop()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_dummy_user)
                    .into(img_driver);
        }

        LatLng source = new LatLng(31.467077, 74.249046);
        MarkerOptions markerDriver = new MarkerOptions().position(source).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo));
        Marker m1 = googleMap.addMarker(markerDriver);

        LatLng destination = new LatLng(lati, longi);
        MarkerOptions markerUser = new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_customer_marker));
        googleMap.addMarker(markerUser);

        new GetPathFromLocation(context, source, destination, new DirectionPointListener() {
            @Override
            public void onPath(PolylineOptions polyLine) {
                googleMap.addPolyline(polyLine);
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(context, ReachedAtPickUpActivity.class);
                        intent.putExtra(Constants.RIDE_ID, callbackCreateRide.getRide().getId().toString());
                        startActivityForResult(intent, REQUEST_DRIVER_REACHED);
                    }
                }.start();
                //startDriverMovement(response, m1);
            }
        }).execute();
    }

    private void startDriverMovement(CallbackAcceptRide response, Marker markerDriver) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //calling method to animate marker
                if (GetPathFromLocation.listOfPaths.size() > loop) {
                    loop++;
                    targetLocation.setLatitude(Double.parseDouble(GetPathFromLocation.listOfPaths.get(loop).get("lat")));
                    targetLocation.setLongitude(Double.parseDouble(GetPathFromLocation.listOfPaths.get(loop).get("lng")));

                    animateMarkerNew(targetLocation, markerDriver);

                    handler.postDelayed(this, 3000);
                } else {
//                    startActivity(new Intent(context,ReachedAtPickUpActivity.class));
                }
            }
        }, 3000);
    }

    private void animateMarkerNew(final Location destination, final Marker marker) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(15.5f)
                                .build()));

                        marker.setRotation(getBearing(startPosition, new LatLng(destination.getLatitude(), destination.getLongitude())));
                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    //Method for finding bearing between two points
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
    /*private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {
        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }
    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final LinearInterpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }*/

    private void selectCarType(int i) {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackUpdateVehicleType> call = apiInterface.updateRideVehicleType("Bearer " + accessToken, updateVehicleTypeParams);
        call.enqueue(new Callback<CallbackUpdateVehicleType>() {
            @Override
            public void onResponse(Call<CallbackUpdateVehicleType> call, Response<CallbackUpdateVehicleType> response) {
                CallbackUpdateVehicleType responseUpdateCarType = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseUpdateCarType != null) {
                    updateCarType(responseUpdateCarType, i);
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackUpdateVehicleType> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void updateCarType(CallbackUpdateVehicleType response, int i) {
        if (i == 1) {
            ll_car_type_economy.setBackground(getResources().getDrawable(R.drawable.round_orange_small));
            ll_car_type_luxury.setBackground(getResources().getDrawable(R.drawable.round_white_small));
            ll_car_type_family.setBackground(getResources().getDrawable(R.drawable.round_white_small));
        } else if (i == 2) {
            ll_car_type_economy.setBackground(getResources().getDrawable(R.drawable.round_white_small));
            ll_car_type_luxury.setBackground(getResources().getDrawable(R.drawable.round_orange_small));
            ll_car_type_family.setBackground(getResources().getDrawable(R.drawable.round_white_small));
        } else {
            ll_car_type_economy.setBackground(getResources().getDrawable(R.drawable.round_white_small));
            ll_car_type_luxury.setBackground(getResources().getDrawable(R.drawable.round_white_small));
            ll_car_type_family.setBackground(getResources().getDrawable(R.drawable.round_orange_small));
        }
        updateVehicleTypeParams.clear();
        tv_calculated_fare.setText(response.getRide().getPrice().toString());
        progressHUD.dismiss();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_TO_SET_ADDRESSES && resultCode == RESULT_OK) {
//            Toast.makeText(context, "here1", Toast.LENGTH_SHORT).show();
            Log.d("REQUEST", "onActivityResult: " + data);
            if (data != null) {
//                Toast.makeText(context, "here2", Toast.LENGTH_SHORT).show();
                callbackCreateRide = (CallbackCreateRide) data.getExtras().getSerializable(Constants.CREATE_RIDE_OBJ);
                Log.d("REQUEST", "onActivityResult: " + callbackCreateRide);
                selectCarTypeView(callbackCreateRide);
            }
        }
        if (requestCode == REQUEST_DRIVER_REACHED) {
            if (resultCode == RESULT_OK) {
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(context, RideCompleteActivity.class);
                        intent.putExtra(Constants.CREATE_RIDE_OBJ, callbackAcceptRide);
                        startActivityForResult(intent,REQUEST_RIDE_COMPLETE);
                    }
                }.start();
            } else {
                recreate();
            }
        }
        if(requestCode==REQUEST_RIDE_COMPLETE && resultCode==RESULT_OK){
            recreate();
        }
    }

    private void selectCarTypeView(CallbackCreateRide callbackCreateRide) {
        tv_title_text.setText(R.string.select_vehicle_type);
        tv_calculated_fare.setText(callbackCreateRide.getRide().getPrice().toString());
        tv_pick_up_address.setText(callbackCreateRide.getStart().getName());
        tv_destination_address.setText(callbackCreateRide.getDestination().getName());

        rl_filled_addresses.setVisibility(View.VISIBLE);
        card_address.setVisibility(View.GONE);
        rl_car_types.setVisibility(View.VISIBLE);
    }
}