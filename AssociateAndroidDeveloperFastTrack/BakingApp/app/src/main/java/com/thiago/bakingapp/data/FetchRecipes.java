package com.thiago.bakingapp.data;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchRecipes {

    private static final String TAG = FetchRecipes.class.getSimpleName();
    private static final String BAKING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static void getRecipes() {
        try {
            HttpUrl.Builder builder = HttpUrl.parse(BAKING_URL).newBuilder();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(builder.toString()).build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }

    }
}
