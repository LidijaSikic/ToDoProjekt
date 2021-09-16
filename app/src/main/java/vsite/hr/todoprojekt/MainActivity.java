package vsite.hr.todoprojekt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import vsite.hr.todoprojekt.db.dbHelper;
import vsite.hr.todoprojekt.db.zcContract;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private dbHelper mHelper;
    private ListView mListaZadataka;
    private ArrayAdapter<String> mAdapter;
    private TextView mCitat;
    private String  s ="Carpe diem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new dbHelper(this);
        mListaZadataka = (ListView) findViewById(R.id.todo_lista);

        mCitat = (TextView) findViewById(R.id.tv_citat);

        Intent i = getIntent();
        if( getIntent().getExtras() == null)
        {
            s="Carpe diem";
        }
        else{
            s = i.getStringExtra("msgCitat");
        }
        TextView tv = (TextView) findViewById(R.id.tv_citat);
        tv.setText(s);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu meni) {
        getMenuInflater().inflate(R.menu.meni, meni);
        return super.onCreateOptionsMenu(meni);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dodaj_zadatak:
                final EditText dodajTekstZadatka = new EditText(this);
                AlertDialog dialogZadatka = new AlertDialog.Builder(this)
                        .setTitle("Dodaj novi zadatak")
                        .setMessage("Koji je tvoj sljedeći zadatak?")
                        .setView(dodajTekstZadatka)
                        .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogZadatka, int which) {
                                String zadatak = String.valueOf(dodajTekstZadatka.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(zcContract.UlazniPodaci.COL_Z_TASK_TITLE, zadatak);
                                db.insertWithOnConflict(zcContract.UlazniPodaci.TABLE_1,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Odustani", null)
                        .create();

                dialogZadatka.show();
                return true;

            case R.id.dodaj_citat:
                final EditText dodajTekstCitata = new EditText(this);
                AlertDialog dialogCitata = new AlertDialog.Builder(this)
                        .setTitle("Dodaj novi citat")

                        .setView(dodajTekstCitata)
                        .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogCitata, int which) {
                                String citat = String.valueOf(dodajTekstCitata.getText());

                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(zcContract.UlazniPodaci.COL_C_QUOTE_TITLE, citat);
                                db.insertWithOnConflict(zcContract.UlazniPodaci.TABLE_2,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                mCitat.setText(citat);

                            }
                        })
                        .setNegativeButton("Odustani", null)
                        .create();
                dialogCitata.show();
                return true;

            case R.id.prikazi_sve_citate:
                Intent sviCitati = new Intent(this, CitatiActivity.class);
                startActivity(sviCitati);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ArrayList<String> popisZadataka = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursorZ = db.query(zcContract.UlazniPodaci.TABLE_1,
                new String[]{zcContract.UlazniPodaci._ID, zcContract.UlazniPodaci.COL_Z_TASK_TITLE},
                null, null, null, null, null);
        while (cursorZ.moveToNext()) {
            int idx = cursorZ.getColumnIndex(zcContract.UlazniPodaci.COL_Z_TASK_TITLE);
            popisZadataka.add(cursorZ.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.todo_zadatak,
                    R.id.naziv_zadatka,
                    popisZadataka);
            mListaZadataka.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(popisZadataka);
            mAdapter.notifyDataSetChanged();
        }

        cursorZ.close();
        db.close();
    }

    public void obrisiZadatak(View view) {
        View parent = (View) view.getParent();
        TextView tvZadaci = (TextView) parent.findViewById(R.id.naziv_zadatka);
        String zadatak = String.valueOf(tvZadaci.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(zcContract.UlazniPodaci.TABLE_1,
                zcContract.UlazniPodaci.COL_Z_TASK_TITLE + " = ?",
                new String[]{zadatak});
        db.close();
        updateUI();
    }

}
