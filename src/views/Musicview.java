package views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.h2.mvstore.MVMap;

import model.SongHistoryModel;
import model.SongInfo;
import model.customer;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Musicview extends DynamicWebPage {


	public Musicview(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);

	}
	
	public boolean process(WebRequest toProcess)
		{
			if(toProcess.path.equalsIgnoreCase("music"))
			{
				String stringToSendToWebBrowser = "";

				MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");
				String youtubeVideoId = toProcess.params.get("id");
				//get the corresponding song from the database

				if(SongRatings.containsKey(youtubeVideoId))
				{
					SongInfo currentSong = SongRatings.get(youtubeVideoId);
					String output = storeUserHistory(youtubeVideoId, toProcess);
					//System.out.println(output);

					stringToSendToWebBrowser = "<!DOCTYPE HTML>\r\n" +
							"<html>\r\n" +
							"<head>\r\n" +
							"	<title>Music Page</title>\r\n" +
							DiscoverView.getNavBarCss() +
							"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/MusicStyle.css\">\r\n" +
							"	 <script src=\"https://www.youtube.com/iframe_api\"></script>"+
							"</head>\r\n" +
							"<body>\r\n" +
							DiscoverView.getNavBar() + 
							"	<div class=\"SongInfo\">\r\n" +
							"	<p id=\"oops\">Oops! Your song didn't load, please refresh the page.</p>\r\n" +	
							"		<div id=\"player\"></div>" +
							"		<p>Song Playing: " + currentSong.songName + "<br></p>\r\n" +
							"		<p>" + currentSong.viewCount + views(currentSong) + "<br></p>\r\n" +
							"	</div>\r\n" +
							"	<div class=\"moodlist\">\r\n" +
							"		<form action=\"SongInfo\" method=\"GET\">\r\n" +
							"				<h3 id=\"Happy\">Happy: " + getData("happy", currentSong) + "</h3>\r\n" +
							"				<label for=\"Happy\"><i class=\"em-svg em-grin\"></i></label>\r\n" +
							"				<input id=\"Happy\" name=\"happy\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\r\n" +
							"				<h3 id=\"Angry\">Angry: " + getData("angry", currentSong) + "</h3>\r\n" +
							"				<label for=\"Angry\"><i class=\"em-svg em-angry\"></i></label>\r\n" +
							"				<input id=\"Angry\" name=\"angry\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\r\n" +
							"				<h3 id=\"Excited\">Excited: " + getData("excited",currentSong) + "</h3>\r\n" +
							"				<label for=\"Excited\"<i class=\"em-svg em-heart_eyes\"></i></label>\r\n" +
							"				<input id=\"Excited\" name=\"excited\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\r\n" +
							"				<h3 id=\"Sad\">Sad: " + getData("sad", currentSong) + "</h3>\r\n" +
							"				<label for=\"Sad\"><i class=\"em-svg em-cry\"></i></label>\r\n" +
							"				<input id=\"Sad\" name=\"sad\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\r\n" +
							"				<h3 id=\"Relaxed\">Relaxed: " + getData("relaxed", currentSong) + "</h3>\r\n" +
							"				<label for=\"Relaxed\"><i class=\"em-svg em-sleeping\"></i></label>\r\n" +
							"				<input id=\"Relaxed\" name=\"relaxed\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\r\n" +
							"				<input name=\"id\" type=\"hidden\" value="+youtubeVideoId+">"+
							"				<br><button type=\"submit\">Submit Rating</button>\r\n" +
							"		</form>\r\n" +
							"	</div>\r\n" + 
							"			<script src=\"/js/autoPlay.js\"></script>" +
							"</body>\r\n" +
							"</html>";
				}
				else stringToSendToWebBrowser = "something broken";

				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			} else if(toProcess.path.equalsIgnoreCase("SongInfo()")) {
				return true;
			
			} else if(toProcess.path.equalsIgnoreCase("SongInfo"))
			{
				MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");
				String youtubeVideoId = toProcess.params.get("id");
				//get the corresponding song from the database
				SongInfo currentSong = SongRatings.get(youtubeVideoId);

				//increment viewcount
				currentSong.viewCount = currentSong.viewCount + 1;
				//calculate new average
				double newHappy = currentSong.calcNewAvgVal(currentSong.happy, Double.parseDouble(toProcess.params.get("happy")));
				double newAngry = currentSong.calcNewAvgVal(currentSong.angry, Double.parseDouble(toProcess.params.get("angry")));
				double newSad = currentSong.calcNewAvgVal(currentSong.sad, Double.parseDouble(toProcess.params.get("sad")));
				double newExcited = currentSong.calcNewAvgVal(currentSong.excited, Double.parseDouble(toProcess.params.get("excited")));
				double newRelaxed = currentSong.calcNewAvgVal(currentSong.relaxed, Double.parseDouble(toProcess.params.get("relaxed")));

				System.out.println("happy val : "+newHappy);

				currentSong.happy = newHappy;
				currentSong.angry = newAngry;
				currentSong.excited = newExcited;
				currentSong.sad = newSad;
				currentSong.relaxed = newRelaxed;

				SongRatings.replace(currentSong.youtubeVideoId, currentSong);

				db.commit();

				String stringToSendToWebBrowser = "<html>\r\n" + 
						"<head>\r\n" +
						"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/MusicStyle.css\">\r\n" +
						"</head>" +
						"<body>" +
						"	<div id=\"rating\">" +
						"		<p>Rating Added!</p><a href=\"musicview\">Discover More Music!</a></body></html>" +
						"	</div>" +
						"</body>" +
						"</html>"; 
				toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML,
				stringToSendToWebBrowser);
			}
			return false;
		} 
	
	public String getData(String emotion, SongInfo currentSong)
	{
			if (emotion.equals("happy")) {
				return String.format("%.2f", currentSong.happy)+"%";
			} else if (emotion.equals("angry")) {
				return String.format("%.2f", currentSong.angry)+"%";
			} else if (emotion.equals("excited")) {
				return String.format("%.2f", currentSong.excited)+"%";
			} else if (emotion.equals("sad")) {
				return String.format("%.2f", currentSong.sad)+"%";
			} else if (emotion.equals("relaxed")) {
				return String.format("%.2f", currentSong.relaxed)+"%";
			} 
		return "No Rating";
	}
	
	public String views(SongInfo currentSong) {
		if (currentSong.viewCount == 1) {
			return " Rating";
		} else {
			return " Ratings";
		}
	}

	private String storeUserHistory(String youtubeId,WebRequest toProcess )
	{
		String outputString = "";
		
		LocalDateTime currentDate = LocalDateTime.now();

		//get current user
		String email = toProcess.cookies.get("email");
		String password =  toProcess.cookies.get("password");
		
		customer c = getCustomer(email,password);
		if(c == null) {
			return "";
		}
		
		String songHistoryId = c.songHistoryId;

		MVMap<String, SongHistoryModel> SongHistory = db.s.openMap("SongHistory");
		SongHistoryModel result = SongHistory.get(songHistoryId);

		result.addSong(youtubeId,currentDate);
		//save to current user history
		
		SongHistory.replace(songHistoryId, result);
		db.commit();
		
		outputString += youtubeId;
		outputString += " "+currentDate;
		
		return outputString;
	}
	
	public customer getCustomer(String email, String password)
	{
		MVMap<String, customer> custo = db.s.openMap("customer");
		List<String> keys = custo.keyList();

		for(int i = 0;i<keys.size();i++)
		{
			customer c = custo.get(keys.get(i));


			if(c.email.equals(email) && c.password.equals(password))
			{
				//valid login
				return c;
			}
		}

		return null;
	}
}
