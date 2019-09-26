import java.util.ArrayList;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import views.*;
import web.WebInterface;
import web.WebRequest;
import web.WebResponse;



//This program is a webserver designed for teaching purposes
	//the focus is to reduce complexity while demonstrating the key components of a web server suitable for web app development, 
	//hopefully this code will enable you to have a platform that you are comfortable reading and modifying
	//it should also be powerful enough that you can create useful web applications
	//Once you are comfortable with this approach you may want to learn more about how web app development is done at cutting edge companies
	//you will probably want to learn how to use some well supported web frameworks
	//for example, many startup web companies use Django (a Python based web framework) or Nodejs (A javascript based system that has powerful web development libraries)
	//Check out this discussion for some expert opinions: "https://www.quora.com/Which-framework-should-I-learn-Django-or-Node-js-Why"
	//Your final year project is a good opportunity to develop some of these skills
public class Main 
{
	//This is a main function, it is the first function that is called when a java program starts
		//The array of strings named commandLineParameters are extra information that can be
		//passed to a program when it is run using the command line
		//this is just one of many ways of varying a programs behaviour by passing information to it
			//for example some programs have config files, which are usually simple text files with name=value settings on each line
			//programs with databases (like this one) can store their settings in the database
			//some programs even read properties stored by the operating system in system variables
	public static void main(String[] commandLineParameters) throws Exception 
	{		
		//Creates a *DatabaseInterface* object
			//when this object is created it will attempt to access the database file
			//it will fix it if there were any problems (e.g. because the webserver crashed while it was writing data)
			//if the database file does not exist it will create it
			//the database is a store of structured information that the webserver can use to 
			//record knowledge used by an application
			//it is good for many small pieces of data that will change frequently
			//this information can be used to dynamically create HTML pages to display information
			//when users interact with web pages (for example clicking on a link) 
				//the page can send information to the server which can then be used to 
				//change the information stored in the database
		String databaseFilepath = "database.mv";
		DatabaseInterface databaseInterface = new DatabaseInterface(databaseFilepath);
		
		//Creates a *FileStoreInterface* object
			//this object manages access to large files that the webserver will provide to a webbrowser
			//such files might include, images, videos or HTML pages that rarely change
			//if a folder for storing these files does not exist it will be created when this object is constructed
		String fileStoreFilepath = "httpdocs";
		FileStoreInterface fileStoreInterface = new FileStoreInterface(fileStoreFilepath);
		
		//Creates a *WebInterface* object
			//when this object is created it will attempt to connect to a network port on the computer
			//and will wait for requests to be made to that port
			//if the connection is successful the URL of the webserver will be printed out to the console
			//when this program is running you can connect to this webserver by typing the URL into a webbrowser on your machine
			//depending on the settings of your firewall you may be able to connect to the webserver from 
			//any other machine connected to the same network 
			//(e.g. other computers in the Lab or on your home network if you share the same wifi connection)
		//Port 80 is the default port for serving webpages
			//when you type "http://127.0.0.1/" as a URL into a webbrowser it will try to access port 80 on your local machine
			//sometimes this port is used by other applications
			//to deal with this you can run your webserver on a different port e.g. 8080
			//then you can access the webserver using "http://127.0.0.1:8080/"
		int port = 8080;
		WebInterface webInterface = new WebInterface(port);
		
		//This method attaches a shutdown function to the running java system
			//on most platforms the attached function will be called as the last piece of code to run before the application terminates
			//however it is not safe to rely on this as it is not guaranteed to be called, for example if the computer power fails
			//it obviously won't run as, without power, it will stop immediately
		registerShutdownHook(databaseInterface);
		
		//An example dynamic page
//		ExampleView exampleDynamicPage = new ExampleView(databaseInterface,fileStoreInterface);
		//An example dynamic page that responds to a form in html
		DiscoverView DiscoverViewPage = new DiscoverView(databaseInterface,fileStoreInterface);
		Searchview searchViewPage = new Searchview(databaseInterface,fileStoreInterface);
		Musicview musicViewPage = new Musicview(databaseInterface,fileStoreInterface);
		Signupview signupviewPage = new Signupview(databaseInterface,fileStoreInterface);
		MerchView MerchViewPage = new MerchView(databaseInterface, fileStoreInterface);
		Stats statsView = new Stats(databaseInterface, fileStoreInterface);

		//this variable indicates that the program should keep running
			//by setting this variable to false the program will exit
		boolean shouldKeepRunning = true;
		
		ArrayList<WebRequest> requestToProcess = new ArrayList<WebRequest>();
		
		//the program goes into an "update" loop repeatedly checking requests from web browsers, 
			//this loop consumes a small amount of resources, 
			//it is based on a model where a program may need to perform regular real time updates
			//such as reading from sensors or updating a simulation
			//this is also a common design for applications with responsive user interfaces
		while(shouldKeepRunning)
		{
			//check the time since the last update of the ip
			//potentially update the otidae redirection
			
			//This application forces a strict flow of control over requests to the webserver from webbrowsers
				//One request is handled at a time to prevent there being issues with two commands modifying the 
				//database at the same time
				//this approach makes it much easier to understand how a web application will run
				//however if the code for responding to a webbrowser request takes a long time it will delay all other
				//web requests and it may seem as if the server has crashed
				//in fact if the code for a page does get into an infinite loop or has a serious error it will crash the server
				//to make this approach work requires that all pages be tested to a high standard 
			webInterface.getQueue(requestToProcess);
			
			for (int i = 0; i < requestToProcess.size(); i++) 
			{
		        WebRequest toProcess = requestToProcess.remove(i);

		        //System.out.println("A web browser has requested the following address: http://"+toProcess.path);

		        //examines each request in turn
					//a set of if statements which identify how a web request should be handled 
					//the URL and parameters determine how to change the database or file store
					//they also determine what data should be sent back to the web browser
		        
		        //If no path was specified then set the path to index.html
		        //this will mean that a static index.html page will be loaded as default
		        //the example dynamic page will process the request if the index.html file is not present
	        	if(toProcess.path.length()==0)
	        		toProcess.path = "discover";

	        	//Uncomment this code to see an example dynamic page or an example dynamic page that responds to a form
//		        if(exampleDynamicPage.process(toProcess))
//		        {		        	//example page is processed
//		        }
		        //else
		        if(musicViewPage.process(toProcess)) {
			    } else if(DiscoverViewPage.process(toProcess)) {
			    } else if(searchViewPage.process(toProcess)) {
			    } else if(signupviewPage.process(toProcess)) {
				} else if(MerchViewPage.process(toProcess)) {
				}else if(statsView.process(toProcess)) {
				}
			    else{
			        String asFilepath = fileStoreInterface.decodeFilePath(toProcess.path);
			        if((asFilepath!=null)&&fileStoreInterface.exists(asFilepath))
			        {
			        	toProcess.r = WebResponse.serveFile(toProcess.params, asFilepath);
			        }
		        }
		        

		        //If none of the previous rules identified a response then either the 
		        //website is broken pointing to a url that no longer exists
		        //or someone is deliberately trying to access the site in a way that wasn't designed for
		        //either way respond with a redirection message to the index page
		        if(toProcess.r == null)
		        {
		        	if(toProcess.path.equalsIgnoreCase("index.html"))
		        	{
			        	toProcess.r = new WebResponse( WebResponse.HTTP_NOTFOUND, WebResponse.MIME_PLAINTEXT,
								 "Error 404, file not found." );
		        	}
		        	else
		        	{
			        	String url = "/";
						toProcess.r = new WebResponse( WebResponse.HTTP_REDIRECT, WebResponse.MIME_HTML,
												   "<html><body>Redirected: <a href=\"" + url + "\">" +
												   url + "</a></body></html>");
						toProcess.r.addHeader( "Location", url );
		        	}
		        }

				//for whenever you don't want the page to refresh
				else if(toProcess.r.mimeType.equals(WebResponse.HTTP_NOCONTENT)) continue;

		        //create a new thread that will send the response to the webbrowser
				Thread t = new Thread( toProcess );
				//daemon means that even if the program is closed this thread will continue until
				//it is finished
				t.setDaemon( true );
				//this starts the thread running
				t.start();		        
			}			
			
			//This command pauses the update loop for 10 milliseconds
				//the goal is to have the total time to calculate each loop be under 16.7 milliseconds
				//if this performance goal is reached then the server will be able to respond to
				//changes faster than the eye can detect changes 
				//if a graphical user interface were added to this application 
				//it would appear to be responding instantaneously
			Thread.sleep(10);
		}
		
		//Cleanly shuts down the application
			//it disconnects from the database and the file store, ensuring that information is saved
			//to disk as required
	}
	
	//NOTE
	//It is useful to have a simple main class for your application that can be easily modified
		//This ensures that you can quickly produce demos or add one-off functionality
		//some libraries take control of the main function and require that you
		//supply functions that modify parts of how they work, 
		//these are typically referred to as "opinionated" libraries
		//"opinionated" libraries try to make development simple and fast by hiding details from the user
		//they also restrict how you can modify them to reduce errors and improve performance
		//the disadvantage of using "opinionated" libraries is that you typically become 
		//locked-in to the libraries. By using them you develop skills with using a library 
		//but not necessarily with core programming skills. To truly master programming you want
		//to be able to get a computer to do anything you want and to understand any part of how it
		//works. To develop this skill it is a good idea to get practice working with "unopinionated" libraries
		//and to understand the software you use to such an extent you are happy to modify any part of it

    public static void registerShutdownHook( final DatabaseInterface s )
    {
        // Registers a shutdown hook for the database so that it
        // shuts down nicely when the Virtual Machine exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                s.close();
            }
        } );
    }

}