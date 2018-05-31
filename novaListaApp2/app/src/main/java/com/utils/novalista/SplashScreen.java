package com.utils.novalista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

/**
 * Created by lucas on 14/03/18.
 */

public class SplashScreen extends AppCompatActivity {
    Handler handle;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handle = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                irIndice();
            }
        };
        handle.postDelayed(runnable, 3000);

    }


    private void irIndice() {
        Intent intento = new Intent(this, IndiceActivity.class);
        startActivity(intento);
        finish();
    }
}
