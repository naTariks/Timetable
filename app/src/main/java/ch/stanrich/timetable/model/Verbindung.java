package ch.stanrich.timetable.model;

public class Verbindung {

    private String startBahnhof;
    private String startZeit;
    private String startGleis;

    private String endBahnhof;
    private String endZeit;
    private String endGleis;

    public Verbindung(){

    }


    public String getStartBahnhof() {
        return startBahnhof;
    }

    public void setStartBahnhof(String startBahnhof) {
        this.startBahnhof = startBahnhof;
    }

    public String getStartZeit() {
        return startZeit;
    }

    public void setStartZeit(String startZeit) {
        this.startZeit = startZeit;
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

    public String getEndZeit() {
        return endZeit;
    }

    public void setEndZeit(String endZeit) {
        this.endZeit = endZeit;
    }

    public String getEndGleis() {
        return endGleis;
    }

    public void setEndGleis(String endGleis) {
        this.endGleis = endGleis;
    }
}
