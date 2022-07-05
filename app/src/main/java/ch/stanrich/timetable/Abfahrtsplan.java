package ch.stanrich.timetable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import ch.stanrich.timetable.model.Bahnhof;

public class Abfahrtsplan extends AppCompatActivity {

    private ProgressBar progressBar;

    private static final String TRANSPORT_API_URL = "http://transport.opendata.ch/v1/stationboard?station=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abfahrtsplan);

        progressBar = findViewById(R.id.loading_verbindungen_progress);
        Intent intent = getIntent();
        String bahnhof = intent.getStringExtra("Bahnhof");

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Abfahrtsplan</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        progressBar.setVisibility(View.VISIBLE);
        getVerbindungen(TRANSPORT_API_URL + bahnhof.toLowerCase(Locale.ROOT));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getVerbindungen(String url) {

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


}