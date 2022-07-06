package ch.stanrich.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import ch.stanrich.timetable.R;
import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

public class AbfahrtsplanAdapter extends ArrayAdapter<Verbindung> {
    public AbfahrtsplanAdapter(Context context, Bahnhof bahnhof) {
        super(context, R.layout.fragment_abfahrtsplan, bahnhof.getVerbindung());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_abfahrtsplan, parent, false);

        TextClock txtTime = (TextClock) rowView.findViewById(R.id.txtTime);
        TextView txtDestination = (TextView) rowView.findViewById(R.id.txtDestination);
        TextView txtGleis = (TextView) rowView.findViewById(R.id.txtGleis);

        Verbindung verbindung = getItem(position);
        txtTime.setText(verbindung.getStartZeit().toString());
        txtDestination.setText(verbindung.getStartBahnhof()); //TODO voll dumm benannt, fix it
        txtGleis.setText(verbindung.getStartGleis());

        return rowView;
    }
}
