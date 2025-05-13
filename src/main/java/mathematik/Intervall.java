package mathematik;

/**
 * Ein generisches Intervall für beliebige Typen {@code T}, die mit sich selbst oder mit einer ihrer Oberklassen vergleichbar sind.
 * <p>
 * Der Typparameter {@code T} muss {@code Comparable<? super T>} implementieren, damit {@code compareTo} auf {@code T}-Objekten verwendet werden kann.
 * Dies erlaubt auch spezielle Typen wie {@link java.sql.Time}, die {@code Comparable<java.util.Date>} implementieren,
 * obwohl sie nicht direkt {@code Comparable<Time>} sind.
 *
 * @param <T> der Typ der Intervallgrenzen, muss mit sich selbst oder einer Oberklasse vergleichbar sein
 */
public class Intervall<T extends Comparable<? super T>> {


    private T untereGrenze;
    private T obereGrenze;

    /**
     * Konstuktor zum erzeugen von Intervall-Instanzen
     * @param untereGrenze soll die untere Grenze des Intervalls sein
     * @param obereGrenze soll die obere Grenze des Intervalls sein
     * @throws IllegalArgumentException, wenn bei den Grenzen der Wert null wäre
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

    /**
     * prüft,ob das Intervall leer ist
     * @return true, wenn die obereGrenze kleiner ist als die untereGrenze
     */
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
     public <E extends Comparable<? super T>> boolean enthaelt(E wert){

            return wert.compareTo(untereGrenze) >= 0 && wert.compareTo(obereGrenze) <= 0;

    }

    /**
     * Bildet ein Schnittmengen-Intervall von {@code this} und {@code anderes}.
     *
     * Zur Compile-Zeit wird durch {@code <A extends Comparable<? super A>>} sichergestellt,
     * dass der Typ A mit sich selbst oder einer seiner Oberklassen vergleichbar ist (kontravarianter Vergleich).
     * Das erlaubt auch Typen wie {@link java.sql.Time}, die {@code Comparable<java.util.Date>} implementieren.
     *
     * @param anderes Das übergebene Intervall, mit dem das Schnittmengen-Intervall gebildet werden soll.
     * @param <A> Der Typ A des übergebenen Intervalls muss das Interface {@code Comparable<? super A>} implementieren,
     *           damit A mit sich selbst oder einer Oberklasse vergleichbar ist.
     * @return Ein neues Intervall, das den Schnitt von {@code this} und {@code anderes} darstellt,
     *         oder {@code null}, wenn die Intervalle disjunkt sind.
     */
    public <A extends Comparable<? super A>> Intervall<T> schnitt(Intervall<A> anderes) {


            T andereUntere = (T) anderes.getUntereGrenze();
            T andereObere = (T) anderes.getObereGrenze();

        T neueUntereGrenze;
        if (this.untereGrenze.compareTo(andereUntere) > 0) {
            neueUntereGrenze = this.untereGrenze;
        } else {
            neueUntereGrenze = andereUntere;
        }

        T neueObereGrenze;
        if ( this.obereGrenze.compareTo(andereObere) < 0){
            neueObereGrenze = this.obereGrenze;
        }else {
            neueObereGrenze = andereObere;
        }

            if (neueUntereGrenze.compareTo(neueObereGrenze) > 0) {
                return null;
            }

            return new Intervall<T>(neueUntereGrenze, neueObereGrenze);
        }
    }



