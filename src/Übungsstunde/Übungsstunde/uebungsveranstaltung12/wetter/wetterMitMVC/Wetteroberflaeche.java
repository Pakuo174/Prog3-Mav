package Übungsstunde.uebungsveranstaltung12.wetter.wetterMitMVC;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Color;

/**
 * Eine Oberfläche, um Wetterdaten verschiedener Städte anzeigen und
 * verändern zu können
 * @author Doro
 *
 */
@SuppressWarnings("serial")
public class Wetteroberflaeche extends JFrame 
		implements PropertyChangeListener{

	/**
	 * Oberfläche
	 */
	private JPanel contentPane;
	/**
	 * Text "Temperatur"
	 */
	private JLabel lblText1;
	/**
	 * Text "Feuchtigkeit"
	 */
	private JLabel lblText2;
	/**
	 * Text "Luftdruck"
	 */
	private JLabel lblText3;
	/**
	 * Balken für die Luftfeuchtigkeit
	 */
	private JProgressBar pbFeuchtigkeit;
	/**
	 * Temperaturwert
	 */
	private JLabel lblTemperatur;
	/**
	 * Luftdruckwert
	 */
	private JLabel lblLuftdruck;
	/**
	 * Sonnentanz durchführen
	 */
	private JButton btnSonnentanz;
	/**
	 * Regentanz durchführen
	 */
	private JButton btnRegentanz;
	/**
	 * Der Controller für diese Oberfläche
	 */
	private WetterController controller;

	/**
	 * Erstellt die Oberfläche
	 * @param controller der zuständige Controller
	 */
	public Wetteroberflaeche(WetterController controller) {
		this.controller = controller;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblText1 = new JLabel("Temperatur:");
		lblText1.setBounds(10, 11, 113, 14);
		contentPane.add(lblText1);
		
		lblText2 = new JLabel("Feuchtigkeit:");
		lblText2.setBounds(10, 35, 102, 14);
		contentPane.add(lblText2);
		
		lblText3 = new JLabel("Luftdruck:");
		lblText3.setBounds(10, 62, 102, 14);
		contentPane.add(lblText3);
		
		pbFeuchtigkeit = new JProgressBar();
		pbFeuchtigkeit.setBounds(134, 35, 146, 14);
		pbFeuchtigkeit.setMinimum(0);
		pbFeuchtigkeit.setMaximum(100);
		contentPane.add(pbFeuchtigkeit);
		
		lblTemperatur = new JLabel("New label");
		lblTemperatur.setBounds(135, 11, 88, 14);
		contentPane.add(lblTemperatur);
		
		lblLuftdruck = new JLabel("New label");
		lblLuftdruck.setBounds(134, 62, 89, 14);
		contentPane.add(lblLuftdruck);
		
		btnSonnentanz = new JButton("Sonnentanz");
		btnSonnentanz.setBounds(26, 152, 137, 34);
		contentPane.add(btnSonnentanz);
		
		btnRegentanz = new JButton("Regentanz");
		btnRegentanz.setBounds(211, 154, 121, 31);
		contentPane.add(btnRegentanz);

		this.setVisible(true);
		
		btnSonnentanz.addActionListener(e -> controller.sonne());
		btnRegentanz.addActionListener(controller);
	}

	/**
	 * zeigt die Aktion an, die gerade ausgeführt wird
	 * @param gutesWetter true, wenn es sich um eine positive Wetterbeeinflussung handelt
	 */
	public void aktionSetzen(boolean gutesWetter)
	{
		if(gutesWetter)
		{
			this.setTitle("Wetterbesserung");
			this.contentPane.setBackground(Color.YELLOW);
		}
		else
		{
			this.setTitle("Wetterverschlechterung");
			this.contentPane.setBackground(Color.GRAY);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Wetterdaten w = (Wetterdaten) evt.getSource();
		this.lblLuftdruck.setText(w.getLuftdruck() + " hP");
		this.lblTemperatur.setText(w.getTemperatur() + " Grad");
		this.pbFeuchtigkeit.setValue((int) w.getFeuchtigkeit());
		
	}
	
}
