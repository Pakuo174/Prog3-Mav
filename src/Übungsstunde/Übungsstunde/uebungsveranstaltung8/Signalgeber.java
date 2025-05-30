package Übungsstunde.uebungsveranstaltung8;

import Übungsstunde.uebungsveranstaltung8.Spieler;

public class Signalgeber implements Runnable {
    private Spieler s1, s2;

    public Signalgeber(Spieler s1, Spieler s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (s1) {
            s1.setDarfStarten(true);
        }
        synchronized (s2) {
            s2.setDarfStarten(true);
        }
    }
}
