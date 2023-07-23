package com.example.musicplayer.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLComponents;
import cafsoft.foundation.URLQueryItem;
import cafsoft.foundation.URLSession;

public class ItunesMusicServices {
    private URLComponents components = null;
    public ItunesMusicServices(){
        components = new URLComponents();
        components.setScheme("https");
        components.setHost("itunes.apple.com");
        components.setPath("/search");
    }

    public void searchSongsByTerm(String searchTerm, OnDataResponse delegate){
        components.setQueryItems(new URLQueryItem[]{
                new URLQueryItem("media","music"),
                new URLQueryItem("entity", "song"),
                new URLQueryItem("term", searchTerm)
        });
        URLSession.getShared().dataTask(components.getURL(),(data, response, error)->{
            HTTPURLResponse resp = (HTTPURLResponse) response;
            Root root = null;
            int statusCode = -1;

            if(error == null && resp.getStatusCode() == 200){
                String text = data.toText();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                root = gson.fromJson(text, Root.class);
                root.toString();
                Log.d("API", "SUCCESS");
                statusCode = resp.getStatusCode();

            }else {
                Log.d("API", "ERROR");
            }
            if(delegate !=  null){
                delegate.onChange(error != null, statusCode,root);
            }
        }).resume();
    }

    public interface OnDataResponse{
        public abstract void onChange(boolean isNetworkError, int statusCode, Root root);
    }
}
