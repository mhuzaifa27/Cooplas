package com.example.cooplas.activities.Travel.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooplas.R;
import com.example.cooplas.adapters.Travel.PreviousAddressesAdapter;
import com.example.cooplas.models.GoogleDistanceApi.CallbackGetDistanceTime;
import com.example.cooplas.models.Travel.Customer.Callbacks.CallbackCreateRide;
import com.example.cooplas.models.Travel.Customer.Callbacks.CallbackGetRecentPlaces;
import com.example.cooplas.models.Travel.Customer.RecentLocation;
import com.example.cooplas.utils.CheckConnectivity;
import com.example.cooplas.utils.CheckInternetEvent;
import com.example.cooplas.utils.Constants;
import com.example.cooplas.utils.CustomAutoCompleteTextView;
import com.example.cooplas.utils.KeyboardUtils;
import com.example.cooplas.utils.PlaceAutocompleteAdapter;
import com.example.cooplas.utils.PlaceJSONParser;
import com.example.cooplas.utils.ShowDialogues;
import com.example.cooplas.utils.retrofitJava.APIClient;
import com.example.cooplas.utils.retrofitJava.APIInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jobesk.gong.utils.FunctionsKt;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetAddressActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SetAddressActivity";
    private Context context = SetAddressActivity.this;
    private Activity activity = SetAddressActivity.this;

    private RecyclerView rv_previous_addresses;
    private LinearLayoutManager layoutManager;
    private List<RecentLocation> previousAddressesList = new ArrayList<>();
    private PreviousAddressesAdapter previousAddressesAdapter;

    private TextView tv_title;
    private RelativeLayout rl_done;
    private TextView tv_pick_up_address, tv_destination_address;
    private KProgressHUD progressHUD;

    private EventBus eventBus = EventBus.getDefault();
    private BroadcastReceiver mNetworkReceiver;
    private View parentLayout;
    private Map<String, String> params = new HashMap<>();


    /***Places Data***/
    private CustomAutoCompleteTextView et_address;
    private ProgressBar addressProgress;
    boolean isAddressSelected = false;
    private String place_id;
    private double LONGITUDE, LATITUDE;
    private PlacesTask placesTask;
    private ParserTask parserTask;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private SimpleAdapter adapter;
    private boolean setPickUpAddress = true, setDestinationAddress = false;
    private String startName, startLat, startLong, destName, destLat, destLong, distance, time;

    /***Places Data***/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_address);

        initGoogleClient();
        initComponents();
        getRecentPlaces();

        rl_done.setOnClickListener(this::onClick);
        tv_pick_up_address.setOnClickListener(this::onClick);
        tv_destination_address.setOnClickListener(this::onClick);
    }

    private void getRecentPlaces() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackGetRecentPlaces> call = apiInterface.getRecentPlaces("Bearer " + accessToken);
        call.enqueue(new Callback<CallbackGetRecentPlaces>() {
            @Override
            public void onResponse(Call<CallbackGetRecentPlaces> call, Response<CallbackGetRecentPlaces> response) {
                CallbackGetRecentPlaces responseGetRecentPlaces = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetRecentPlaces != null) {
                    if (responseGetRecentPlaces.getRecentLocations().size() > 0)
                        setData(responseGetRecentPlaces);
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackGetRecentPlaces> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage((FragmentActivity) activity, this)
                .build();


        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(context, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);
    }

    private void setData(CallbackGetRecentPlaces response) {
        previousAddressesList = response.getRecentLocations();
        previousAddressesAdapter.addAll(previousAddressesList);
        progressHUD.dismiss();

        previousAddressesAdapter.setOnIClickListener(new PreviousAddressesAdapter.IClicks() {
            @Override
            public void onItemClick(View view, RecentLocation recentLocation) {
                if(tv_pick_up_address.getText().toString().isEmpty()){
                    tv_pick_up_address.setText(recentLocation.getName());
                    params.put(Constants.START_ID,recentLocation.getId().toString());
                }
                else{
                    params.put(Constants.DESTINATION_ID,recentLocation.getId().toString());
                    tv_destination_address.setText(recentLocation.getName());
                }
            }
        });
    }

    private void initComponents() {
        eventBus.register(this);
        mNetworkReceiver = new CheckConnectivity();
        registerNetworkBroadcastForNougat();
        parentLayout = findViewById(android.R.id.content);

        progressHUD = KProgressHUD.create(activity);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);

        rv_previous_addresses = findViewById(R.id.rv_prevois_addresses);
        layoutManager = new LinearLayoutManager(context);
        previousAddressesAdapter = new PreviousAddressesAdapter(previousAddressesList, context);
        rv_previous_addresses.setLayoutManager(layoutManager);
        rv_previous_addresses.setAdapter(previousAddressesAdapter);

        tv_pick_up_address = findViewById(R.id.tv_pick_up_address);
        tv_destination_address = findViewById(R.id.tv_destination_address);

        rl_done = findViewById(R.id.rl_done);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_done:
                if (tv_pick_up_address.getText().toString().isEmpty())
                    Toast.makeText(context, "Enter Pick-up point", Toast.LENGTH_SHORT).show();
                else if (tv_destination_address.getText().toString().isEmpty())
                    Toast.makeText(context, "Enter Pick-up point", Toast.LENGTH_SHORT).show();
                else {
                    startName = tv_pick_up_address.getText().toString();
                    destName = tv_destination_address.getText().toString();
                    getDistanceAndTime();
                }
                //startActivity(new Intent(context, ReachedAtPickUpActivity.class));
                break;
            case R.id.tv_pick_up_address:
                setPickUpAddress = true;
                setDestinationAddress = false;
                showPlacesDialog();
                break;
            case R.id.tv_destination_address:
                setPickUpAddress = false;
                setDestinationAddress = true;
                showPlacesDialog();
                break;
        }
    }

    private void getDistanceAndTime() {
        progressHUD.show();
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClientForGoogleApi(context).create(APIInterface.class);
        Call<CallbackGetDistanceTime> call = apiInterface.getDistanceFromGoogleApi(startName, destName, "driving", "AIzaSyAJ1gx1nwkfFjjcQd7a-UAqJBM-KxANtbM");
        call.enqueue(new Callback<CallbackGetDistanceTime>() {
            @Override
            public void onResponse(Call<CallbackGetDistanceTime> call, Response<CallbackGetDistanceTime> response) {
                CallbackGetDistanceTime responseGetRecentPlaces = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetRecentPlaces != null) {
                    if (responseGetRecentPlaces.getRoutes().size() > 0) {
                        if (responseGetRecentPlaces.getRoutes().get(0).getLegs().get(0).getDistance().getText() != null) {
                            Log.d(TAG, "onResponse: " + responseGetRecentPlaces.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                            distance = responseGetRecentPlaces.getRoutes().get(0).getLegs().get(0).getDistance().getText();
                            if (responseGetRecentPlaces.getRoutes().get(0).getLegs().get(0).getDuration().getText() != null) {
                                Log.d(TAG, "onResponse: " + responseGetRecentPlaces.getRoutes().get(0).getLegs().get(0).getDuration().getText());
                                time = responseGetRecentPlaces.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                                params.put(Constants.START_NAME, startName);
                                params.put(Constants.START_LATITUDE, startLat);
                                params.put(Constants.START_LONGITUDE, startLong);
                                params.put(Constants.DESTINATION_NAME, destName);
                                params.put(Constants.DESTINATION_LATITUDE, destLat);
                                params.put(Constants.DESTINATION_LONGITUDE, destLong);
                                createRide(params);
                            } else {
                                Toast.makeText(SetAddressActivity.this, "No Route available!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } else {
                        Toast.makeText(SetAddressActivity.this, "No Route available!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackGetDistanceTime> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void createRide(Map<String, String> params) {
        String accessToken = FunctionsKt.getAccessToken(context);
        Log.d(TAG, "onResponse: " + accessToken);
        APIInterface apiInterface = APIClient.getClient(context).create(APIInterface.class);
        Call<CallbackCreateRide> call = apiInterface.createRide("Bearer " + accessToken, params);
        call.enqueue(new Callback<CallbackCreateRide>() {
            @Override
            public void onResponse(Call<CallbackCreateRide> call, Response<CallbackCreateRide> response) {
                CallbackCreateRide responseGetRecentPlaces = response.body();
                Log.d(TAG, "onResponse: " + response);
                if (responseGetRecentPlaces != null) {
                    progressHUD.dismiss();
                    params.clear();
//                    Toast.makeText(SetAddressActivity.this, "Ride Created!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();

                    intent.putExtra(Constants.CREATE_RIDE_OBJ,responseGetRecentPlaces);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                } else {
                    progressHUD.dismiss();
                    ShowDialogues.SHOW_SERVER_ERROR_DIALOG(context);
                }
            }

            @Override
            public void onFailure(Call<CallbackCreateRide> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d(TAG, "onResponse: " + t.getMessage());
                    progressHUD.dismiss();
                }
            }
        });
    }

    private void showPlacesDialog() {
        final Dialog dialog1 = new Dialog(context, R.style.MyDialogTheme);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_places_alert);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        et_address = dialog1.findViewById(R.id.et_address);
        addressProgress = dialog1.findViewById(R.id.addressProgress);

        et_address.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressProgress.setVisibility(View.VISIBLE);
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        et_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {

                if (parent != null && parent.getItemAtPosition(position) != null & !parent.getItemAtPosition(position).toString().isEmpty()) {
                    String result = parent.getItemAtPosition(position).toString();
                    isAddressSelected = true;

                    String delims = "[ {},]+";
                    String[] tokens = result.split(delims);
                    for (int i = 0; i < tokens.length; i++) {
                        if (tokens[i].contains("_id")) {
                            String id = tokens[i].split("[ =]+")[1];
                            place_id = id;
                            break;

                        }
                    }

                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, place_id)
                            .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getStatus().isSuccess()) {
                                        final Place myPlace = places.get(0);
                                        LatLng queriedLocation = myPlace.getLatLng();

                                        Log.v(TAG, "" + queriedLocation.latitude);
                                        Log.v(TAG, "" + queriedLocation.longitude);
                                        Log.d(TAG, "onResult: " + myPlace.getId());
                                    }
                                    places.release();
                                }
                            });


                    PlacesDetailTask placesDetailTask = new PlacesDetailTask();
                    placesDetailTask.execute(place_id);
                }

                //TODO Do something with the selected text
            }
        });

        et_address.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isAddressSelected) {
                    // TODO: 7/11/2020 Select text
                    getLatLng(et_address.getText().toString());
                    Log.d("lat_long", "onDismiss: " + LATITUDE + " " + LONGITUDE);
                    LatLng location = new LatLng(LATITUDE, LONGITUDE);
                    /*******Set locations to edit text*******/
                    if (setPickUpAddress)
                        tv_pick_up_address.setText(et_address.getText().toString());
                    else tv_destination_address.setText(et_address.getText().toString());
//                    tv_current_location.setText(et_address.getText().toString());
                    /*moveCamera(location,
                            DEFAULT_ZOOM,
                            "");*/
                    /*******Set locations to edit text*******/

                    dialog1.dismiss();
                    KeyboardUtils.hideKeyboard(context, et_address);
                    isAddressSelected = false;
                }
            }
        });

        dialog1.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /*******************************************************************/
    private class PlacesDetailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";
//https://maps.googleapis.com/maps/api/place/autocomplete/json?input=australia&types=geocode&sensor=false&key=AIzaSyC_qWc9xiI-TpRZcjHWC5fur3CziPeFOBA
            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyD5BeaBfn-VOyt3uC_eV3vluYJa6SsTh2A";


            // place type to be searched
            String placeid = "placeid=" + place_id;

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = placeid + "&" + "fields=geometry" + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            ParserPlaceDetailsTask parserPlaceDetailsTask = new ParserPlaceDetailsTask();

            // Starting Parsing the JSON string returned by Web Service
            parserPlaceDetailsTask.execute(result);
        }
    }

    private class ParserPlaceDetailsTask extends AsyncTask<String, Integer, HashMap<String, String>> {

        JSONObject jObject;

        @Override
        protected HashMap<String, String> doInBackground(String... jsonData) {

            HashMap<String, String> placesDetails = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                placesDetails = placeJsonParser.parsePlaceDetails(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return placesDetails;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {

            if (result != null && !result.isEmpty()) {

                String types = result.get("types");
                String postalcode = "";
                if (types != null && !types.isEmpty() && types.equals("postal_code")) {
                    postalcode = result.get("long_name");
                }
                //postalCode.setText(postalcode);
//                tvNext.setBackgroundResource(R.drawable.yes);
            }
            et_address.dismissDropDown();
        }
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {

            // For storing data from web service
            String data = "";
//https://maps.googleapis.com/maps/api/place/autocomplete/json?input=australia&types=geocode&sensor=false&key=AIzaSyC_qWc9xiI-TpRZcjHWC5fur3CziPeFOBA
            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyDv7B05izVAv4muJbK0-iq6N2Q3HTQEmyM";//AIzaSyCElLmSDZRMTvBK9WbKE5y3IedASjg4XvM

            String input = "";

            input = "input=" + place[0];//URLEncoder.encode(place[0], "utf-8");
            input = input.replace(" ", "%20");

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + "fields=geometry" + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("DOWNLOAD", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description", "reference", "_id"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            adapter = new SimpleAdapter(context, result, R.layout.custom_text_view, from, to);
            // Setting the adapter
            et_address.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (et_address == null) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            } else {
                et_address.showDropDown();
            }
//            addressProgress.setVisibility(View.GONE);
        }
    }

    public String getAddress(Double lat, Double lng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses.get(0).getAddressLine(0);

//
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
    }

    public void getLatLng(String address) {

        Geocoder coder = new Geocoder(context);
        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(address, 1);
            for (Address add : adresses) {
                if (address.length() > 0) {//Controls to ensure it is right address such as country etc.

                    if (setPickUpAddress) {
                        startLat = String.valueOf(add.getLatitude());
                        startLong = String.valueOf(add.getLongitude());

                    } else {
                        destLat = String.valueOf(add.getLatitude());
                        destLong = String.valueOf(add.getLongitude());
                    }
                    LONGITUDE = add.getLongitude();
                    LATITUDE = add.getLatitude();
//                    Toast.makeText(this, longitude + " lat: " + latitude, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage((FragmentActivity) activity);
        mGoogleApiClient.disconnect();
    }

    /*******************************************************************/

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
        unregisterNetworkChanges();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CheckInternetEvent event) {
        Log.d("SsS", "checkInternetAvailability: called");
        if (event.isIS_INTERNET_AVAILABLE()) {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_internet_available));

        } else {
            ShowDialogues.SHOW_SNACK_BAR(parentLayout, activity, getString(R.string.snackbar_check_internet));
        }
    }
}