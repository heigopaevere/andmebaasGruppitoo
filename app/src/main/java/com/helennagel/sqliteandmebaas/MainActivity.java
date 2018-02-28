package com.helennagel.sqliteandmebaas;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // deklareerime klassidemuutujad
    EditText nimi, kogus, hind;
    Button btnLisa;
    ListView nimekiri;
    // deklareerime andmebaasi klassimuutujad
    DbToode dbToode;
    // deklareerime andmebaasi abi muutujad, mille kaudu pääseme andmetele ligi
    DbToode.DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initsialiseerime oma muutujad
        nimi = findViewById(R.id.tooteNimi);
        kogus = findViewById(R.id.tooteKogus);
        hind = findViewById(R.id.tooteHind);
        btnLisa = findViewById(R.id.btn_lisa);
        dbToode = new DbToode(this);
        // meetod laadi, mis loeb sisse loodud andmebaasi äppi ning kuvab seda
        laadi();
        // Nupu lisa tegevus
        btnLisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// loon nimele, kogusele ja hinnale abimuutujad, et kasutaja poolt sisestatud info edittext vidinatest kätte saada - saadud info on string kujul
                String Nimi = nimi.getText().toString();
                String kasutajaKogus = kogus.getText().toString();
                String kasutajaHind = hind.getText().toString();
// loon kogusele ja hinnale vastava tüübiga muutujad ning see järel konverteerin loetud string info ümber õigesse andmeformaati
                int Kogus = Integer.parseInt(kasutajaKogus);
                double Hind = Double.parseDouble(kasutajaHind);
                try {
// sisestan kasutaja poolt sisestatud andmed andmebaasi, kasutades loodud muutujaid
                    dbToode.insert(Nimi,Kogus,Hind);
                    // käivitatakse laadi meetod, laeb andmed andmebaasi
                    laadi();
                    // käivitatakse teate meetod, kuhu on kirjutatud teade
                    teade("Andmed salvestati!");
                    // nime, kogus ja hinna lahtrid tühjendatakse tekstist
                    nimi.setText("");
                    kogus.setText("");
                    hind.setText("");
                }
                catch(Exception e)
                {
                    teade("Andmete salvestamisel esines viga!");
                    // prinditakse veateade logisse, kui esineb mingi viga
                    e.printStackTrace();
                }
            }
        });
        // tegevus, mis toimub siis kui kasutaja vajutab nimekirja olevale tootele
        nimekiri.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// Kasutades Cursor liidest saame read-write juurdepääsu andmebaasi päringule. Kasutame andmebaasi abilist millega käivitame meetodi valitud.
                Cursor s = dbToode.valitud(id);
// Intent abiga kuvame andmeid ühest activityst teise - mainactivtyst kuvadetailid omasse
                Intent intent = new Intent(MainActivity.this, KuvaDetailid.class);
// loome string tüüpi parameetrid, mis saavad oma väärtuse andmebaasist võttes oma parameetriks andmebaasi tulba indexi, mille järgi õiged andmed saada
                String saadaId = s.getString(0);
                String saadaNimi = s.getString(1);
                String saadaKogus = s.getString(2);
                String saadaHind = s.getString(3);
// kasutades putextrat lisame andmed intentile, esimene parameeter on nimi mille läbi saame teises activity juurdepääsu ning teiseks on eelnev string muutja kust saame andmed
                intent.putExtra("saadaId", saadaId);
                intent.putExtra("saadaNimi", saadaNimi);
                intent.putExtra("saadaKogus", saadaKogus);
                intent.putExtra("saadaHind", saadaHind);
                // käivitame uue activity ja anname intenti kaasa
                startActivity(intent);
            }
        });
    }
    // meetod laadi, mis laeb meie andmed oma loodud custom listviewsse
    public void laadi(){
// kasutame crusori abi et saada andmed, kuvame andmed tabelis, teatega anname edukast laadimisest teada
        Cursor cursor = null;
        try{
            dbToode.ava();
            cursor = dbToode.kuvaAndmed();
            teade("Andmete laadimine õnnestus!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
// teeme stringi massiivi nimega kust - sinna võtame andmebaasist andmed. teeme int massiivi kuhu võtame custom loodud listview id'd
        String[] kust = new String[]{dbHelper.ID, dbHelper.NIMI, dbHelper.KOGUS, dbHelper.HIND};
        int [] kuhu = new int[]{R.id.nkId, R.id.nkNimi, R.id.nkKogus, R.id.nkHind};
// kasutame adapteri abi, et laadida ühest massiivist andmed teise ning need oma custom listview layoutis kuvada
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.nimekiri, cursor, kust, kuhu);
        // teavitame adapterit muudatustest andmetes
        adapter.notifyDataSetChanged();
        nimekiri = findViewById(R.id.nimekiri);
        nimekiri.setAdapter(adapter);
    }


























    public void teade(String message){
        //standard teatestruktuur
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
