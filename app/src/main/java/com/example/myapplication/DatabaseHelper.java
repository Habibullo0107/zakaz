package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "delfin3.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "molho"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_MOL = "id_mol";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_ID_KURB = "id_kurb";
    public static final String COLUMN_ZAVISMA = "zavisma";
    public static final String COLUMN_NARKHI_O = "narkhi_o";
    public static final String COLUMN_NARKHI_F = "narkhi_f";
    public static final String COLUMN_NARKHI_P = "narkhi_p";
    public static final String COLUMN_VALUTA = "valuta";
    public static final String COLUMN_KOD = "kod";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("CREATE TABLE molho (" + COLUMN_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ID_MOL + " INTEGER, " + COLUMN_NOM
//                + " TEXT, " + COLUMN_ID_KURB + " INTEGER, " + COLUMN_ZAVISMA + " INTEGER, " + COLUMN_NARKHI_O + " TEXT, "
//                + COLUMN_NARKHI_F + " TEXT, " + COLUMN_NARKHI_P + " TEXT, " + COLUMN_VALUTA + " TEXT)");
        db.execSQL("CREATE TABLE molho (_id INTEGER PRIMARY KEY, id_mol INTEGER, kod INTEGER, nom TEXT, valuta TEXT, narkhi_o TEXT, narkhi_f TEXT, id_kurb INTEGER );");
        db.execSQL("CREATE TABLE user (_id INTEGER PRIMARY KEY, id_user TEXT, dostup TEXT, nom TEXT, parol TEXT, HOLAT TEXT);");

        // добавление начальных данных
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
//                + ", " + COLUMN_YEAR  + ") VALUES ('Том Смит', 1981);");
        db.execSQL("INSERT INTO user (id_user, dostup, nom,  parol, HOLAT )  VALUES('33', '1', 'Пользователь офлайн', '123', '1') "  );


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
