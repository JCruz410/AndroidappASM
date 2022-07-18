package com.example.androidapp;

import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import android.widget.Toast;

import androidx.annotation.NonNull;

public class Connectivity {
    public static String geturl (String url_esp32){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_esp32)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });

        try
        {
            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch(IOException error) {

            return error.toString();

        }

    }
}
