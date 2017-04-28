package com.ictmakers.eventmarker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.adobe.phonegap.push.PushConstants;
import com.adobe.phonegap.push.PushPlugin;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Andrea on 24/04/2017.
 */

public class SendPostionHandler implements NotificationHandler, GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient mGoogleApiClient;

    protected SendPostionHandler() {
    }

    public boolean canHandle(Bundle extras) {
        String info = extras.getString("info");
        Log.d("SendPosition", "info: " + info);
        return info != null && info.equals("GetPosition");
    }

    public void handle(Context context, Bundle extras) {

        CommandManager.instance.setAccessToken(extras.getString("accessToken"));

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try
        {
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (currentLocation != null) {

                Log.d("SendPosition", "Current location: " + currentLocation.toString());

                String regId = PushPlugin.getRegistrationID();
                SetPosizioneUtente command = new SetPosizioneUtente();
                command.Latitudine = currentLocation.getLatitude();
                command.Longitudine = currentLocation.getLongitude();
                command.Data = new Date();

                CommandManager.instance.sendCommandTask(command, Boolean.TYPE).execute();
            }
            else
                Log.d("SendPosition", "no location");
        }
        catch (SecurityException ex){
            Log.e("DEBUG", "Get position failed", ex);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static final SendPostionHandler instance = new SendPostionHandler();

}
