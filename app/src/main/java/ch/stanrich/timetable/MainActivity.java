package ch.stanrich.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Uncomment next Line to set App into Darkmode
        /*AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);*/

        //setting the Title Color in a cursed way
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">" + getString(R.string.app_name) + "</font>"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Button btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(v -> btnGetAbfahrtsplan());

        FloatingActionButton fltHelp = findViewById(R.id.fltHelp);
        fltHelp.setOnClickListener(v -> fltOpenHelp());
    }

    /**
     * Opens Abfahrtsplan Activity and handsover the inserted Bahnhof
     */
    private void btnGetAbfahrtsplan() {
        EditText inputBhf = findViewById(R.id.bahnhofInput);
        String bahnhof = inputBhf.getText().toString();
        Intent intent = new Intent(this, Abfahrtsplan.class);
        intent.putExtra("Bahnhof", bahnhof);
        startActivity(intent);
    }

    /**
     * Is called if clicked on Floating Action Button with Question Mark and opens HelpImpressum Activity.
     */
    private void fltOpenHelp() {
        Intent intent = new Intent(this, HelpImpressum.class);
        startActivity(intent);
    }
}