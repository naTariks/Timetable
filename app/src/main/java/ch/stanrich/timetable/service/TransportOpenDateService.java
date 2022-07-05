package ch.stanrich.timetable.service;

import android.util.Log;

public class TransportOpenDateService {

    private static final String TRANSPORT_API_URL = "http://transport.opendata.ch/v1/stationboard?station=";

    public static void getVerbindungenVon(String abfahrtsOrt) {
        String request = TRANSPORT_API_URL + abfahrtsOrt;
        Log.d(TransportOpenDateService.class.getName(), "RESULATAT DA INE");
    }
}
