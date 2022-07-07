package ch.stanrich.timetable.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.core.util.TimeUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ch.stanrich.timetable.R;
import ch.stanrich.timetable.helper.VerbindungJsonParser;

public class Verbindung implements Parcelable {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    static {
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private Date startZeit;
    private String startBahnhof;
    private String startGleis;

    private Date endZeit;
    private String endBahnhof;
    private String endGleis;


    public Verbindung(){

    }


    protected Verbindung(Parcel in) {
        startZeit = new Date(in.readLong());
        startBahnhof = in.readString();
        startGleis = in.readString();

        endZeit = new Date(in.readLong());
        endBahnhof = in.readString();
        endGleis = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startZeit.getTime());
        dest.writeString(startBahnhof);
        dest.writeString(startGleis);

        dest.writeLong(endZeit.getTime());
        dest.writeString(endBahnhof);
        dest.writeString(endGleis);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Verbindung> CREATOR = new Creator<Verbindung>() {
        @Override
        public Verbindung createFromParcel(Parcel in) {
            return new Verbindung(in);
        }

        @Override
        public Verbindung[] newArray(int size) {
            return new Verbindung[size];
        }
    };

    public String getStartBahnhof() {
        return startBahnhof;
    }

    public void setStartBahnhof(String startBahnhof) {
        this.startBahnhof = startBahnhof;
    }

    public Date getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(String time) {
        try {
            this.startZeit = dateFormatter.parse(time);
        } catch (ParseException e) {
            Log.e(VerbindungJsonParser.class.getName(), "Time not parseable", e);
            throw new RuntimeException(e);
            //TODO perfect handling: propagate up to activity, handle there, display toast or similar error message to user explaining that something broke that has to be fixed by the developer
        }
    }

    public String getStartGleis() {
        return startGleis;
    }

    public void setStartGleis(String startGleis) {
        this.startGleis = startGleis;
    }

    public String getEndBahnhof() {
        return endBahnhof;
    }

    public void setEndBahnhof(String endBahnhof) {
        this.endBahnhof = endBahnhof;
    }

    public Date getEndZeit() {
        return endZeit;
    }

    public void setEndZeit(String time) {
        try {
            this.endZeit = dateFormatter.parse(time);
        } catch (ParseException e) {
            Log.e(VerbindungJsonParser.class.getName(), "Time not parseable", e);
            throw new RuntimeException(e);
            //TODO perfect handling: propagate up to activity, handle there, display toast or similar error message to user explaining that something broke that has to be fixed by the developer
        }
    }

    public String getEndGleis() {
        return endGleis;
    }

    public void setEndGleis(String endGleis) {
        this.endGleis = endGleis;
    }


}
