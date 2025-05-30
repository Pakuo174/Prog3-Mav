package Nullstelle;

import java.util.function.DoubleUnaryOperator; // funktionelles Interface Eingabe : double , Ausgabe : double

/**
 * Bisektionsverfahren
 */
public class NullstellenSuche {

    /**
     * Es wird ein x gesucht, für das f(x) ≈ 0 gilt, also der y-Wert möglichst nah an 0 liegt.
     *
     * @param f  die Funktion, deren Nullstelle gesucht wird
     * @param a  untere Intervallgrenze (kann negativ oder 0 sein)
     * @param b  obere Intervallgrenze (größer als a)
     * @return   eine Nullstelle (x-Wert), bei der f(x) ungefähr 0 ist
     * @throws IllegalArgumentException wenn im Intervall [a, b] keine Nullstelle (kein Vorzeichenwechsel) vorliegt
     */
    public static Double findeNullstelle(DoubleUnaryOperator f, double a, double b){

            if (f.applyAsDouble(a) * f.applyAsDouble(b) > 0) {
                throw new IllegalArgumentException("Keine Nullstelle im Intervall.");
            }

        while(Math.abs(b-a) >= 0.01){
            double mid = (a+b) /2;
            if (f.applyAsDouble(a) * f.applyAsDouble(mid) <= 0){
                b = mid;
            }else {
                a = mid;
            }
        }
        return (a+b)/2;
    }

}
