package com.aplicacion.intercapapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Matias on 26/11/2015.
 */
public class PushAplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Parse.initialize(this, "nKiGA5FvzKpJ9c1M8TK450hvtWyEn1RfFe6CRqCv", "0uDpCdOpuaG4jjh7kTCzMaJ5TycW9U4iivvx5HPJ");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
