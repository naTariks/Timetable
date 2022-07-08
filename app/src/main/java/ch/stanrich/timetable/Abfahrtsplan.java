package ch.stanrich.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.text.ParseException;

import ch.stanrich.timetable.adapter.AbfahrtsplanAdapter;
import ch.stanrich.timetable.helper.VerbindungJsonParser;
import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

/**
 * The Abfahrtsplan is a list of departures from the selected Bahnhof.
 */
public class Abfahrtsplan extends AppCompatActivity {

    private final String STATIONBOARD_STATION_API_URL = "https://transport.opendata.ch/v1/stationboard?station=";
    private ProgressBar progressBar;
    private String bahnhof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abfahrtsplan);

        progressBar = findViewById(R.id.loading_verbindungen_progress);
        Intent intent = getIntent();
        bahnhof = intent.getStringExtra("Bahnhof");

        //setting the Title Color in a cursed way
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Abfahrtsplan</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        progressBar.setVisibility(View.VISIBLE);

        //Setting the UpButton
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        if (!isNetworkConnectionAvailable()) {
            generateAlertDialog(ErrorType.NO_INTERNET);
        } else {
            getVerbindungenVon(bahnhof.toLowerCase());
        }

        FloatingActionButton fltHelp = findViewById(R.id.fltHelp);
        fltHelp.setOnClickListener(v -> fltOpenHelp());

    }

    /**
     * Generate an Alert and looks with the given param what the problem is and returns an error Message for the specific problem
     *
     * @param type the ErroryType according to the Enum
     */
    public void generateAlertDialog(ErrorType type) {
        //Building the AlertDialog and hide the ProgressBar
        progressBar.setVisibility(View.GONE);
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        switch (type) {
            case NO_INTERNET:
                dialogBuilder.setMessage("Es konnte keine Verbindung zum Internet hergestellt werden. Versuchen Sie es später nochmals").setTitle("Fehler");
                break;
            case FALSE_BAHNHOF:
                dialogBuilder.setMessage("Der eingegebene Bahnhof, " + bahnhof + ", konnte nicht gefunden werden.").setTitle("Fehler");
                break;
            case TIME_NOT_PARSABLE:
                dialogBuilder.setMessage("Systemfehler. Bitte wenden Sie sich an einen Entwickler.").setTitle("Fehler");
                break;
            default:
                dialogBuilder.setMessage("Leider ist ein Fehler aufgetretten. Versuchen Sie es später nochmals.").setTitle("Fehler");
                break;
        }
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Gets Verbindungen for inserted Bahnhof by calling the <a href="https://transport.opendata.ch">opentransport API</a>
     *
     * @param abfahrtsOrt the name of the station we want to look up
     */
    public void getVerbindungenVon(String abfahrtsOrt) {
        String requestUrl = STATIONBOARD_STATION_API_URL + abfahrtsOrt;

        //Using our selfmade Adapter. See AbfahrtsplanAdapter
        final AbfahrtsplanAdapter verbindungInfosAdapter = new AbfahrtsplanAdapter(getApplicationContext(), new Bahnhof());
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl, response -> {
            //Try to Parse the JSON String or else it will generate an "Given Bahnhof not found" or "Time not parsable" Alert. Depending on the thrown Exception
            try {
                Bahnhof bahnhof = VerbindungJsonParser.createTimetableFromJsonString(response);

                if (bahnhof.getVerbindungen().isEmpty()) {
                    generateAlertDialog(ErrorType.FALSE_BAHNHOF);
                }

                verbindungInfosAdapter.addAll(bahnhof.getVerbindungen());
                ListView timeTable = findViewById(R.id.verbindungen);
                timeTable.setAdapter(verbindungInfosAdapter);
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                generateAlertDialog(ErrorType.FALSE_BAHNHOF);

            } catch (ParseException e) {
                generateAlertDialog(ErrorType.TIME_NOT_PARSABLE);
                Log.e(VerbindungJsonParser.class.getName(), "Time from API not parsable anymore", e);
            }
        }, error -> generateAlertDialog(ErrorType.FALSE_BAHNHOF));
        queue.add(stringRequest);

        //Definition on what happens when clicked on ListItem
        AdapterView.OnItemClickListener mListClickedHandler = (parent, v, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), VerbindungsDetail.class);
            Verbindung selected = (Verbindung) parent.getItemAtPosition(position);

            intent.putExtra("Bahnhof", selected.getEndBahnhof());
            intent.putExtra("Verbindung", selected);
            startActivity(intent);
        };
        //seting the onItemClickListener for every ListItem
        ListView listViewItem = findViewById(R.id.verbindungen);
        listViewItem.setOnItemClickListener(mListClickedHandler);
    }

    /**
     * Method for checking if phone is connected to the Internet. Just copied from presentation.
     *
     * @return <code>false</code> no network connection could be established OR if there's no networking devices
     */
    private boolean isNetworkConnectionAvailable() {
        ConnectivityManager connectivityService = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityService.getActiveNetworkInfo();

        return null != networkInfo && networkInfo.isConnected();
    }

    /**
     * Is called if clicked on Floating Action Button with Question Mark and opens HelpImpressum Activity.
     */
    private void fltOpenHelp() {
        Intent intent = new Intent(this, HelpImpressum.class);
        startActivity(intent);
    }

    public enum ErrorType {
        NO_INTERNET,
        FALSE_BAHNHOF,
        TIME_NOT_PARSABLE,
        DEFAULT_ERROR
    }

}