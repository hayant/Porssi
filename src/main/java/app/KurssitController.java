package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Firmat;
import data.Paivamaara;
import data.Tietokanta;


// KurssitController: Kurssisivun controller
@WebServlet("/kurssit")
public class KurssitController extends HttpServlet {
	
	private static final long serialVersionUID = -6069530460604258980L;
	private String paivamaara="";
	
	// laskePaivamaara: laskee haettavien kurssien alkupäivämäärän käyttäjän valitseman
	// vaihtoehdon mukaan.
	private void laskePaivamaara(String aika) {
		Calendar kalenteri = GregorianCalendar.getInstance();
		if(aika.equals("3kk")) {
			kalenteri.add(Calendar.MONTH, -3);
		}
		else if(aika.equals("6kk")) {
			kalenteri.add(Calendar.MONTH, -6);
		}
		else if(aika.equals("1v")) {
			kalenteri.add(Calendar.MONTH, -12);
		}
		else {
			kalenteri.add(Calendar.MONTH, -24);
		}
		
		paivamaara = Paivamaara.laskePaivamaara(kalenteri);
	}
	
	// luoKuvaajat: metodi piirtää pörssikuvaajat pörssikurssien mukaan luomalla
	// stringin, joka sisältää valmiin html-koodin.
	private String luoKuvaajat(Firmat firmat) {
		String kuvaajat = "";
		
		if(firmat.getSize() == 0) {
			return kuvaajat;
		}
		
		Tietokanta tietokanta = new Tietokanta();
		tietokanta.avaaYhteys();
		
		for(int i=0; i < firmat.getSize(); i++) {
			kuvaajat += "var data = google.visualization.arrayToDataTable([";
			kuvaajat += "['Päivämäärä', '" + firmat.getFirmanNimi(i) + "']";
			
			ArrayList<String> kurssit = new ArrayList<String>();
			ArrayList<String> paivamaarat = new ArrayList<String>();
			
			tietokanta.haeKurssit(firmat.getFirmaID(i), paivamaara, kurssit, paivamaarat);
			
			for(int j = 0; j < kurssit.size(); j++) {
				kuvaajat += ",";
				kuvaajat += "['" + paivamaarat.get(j) + "', " + kurssit.get(j) + "]";
			}
			
			kuvaajat += "]);";
			kuvaajat += "";
			kuvaajat += "var options = {";
			kuvaajat += "title: '"+ firmat.getFirmanNimi(i) + "',";
			kuvaajat += "legend: { position: 'none' },";
			kuvaajat += "titleTextStyle: { color: 'black', fontName: 'Arial', fontSize: 18 }";
			kuvaajat += "";
			kuvaajat += "";
			kuvaajat += "";
			kuvaajat += "};";
			kuvaajat += "";
			kuvaajat += "var chart = new google.visualization.LineChart(document.getElementById('" + firmat.getFirmaID(i) + "'));";
			kuvaajat += "";
			kuvaajat += "chart.draw(data, options);";
			kuvaajat += "";
		}
		tietokanta.suljeYhteys();
		return kuvaajat;
	}

	// luoIndeksit: metodi piirtää indeksit pörssikursseista luomalla
	// stringin, joka sisältää valmiin html-koodin.
	private String luoIndeksit(Firmat firmat) {
		String kuvaajat = "";
		
		if(firmat.getSize() == 0) {
			return kuvaajat;
		}
		
		float vertailuluku = 0;
		
		Tietokanta tietokanta = new Tietokanta();
		tietokanta.avaaYhteys();
		
		for(int i=0; i < firmat.getSize(); i++) {
			boolean eka = true;
			
			kuvaajat += "var data = google.visualization.arrayToDataTable([";
			kuvaajat += "['Päivämäärä', '" + firmat.getFirmanNimi(i) + "']";
			
			ArrayList<String> kurssit = new ArrayList<String>();
			ArrayList<String> paivamaarat = new ArrayList<String>();
			
			tietokanta.haeKurssit(firmat.getFirmaID(i), paivamaara, kurssit, paivamaarat);
			
			for(int j = 0; j < kurssit.size(); j++) {
				kuvaajat += ",";
					
				if(eka && (Float.parseFloat(kurssit.get(j)) != 0)) {
					vertailuluku = 100 / Float.parseFloat(kurssit.get(j));
					eka = false;
				}
					
				kuvaajat += "['" + paivamaarat.get(j) + "', " + Float.toString(vertailuluku * Float.parseFloat(kurssit.get(j))) + "]";				
			}

			
			kuvaajat += "]);";
			kuvaajat += "";
			kuvaajat += "var options = {";
			kuvaajat += "title: '"+ firmat.getFirmanNimi(i) + "',";
			kuvaajat += "legend: { position: 'none' },";
			kuvaajat += "titleTextStyle: { color: 'black', fontName: 'Arial', fontSize: 18 }";
			kuvaajat += "";
			kuvaajat += "";
			kuvaajat += "";
			kuvaajat += "};";
			kuvaajat += "";
			kuvaajat += "var chart = new google.visualization.LineChart(document.getElementById('" + firmat.getFirmaID(i) + "'));";
			kuvaajat += "";
			kuvaajat += "chart.draw(data, options);";
			kuvaajat += "";
		}
		return kuvaajat;
	}

	// luoIndeksitSamaan: metodi piirtää indeksit pörssikursseista yhteen kuvaajaan luomalla
	// stringin, joka sisältää valmiin html-koodin.
	private String luoIndeksitSamaan(Firmat firmat) {
		String kuvaajat = "";
		
		if(firmat.getSize() == 0) {
			return kuvaajat;
		}
		
		Tietokanta tietokanta = new Tietokanta();
		tietokanta.avaaYhteys();
		
		ArrayList<String> paivamaarat = tietokanta.haePaivamaarat(paivamaara);
		
		ArrayList<Float> vertailuluvut = new ArrayList<Float>();
		for(int i = 0; i < firmat.getSize(); i++) {
			boolean notFound = true;
			for(int j = 0; j < paivamaarat.size(); j++) {
				String kurssiTemp = tietokanta.haeKurssi(firmat.getFirmaID(i) , paivamaarat.get(j));
				if(Float.parseFloat(kurssiTemp) != 0) {
					vertailuluvut.add(100 / Float.parseFloat(kurssiTemp));
					notFound = false;
					break;
				}
			}
			if(notFound) {
				vertailuluvut.add((float)0);
			}
		}
		
		kuvaajat += "var data = new google.visualization.DataTable();";
		kuvaajat += "data.addColumn('string', 'Päivämäärä');";
				
		for(int i=0; i < firmat.getSize(); i++) {
			kuvaajat += "data.addColumn('number', '" + firmat.getFirmanNimi(i) + "');";
		}
		
		kuvaajat += "data.addRows([";
		
		for(int i = 0; i < paivamaarat.size(); i++) {
			kuvaajat += "['" + paivamaarat.get(i) + "'";
			for(int j = 0; j < firmat.getSize(); j++) {
				kuvaajat += ", ";
				//Float.toString(vertailuluku * Float.parseFloat(kurssit.get(j)))
				kuvaajat += Float.toString(vertailuluvut.get(j) * Float.parseFloat(tietokanta.haeKurssi(firmat.getFirmaID(j) , paivamaarat.get(i))));
			}
			kuvaajat += "], ";
		}
	
		kuvaajat = kuvaajat.substring(0, kuvaajat.length() - 2);
		
		kuvaajat += "]);";
		kuvaajat += "";
		kuvaajat += "var options = {";
		kuvaajat += "title: 'Indeksit',";
		kuvaajat += "titleTextStyle: { color: 'black', fontName: 'Arial', fontSize: 18 }";
		kuvaajat += "";
		kuvaajat += "";
		kuvaajat += "";
		kuvaajat += "};";
		kuvaajat += "";
		kuvaajat += "var chart = new google.visualization.LineChart(document.getElementById('indeksi'));";
		kuvaajat += "";
		kuvaajat += "chart.draw(data, options);";
		kuvaajat += "";
		
		return kuvaajat;
	}
	
	// haeFirmat: metodi palauttaa Firmat-olion, joka sisältää ne yritykset jotka
	// käyttäjä on valinnut lomakkeelta.
	private Firmat haeFirmat(Firmat firmat, String[] firmatHtml) {
		Firmat firmatTemp = new Firmat();
		int firmatHtmlLength = 0;
		
		try {
			firmatHtmlLength = firmatHtml.length;
		} catch(NullPointerException npe) {
			return firmatTemp;
		}
		
		for(int i = 0; i < firmatHtmlLength; i++) {
			for(int j = 0; j < firmat.getSize(); j++) {
				if(firmatHtml[i].equals(firmat.getFirmaID(j))) {
					firmatTemp.lisaaFirma(firmat.getFirmanNimi(j), firmat.getFirmaID(j));
					break;
				}
			}
		}
		
		return firmatTemp;
	}
	
	// luoDivit: metodi generoi kuvaajille tarvittavan html-koodin.
	public String luoDivit(Firmat firmat) {		
		String divit = "";
		
		for(int i=0; i < firmat.getSize(); i++) {
			divit += "<div id=\"" + firmat.getFirmaID(i) + "\" style=\"width: 700px; height: 300px;\"></div>";
			if((i % 3) == 2) {
				divit += "<p class=\"break\"></p>";
			}
		}		
		
		return divit;
	}
	
	// doPost: ottaa vastaan pyynnön lomakkeelta ja tekee tarvittavat toimenpiteet, eli
	// rakentaa kurssinäkymän käyttäjän valitsemien vaihtoehtojen mukaan.
	public void doPost(HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException, NullPointerException
	{
		Firmat firmat = new Firmat();
		
		// Set response content type
		response.setContentType("text/html");

		RequestDispatcher rd = request.getRequestDispatcher("KurssitView.jsp");
		
		// Otetaan lomakkeelta valitut firmat talteen.
		String[] firmatHtml = request.getParameterValues("firma");
			
		String aika = request.getParameter("aika");
		String kuvaajatyyppi = request.getParameter("tyyppi");
		laskePaivamaara(aika);
			
		// Tietokanta tietokanta = new Tietokanta();
			
		// Haetaan firmat ja niiden id:t taulusta ja tallennetaan ne kahteen listaan.
		firmat.haeFirmatTietokannasta();
			
		// Muokataan firmalistaa haun mukaiseksi
		firmat = haeFirmat(firmat, firmatHtml);
			
		if(kuvaajatyyppi.equals("kurssit")) {
			request.setAttribute("kuvaajat", luoKuvaajat(firmat));
			request.setAttribute("divit", luoDivit(firmat));
		}
		else if(kuvaajatyyppi.equals("indeksit")) {
			request.setAttribute("kuvaajat", luoIndeksit(firmat));
			request.setAttribute("divit", luoDivit(firmat));
		}
		else {
			request.setAttribute("kuvaajat", luoIndeksitSamaan(firmat));
			Firmat dummy = new Firmat();
			dummy.lisaaFirma("dummy", "indeksi");
			request.setAttribute("divit", luoDivit(dummy));
		}
				
		rd.forward(request, response);
				
	}
}
