package com.ictmakers.eventmarker;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Andrea on 24/04/2017.
 */

public interface NotificationHandler {
    boolean canHandle(Bundle extras);
    void handle(Context context, Bundle extras);
}
