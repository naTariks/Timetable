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
import com.android.volley.Response;
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
 * The type Abfahrtsplan.
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
     * Calls isNetworkConnectionAvailable() and generates an Alert if not. Set Click Listener for Floating Action Button.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (!isNetworkConnectionAvailable()) {
            generateAlertDialog(1);
        } else {
            getVerbindungenVon(bahnhof.toLowerCase());
        }

        FloatingActionButton fltHelp = findViewById(R.id.fltHelp);
        fltHelp.setOnClickListener(v -> fltOpenHelp());

    }

    /**
     * Generate an Alert and looks with the given param what the problem is and returns an error Message for the specific problem
     *
     * @param id the id
     */
    public void generateAlertDialog(int id) {
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
        switch (id) {
            case 1: //No Internet
                dialogBuilder.setMessage("Es konnte keine Verbindung zum Internet hergestellt werden. Versuchen Sie es später nochmals").setTitle("Fehler");
                break;
            case 2: //Given Bahnhof not found
                dialogBuilder.setMessage("Der eingegebene Bahnhof, " + bahnhof + ", konnte nicht gefunden werden.").setTitle("Fehler");
                break;
            case 3: //Systemfehler, Actually just a problem with parsing the departure or arrival Time
                dialogBuilder.setMessage("Systemfehler. Bitte wenden Sie sich an einen Entwickler.").setTitle("Fehler");
                break;
            default: //Any other Error. Not used at the moment
                dialogBuilder.setMessage("Leider ist ein Fehler aufgetretten. Versuchen Sie es später nochmals.").setTitle("Fehler");
                break;
        }
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Gets Verbindungen for inserted Bahnhof with calling the API and call the JSON Parser. Handles also the JSONException and a ParseException.
     *
     * @param abfahrtsOrt the abfahrts ort
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
                    generateAlertDialog(2);
                }

                verbindungInfosAdapter.addAll(bahnhof.getVerbindungen());
                ListView timeTable = findViewById(R.id.verbindungen);
                timeTable.setAdapter(verbindungInfosAdapter);
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                generateAlertDialog(2);

            } catch (ParseException e) {
                generateAlertDialog(3);
                Log.e(VerbindungJsonParser.class.getName(), "Time from API not parsable anymore", e);
            }
        }, error -> generateAlertDialog(2));
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
     * @return
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

}