package views;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.h2.mvstore.MVMap;

import model.SongHistoryModel;
import model.customer;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Signupview extends DynamicWebPage{

	public static boolean validLoginCookies()
	{
		//String username = toProcess.cookies.get("username");
		//String password = toProcess.cookies.get("password");
		return false;
	}

	public Signupview(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);
		
	}
	public boolean process(WebRequest toProcess)
	{
        if(toProcess.path.equalsIgnoreCase("Signup"))
        {
        	//Lab 1 Task 4
        	//Change this string so that it contains HTML from a page you created in Pingendo 
        	//Lab 1 Task 4
        	//Change this string so that it contains HTML from a page you created in Pingendo 
        	String stringToSendToWebBrowser = "<!DOCTYPE html>\n" + 
        			"<html>\n" + 
        			"\n" + 
        			"<head>\n" + 
        			"  <meta charset=\"utf-8\">\n" + 
        			"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" + 
        			"  <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\" type=\"text/css\">\n" + 
        			"  <link rel=\"stylesheet\" href=\"../css/theme.css\" type=\"text/css\">\n" + 
        			"</head>\n" + 
        			"\n" + 
        			"<body>\n" + 
        			"  <nav class=\"navbar navbar-expand-md navbar-dark bg-dark\">\n" + 
        			"    <div class=\"container\"> <a class=\"navbar-brand\" href=\"#\">\n" + 
        			"        <i class=\"fa d-inline fa-lg fa-stop-circle\"></i>\n" + 
        			"        <b> BRAND</b>\n" + 
        			"      </a> <button class=\"navbar-toggler navbar-toggler-right border-0\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbar10\">\n" + 
        			"        <span class=\"navbar-toggler-icon\"></span>\n" + 
        			"      </button>\n" + 
        			"      <div class=\"collapse navbar-collapse\" id=\"navbar10\">\n" + 
        			"        <ul class=\"navbar-nav ml-auto\">\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Search</a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Merch</a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Login/Sign Up</a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Discover</a> </li>\n" + 
        			"        </ul>\n" + 
        			"      </div>\n" + 
        			"    </div>\n" + 
        			//"    <h3> " + title + "</h3> " + //added
        			"  </nav>\n" + 
        			"  <div class=\"py-5 text-center\">\n" + 
        			"    <div class=\"container\">\n" + 
        			"      <div class=\"row\">\n" + 
        			"        <div class=\"mx-auto col-lg-6 col-10\">\n" + 
        			"          <h1>Sign Up</h1>\n" + 
        			"          <form action=\"customer\" action=\"GET\" class=\"text-left\">\n" + 
        			"            <div class=\"form-group\"> <label for=\"form16\">Your Name</label> <input type=\"text\" name=\"title\" class=\"form-control\" id=\"form16\" placeholder=\"John Smith\"> </div>\n" + 
        			"            <div class=\"form-group\"> <label for=\"form17\">Your email</label> <input type=\"text\" name=\"email\" class=\"form-control\" id=\"form17\" placeholder=\"example@outlook.com\"> </div>\n" + 
        			"            <div class=\"form-group\"> <label for=\"form18\">Verify your email</label> <input type=\"verifyemail=\"varifyemail\" class=\"form-control\" placeholder=\"example@outlook.com\" id=\"form18\"> </div>\n" + 
        			"            <div class=\"form-row\">\n" + 
        			"              <div class=\"form-group col-md-6\"> <label for=\"form19\">Create a Password</label> <input type=\"password\" name=\"password\" class=\"form-control\" id=\"form19\"> </div>\n" + 
        			"              <div class=\"form-group col-md-6\"> <label for=\"form20\">Confirm Password</label> <input type=\"password\" name=\"verifypassword\" class=\"form-control\" id=\"form20\"> </div>\n" + 
        			"            </div>\n" + 
        			"            <div class=\"form-group\">\n" + 
        			"              <div class=\"form-check\"> <input class=\"form-check-input\" type=\"checkbox\" id=\"form21\" value=\"on\"> <label class=\"form-check-label\" for=\"form21\"> I Agree with <a href=\"#\">Term and Conditions</a> of the service </label> </div>\n" + 
        			"            </div> <button type=\"submit\" class=\"btn btn-primary\">Sign in</button>\n" + 
        			"          </form><a href=\"#\"> Already have an account?</a>\n" + 
        			"        </div>\n" + 
        			"      </div>\n" + 
        			"    </div>\n" + 
        			"  </div>\n" + 
        			"  <div class=\"py-5 text-center bg-dark text-white\" >\n" + 
        			"    <div class=\"container\">\n" + 
        			"      <div class=\"row\">\n" + 
        			"        <div class=\"p-5 col-lg-6 col-10 mx-auto border\">\n" + 
        			"          <h1 class=\"mb-4\">Login</h1>\n" + 
        			"          <form action=\"login\" method=\"GET\">\n" + 
        			"            <div class=\"form-group\"> <input type=\"email\" name=\"email\" class=\"form-control\" placeholder=\"Email\" id=\"form14\"> </div>\n" + 
        			"            <div class=\"form-group\"> <input type=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" id=\"form15\"> <small class=\"form-text text-muted text-right\">\n" + 
        			"                <a href=\"#\"> Already have an account?</a>\n" + 
        			"              </small> </div> <button type=\"submit\" class=\"btn btn-primary\">Submit</button>\n" + 
        			"          </form>\n" + 
        			//"        </div>\n <h2>Customer Details" + cust.name + cust.email + cust.password +
        			"      </div>\n" + 
        			"    </div>\n" + 
        			"  </div>\n" + 
        			"  <nav class=\"navbar navbar-expand-md navbar-dark bg-dark\">\n" + 
        			"    <div class=\"container\"> <button class=\"navbar-toggler navbar-toggler-right border-0\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbar13\">\n" + 
        			"        <span class=\"navbar-toggler-icon\"></span>\n" + 
        			"      </button>\n" + 
        			"      <div class=\"collapse navbar-collapse\" id=\"navbar13\"> <a class=\"navbar-brand d-none d-md-block\" href=\"#\">\n" + 
        			"          <i class=\"fa d-inline fa-lg fa-stop-circle-o\"></i>\n" + 
        			"          <b> BRAND</b>\n" + 
        			"        </a>\n" + 
        			"        <ul class=\"navbar-nav\">\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">\n" + 
        			"              <i class=\"fa fa-twitter fa-fw\"></i>\n" + 
        			"            </a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">\n" + 
        			"              <i class=\"fa fa-facebook fa-fw\"></i>\n" + 
        			"            </a> </li>\n" + 
        			"          <li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">\n" + 
        			"            </a> </li>\n" + 
        			"        </ul>\n" + 
        			"      </div>\n" + 
        			"    </div>\n" + 
        			"  </nav>\n" + 
        			"  <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n" + 
        			"  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js\" integrity=\"sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49\" crossorigin=\"anonymous\"></script>\n" + 
        			"  <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js\" integrity=\"sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k\" crossorigin=\"anonymous\"></script>\n" + 
        			"</body>\n" + 
        			"\n" + 
        			"</html>";
        	
        	toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
        	
        
        	return true;
        }
        //saving to the database
        else if(toProcess.path.equalsIgnoreCase("customer")) { //lab6 added
        	customer cust = new customer(); //added code lab6
        	//step 2
        	cust.name = toProcess.params.get("title");
        	cust.email = toProcess.params.get("email");
        	cust.password = toProcess.params.get("password");
        	String songHistoryId = "Song History Id: " + System.currentTimeMillis();
        	cust.songHistoryId = songHistoryId;
        	cust.friendsList = new ArrayList<String>();

    		MVMap<String, SongHistoryModel> SongHistory = db.s.openMap("SongHistory");
    		SongHistoryModel tempSongHistoryModel = new SongHistoryModel();
    		tempSongHistoryModel.timeStamps = new ArrayList<LocalDateTime>();
    		tempSongHistoryModel.videoIds = new ArrayList<String>();
        	
    		SongHistory.put(songHistoryId, tempSongHistoryModel);
    		
        	//add the customer to the database
        	MVMap<String, customer> custo = db.s.openMap("customer");

        	if(custo.containsKey(cust.email))
			{
				toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML, "\"<html><body><p>Account already exists</p> <a href=\"discover\">Home</a> </body></html>\"");
				return true;
			}

        	custo.put(cust.email, cust);
        	
        	db.commit();
        	
        	//an html page to redirect the user to once they create an account
        	String stringToSendToWebBrowser = "<html><body><p>New Account Added</p> <a href=\"discover\">Home</a> </body></html>";
           
        	toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser);

        	return true;
        }
        else if(toProcess.path.equalsIgnoreCase("login")) { //lab6 added
			//added code lab6
			//step 2

			String email = toProcess.params.get("email");
			String password = toProcess.params.get("password");

			//check if valid login details
			boolean validLogin = validLoginDetails(email, password);
			//add the customer to the database

			if(validLogin)
			{
				String stringToSendToWebBrowser = "<html><body onload='saveLoginCookie()' 1><p>Logged in</p> <a href=\"discover\">Home</a>";

				stringToSendToWebBrowser += "  <script>\n";
				stringToSendToWebBrowser += "      function setCookie(cname, cvalue, exdays)\n";
				stringToSendToWebBrowser += "      {\n";
				stringToSendToWebBrowser += "          var d = new Date();\n";
				stringToSendToWebBrowser += "          d.setTime(d.getTime() + (exdays*24*60*60*1000));\n";
				stringToSendToWebBrowser += "          var expires = 'expires='+d.toUTCString();\n";
				stringToSendToWebBrowser += "          document.cookie = cname + '=' + cvalue + ';' + expires + ';path=/';\n";
				stringToSendToWebBrowser += "      }\n";

				stringToSendToWebBrowser += "      function saveLoginCookie()\n";
				stringToSendToWebBrowser += "      {\n";
				stringToSendToWebBrowser += "          setCookie('email','"+email+"',365);\n";
				stringToSendToWebBrowser += "          setCookie('password','"+password+"',365);\n";
				stringToSendToWebBrowser += "      }\n";
				stringToSendToWebBrowser += "  </script>\n";

				stringToSendToWebBrowser += "</body></html>";

				toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser);
				return true;
			}

			else
			{
				//an html page to redirect the user to once they create an account
				String stringToSendToWebBrowser = "<html><body><p>New Account Added</p> <a href=â€�Signuplogin.htmlâ€�>Home</a> </body></html>â€�";

				toProcess.r = new WebResponse(WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser);

				return true;
			}

        }
		return false;
	}

	public boolean validLoginDetails(String email, String password)
	{
		MVMap<String, customer> custo = db.s.openMap("customer");
		List<String> keys = custo.keyList();

		for(int i = 0;i<keys.size();i++)
		{
			customer c = custo.get(keys.get(i));

			if(c.email.equals(email) && c.password.equals(password))
			{
				//valid login
				return true;
			}
		}

		return false;
	}

}



