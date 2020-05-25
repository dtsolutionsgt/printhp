package com.hp.retail.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.hp.android.possdk.IJPOSInitCompleteCallBack;
import jpos.JPOSApp;

public class MainActivity extends AppCompatActivity  implements IJPOSInitCompleteCallBack  {


    void getStorageReadWritePermission() {
        if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            this.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);
        }
    }

    @Override
    public void onComplete() { // IJPOSInitCompleteCallBack callback for SDK initialization complete

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getStorageReadWritePermission();
        JPOSApp.setsContext(getApplicationContext(),(IJPOSInitCompleteCallBack)this);
        setTitle(R.string.app_loading);
        final Context ctxt = this;
        startMenuActivity(ctxt);
    }

    private void startMenuActivity(final Context ctxt) {
        startActivity(new Intent(ctxt, MenuActivity.class));
        finish();
    }
}
