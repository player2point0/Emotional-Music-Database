package views;

import model.newsEvent;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Loginview extends DynamicWebPage {

	public Loginview(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);
		
	}

	public boolean process(WebRequest toProcess) {
		if(toProcess.path.equalsIgnoreCase("Loginview")) { //main.java will call this to check if this is the right page depending on what the user has requested
			
				newsEvent news = new newsEvent();
				news.title = "Login";
				news.description = "Login:";
				news.filePathToImage = "https://cdn.vox-cdn.com/thumbor/6GcQn-hFlof-V60i88eCgSiY66o=/0x0:819x548/1200x800/filters:focal(345x209:475x339)/cdn.vox-cdn.com/uploads/chorus_image/image/60773701/Screen_Shot_2018_08_08_at_6.54.35_PM.0.png";
			String exampleUserName = "Login or Sign up";
			String stringToSendToWebBrowser = "<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"    <head>\r\n" + 
					"        <title>\r\n" + 
					"        Login/Sign Up\r\n" + 
					"        </title>\r\n" +
					"        <body>\r\n" + 
					"            <h1>" +exampleUserName+"</h1>\r\n" + 
					"            <p>Login to your account below, or if you don't have one yet then sign up now!" + news.title+"</p>\r\n" + 
					"            <form>\r\n" + 
					"                <p>" + news.description + "</p>\r\n" + 
					"                <label for=\"username\">Username:</label>\r\n" + 
					"                <input type = \"text\" name=\"username\" id=\"username\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <label for = \"user-pw\">Password</label>\r\n" + 
					"                <input type=\"password\" name=\"user-pw\" id=\"user-pw\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <input type = \"submit\" value=\"Login\">\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"                <p>Sign up:</p>\r\n" + 
					"                <label for = \"firstname\">First Name:</label>\r\n" + 
					"                <input type=\"text\" name=\"firstname\" id=\"firstname\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <label for = \"surtname\">Surame:</label>\r\n" + 
					"                <input type=\"text\" name=\"surname\" id=\"surname\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <label for=\"username\">Username:</label>\r\n" + 
					"                <input type = \"text\" name=\"username\" id=\"username\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                 <label for = \"user-pw\">Create a password</label>\r\n" + 
					"                <input type=\"password\" name=\"user-pw\" id=\"user-pw\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                 <label for = \"user-pw\">Confirm your password</label>\r\n" + 
					"                <input type=\"password\" name=\"user-pw\" id=\"user-pw\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <label for = \"age\"> Age: </label>\r\n" + 
					"                <input id=\"age\" name=\"age\" type=\"number\" step=\"1\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <p>Select your gender: </p>\r\n" + 
					"                <label for = \"female\">Female </label>\r\n" + 
					"                <input id = \"female\" name = \"female\" type = \"checkbox\" value = \"female\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                 <label for = \"male\">Male </label>\r\n" + 
					"                <input id = \"male\" name = \"male\" type = \"checkbox\" value = \"male\">\r\n" + 
					"                <br>\r\n" + 
					"                <br>\r\n" + 
					"                <input type=\"submit\" value=\"Create Account\">\r\n" + 
					"            </form>\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"        </body>\r\n" + 
					"    </head>\r\n" + 
					"</html>";
			
			toProcess.r = new WebResponse(WebResponse.HTTP_OK,WebResponse.MIME_HTML, stringToSendToWebBrowser);
			return true;
		}
		return false;
	}
	
	

	
}
