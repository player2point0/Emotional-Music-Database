package views;

import java.util.Iterator;
import java.util.Map;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class TestForm extends DynamicWebPage
{
	public TestForm(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("testform"))
		{
			//Lab 2 Task 4
			//This page goes through the parameters that were sent through a form
			//<br> will create a new line in html
			String stringToSendToWebBrowser = "The form input names and values: <br>";
			//Params is a hashmap which contains a set of Map.Entries
			//It maps String keys to String values
			//When a form is submitted the values of the input elements are placed in this map
			//so that webserver code like this can read the values the user has submitted
			//you can use this to enable users to store data in your web application
			for (Map.Entry<String, String> entry : toProcess.params.entrySet())
			{
				stringToSendToWebBrowser += entry.getKey();
				stringToSendToWebBrowser += " : ";
				stringToSendToWebBrowser += entry.getValue();
				stringToSendToWebBrowser += "<br>";
			}

			//you can check if certain keys will be present with:
			//if(toProcess.params.containsKey("key_to_check_for"))
			//if you know certain keys will be present 
			//String value = toProcess.params.get("key_you_know_is_present");

			//For each line of html you add need to add this escape character to indicate that the line is complete
			stringToSendToWebBrowser += "\n";

			//The next line is a comment with the message for lab 2 task 4. Just copy the number
			//MESSAGELAB2TASK4

			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );

			return true;
		}
		return false;
	}

}
