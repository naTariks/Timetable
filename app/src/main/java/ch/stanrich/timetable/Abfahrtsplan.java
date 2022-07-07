package ch.stanrich.timetable;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import ch.stanrich.timetable.adapter.AbfahrtsplanAdapter;
import ch.stanrich.timetable.helper.VerbindungJsonParser;
import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

public class Abfahrtsplan extends AppCompatActivity {

    private ProgressBar progressBar;
    private String bahnhof;
    private final String TRANSPORT_API_URL = "https://transport.opendata.ch/v1/stationboard?station=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abfahrtsplan);

        progressBar = findViewById(R.id.loading_verbindungen_progress);
        Intent intent = getIntent();
        bahnhof = intent.getStringExtra("Bahnhof");

        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">Abfahrtsplan</font>"));

        TextView txtBahnhof = findViewById(R.id.txtBahnhof);
        txtBahnhof.setText(bahnhof);

        progressBar.setVisibility(View.VISIBLE);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        if (!isNetworkConnectionAvailable()) {
            generateAlertDialog(true);
        }
        else {
            getVerbindungenVon(bahnhof.toLowerCase());
        }

    }

    public void generateAlertDialog(boolean noInternet){
        progressBar.setVisibility(View.GONE);
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        if (noInternet){
            dialogBuilder.setMessage("Es konnte keine Verbindung zum Internet hergestellt werden. Versuchen Sie es später nochmals").setTitle("Fehler");
        }
        else{
            dialogBuilder.setMessage("Der eingegebene Bahnhof, " + bahnhof + ", konnte nicht gefunden werden.").setTitle("Fehler");
        }
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public void getVerbindungenVon(String abfahrtsOrt) {
        String requestUrl = TRANSPORT_API_URL + abfahrtsOrt;

        final AbfahrtsplanAdapter verbindungInfosAdapter = new AbfahrtsplanAdapter(getApplicationContext(), new Bahnhof());
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Bahnhof bahnhof = VerbindungJsonParser.createTimetableFromJsonString(response);

                    if(bahnhof.getVerbindung().isEmpty()) {
                        generateAlertDialog(false);
                    }

                    verbindungInfosAdapter.addAll(bahnhof.getVerbindung());
                    ListView timeTable = findViewById(R.id.verbindungen);
                    timeTable.setAdapter(verbindungInfosAdapter);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    generateAlertDialog(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                generateAlertDialog(false);
            }
        });
        queue.add(stringRequest);

        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), VerbindungsDetail.class);
                Verbindung selected = (Verbindung)parent.getItemAtPosition(position);

                intent.putExtra("Bahnhof", selected.getEndBahnhof());
                intent.putExtra("Verbindung", selected);
                startActivity(intent);
            }
        };
        ListView ka = findViewById(R.id.verbindungen);
        ka.setOnItemClickListener(mListClickedHandler);
    }

    private boolean isNetworkConnectionAvailable() {
        ConnectivityManager connectivityService = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityService.getActiveNetworkInfo();

        return null != networkInfo && networkInfo.isConnected();
    }



}