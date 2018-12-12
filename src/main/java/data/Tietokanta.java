package data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.*;
import javax.sql.DataSource;

import data.Firmat;

// Tietokanta-luokka: tietokannan käsittelyn DAO.
public class Tietokanta {

	private static DataSource dataSource;
	protected Connection conn=null;
	protected Statement stmt=null;
	protected ResultSet RS=null;
	
	public Tietokanta() {
		
	}
	
	// getDataSource: hakee tietokannan parametrit palvelimen kontekstista.
	private static DataSource getDataSource() {
		if(dataSource == null)
		{
			try{
				Context ctx = new InitialContext();
				dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/porssiDB");
			}
			catch (NamingException ne){
				ne.printStackTrace();
				dataSource=null;
			}
			catch (Exception e){
				e.printStackTrace();
				dataSource=null;
			}
		}
		return dataSource;
	}
	
	// avaaYhteys: avaa tietokantayhteyden tietokantaparametrien mukaan.
	public boolean avaaYhteys(){
        try{
        	conn = getDataSource().getConnection();
        }
        catch(SQLException e){
            conn=null;
            e.printStackTrace();
            return false;
        }
		catch (Exception e){
            e.printStackTrace();
			return false;
		}
        return true;
	}
	/*public boolean avaaYhteys(){
    try{
    	conn = getDataSource().getConnection();
    }
    catch(SQLException e){
        conn=null;
        e.printStackTrace();
        return false;
    }
	catch (Exception e){
        e.printStackTrace();
		return false;
	}
    return true;
}*/
	
	// suljeYhteys: sulkee tietokantayhteyden.
	public boolean suljeYhteys(){
		try{
			if (RS!=null){
				RS.close();
			}
			if (stmt!=null){
				stmt.close();
			}
			if (conn!=null){
				conn.close();
			}
		}
		catch(SQLException e){
			return false;
		}
		catch (Exception e){}
		return true;
	}
	
	// poistaKurssit: poistaa kurssit-taulun tietokannasta.
	public boolean poistaKurssit() {
		try {
			stmt = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS kurssit";
			stmt.executeUpdate(sql);
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {}
		return true;
	}
	
	// poistaKurssit: poistaa kurssit-taulusta ennen paivamaaraa olevat rivit.
	public boolean poistaKurssit(String paivamaara) {
		try {
			stmt = conn.createStatement();
			String sql = "DELETE FROM kurssit WHERE paivamaara < " + paivamaara;
			stmt.executeUpdate(sql);
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {}
		return true;
	}
		
	// createKurssit: luo kurssit-taulun tietokantaan.
	public boolean luoKurssit() {
		try {
			stmt = conn.createStatement();
			String sql = "CREATE TABLE kurssit (paivamaara DATE, PRIMARY KEY (paivamaara))";
			stmt.executeUpdate(sql);
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {}
		return true;
	}
	
	// lisaaKurssit: hakee kurssit Nasdaq OMX Nordicin siviulta ja lisää ne tietokantaan.
/*	public boolean lisaaKurssit() {
		
		String sisalto = "";
		List<String> paivamaarat = new ArrayList<>();
		List<String> kurssit = new ArrayList<>();
		String sql;
		
		Firmat firmat = new Firmat();
		firmat.haeFirmatTietokannasta();
		
		// Muodostetaan haun ymmärtämä päivämäärä-string.
		Calendar kalenteri = GregorianCalendar.getInstance();
		kalenteri.add(Calendar.YEAR, -2);
		String alkupaivamaara = Paivamaara.laskePaivamaara(kalenteri);
		String paivamaara = Paivamaara.laskePaivamaara();
					
		// Lisätään kurssit tietokantaan.
		try {
			
			stmt = conn.createStatement();
			
			// Käydään firmalista läpi ja haetaan jokaisen kurssit kahden viime vuoden ajalta.
			for(int i = 0; i < firmat.getSize(); i++) {
				String url = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?Subsystem=History&Action=GetDataSeries&Instrument="
						+ firmat.getFirmaID(i) + "&FromDate=" + alkupaivamaara +"&ToDate=" + paivamaara;
				URL kurssitXML = new URL(url);
				HttpURLConnection kurssitConn = (HttpURLConnection)kurssitXML.openConnection();
				kurssitConn.setRequestMethod("GET");
				kurssitConn.setRequestProperty("User-Agent", "Mozilla/4.0");
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
						kurssitConn.getInputStream()));
		        String inputLine = "";
		        sisalto = "";
		        paivamaarat.clear();
		        kurssit.clear();
				       
		        while ((inputLine = in.readLine()) != null) {
		        	sisalto += inputLine;
		        }	        
		        in.close();
				        				
				while(true) {
					if(sisalto.indexOf("dt=\"") != -1) {
		
						sisalto = sisalto.substring(sisalto.indexOf("dt=\"") + 4, sisalto.length());
						paivamaarat.add(sisalto.substring(0, 10));
								
						sisalto = sisalto.substring(sisalto.indexOf("cp=\"") + 4, sisalto.length());
						kurssit.add(sisalto.substring(0, sisalto.indexOf("\"")));
					}
					else {
						break;
					}
				}
						
				for(int j = 0; j < paivamaarat.size(); j++) {

					sql = "SELECT * FROM kurssit WHERE paivamaara=\"" + paivamaarat.get(j) + "\"";
					RS = stmt.executeQuery(sql);
							
					if(!RS.next()) {
						sql = "INSERT INTO kurssit (paivamaara) VALUES (\"" + paivamaarat.get(j) + "\")";
								stmt.executeUpdate(sql);
					}
					
					sql = "UPDATE kurssit SET " + firmat.getFirmaID(i) + "=\"" + kurssit.get(j) + "\" WHERE paivamaara=\"" + paivamaarat.get(j) + "\"";
					stmt.execute(sql);
				}
				sql = "UPDATE kurssit SET " + firmat.getFirmaID(i) + "=\"0\" WHERE " + firmat.getFirmaID(i) + " IS NULL";
				stmt.execute(sql);
				sql = "UPDATE kurssit SET " + firmat.getFirmaID(i) + "=\"0\" WHERE " + firmat.getFirmaID(i) + "=\"\"";
				stmt.execute(sql);
			}
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {}
		return true;
	}
*/
	// lisaaKurssit: hakee kurssit Nasdaq OMX Nordicin siviulta ja lisää ne tietokantaan.
	public boolean lisaaKurssit(String alkupaivamaara) {
		
		String sisalto = "";
		List<String> paivamaarat = new ArrayList<>();
		List<String> kurssit = new ArrayList<>();
		String sql;
		
		Firmat firmat = new Firmat();
		firmat.haeFirmatTietokannasta();
		
		// Muodostetaan haun ymmärtämä päivämäärä-string.
		String paivamaara = Paivamaara.laskePaivamaara();
					
		// Lisätään kurssit tietokantaan.
		try {
			
			stmt = conn.createStatement();
			
			// Käydään firmalista läpi ja haetaan jokaisen kurssit kahden viime vuoden ajalta.
			for(int i = 0; i < firmat.getSize(); i++) {
				String url = "http://www.nasdaqomxnordic.com/webproxy/DataFeedProxy.aspx?Subsystem=History&Action=GetDataSeries&Instrument="
						+ firmat.getFirmaID(i) + "&FromDate=" + alkupaivamaara +"&ToDate=" + paivamaara;
				URL kurssitXML = new URL(url);
				HttpURLConnection kurssitConn = (HttpURLConnection)kurssitXML.openConnection();
				kurssitConn.setRequestMethod("GET");
				kurssitConn.setRequestProperty("User-Agent", "Mozilla/4.0");
				BufferedReader in = new BufferedReader(
						new InputStreamReader(
						kurssitConn.getInputStream()));
		        String inputLine = "";
		        sisalto = "";
		        paivamaarat.clear();
		        kurssit.clear();
				       
		        while ((inputLine = in.readLine()) != null) {
		        	sisalto += inputLine;
		        }	        
		        in.close();
				        				
				while(true) {
					if(sisalto.indexOf("dt=\"") != -1) {
		
						sisalto = sisalto.substring(sisalto.indexOf("dt=\"") + 4, sisalto.length());
						paivamaarat.add(sisalto.substring(0, 10));
								
						sisalto = sisalto.substring(sisalto.indexOf("cp=\"") + 4, sisalto.length());
						kurssit.add(sisalto.substring(0, sisalto.indexOf("\"")));
					}
					else {
						break;
					}
				}
				
				sql = "SELECT * FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = 'porssi' AND "
						+ "TABLE_NAME = 'kurssit'  AND COLUMN_NAME = '" + firmat.getFirmaID(i) + "'";
				RS = stmt.executeQuery(sql);
				if(!RS.next()) {
					sql = "ALTER TABLE kurssit ADD " + firmat.getFirmaID(i) + " VARCHAR(10)";
					stmt.execute(sql);
				}
		
				for(int j = 0; j < paivamaarat.size(); j++) {

					sql = "SELECT * FROM kurssit WHERE paivamaara=\"" + paivamaarat.get(j) + "\"";
					RS = stmt.executeQuery(sql);
							
					if(!RS.next()) {
						sql = "INSERT INTO kurssit (paivamaara) VALUES (\"" + paivamaarat.get(j) + "\")";
								stmt.executeUpdate(sql);
					}
					
					sql = "UPDATE kurssit SET " + firmat.getFirmaID(i) + "=\"" + kurssit.get(j) + "\" WHERE paivamaara=\"" + paivamaarat.get(j) + "\"";
					stmt.execute(sql);
				}
				sql = "UPDATE kurssit SET " + firmat.getFirmaID(i) + "=\"0\" WHERE " + firmat.getFirmaID(i) + " IS NULL";
				stmt.execute(sql);
				sql = "UPDATE kurssit SET " + firmat.getFirmaID(i) + "=\"0\" WHERE " + firmat.getFirmaID(i) + "=\"\"";
				stmt.execute(sql);
			}
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {}
		return true;
	}

	// haeFirmat: haetaan firmojen nimet tietokannasta.
	public boolean haeFirmat(ArrayList<String> firmanNimet, ArrayList<String> firmaIDt) {
		
		String sql = "SELECT firmannimi, firmaid FROM firmat ORDER BY firmannimi ASC";

		try {
			stmt = conn.createStatement();
			RS = stmt.executeQuery(sql);
			
			while(RS.next()) {
				String firmanNimi=RS.getString("firmannimi");
				String firmaID=RS.getString("firmaid");
				firmanNimet.add(firmanNimi);
				firmaIDt.add(firmaID);
			}
			
		} catch(SQLException e) {
			return false;
		} catch(Exception e) {}
		
		return true;
	}
	
	// haeKurssit: haetaan tietyn firman kurssit paivamaarasta eteenpäin kahteen ArrayListiin
	public void haeKurssit(String firma, String paivamaara, ArrayList<String> kurssit, ArrayList<String> paivamaarat) {

		String sql = "SELECT paivamaara, " + firma + " FROM kurssit WHERE paivamaara >= '"
				+ paivamaara + "'";
		
		try {
			stmt = conn.createStatement();
			RS = stmt.executeQuery(sql);
			
			while(RS.next()) {
				
				paivamaarat.add(RS.getString("paivamaara"));
				kurssit.add(RS.getString(firma));
			}
		} catch(Exception e) {}
	}
	
	// haeKurssi: hakee tietyn firman tietyn päivämäärän kurssin.
	public String haeKurssi(String firma, String paivamaara) {
		String sql = "SELECT " + firma + " FROM kurssit WHERE paivamaara = '"
				+ paivamaara + "'";
		String kurssi = "tyhja";
		
		try {
			stmt = conn.createStatement();
			RS = stmt.executeQuery(sql);
			
			while(RS.next()) {
				kurssi = RS.getString(firma);
			}
			
		} catch(Exception e) {}
		
		return kurssi;
	}
	
	// haePaivamaarat: hakee kaikki tietokannasta löytyvät päivämäärät. Tarvitaan,
	// jotta kuvaajat voidaan piirtää järkevästi.
	public ArrayList<String> haePaivamaarat(String paivamaara) {
		ArrayList<String> paivamaarat = new ArrayList<String>();
		String sql = "SELECT paivamaara FROM kurssit WHERE paivamaara >= '" + paivamaara + "'";
		
		try {
			stmt = conn.createStatement();
			RS = stmt.executeQuery(sql);
			
			while(RS.next()) {
				paivamaarat.add(RS.getString("paivamaara"));
			}
		} catch(Exception e) {}
		
		return paivamaarat;
	}
}
