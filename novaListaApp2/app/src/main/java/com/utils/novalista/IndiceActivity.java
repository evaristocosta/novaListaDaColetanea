package com.utils.novalista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;

public class IndiceActivity extends AppCompatActivity {
    //receptores das string do "bd"
    ArrayList<String> hinos = new ArrayList<>();
    ArrayList<String> numAntigo = new ArrayList<>();
    ArrayList<String> numNovo = new ArrayList<>();

    //linhas sem acentos
    String sNA, itemNA;

    //adaptador e interfaceamento
    HinosAdapter adapter;
    ListView listaHinos;
    EditText editText;

    //contadores de controle
    int i = 1, j = 0, k=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indice);

        //preenche strings de trabalho
        for (int i = 0, j = HinosData.NUMANTIGO.length; i<j; i++) {
            numAntigo.add(HinosData.NUMANTIGO[i]);
            numNovo.add(HinosData.NUMNOVO[i]);
            hinos.add(HinosData.HINOS[i]);
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
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //entrada sem acentos
                sNA = removeDiacriticalMarks(s.toString());
                // se for vazia, inicia. Se for maior q entrada anterior,
                // busca. Se for menor, reinicia e busca
                if (sNA.equals("")) {
                    iniciaLista();
                } else {
                    if (j > sNA.length()) {
                        iniciaLista();
                        busca(sNA);
                        j = sNA.length();
                    } else {
                        busca(sNA);
                        j = sNA.length();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // método de remoção de acentos
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // método de busca
    public void busca(String textoDePesquisa) {
        //diferencia busca numérica de textual
        if(!TextUtils.isDigitsOnly(textoDePesquisa)){
            i=1;
            for(String item:HinosData.HINOS){
                itemNA = removeDiacriticalMarks(item);
                if(!itemNA.contains(textoDePesquisa.toUpperCase())){
                    if(hinos.remove(item)) {
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
                        numAntigo.remove(item1);
                        numNovo.remove(item2);
                        hinos.remove(i-1);
                        i--;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    // inicializador da lista
    public void iniciaLista() {
        hinos = new ArrayList<>(Arrays.asList(HinosData.HINOS));
        numNovo = new ArrayList<>(Arrays.asList(HinosData.NUMNOVO));
        numAntigo = new ArrayList<>(Arrays.asList(HinosData.NUMANTIGO));

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



    // método de busca antigo
    /*
    public void buscaPorNome(String textoDePesquisa) {
        for(String item:HinosData.HINOS){
            itemNA = removeDiacriticalMarks(item);
            if(!itemNA.contains(textoDePesquisa)){
               if(hinos.remove(item)) {
                   numAntigo.remove(i-1);
                   numNovo.remove(i-1);
               }
            } else {
                i++;
            }
        }
        i=1;
        adapter.notifyDataSetChanged();
    }*/
}
