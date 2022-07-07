package ch.stanrich.timetable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import ch.stanrich.timetable.adapter.AbfahrtsplanAdapter;
import ch.stanrich.timetable.model.Verbindung;

public class VerbindungsDetail extends AppCompatActivity {

    private String bahnhof;
    private Verbindung verbindung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbindungs_detail);

        Intent intent = getIntent();
        bahnhof = intent.getStringExtra("Bahnhof");
        verbindung = intent.getParcelableExtra("Verbindung");

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Verbindung</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addInfos();
    }

    private void addInfos() {
        TextView txtStartZeit = findViewById(R.id.txtStartZeit);
        TextView txtStartBahnhof = findViewById(R.id.txtStartBahnhof);
        TextView txtStartGleis = findViewById(R.id.txtStartGleis);

        TextView txtEndZeit = findViewById(R.id.txtEndZeit);
        TextView txtEndBahnhof = findViewById(R.id.txtEndBahnhof);
        TextView txtEndGleis = findViewById(R.id.txtEndGleis);


        txtStartZeit.setText(verbindung.getStartZeit().toString());
        txtStartBahnhof.setText(verbindung.getStartBahnhof());
        txtStartGleis.setText(verbindung.getStartGleis());

        txtEndZeit.setText(verbindung.getEndZeit().toString());
        txtEndBahnhof.setText(verbindung.getEndBahnhof());
        txtEndGleis.setText(verbindung.getEndGleis());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}