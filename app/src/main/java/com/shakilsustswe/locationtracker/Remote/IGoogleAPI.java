package com.shakilsustswe.locationtracker.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPI {
    Call<String> getPath(String url);
}
