package com.shakilsustswe.locationtracker;

import com.shakilsustswe.locationtracker.Remote.IGoogleAPI;
import com.shakilsustswe.locationtracker.Remote.RetrofitClient;


public class Common {
    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleAPI getGoolgeAPI(){

        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
