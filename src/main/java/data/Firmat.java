package data;

import java.util.ArrayList;
import data.Tietokanta;

// Firmat-luokka: sisältää metodeja firmojen hallintaan.
public class Firmat {
	
	private ArrayList<String> firmanNimet = new ArrayList<String>();
	private ArrayList<String> firmaIDt = new ArrayList<String>();
	
	private int firmatSize = 0;

	// haeFirmatTietokannasta: hakee yritysten nimet ja IDt tietokannasta ja tallentaa ne
	// kahteen ArrayListiin.
	public void haeFirmatTietokannasta() {
		
		Tietokanta tietokanta = new Tietokanta();
		tietokanta.avaaYhteys();
		tietokanta.haeFirmat(firmanNimet, firmaIDt);
		tietokanta.suljeYhteys();
		firmatSize = firmanNimet.size();
	}
	
	// Gettereitä...
	public ArrayList<String> getFirmanNimet() {
		return firmanNimet;
	}
	
	public ArrayList<String> getFirmaIDt() {
		return firmaIDt;
	}
	
	public String getFirmanNimi(int indeksi) {
		return firmanNimet.get(indeksi);
	}
	
	public String getFirmaID(int indeksi) {
		return firmaIDt.get(indeksi);
	}
	
	public int getSize() {
		return firmatSize;
	}
	
	// Lisää firman olioon
	public void lisaaFirma(String firmanNimi, String firmaID) {
		firmanNimet.add(firmanNimi);
		firmaIDt.add(firmaID);
		firmatSize++;
	}
	
	// Tyhjentää olion
	public void tyhjenna() {
		firmanNimet.clear();
		firmaIDt.clear();
		firmatSize = 0;
	}
	
	// tulostaFirmat: muodostaa valikko-näkymälle sopivan firmalistan html-koodin.
	public String tulostaFirmat() {
		
		String htmlFirmat = "";
		
		try {
			for(int i = 0; i < firmatSize; i++) {
				htmlFirmat += "<input type=\"checkbox\" name=\"firma\" value=\""
						+ firmaIDt.get(i) + "\">" + firmanNimet.get(i) + "<br />" + "\n";
			}
		} catch(OutOfMemoryError E) {
			System.err.println(htmlFirmat);
		}
		return htmlFirmat;
	}
}

