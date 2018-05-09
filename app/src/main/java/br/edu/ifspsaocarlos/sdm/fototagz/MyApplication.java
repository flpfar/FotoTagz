package br.edu.ifspsaocarlos.sdm.fototagz;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }

}
