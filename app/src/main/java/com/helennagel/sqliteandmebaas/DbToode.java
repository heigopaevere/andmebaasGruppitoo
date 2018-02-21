package com.helennagel.sqliteandmebaas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbToode {
    // Context liidese muutuja
    Context sisu;
    // loome sqlite andmebaasi nimega db.
    SQLiteDatabase db;
    // loome oma andmebaasi klassile abilise
    DbToode.DbHelper dbHelper;
    // saame läbi context liidese kätte rakenduse keskkonna/sisu/konteksti
    public DbToode(Context s){
        sisu=s;
    }
    public DbToode ava(){
        // kasutades dbhelperit saame kätte andmebaasi konteksti mille omista,e andmebaasile
        try{
            dbHelper = new DbHelper(sisu);
            db = dbHelper.getWritableDatabase();
        }
        catch (Exception e){
            e.printStackTrace();;
        } finally {
            return this;
        }
    }
    public void sulge() { db.close();}
    public void insert(String nimi, int kogus, double hind){
        ContentValues values = new ContentValues();
        values.put(dbHelper.NIMI, nimi);
        values.put(dbHelper.KOGUS, kogus);
        values.put(dbHelper.HIND, hind);
        db.insert(dbHelper.TABELI_NIMI,null,values);
    }
    public Cursor kuvaAndmed(){
        String[] tulbad = new String[]{dbHelper.ID, dbHelper.NIMI, dbHelper.KOGUS, dbHelper.HIND};
        Cursor s = db.query(dbHelper.TABELI_NIMI, tulbad, null, null,
                null, null, dbHelper.ID + " desc");
        if (s != null){
            s.moveToFirst();
        }
        return s;
    }
    public Cursor valitud(long id){
        String[] tulbad = new String[]{dbHelper.ID, dbHelper.NIMI, dbHelper.KOGUS, dbHelper.HIND};
        Cursor s = db.query(dbHelper.TABELI_NIMI, tulbad, dbHelper.ID + "=" + id,
                null, null,null,null);
        if (s != null){
            s.moveToFirst();
        }
        return s;
    }
    public void kustuta(long id){
        ava();
        db.delete(dbHelper.TABELI_NIMI,dbHelper.ID + "=" + id, null);
        sulge();
    }
    public void uuenda(long id, String nimi, int kogus, double hind){
        ava();
        ContentValues values = new ContentValues();
        values.put(dbHelper.NIMI, nimi);
        values.put(dbHelper.KOGUS, kogus);
        values.put(dbHelper.HIND, hind);
        db.update(dbHelper.TABELI_NIMI,values,dbHelper.ID + "=" + id, null);
        sulge();
    }
    // loome andmebaasi abistaja klassi mis pärib sqliteopenhelper klassi omadused
    public class DbHelper extends SQLiteOpenHelper{
        // loome muutujad andmebaasile: andmebaasi nimi, tabeli nimi, tabelis olevate tulpade nimed
        public static final String DB_NIMI = "toode.db";
        public static final String TABELI_NIMI = "tooted";
        public static final String ID = "_id";
        public static final String NIMI = "nimi";
        public static final String KOGUS = "kogus";
        public static final String HIND = "hind";
        // loome tabli kuhu lisame tulbad ja määrame ära nende tüübid
        public static final String CREATE_TABLE = "CREATE TABLE " + TABELI_NIMI + " ( " + ID +
                " INTEGER PRIMARY KET AUTOINCREMENT , " + NIMI + " TEXT , " + KOGUS + " INTEGER , "
                + HIND + " DOUBLE );";
        // anname andmebaasile versiooni numbri, sest iga kord kui andmebaasi uuendame uuendame ka selle versiooni
        public static final int VERSION = 1;
        // andmebaasi nö abiline kust saab andmebaasi nime ja versiooni
        public DbHelper (Context context){
            super(context, DB_NIMI, null, VERSION);
        }
        // andmebaasi luues peab olema meil kaks overrite meetodit oncreate ja upgrade
        @Override
        // loome sqlite andmebaasi, kus loome tabeli
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }
        // uuendame andmebaasi tabelit. vana versioon asendatakse uuega
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // kui sellise versiooni numbriga tabel on juba olemas siis see hüljatakse ja võetakse uus asemele
            db.execSQL("DROP TABLE IF EXISTS " + TABELI_NIMI);
            // luuakse uus
            onCreate(db);
        }
    }
}
