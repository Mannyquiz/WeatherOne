package com.example.manny.weatherone;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manny.weatherone.data.CurrentWeather;
import com.example.manny.weatherone.data.FiveDay;
import com.example.manny.weatherone.retrofit.WeatherService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_API = "http://api.openweathermap.org/";
    public static final String API_KEY = "38aad8b5df4ddfb3ecf7e23bbb108207";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private RelativeLayout relativeLayout;

    private TextView location;
    private TextView sunSet;
    private TextView temp;
    private TextView dayOne;
    private TextView dayTwo;
    private TextView dayThree;
    private TextView dayFour;
    private TextView dayFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
        }

        location = (TextView) findViewById(R.id.location);
        sunSet = (TextView) findViewById(R.id.sunSet);
        temp = (TextView) findViewById(R.id.temp);
        dayOne = (TextView) findViewById(R.id.dayOne);
        dayTwo = (TextView) findViewById(R.id.dayTwo);
        dayThree = (TextView) findViewById(R.id.dayThree);
        dayFour = (TextView) findViewById(R.id.dayFour);
        dayFive = (TextView) findViewById(R.id.dayFive);
    }



    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    /*
      Method to verify google play services on the device
      PLAY_SERVICES_RESOLUTION_REQUEST
     */

    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {
                GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void displayLocation() {

        try {
            mLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            Log.d("flow","security excepetion: " + e.getMessage());
        }

        if (mLocation != null) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();

            Log.d("flow", latitude + ", " + longitude);
            getAddress(longitude, latitude);

        } else {

            Snackbar.make(relativeLayout, "(Couldn't get the location. Make sure location is enabled on the device)", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    public void useRetrofit(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        WeatherService service = retrofit.create(WeatherService.class);

        Call<CurrentWeather> weather = service.currentWeatherByCity("NewYork,us", "imperial", API_KEY);

        weather.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Response<CurrentWeather> response, Retrofit retrofit) {
                Log.d("flow", "success: " + response.body().getName() + ", " + response.body().getSys().getCountry());
                Log.d("flow", "success: " + response.body().getMain().getTemp());
                Log.d("flow", "success: " + response.body().getMain().getHumidity());
                Log.d("flow", "success: " + response.body().getWind().getSpeed());
                Log.d("flow", "success: " + response.body().getSys().getSunset());

                populateWeather(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("flow", "failure");
            }
        });

        Call<FiveDay> fiveDayCall = service.fiveDayWeather("NewYork,us", "imperial", API_KEY);
        fiveDayCall.enqueue(new Callback<FiveDay>() {
            @Override
            public void onResponse(Response<FiveDay> response, Retrofit retrofit) {



                Log.d("flow", "success: " + response.body().getCity().getName());
                Log.d("flow", "success: " + response.body().getList().get(0).getMain().getTemp());
                Log.d("flow", "success: " + response.body().getList().get(1).getMain().getTemp());
                Log.d("flow", "success: " + response.body().getList().get(2).getMain().getTemp());
                populateFiveDayWeather(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("flow", "failure");
            }
        });

    }

    private void populateWeather(CurrentWeather currentWeather){

        String sun = new java.text.SimpleDateFormat("h:mm").format(new Date(currentWeather.getSys().getSunset()));

        location.setText(currentWeather.getName() + ", " + currentWeather.getSys().getCountry());
        sunSet.setText("Sunset at " + sun +" PM");
        temp.setText(Math.round(currentWeather.getMain().getTemp()) + "\u2109");
    }

    private void populateFiveDayWeather(FiveDay fiveDay){

        parseTime(fiveDay.getList().get(0).getDt());
        parseTime(fiveDay.getList().get(1).getDt());
        parseTime(fiveDay.getList().get(2).getDt());
        parseTime(fiveDay.getList().get(3).getDt());
        parseTime(fiveDay.getList().get(4).getDt());
        parseTime(fiveDay.getList().get(5).getDt());
        parseTime(fiveDay.getList().get(6).getDt());
        parseTime(fiveDay.getList().get(7).getDt());

        String dt2 = new java.text.SimpleDateFormat("ccc").format(fiveDay.getList().get(1).getDt()) + ": ";

        dayOne.setText(Math.round(fiveDay.getList().get(0).getMain().getTemp()) + "\u2109");
        dayTwo.setText(dt2 + Math.round(fiveDay.getList().get(1).getMain().getTemp()) + "\u2109");
        dayThree.setText(Math.round(fiveDay.getList().get(2).getMain().getTemp()) + "\u2109");
        dayFour.setText(Math.round(fiveDay.getList().get(3).getMain().getTemp()) + "\u2109");
        dayFive.setText(Math.round(fiveDay.getList().get(4).getMain().getTemp()) + "\u2109");
    }

    private void parseTime(long utcTime){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        /* debug: is it local time? */
        Log.d("Time zone: ", tz.getDisplayName());

        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("ccc");
        sdf.setTimeZone(tz);

        /* print your timestamp and double check it's the date you expect */
        long timestamp = utcTime;
        String localTime = sdf.format(new Date(timestamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        Log.d("Time: ", localTime);
    }

    /*
    *
    * */

    private void getAddress(double longitude, double latitude) {
        Geocoder geocoder;

        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            Log.d("flow", e.getMessage());
        } catch (IllegalArgumentException e){
            Log.d("flow", e.getMessage());
        }

        if(addresses != null) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.d("flow", "this is the zip code: " + city + ", " + state + " " + country);

            //getCurrentForecastByZip(postalCode);
        } else {

            Log.d("flow", "Geo Location Addresses was null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        useRetrofit();
    }
}
