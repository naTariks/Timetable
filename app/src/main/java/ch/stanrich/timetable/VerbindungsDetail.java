package ch.stanrich.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ch.stanrich.timetable.adapter.AbfahrtsplanAdapter;
import ch.stanrich.timetable.helper.VerbindungJsonParser;
import ch.stanrich.timetable.model.Verbindung;

/**
 * The type Verbindungs detail.
 */
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
        //getting the Verbindung. This is possible with Parcel(able)
        verbindung = intent.getExtras().getParcelable("Verbindung");

        //setting the Title Color in a cursed way
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Verbindung</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        //Setting the UpButton
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addInfos();
    }

    /**
     * Sets Click Listener for the Floating Action Button
     */
    @Override
    protected void onStart() {
        super.onStart();

        FloatingActionButton fltHelp = findViewById(R.id.fltHelp);
        fltHelp.setOnClickListener(v -> fltOpenHelp());
    }

    /**
     * Fills the 9 fields in the DetailAnsicht with the Data from this Verbindung.
     */
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

        Date endZeitDate = verbindung.getEndZeit();
        String endZeit;

        if (endZeitDate.getTime() == VerbindungJsonParser.INVALID_TIME) {
            endZeit = VerbindungJsonParser.UNKNOWN_DATA;
        } else {
            endZeit = hourminutesFormatter.format(endZeitDate);
        }


        txtZeitInfo.setText("Zeit");
        txtBahnhofInfo.setText("Bahnhof");
        txtGleisInfo.setText("Gleis");

        txtStartZeit.setText(startZeit);
        txtStartBahnhof.setText(AbfahrtsplanAdapter.VON + verbindung.getStartBahnhof() + verbindung.getZugArt());
        txtStartGleis.setText(verbindung.getStartGleis());

        txtEndZeit.setText(endZeit);
        txtEndBahnhof.setText(AbfahrtsplanAdapter.NACH + verbindung.getEndBahnhof() + verbindung.getZugArt());
        txtEndGleis.setText(verbindung.getEndGleis());

    }

    /**
     * Is responsible for going one Activity Back if clicked on UpButton
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Is called if clicked on Floating Action Button with Question Mark and opens HelpImpressum Activity.
     */
    private void fltOpenHelp() {
        Intent intent = new Intent(this, HelpImpressum.class);
        startActivity(intent);
    }

}