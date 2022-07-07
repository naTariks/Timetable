package ch.stanrich.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import android.view.View.OnKeyListener;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#323437\">" + getString(R.string.app_name) + "</font>"));
    }

    public void btnGoClicked(View view) {
        EditText inputBhf = findViewById(R.id.bahnhofInput);
        String bahnhof = inputBhf.getText().toString();
        Intent intent = new Intent(this, Abfahrtsplan.class);
        intent.putExtra("Bahnhof", bahnhof);
        startActivity(intent);
    }

    public void btnHelpClicked(View view) {
        Intent intent = new Intent(this, HelpImpressum.class);
        startActivity(intent);
    }
}