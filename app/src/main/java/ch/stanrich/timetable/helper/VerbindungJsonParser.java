package ch.stanrich.timetable.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Iterator;

import ch.stanrich.timetable.Abfahrtsplan;
import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

public class VerbindungJsonParser {

    private final static String STATION_KEY = "station";
    private final static String STATIONBOARD_KEY = "stationboard";
    private final static String DEFAULT_TIME = "1970-01-01T00:00:00Z";
    private final static String UNBEKANNT = "Unbekannt";

    public static Bahnhof createTimetableFromJsonString(String timetableJsonString) throws JSONException{
        Bahnhof bahnhof = new Bahnhof();

        JSONObject timetableObject = new JSONObject(timetableJsonString);

        JSONObject stationObject = timetableObject.getJSONObject(STATION_KEY);

        if (!stationObject.isNull("name")) {
            bahnhof.setName(stationObject.getString("name"));
        }
        else {
            bahnhof.setName(UNBEKANNT);
        }

        JSONArray stationboardArray = timetableObject.getJSONArray(STATIONBOARD_KEY);
        if (stationboardArray.length() > 0) {
            for (int i = 0; i < stationboardArray.length(); i++) {
                JSONObject stationboardObject = stationboardArray.getJSONObject(i);
                Verbindung verbindung = new Verbindung();

                JSONObject stopObject = stationboardObject.getJSONObject("stop");

                if (!stopObject.getJSONObject("station").isNull("name")) {
                    verbindung.setStartBahnhof(stopObject.getJSONObject("station").getString("name"));
                }
                else {
                    verbindung.setStartBahnhof(UNBEKANNT);
                }

                if (!stopObject.isNull("departure")){
                    verbindung.setStartZeit(stopObject.getString("departure"));
                }
                else {
                    verbindung.setStartZeit(DEFAULT_TIME);
                }

                if (!stopObject.isNull("platform")) {
                    verbindung.setStartGleis(stopObject.getString("platform"));
                }
                else {
                    verbindung.setStartGleis(UNBEKANNT);
                }

                if (!stationboardObject.isNull("to")) {
                    verbindung.setEndBahnhof(stationboardObject.getString("to"));
                }
                else {
                    verbindung.setEndBahnhof(UNBEKANNT);
                }

                JSONArray passListArray = stationboardObject.getJSONArray("passList");
                JSONObject passListObject = passListArray.getJSONObject(passListArray.length()-1);

                if (!passListObject.isNull("arrival")) {
                    verbindung.setEndZeit(passListObject.getString("arrival"));
                }
                else {
                    verbindung.setEndZeit(DEFAULT_TIME);
                }

                if (!passListObject.isNull("platform")){
                    verbindung.setEndGleis(passListObject.getString("platform"));
                }
                else if (!passListObject.getJSONObject("prognosis").isNull("platform")){
                    verbindung.setEndGleis(passListObject.getJSONObject("prognosis").getString("platform"));
                }
                else{
                    verbindung.setEndGleis(UNBEKANNT);
                }

                bahnhof.addVerbindung(verbindung);
            }
        }



        return bahnhof;
    }

}
