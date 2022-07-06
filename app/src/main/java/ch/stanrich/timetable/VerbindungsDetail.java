package ch.stanrich.timetable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ch.stanrich.timetable.model.Verbindung;

public class VerbindungsDetail extends AppCompatActivity {

    private String bahnhof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbindungs_detail);

        Intent intent = getIntent();
        bahnhof = intent.getStringExtra("Bahnhof");

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Verbindung</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addInfosToList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addInfosToList(){
        ListView infos = findViewById(R.id.infos);
        ArrayAdapter<Verbindung> infosAdpater = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
        infosAdpater.addAll();
    }
}