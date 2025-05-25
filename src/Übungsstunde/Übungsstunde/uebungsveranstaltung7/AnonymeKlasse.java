package Übungsstunde.uebungsveranstaltung7;

public class AnonymeKlasse {

    static EinInterface var = x -> {
        x = x +1;
        return x;
    };


    public static void main(String[] args) {
        System.out.println(var.meth(2));
    }


}
