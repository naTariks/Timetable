package ch.stanrich.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import ch.stanrich.timetable.R;
import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

public class AbfahrtsplanAdapter extends ArrayAdapter<Verbindung> {

    public static final String VON = "von ";
    public static final String NACH = "nach ";

    private static final SimpleDateFormat hourminutesFormatter = new SimpleDateFormat("HH:mm");

    static {
        hourminutesFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
    }

    public AbfahrtsplanAdapter(Context context, Bahnhof bahnhof) {
        super(context, R.layout.fragment_abfahrtsplan, bahnhof.getVerbindung());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_abfahrtsplan, parent, false);

        TextView txtTime = (TextView) rowView.findViewById(R.id.txtTime);
        TextView txtDestination = (TextView) rowView.findViewById(R.id.txtDestination);
        TextView txtGleis = (TextView) rowView.findViewById(R.id.txtGleis);

        Verbindung verbindung = getItem(position);

        txtTime.setText(hourminutesFormatter.format(verbindung.getStartZeit()));
        txtDestination.setText(NACH + verbindung.getEndBahnhof() + verbindung.getZugArt());
        txtGleis.setText(verbindung.getStartGleis());

        return rowView;
    }
}
