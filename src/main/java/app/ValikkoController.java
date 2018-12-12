package app;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Firmat;

@WebServlet("/valikko")

public class ValikkoController extends HttpServlet {
	
	private static final long serialVersionUID = 7126040589243921021L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException {
		
		// Asetetaan ContentType.
		response.setContentType("text/html");
		
		RequestDispatcher rd = request.getRequestDispatcher("ValikkoView.jsp");

		// Luodaan firmat-luokka, jonka sisältö haetaan tietokannasta.
		Firmat firmat = new Firmat();
		firmat.haeFirmatTietokannasta();
		
		// Tehdään firmoista valikko-sivulle sopiva html-muotoinen string.
		String htmlString = firmat.tulostaFirmat();
		
		// Toimitetaan request eteenpäin.
		request.setAttribute("firmalista", htmlString);
		rd.forward(request, response);
	}
}
