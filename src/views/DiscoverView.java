package views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.h2.mvstore.MVMap;

import model.SongHistoryModel;
import model.SongInfo;
import model.customer;
//import model.DiscoverModel;
import org.json.JSONObject;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class DiscoverView extends DynamicWebPage {

	public static String getNavBar() {

		return "\n" +
				"\t\t<nav id=\"search-bar\">\n" +
				"\t\t\t<a href=\"Discover\" id=\"search-bar-home\"><i class=\"fas fa-home\"></i></a>\n" +
				"\t\t\t<form action=\"search\" method=\"GET\">\n" +
				"\t\t\t\t<input type=\"text\" name=\"searchQuery\">\n" +
				"\t\t\t\t<button type=\"submit\"><i class=\"fas fa-search\"></i></button>\n" +
				"\t\t\t</form>\n" +
				"\t\t\t<a href=\"javascript:void(0);\" id=\"hamburger\" onclick=\"myFunction()\"><i class=\"fa fa-bars\"></i></a>\n" +
				"\t\t\t<div id=\"mobile-links\">\n" +
				"\t\t\t\t<a id=\"login\" href=\"Signup\">Login/SignUp</a>\n" +
				"\t\t\t\t<a id=\"merch\" href=\"Merch\">Merch</a>\n" +
				"\t\t\t\t<a id=\"stats\" href=\"Stats\">Stats</a>\n" +
				"\t\t\t</div>\n" +
				"\t\t\t<script>\n" +
				"\t\t\t\tfunction myFunction() {\n" +
				"\t\t\t\t\tvar x = document.getElementById(\"mobile-links\");\n" +
				"\t\t\t\t\tif (x.style.display === \"grid\") {\n" +
				"\t\t\t\t\t\tx.style.display = \"none\";\n" +
				"\t\t\t\t\t} else {\n" +
				"\t\t\t\t\t\tx.style.display = \"grid\";\n" +
				"\t\t\t\t\t}\n" +
				"\t\t\t\t}\n" +
				"\t\t\t\t</script>\n" +
				"\t\t</nav>";

	}

	public static String getNavBarCss() {
		return 	"    <!-- Bootstrap -->\n" +
				"    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\t\t\n" +
				"	 <link href=\"css/nav-bar.css\" rel=\"stylesheet\" type=\"text/css\">"+
				"    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n" +
				"    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\n" +
				"    <!--[if lt IE 9]>\n" +
				"      <script src=\"https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js\"></script>\n" +
				"      <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\n" +
				"    <![endif]-->\n"+
				"    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n" +
				"    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js\"></script>\n" +
				"    <!-- Include all compiled plugins (below), or include individual files as needed -->\n" +
				"    <script src=\"js/bootstrap.min.js\"></script>\n"+
				"	<script defer src=\"https:/use.fontawesome.com/releases/v5.7.2/js/all.js\" integrity=\"sha384-0pzryjIRos8mFBWMzSSZApWtPl/5++eIfzYmTgBBmXYdhvxPc+XcFEk+zJwDgWbP\" crossorigin=\"anonymous\"></script>"	+
				"    <link href=\"https://afeld.github.io/emoji-css/emoji.css\" rel=\"stylesheet\">\n";
	}

	public DiscoverView(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);
		// TODO Auto-generated constructor stub
	}

	
	public boolean process(WebRequest toProcess)
	{
	if(toProcess.path.equalsIgnoreCase("discover"))
	{
		//DiscoverModel d1 = new DiscoverModel();
		//d1.emotion ="emotions";
	String stringToSendToWebBrowser = "<html><head>\n" +
			"    <meta charset=\"utf-8\">\n" +
			DiscoverView.getNavBarCss()+
			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
			"    <link href=\"css/Discover.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
			"	<script src=\"/js/addFriend.js\"></script>" +
			"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
			"  </head><body>\n" +
			DiscoverView.getNavBar()+
			"    <div id=\"Sliders\">\n" +
			"        <form action=\"search\" method=\"GET\"> \t\n" +
			"\t\t<label for=\"Happy\"><i class=\"em-svg em-grin\"></i></label>\n" +
			"\t\t<input id=\"Happy\" class=\"slider\" name=\"happy\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\n" +
			"        <label for=\"Angry\"><i class=\"em-svg em-angry\"></i></label>\n" +
			"        <input id=\"Angry\" class=\"slider\" name=\"angry\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\t\n" +
			"        <label for=\"Excited\"><i class=\"em-svg em-heart_eyes\"></i></label>\n" +
			"        <input id=\"Excited\" class=\"slider\" name=\"excited\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\n" +
			"        <label for=\"Sad\"><i class=\"em-svg em-cry\"></i></label>\n" +
			"        <input id=\"Sad\" class=\"slider\" name=\"sad\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\t\n" +
			"        <label for=\"Relaxed\"><i class=\"em-svg em-sleeping\"></i></label>\n" +
			"        <input id=\"Relaxed\" class=\"slider\" name=\"relaxed\" type=\"range\" min=\"0\" max=\"100\" step=\"1\">\n" +
			"        <br><input type=\"submit\" value=\"Submit\">\n" +
			"        </form>\n" +
			"    </div>\n" +
			"    <div id=\"FriendsList\">\n" +
			"        <div id=\"Friend\">\n" +
						loadFriendHistory(toProcess)+
			"        </div>\n" +
			"          <button id=\"AddFriendBtn\" onclick=\"addNewFriend()\">Add Friend</button>\n" +
			"    </div>\n" +
			"\n" +
			"</body>\n" +
			"</html>";
	toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
	return true;
	}
	else if(toProcess.path.equalsIgnoreCase("AddFriend")) {
		//get current user
		String email = toProcess.cookies.get("email");
		String password =  toProcess.cookies.get("password");
		
		customer c = getCustomer(email,password);
		if(c == null) {
			System.out.println("not logged in");
		}
		//get friend past in as a param
		String friendName = toProcess.params.get("FriendName");
		MVMap<String, customer> custo = db.s.openMap("customer");
		List<String> keys = custo.keyList();
	

		JSONObject data = new JSONObject();
		data.put("added", false);

		for(int i = 0;i<keys.size();i++)
		{
			customer tempFriend = custo.get(keys.get(i));
		//add friend to current user's friendList
			if(tempFriend.name.equalsIgnoreCase(friendName)) {
				c.addFriend(tempFriend.email);
				data.put("added", true);
				System.out.println("added");

			}
		} 
	
		toProcess.r = new WebResponse( WebResponse.HTTP_OK,
				WebResponse.MIME_HTML, data.toString() );
		return true;
	}
	return false;
	}
	

	public customer getCustomer(String email, String password) {
		MVMap<String, customer> custo = db.s.openMap("customer");
		List<String> keys = custo.keyList();

		for (int i = 0; i < keys.size(); i++) {
			customer c = custo.get(keys.get(i));

			if(c.email.equals(email) && c.password.equals(password))
			{
				//valid login
				return c;
			}
		}

		return null;
	}
			 

	public String loadFriendHistory(WebRequest toProcess) {
		//could add async updating
		//get current user
		String email = toProcess.cookies.get("email");
		String password = toProcess.cookies.get("password");
		
		customer c = getCustomer(email,password);
		if(c == null) {
			return "Not logged in";
		}
		
		String output = "<h1>"+c.name+"'s Friends"+"</h1>";
		output += "\n ----------------------------------";
		ArrayList<String> friendIds = c.friendsList;
		
		MVMap<String, customer> custo = db.s.openMap("customer");
		for(int i=0; i<friendIds.size(); i++)
		{
			String tempId = friendIds.get(i);
			customer custTemp = custo.get(tempId);
			output += "<div class=\"friendHistory\">\r\n" + 
					"            <div>\r\n" + 
					"               <h2>" + custTemp.name + "</h2>\r\n" + 
					"              <h3>" + readUserLatestSong(custTemp) + "</h3>\r\n" + 
					"            </div>\r\n" + 
					"            <h4>" + getTime(custTemp) + "</h4>\r\n" + 
					"          </div>  ";
		}

			return output;
	}
	
	
	public String readUserLatestSong(customer custo) {
		String songHistoryId = custo.songHistoryId;
		MVMap<String, SongHistoryModel> SongHistory = db.s.openMap("SongHistory");
		SongHistoryModel result = SongHistory.get(songHistoryId);
		String songId = result.getLatestSong();

		if(songId.isEmpty()) return "";

		MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");

		if(SongRatings.containsKey(songId))
		{
			SongInfo song = SongRatings.get(songId);
			return song.songName;
		}

		else return "Song not found";
	}
	
	
	public String getTime(customer custo) {
		String songHistoryId = custo.songHistoryId;
		MVMap<String, SongHistoryModel> SongHistory = db.s.openMap("SongHistory");
		SongHistoryModel result = SongHistory.get(songHistoryId);
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime time = result.getLatestSongTime();
		if(time == null)
		{
			return "";
		}
		int daysBetween = (int) ChronoUnit.DAYS.between(time, currentTime);
		if(daysBetween == 0)
		{
			int hoursBetween = (int) ChronoUnit.HOURS.between(time, currentTime);
			
			if(hoursBetween == 0)
			{
				int minsBetween = (int) ChronoUnit.MINUTES.between(time, currentTime);
				return Integer.toString(minsBetween) + " mins";
			}
			return Integer.toString(hoursBetween) + " hours";
		}
		
		
		return Integer.toString(daysBetween) + " days";
	}

}
