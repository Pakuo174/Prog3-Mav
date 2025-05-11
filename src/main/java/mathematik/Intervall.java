package mathematik;

/**
 * generische Typ T das Interface Comparable<T> implementiert - damit compareTo genutz werden kann
 * @param <T>
 */
public class Intervall <T extends Comparable<T>> {

    private T untereGrenze;
    private T obereGrenze;

    /**
     * Konstuktor zum erzeugen von Intervall-Instanzen
     * @param untereGrenze soll die untere Grenze des Intervalls sein
     * @param obereGrenze soll die obere Grenze des Intervalls sein
     */
    public Intervall (T untereGrenze, T obereGrenze){
       if (untereGrenze == null || obereGrenze == null){
           throw new IllegalArgumentException("Grenzen dürfen nicht null sein");
       }

        this.untereGrenze = untereGrenze;
        this.obereGrenze = obereGrenze;
    }

    public T getObereGrenze() {
        return obereGrenze;
    }

    public T getUntereGrenze() {
        return untereGrenze;
    }

    public boolean isLeer() {
        return obereGrenze.compareTo(untereGrenze) < 0;
    }

    /**
     * prüft, ob wert im Intervall enthalten ist, d.h. ob wert größer als die untere Grenze des
     * Intervalles ist und kleiner als die obere
     *
     * E muss dabei ein Subtyp von T sein
     * @param wert dient als Wert um das Intervall zu untersuchen
     * @return true wenn es enthalten ist
     * @param <E> muss ein Subtyp von T sein ➡️ Das ist ein Upper Bound ➡️ "E darf maximal T sein."
     */
    public <E extends T> boolean enthaelt(E wert){

            return wert.compareTo(untereGrenze) >= 0 && wert.compareTo(obereGrenze) <= 0;

    }

    /**
     * bildet ein Schnittmengen-Intervall von this und anderes
     * @param anderes
     * @return ein neues Intervall mit dem Schnitt aus dem this und anderes
     * @param <A>
     */
    public <A extends Comparable<A>> Intervall<T> schnitt(Intervall<A> anderes) {


            T andereUntere = (T) anderes.getUntereGrenze();
            T andereObere = (T) anderes.getObereGrenze();

            T neueUntereGrenze = (this.untereGrenze.compareTo(andereUntere) > 0)
                    ? this.untereGrenze : andereUntere;

            T neueObereGrenze = (this.obereGrenze.compareTo(andereObere) < 0)
                    ? this.obereGrenze : andereObere;

            if (neueUntereGrenze.compareTo(neueObereGrenze) > 0) {
                return null;
            }

            return new Intervall<>(neueUntereGrenze, neueObereGrenze);
        }
    }



