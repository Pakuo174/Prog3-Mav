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
     * Konstruktor zum Erzeugen von Intervall-Instanzen.
     *
     * @param untereGrenze die untere Grenze des Intervalls
     * @param obereGrenze die obere Grenze des Intervalls
     * @throws IllegalArgumentException wenn bei den Grenzen der Wert {@code null} ist
     */
    public Intervall (T untereGrenze, T obereGrenze){
       if (untereGrenze == null || obereGrenze == null){
           throw new IllegalArgumentException("Grenzen dürfen nicht null sein");
       }

        this.untereGrenze = untereGrenze;
        this.obereGrenze = obereGrenze;
    }

    /**
     *
     * @return gibt den Wert der Obergrenze des aufrufenden Objektes zurück
     */
    public T getObereGrenze() {
        return obereGrenze;
    }

    /**
     *
     * @return gibt den Wert der Untergrenze des aufrufenden Objektes zurück
     */
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
     * Prüft, ob ein gegebener Wert innerhalb des Intervalls liegt – also
     * größer oder gleich der unteren Grenze und kleiner oder gleich der oberen Grenze ist.
     *
     * @param wert der zu überprüfende Wert
     * @param <E> ein generischer Typ, der mit T vergleichbar sein muss
     *            (E muss also T oder ein Obertyp von T sein und Comparable von T implementieren)
     * @return true, wenn der Wert im Intervall liegt, sonst false
     */
     public <E extends Comparable<? super T>> boolean enthaelt(E wert){

            return wert.compareTo(untereGrenze) >= 0 && wert.compareTo(obereGrenze) <= 0;

    }

    /**
     * Bildet den Schnitt mit einem anderen Intervall.
     * @param anderes Das andere Intervall mit Grenzen vom Typ T oder Subtyp.
     * @return Ein neues Intervall-Objekt mit der Schnittmenge (evtl. leer).
     * @param <A> des Intervalls-Objektes muss T oder ein Subtyp von sein 
     */
    public <A extends T> Intervall<T> schnitt(Intervall<A> anderes) {


            T andereUntere = anderes.getUntereGrenze();
            T andereObere = anderes.getObereGrenze();

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
                return new Intervall<>(neueUntereGrenze, neueObereGrenze); // auch wenn leer
            }

            return new Intervall<>(neueUntereGrenze, neueObereGrenze);
        }
    }



