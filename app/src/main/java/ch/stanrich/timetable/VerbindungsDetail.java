package ch.stanrich.timetable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ch.stanrich.timetable.adapter.AbfahrtsplanAdapter;
import ch.stanrich.timetable.model.Verbindung;

public class VerbindungsDetail extends AppCompatActivity {

    private static final SimpleDateFormat hourminutesFormatter = new SimpleDateFormat("HH:mm");
    static {
        hourminutesFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
    }

    private String bahnhof;
    private Verbindung verbindung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verbindungs_detail);

        Intent intent = getIntent();
        bahnhof = intent.getStringExtra("Bahnhof");
        verbindung = intent.getExtras().getParcelable("Verbindung");

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Verbindung</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addInfos();
    }

    @SuppressLint("SetTextI18n")
    private void addInfos() {
        TextView txtZeitInfo = findViewById(R.id.txtZeitInfo);
        TextView txtBahnhofInfo = findViewById(R.id.txtBahnhofInfo);
        TextView txtGleisInfo = findViewById(R.id.txtGleisInfo);

        TextView txtStartZeit = findViewById(R.id.txtStartZeit);
        TextView txtStartBahnhof = findViewById(R.id.txtStartBahnhof);
        TextView txtStartGleis = findViewById(R.id.txtStartGleis);

        TextView txtEndZeit = findViewById(R.id.txtEndZeit);
        TextView txtEndBahnhof = findViewById(R.id.txtEndBahnhof);
        TextView txtEndGleis = findViewById(R.id.txtEndGleis);


        String startZeit = hourminutesFormatter.format(verbindung.getStartZeit());
        String endZeit = hourminutesFormatter.format(verbindung.getEndZeit());


        txtZeitInfo.setText("Zeit");
        txtBahnhofInfo.setText("Bahnhof");
        txtGleisInfo.setText("Gleis");

        txtStartZeit.setText(startZeit);
        txtStartBahnhof.setText(verbindung.getStartBahnhof());
        txtStartGleis.setText(verbindung.getStartGleis());

        txtEndZeit.setText(endZeit);
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