package com.helennagel.sqliteandmebaas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class KuvaDetailid extends AppCompatActivity {
    // loome edittexti klassi muutujad
    EditText nimiDetail, kogusDetail, hindDetail;
    // loome button klassi muutujad
    Button btnKustuta, btnUuenda;
    // loome oma andmebaasi muutuja ning läbi selle saame andmed
    DbToode dbToode = new DbToode(this);
    // loome string tüüpi muutujad
    String id,nimi,kogus,hind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuva_detailid);
        // initsialiseerida oma muutujad
        nimiDetail = findViewById(R.id.nimiDetail);
        kogusDetail = findViewById(R.id.kogusDetail);
        hindDetail = findViewById(R.id.hindDetail);
        btnKustuta = findViewById(R.id.btn_kustuta);
        btnUuenda = findViewById(R.id.btn_uuenda);
        // saame oma intentile juurdepääsu
        Intent intent = getIntent();
//loome muutujad, millele anname intent abiga eelmise activitys saadud andmed. need saame kätte kasutades ühte loodud parameetrit, selleks oli nimi
        id = intent.getStringExtra("saadaId");
        nimi = intent.getStringExtra("saadaNimi");
        kogus = intent.getStringExtra("saadaKogus");
        hind = intent.getStringExtra("saadaHind");
// edittexti muutujad saavad oma väärtused intent muutujatelt. kuvame nendes andmebaasist võetud väärtusi
        nimiDetail.setText(nimi);
        kogusDetail.setText(kogus);
        hindDetail.setText(hind);
        // kustuta nupu tegevus
        btnKustuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbToode.kustuta(Long.parseLong(id));
                tagasi();
            }
        });
        // uuenda tegevus
        btnUuenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbToode.uuenda(Long.parseLong(id),nimiDetail.getText().toString(),
                        Integer.parseInt(kogusDetail.getText().toString()),
                        Double.parseDouble(hindDetail.getText().toString()));
                tagasi();
            }
        });
    }
    // meetod tagasi mis viib meid esilehele,kasutame selleks intent abi
    public void tagasi(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        ///kommentaar
    }
}




























}
