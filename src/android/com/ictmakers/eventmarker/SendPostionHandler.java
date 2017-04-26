package com.ictmakers.eventmarker;

import android.content.Context;
import android.os.Bundle;

import com.adobe.phonegap.push.PushPlugin;

/**
 * Created by Andrea on 24/04/2017.
 */

public class SendPostionHandler implements NotificationHandler {

    protected SendPostionHandler(){

    }

    public boolean canHandle(Bundle extras){
        return false;
    }

    public void handle(Context context, Bundle extras){
        String regId = PushPlugin.getRegistrationID();


    }

    public static final SendPostionHandler instance = new SendPostionHandler();
}
