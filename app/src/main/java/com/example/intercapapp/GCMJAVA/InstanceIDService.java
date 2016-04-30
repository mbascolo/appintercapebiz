package com.example.intercapapp.GCMJAVA;

import android.content.Intent;

import com.example.intercapapp.GCM.GCMNotificationIntentService;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Matias on 30/04/2016.
 */
public class InstanceIDService extends InstanceIDListenerService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, GcmMessageHandler.class);
        startService(intent);
    }
}
