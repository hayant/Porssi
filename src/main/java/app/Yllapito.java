package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Paivamaara;
import data.Tietokanta;


@WebServlet("/luo")
//@Stateless
//@LocalBean
// Yllapito-luokan tehtävänä on tietokannan automaattinen ylläpito.
public class Yllapito extends HttpServlet {

	public Yllapito() {
		
	}
	
	// luoTietokanta: Käytetään vain ensimmäisellä kerralla luomaan tietokanta tyhjästä.
	/*public void luoTietokanta()
	{			
		// Luodaan tietokanta-olio
		Tietokanta tietokanta = new Tietokanta();
		
		// Avataan tietokantayhteys, poistetaan vanha tietokanta ja luodaan uusi.
		tietokanta.avaaYhteys();
		tietokanta.poistaKurssit();
		tietokanta.luoKurssit();
		tietokanta.lisaaKurssit();
		
		// Suljetaan yhteys
		tietokanta.suljeYhteys();
	}*/

	

	/*public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException, NullPointerException {
		paivitaTietokanta();
	}*/
	
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException, NullPointerException {
		paivitaTietokanta();
	}

	// Ajastettu metodi paivitaTietokanta päivittää pörssitietokannan arkipäivisin klo 19.00
	// @Schedule(minute="1", hour="0", dayOfWeek="Mon-Fri", persistent=false)
	public void paivitaTietokanta() {	
		System.out.println("Tietokannan päivitys alkoi");
		Calendar kalenteri = GregorianCalendar.getInstance();
		kalenteri.add(Calendar.YEAR, -2);
		
		// Luodaan tietokanta-olio
		Tietokanta tietokanta = new Tietokanta();
		
		// Avataan tietokantayhteys.
		tietokanta.avaaYhteys();
		
		// Lisätään puuttuvat kurssit
		ArrayList<String> paivamaarat = tietokanta.haePaivamaarat(Paivamaara.laskePaivamaara(kalenteri));
		if(paivamaarat.size() > 0) {
			tietokanta.lisaaKurssit(paivamaarat.get(paivamaarat.size() - 1));
			tietokanta.poistaKurssit(Paivamaara.laskePaivamaara(kalenteri));
		}
		else {
			tietokanta.lisaaKurssit(Paivamaara.laskePaivamaara(kalenteri));
		}
		
		// Suljetaan yhteys
		tietokanta.suljeYhteys();
		System.out.println("Tietokannan päivitys päättyi");
	}
}
