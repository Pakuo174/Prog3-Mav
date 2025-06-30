package Übungsstunde.uebungsveranstalung11;

/**
 * stellt eine Getränk dar, das in einem Getränkeautomaten angeboten werden kann
 */
public abstract class AutomatenGetraenk {
	/**
	 * bereitet das Getränk zu
	 */
	public final void kochen()
    {
    	for(int i=0; i< getTemperatur(); i++)
    		System.out.println("Wasser aufheizen");
	    this.aufgiessen();
	    this.inTasseSchuetten();
	    this.zutatHinzufuegen();
    }
	
	/**
	 * fügt ein Topping zu einem im Prinzip fertigen getränk hinzu
	 * Hook-Methode, kann für kochen überschrieben werden
	 */
	public void zutatHinzufuegen() {}

	/**
	 * bereitet das Getränk aus bereitgestelltem heißem Wasser zu
	 */
	protected abstract void aufgiessen();

	/**
	 * liefert die Temperatur des Wasser in Heizschritten
	 * @return
	 */
	public abstract int getTemperatur();

	/**
     * der fertige Kaffee wird in eine Tasse gegossen
     */
    protected void inTasseSchuetten()
    {
        System.out.println("eingiessen");
    }
}
