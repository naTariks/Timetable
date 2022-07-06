package ch.stanrich.timetable.model;

import java.util.ArrayList;
import java.util.List;

public class Bahnhof {

    private int id;
    private String name;
    private List<Verbindung> verbindungen;


    public Bahnhof(int id, String name){

    }

    public Bahnhof(){
        this.verbindungen = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVerbindung(List<Verbindung> verbindung) {
        this.verbindungen = verbindung;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Verbindung> getVerbindung() {
        return verbindungen;
    }

    public void addVerbindung(Verbindung verbindung) {
        this.verbindungen.add(verbindung);
    }
}
