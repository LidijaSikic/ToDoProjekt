package vsite.hr.todoprojekt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context) {
        super(context, zcContract.DB_NAME, null, zcContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableZ = "CREATE TABLE " + zcContract.UlazniPodaci.TABLE_1 + " ( " +
                zcContract.UlazniPodaci._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                zcContract.UlazniPodaci.COL_Z_TASK_TITLE + " TEXT NOT NULL);";

        String createTableC = "CREATE TABLE " + zcContract.UlazniPodaci.TABLE_2 + " ( " +
                zcContract.UlazniPodaci._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                zcContract.UlazniPodaci.COL_C_QUOTE_TITLE + " TEXT NOT NULL);";


        db.execSQL(createTableZ);
        db.execSQL(createTableC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + zcContract.UlazniPodaci.TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + zcContract.UlazniPodaci.TABLE_2);
        onCreate(db);
    }
}
