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

public class CitatiActivity extends AppCompatActivity {
    private ListView mListaCitata;
    private dbHelper mHelper;
    private ArrayAdapter<String> mAdapter;
    private String mString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citati);

        mListaCitata = (ListView) findViewById(R.id.lista_citata);
        updateUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu meni) {
        getMenuInflater().inflate(R.menu.meni2, meni);
        return super.onCreateOptionsMenu(meni);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dodaj_citat:
                final EditText dodajTekstCitata = new EditText(this);
                AlertDialog dialogCitata = new AlertDialog.Builder(this)
                        .setTitle("Dodaj novi citat")

                        .setView(dodajTekstCitata)
                        .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogCitata, int which) {
                                String citat = String.valueOf(dodajTekstCitata.getText());
                                mString = citat;
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(zcContract.UlazniPodaci.COL_C_QUOTE_TITLE, citat);
                                db.insertWithOnConflict(zcContract.UlazniPodaci.TABLE_2,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                            }
                        })
                        .setNegativeButton("Odustani", null)
                        .create();
                dialogCitata.show();
                return true;

            case R.id.idi_na_zadatke:
                Intent naZadatke = new Intent(this, MainActivity.class);
                startActivity(naZadatke);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
       }

    private void updateUI() {
        ArrayList<String> popisCitata = new ArrayList<>();
        mHelper = new dbHelper(this);

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursorC = db.query(zcContract.UlazniPodaci.TABLE_2,
                new String[]{zcContract.UlazniPodaci._ID, zcContract.UlazniPodaci.COL_C_QUOTE_TITLE},
                null, null, null, null, null);
        while (cursorC.moveToNext()) {
            int idx = cursorC.getColumnIndex(zcContract.UlazniPodaci.COL_C_QUOTE_TITLE);
            popisCitata.add(cursorC.getString(idx));
        }
        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.todo_citat,
                    R.id.naziv_citata,
                    popisCitata);
            mListaCitata.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(popisCitata);
            mAdapter.notifyDataSetChanged();
        }
        cursorC.close();
        db.close();
    }

    public void obrisiCitat(View view) {
        View parent = (View) view.getParent();
        TextView tvCitati = (TextView) parent.findViewById(R.id.naziv_citata);
        String citat = String.valueOf(tvCitati.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(zcContract.UlazniPodaci.TABLE_2,
                zcContract.UlazniPodaci.COL_C_QUOTE_TITLE + " = ?",
                new String[]{citat});
        db.close();

        updateUI();
    }

    public void postaviCitat(View view){
        View parent = (View) view.getParent();
        TextView tvCitati = (TextView) parent.findViewById(R.id.naziv_citata);
        String citat = String.valueOf(tvCitati.getText());

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("msgCitat",citat);
        startActivity(i);
    }
}
