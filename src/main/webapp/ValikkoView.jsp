<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
	<link href='http://fonts.googleapis.com/css?family=Dancing+Script:700' rel='stylesheet' type='text/css'>
	
	<link rel="stylesheet" type="text/css" href="tyylit.css">
	
	<title>P&ouml;rssi: Valikko</title>

	<style>
		
		#vasen {
			float: left;
			width: 410px;
			background-color: beige;
			padding: 5px;
			font-family: arial;
			text-align: left;
			border-right: solid 2px #cc0;
			font-size: 12px;			
		}

		#formcontainer {
			padding: 0px;
			overflow: hidden;
			width: 720px;
			height: 100%;
			background-color: white;
			margin-bottom: 5px;
		}
		
		#inneroikea1 {
			float: right;
			width: 260px;
			background-color: beige;
			padding: 5px;
			font-family: arial;
			text-align: left;
			border-left: solid 2px #cc0;
			margin-bottom: 5px;
		}
		
		#inneroikea2 {
			float: right;
			width: 260px;
			background-color: beige;
			padding: 5px;
			font-family: arial;
			text-align: left;
			border-left: solid 2px #cc0;
		}
		
		#oikea {
			float: right;
			width: 260px;
			background-color: white;
			padding: 0px;
			font-family: arial;
			text-align: left;
		}
		
	</style>
		
</head>


<body>
		<div id="container">
			<div id="ylaosa">
				<%@ include file="ylaosa.html" %>
			</div>
			
			<h2>PÖRSSI</h2>
			
			<div id="formcontainer">
			<form id="lomake" action="kurssit" method="post">
				<div id="vasen">
					${firmalista}
				<input type="submit" value="Hae">
				</div>
		
				<div id="oikea">
					<div id="inneroikea1">
						<input type="radio" name="aika" value="3kk">3kk<br />
						<input type="radio" name="aika" value="6kk">6kk<br />
						<input type="radio" name="aika" value="1v">1 vuosi<br />
						<input type="radio" name="aika" value="2v" checked="checked">2 vuotta<br />
					</div>
					<div id="inneroikea2">
						<input type="radio" name="tyyppi" value="kurssit" checked="checked">Kurssit<br />
						<input type="radio" name="tyyppi" value="indeksit">Indeksit<br />
						<input type="radio" name="tyyppi" value="indeksitsamaan">Indeksit (samaan kuvaajaan)<br />
					</div>
				</div>
			</form>
			</div>
			
			<div id="footer">
				<%@ include file="footer.html" %>
			</div>
		</div>
		
</body>
</html>
