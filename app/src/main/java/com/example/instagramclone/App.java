package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Z1DjvRS6w8f4m9eKccNbpsIkBif6VvWRNoFvSFX2")
                // if defined
                .clientKey("8Wa9vHgg8H2YuvoG4COg369sn0woqOLpUcVwip5X")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
