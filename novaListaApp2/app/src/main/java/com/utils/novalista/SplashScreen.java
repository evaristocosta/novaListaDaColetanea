package com.utils.novalista;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by lucas on 14/03/18.
 */

public class SplashScreen extends AppCompatActivity {
    Handler handle;
    Runnable runnable;
    ImageView splashChoice;
    int[] imageList;
    Random random;
    int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageList = new int[]{
                R.drawable.splash3,
                R.drawable.splash4,
                R.drawable.splash6,
                R.drawable.splash7,
                R.drawable.dica1,
                R.drawable.dica2,
                R.drawable.dica3,
                R.drawable.dica4};

        random = new Random(System.currentTimeMillis());
        posicao = random.nextInt(imageList.length - 1);

        splashChoice = findViewById(R.id.splash);
        splashChoice.setImageResource(imageList[posicao]);

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
