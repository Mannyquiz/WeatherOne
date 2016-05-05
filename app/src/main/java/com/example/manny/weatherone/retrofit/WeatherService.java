package com.example.manny.weatherone.retrofit;

import com.example.manny.weatherone.data.CurrentWeather;
import com.example.manny.weatherone.data.FiveDay;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by manny on 5/2/16.
 */
public interface WeatherService {

    @GET("data/2.5/weather?")
    Call<CurrentWeather> currentWeatherByCity(@Query("q") String cityName,
                                              @Query("units") String unit,
                                              @Query("appid") String apiKey);

    @GET("data/2.5/forecast?")
    Call<FiveDay> fiveDayWeather(@Query("q") String cityName,
                                 @Query("units") String unit,
                                 @Query("appid") String apiKey);

}
