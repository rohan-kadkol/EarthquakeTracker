package com.rohan.earthquaketracker.retrofit;

import com.rohan.earthquaketracker.pojos.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiMethods {
//        @GET("query?format=geojson&eventtype=earthquake&minmagnitude=5")
    @GET("query?format=geojson&eventtype=earthquake")
    Call<ApiResponse> getEarthquakes(@Query("limit") int limit);
}
// TODO: Date utility class to show year when not the current year. Also can implement to show
//  "Today" instead of "Oct 10"
