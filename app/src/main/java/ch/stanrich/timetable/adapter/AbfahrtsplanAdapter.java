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

/**
 * This is our own made ArrayAdapter. We use it, so that we can fill the list properties into the list, but with our own fragment. In the end it just makes, that i look like the information are in a table. See Abfahrtsplan
 */
public class AbfahrtsplanAdapter extends ArrayAdapter<Verbindung> {

    public static final String VON = "von ";
    public static final String NACH = "nach ";

    private static final SimpleDateFormat hourminutesFormatter = new SimpleDateFormat("HH:mm");
    //static Block for setting the Timezone of Switzerland for the Time formatter
    static {
        hourminutesFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
    }


    /**
     * Instantiates a new Abfahrtsplan adapter.
     *
     * @param context the context
     * @param bahnhof the bahnhof
     */
    public AbfahrtsplanAdapter(Context context, Bahnhof bahnhof) {
        super(context, R.layout.fragment_abfahrtsplan, bahnhof.getVerbindungen());
    }

    /**
     * Fills the Data of each Verbindung into the Views of the fragment and inflates this into the ListView as Line.
     * @param position
     * @param convertView
     * @param parent
     * @return one Row which will be in the List.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
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
