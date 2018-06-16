package com.utils.novalista;

/**
 * Created by lucas on 29/05/18.
 */

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Sobre extends AppCompatActivity {
    String versionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);


        try {
            versionName = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView vname = findViewById(R.id.versionControl);
        vname.setText(versionName);

        Button atualizate = findViewById(R.id.atualizar);
        atualizate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://lucascostaportfolio.wordpress.com/2018/05/30/nova-lista-da-coletanea"));
                startActivity(browser);
            }
        });

        Button git = findViewById(R.id.cod_font);
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browser = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://gitlab.com/evaristocosta/novaListaDaColetanea"));
                startActivity(browser);
            }
        });


        Button send = findViewById(R.id.suges);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sender = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:lucascosta74@gmail.com"));
                startActivity(sender);
            }
        });

    }

}
