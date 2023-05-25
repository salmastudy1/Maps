package com.example.lec10_maps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class MyNavigationService extends Service {
    public MyNavigationService(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
