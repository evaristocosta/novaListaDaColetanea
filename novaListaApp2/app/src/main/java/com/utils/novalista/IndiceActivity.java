package com.utils.novalista;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IndiceActivity extends AppCompatActivity implements ForceUpdateChecker.OnUpdateNeededListener {

    //imutável pra pesquisa
    ArrayList<String> hinosSemAcento = new ArrayList<>();
    //receptores das string do "bd"
    ArrayList<String> hinos;
    ArrayList<String> hinosNA;
    ArrayList<String> numAntigo;
    ArrayList<String> numNovo;

    //linhas sem acentos
    String sNA;

    //adaptador e interfaceamento
    HinosAdapter adapter;
    ListView listaHinos;
    EditText editText;

    private static final String TAG = IndiceActivity.class.getSimpleName();

    //contadores de controle
    int i = 1, j = 0, k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indice);

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://lucascostaportfolio.wordpress.com/2018/05/30/nova-lista-da-coletanea/");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });


        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        for (int i = 0, j = HinosData.HINOS.length; i<j; i++) {
            hinosSemAcento.add(removeDiacriticalMarks(HinosData.HINOS[i]));
        }

        listaHinos = findViewById(R.id.lista);
        editText = (EditText) findViewById(R.id.barraDeBusca);

        //método de preenchimento inicial da lista
        iniciaLista();

        //funcionamento da barra de pesquisa
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}


            @Override
            public void afterTextChanged(Editable s) {
                //entrada sem acentos
                sNA = removeDiacriticalMarks(s.toString());
                // se for vazia, inicia. Se não, inicia e busca
                // acontece pra previnir trocas de corretor automático
                if (sNA.equals("")) {
                    iniciaLista();
                } else {
                    iniciaLista();
                    busca(sNA.toUpperCase());
                }
            }
        });
    }

    // método de remoção de acentos
    public static String removeDiacriticalMarks(String string) {
        String newString = string.replaceAll(",","");
        return Normalizer.normalize(newString, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // método de busca
    public void busca(String textoDePesquisa) {
        //diferencia busca numérica de textual
        if (textoDePesquisa.equals("ENTRARAM") || textoDePesquisa.equals("ENTRARAM ")) {
            i=hinos.size();
            for(k=(HinosData.NUMANTIGO.length-1); k>=0; k--) {
                String item1 = HinosData.NUMANTIGO[k];
                if(!item1.contains("-")) {
                    // faz remoção apenas se ambos contém a string
                    if(numAntigo.contains(item1)) {
                        numAntigo.remove(i-1);
                        numNovo.remove(i-1);
                        hinos.remove(i-1);
                        hinosNA.remove(i-1);
                    } else {
                        i++;
                    }
                }
                i--;
            }
        } else if (textoDePesquisa.equals("SAIRAM") || textoDePesquisa.equals("SAIRAM ")) {
            i=hinos.size();
            for(k=(HinosData.NUMNOVO.length-1); k>=0; k--) {
                String item1 = HinosData.NUMNOVO[k];
                if(!item1.contains("-")) {
                    // faz remoção apenas se ambos contém a string
                    if(numNovo.contains(item1)) {
                        numAntigo.remove(i-1);
                        numNovo.remove(i-1);
                        hinos.remove(i-1);
                        hinosNA.remove(i-1);
                    } else {
                        i++;
                    }
                }
                i--;
            }
        } else if(!TextUtils.isDigitsOnly(textoDePesquisa)){
            i=1;
            for(String item:hinosSemAcento){
                if(!item.contains(textoDePesquisa)){
                    if (hinosNA.remove(item)) {
                        hinos.remove(i-1);
                        numAntigo.remove(i-1);
                        numNovo.remove(i-1);
                    }
                } else {
                    i++;
                }
            }
        } else {
            i=hinos.size();
            for(k=(HinosData.NUMANTIGO.length-1); k>=0; k--) {
                String item1 = HinosData.NUMANTIGO[k];
                String item2 = HinosData.NUMNOVO[k];
                if(!item1.contains(textoDePesquisa) && !item2.contains(textoDePesquisa)) {
                    // faz remoção apenas se ambos contém a string
                    if(numAntigo.contains(item1) && numNovo.contains(item2)) {
                        numAntigo.remove(i-1);
                        numNovo.remove(i-1);
                        hinos.remove(i-1);
                        hinosNA.remove(i-1);
                    } else {
                        i++;
                    }
                }
                i--;
            }
        }
        adapter.notifyDataSetChanged();
    }

    // inicializador da lista
    public void iniciaLista() {
        hinos = new ArrayList<>();
        hinosNA = new ArrayList<>();
        numAntigo = new ArrayList<>();
        numNovo = new ArrayList<>();

        for (int i = 0, j = HinosData.NUMANTIGO.length; i<j; i++) {
            numAntigo.add(HinosData.NUMANTIGO[i]);
            numNovo.add(HinosData.NUMNOVO[i]);
            hinos.add(HinosData.HINOS[i]);
            hinosNA.add(hinosSemAcento.get(i));

        }

        adapter = new HinosAdapter();
        listaHinos.setAdapter(adapter);
    }

    // adaptador personalizado
    class HinosAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return hinos.size();
        }

        @Override
        public Object getItem(int pos) {
            return hinos.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return hinos.get(pos).hashCode();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.indice_item, parent, false);
            }

            TextView listItemText = (TextView) convertView.findViewById(R.id.item_hinos);
            listItemText.setText(hinos.get(position));

            TextView numAntigoText = (TextView) convertView.findViewById(R.id.num_antigo);
            numAntigoText.setText(numAntigo.get(position));

            TextView numNovoText = (TextView) convertView.findViewById(R.id.num_novo);
            numNovoText.setText(numNovo.get(position));

            return convertView;
        }
    }


    // menu "sobre"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nova_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent sobre = new Intent(this, Sobre.class);
                startActivity(sobre);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // checagem de atualização
    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nova versão disponível!")
                .setMessage("Uma nova versão, com melhorias e correções, foi disponibilizada.")
                .setPositiveButton("Atualizar agora",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();

        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
