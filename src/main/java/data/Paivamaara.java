package data;

import java.util.Calendar;
import java.util.GregorianCalendar;

// Paivamaara-luokka: muodostaa Nasdaq OMX Nordicin / tietokannan hakuun sopivan muotoisen
// päivämäärä-stringin ("YYYY-MM-DD").
public class Paivamaara {
	
	// laskePaivamaara: jos parametrejä ei anneta, muodostetaan päivämäärä käyttäen
	// tätä päivää.
	public static String laskePaivamaara() {
		Calendar kalenteri = GregorianCalendar.getInstance();
		kalenteri.add(Calendar.DAY_OF_MONTH, -1);
		int paiva_ = kalenteri.get(Calendar.DAY_OF_MONTH);
		int kuukausi_ = kalenteri.get(Calendar.MONTH) + 1;
		int vuosi_ = kalenteri.get(Calendar.YEAR);
		String vuosi__ = Integer.toString(vuosi_);
		String kuukausi__;
		String paiva__;
		if(kuukausi_ < 10) {
			kuukausi__ = "0" + Integer.toString(kuukausi_);
		} 
		else {
			kuukausi__ = Integer.toString(kuukausi_);
		}
		if(paiva_ < 10) {
			paiva__ = "0" + Integer.toString(paiva_);
		}
		else {
			paiva__ = Integer.toString(paiva_);
		}
		String paivamaara = vuosi__ + "-" + kuukausi__ + "-" + paiva__;
		return paivamaara;
	}
	
	// laskePaivamaara: jos parametriksi annetaan jokin Calender-muotoinen päivämäärä,
	// muodostetaan hakuun sopiva päivämäärä kyseisen päiväyksen mukaan.
	public static String laskePaivamaara(Calendar kalenteri) {
		// Tehdään URL-hakuun sopiva päivämäärä-string.
		kalenteri.add(Calendar.DAY_OF_MONTH, -1);
		int paiva_ = kalenteri.get(Calendar.DAY_OF_MONTH);
		int kuukausi_ = kalenteri.get(Calendar.MONTH) + 1;
		int vuosi_ = kalenteri.get(Calendar.YEAR);
		String vuosi__ = Integer.toString(vuosi_);
		String kuukausi__;
		String paiva__;
		if(kuukausi_ < 10) {
			kuukausi__ = "0" + Integer.toString(kuukausi_);
		} 
		else {
			kuukausi__ = Integer.toString(kuukausi_);
		}
		if(paiva_ < 10) {
			paiva__ = "0" + Integer.toString(paiva_);
		}
		else {
			paiva__ = Integer.toString(paiva_);
		}
		String paivamaara = vuosi__ + "-" + kuukausi__ + "-" + paiva__;
		return paivamaara;
	}
}
