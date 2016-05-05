package com.example.manny.weatherone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.manny.weatherone.data.CurrentWeather;
import com.example.manny.weatherone.data.FiveDay;
import com.example.manny.weatherone.retrofit.WeatherService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_API = "http://api.openweathermap.org/";
    public static final String API_KEY = "38aad8b5df4ddfb3ecf7e23bbb108207";

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

        location = (TextView) findViewById(R.id.location);
        sunSet = (TextView) findViewById(R.id.sunSet);
        temp = (TextView) findViewById(R.id.temp);
        dayOne = (TextView) findViewById(R.id.dayOne);
        dayTwo = (TextView) findViewById(R.id.dayTwo);
        dayThree = (TextView) findViewById(R.id.dayThree);
        dayFour = (TextView) findViewById(R.id.dayFour);
        dayFive = (TextView) findViewById(R.id.dayFive);
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

        String dt = new java.text.SimpleDateFormat("ccc").format(new Date(fiveDay.getList().get(0).getDt())) + ": ";
        String dt2 = new java.text.SimpleDateFormat("ccc").format(fiveDay.getList().get(1).getDt()) + ": ";

        dayOne.setText(dt + Math.round(fiveDay.getList().get(0).getMain().getTemp()) + "\u2109");
        dayTwo.setText(dt2 + Math.round(fiveDay.getList().get(1).getMain().getTemp()) + "\u2109");
        dayThree.setText(Math.round(fiveDay.getList().get(2).getMain().getTemp()) + "\u2109");
        dayFour.setText(Math.round(fiveDay.getList().get(3).getMain().getTemp()) + "\u2109");
        dayFive.setText(Math.round(fiveDay.getList().get(4).getMain().getTemp()) + "\u2109");
    }

    @Override
    protected void onResume() {
        super.onResume();
        useRetrofit();
    }
}
