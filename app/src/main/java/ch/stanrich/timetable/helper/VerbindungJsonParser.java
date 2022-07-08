package ch.stanrich.timetable.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import ch.stanrich.timetable.model.Bahnhof;
import ch.stanrich.timetable.model.Verbindung;

public class VerbindungJsonParser {

    /**
     * The constant UNKNOWN_DATA.
     * This is used if we get some null data from the API
     */
    public final static String UNKNOWN_DATA = "unb.";
    /**
     * The constant INVALID_TIME.
     * This is used when the Time we get from the API is null, so we can check in {@link ch.stanrich.timetable.Abfahrtsplan} if time is null and print UNKNOWN_DATA.
     */
    public final static long INVALID_TIME = 0;
    private final static String STATION_KEY = "station";
    private final static String STATIONBOARD_KEY = "stationboard";

    /**
     * Create all Verbindungen for the corresponding Bahnhof with the given Data from the API-Call (timetableJsonString).
     *
     * @param timetableJsonString the timetable json string
     * @return the bahnhof with a list of all Verbindungen
     * @throws JSONException  the json exception
     * @throws ParseException the parse exception
     */
    public static Bahnhof createTimetableFromJsonString(String timetableJsonString) throws JSONException, ParseException {
        Bahnhof bahnhof = new Bahnhof();

        JSONObject timetableObject = new JSONObject(timetableJsonString);

        JSONObject stationObject = timetableObject.getJSONObject(STATION_KEY);

        if (!stationObject.isNull("name")) {
            bahnhof.setName(stationObject.getString("name"));
        } else {
            bahnhof.setName(UNKNOWN_DATA);
        }

        JSONArray stationboardArray = timetableObject.getJSONArray(STATIONBOARD_KEY);
        if (stationboardArray.length() > 0) {
            //for every Verbindung in the stationBoardArray a new Verbindung Object is created and filled with the Data from the API
            for (int i = 0; i < stationboardArray.length(); i++) {
                JSONObject stationboardObject = stationboardArray.getJSONObject(i);
                Verbindung verbindung = new Verbindung();

                JSONObject stopObject = stationboardObject.getJSONObject("stop");

                if (!stopObject.getJSONObject("station").isNull("name")) {
                    verbindung.setStartBahnhof(stopObject.getJSONObject("station").getString("name"));
                } else {
                    verbindung.setStartBahnhof(UNKNOWN_DATA);
                }

                if (!stopObject.isNull("departure")) {
                    verbindung.setStartZeit(stopObject.getString("departure"));
                } else {
                    verbindung.setStartZeit(INVALID_TIME);
                }

                if (!stopObject.isNull("platform")) {
                    verbindung.setStartGleis(stopObject.getString("platform"));
                } else {
                    verbindung.setStartGleis(UNKNOWN_DATA);
                }

                if (!stationboardObject.isNull("to")) {
                    verbindung.setEndBahnhof(stationboardObject.getString("to"));
                } else {
                    verbindung.setEndBahnhof(UNKNOWN_DATA);
                }

                if (!stationboardObject.isNull("category")) {
                    verbindung.setZugArt(" (" + stationboardObject.getString("category") + ")");
                } else {
                    verbindung.setZugArt(null);
                }

                JSONArray passListArray = stationboardObject.getJSONArray("passList");
                JSONObject passListObject = passListArray.getJSONObject(passListArray.length() - 1);

                if (!passListObject.isNull("arrival")) {
                    verbindung.setEndZeit(passListObject.getString("arrival"));
                } else {
                    verbindung.setEndZeit(INVALID_TIME);
                }

                if (!passListObject.isNull("platform")) {
                    verbindung.setEndGleis(passListObject.getString("platform"));
                } else if (!passListObject.getJSONObject("prognosis").isNull("platform")) {
                    verbindung.setEndGleis(passListObject.getJSONObject("prognosis").getString("platform"));
                } else {
                    verbindung.setEndGleis(UNKNOWN_DATA);
                }

                bahnhof.addVerbindung(verbindung);
            }
        }


        return bahnhof;
    }

}
