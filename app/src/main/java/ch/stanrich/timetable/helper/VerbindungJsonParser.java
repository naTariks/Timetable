package ch.stanrich.timetable.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Iterator;

import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

public class VerbindungJsonParser {

    private final static String stationKey = "station";
    private final static String stationboardKey = "stationboard";

    public static Bahnhof createTimetableFromJsonString(String timetableJsonString) throws JSONException{
        Bahnhof bahnhof = new Bahnhof();

        JSONObject timetableObject = new JSONObject(timetableJsonString);

        JSONObject stationObject = timetableObject.getJSONObject(stationKey);
        bahnhof.setName(stationObject.getString("name"));

        JSONArray stationboardArray = timetableObject.getJSONArray(stationboardKey);
        for (int i = 0; i < stationboardArray.length(); i++) {
            JSONObject stationboardObject = stationboardArray.getJSONObject(i);
            Verbindung verbindung = new Verbindung();

            JSONObject stopObject = stationboardObject.getJSONObject("stop");

            verbindung.setStartBahnhof(stopObject.getJSONObject("station").getString("name"));
            verbindung.setStartZeit(stopObject.getString("departure"));
            verbindung.setStartGleis(stopObject.getString("platform"));
            verbindung.setEndBahnhof(stationboardObject.getString("to"));

            JSONArray passListArray = stationboardObject.getJSONArray("passList");
            JSONObject passListObject = passListArray.getJSONObject(passListArray.length()-1);
            verbindung.setEndZeit(passListObject.getString("arrival"));

            if (passListObject.getString("platform") != null){
                verbindung.setEndGleis(passListObject.getString("platform"));
            }
            else if (passListObject.getJSONObject("prognosis").getString("platform") != null){
                verbindung.setEndGleis(passListObject.getJSONObject("prognosis").getString("platform"));
            }
            else{
                verbindung.setEndGleis("Unbekannt");
            }
            bahnhof.addVerbindung(verbindung);
        }


        return bahnhof;
    }

}
