package storage;

import org.h2.mvstore.MVStore;


public class DatabaseInterface 
{
	//The MVStore is what is known as a NoSQL database it is the core of a much larger database 
		//program called H2 which supports a wide range of advanced features
		//MVStore provides a fast and reliable way of storing and manipulating information on a hard disk
		//This example program uses the MVStore class directly to introduce the concept of NoSQL databases
		//and to reduce the complexity of having to learn about all the features of SQL databases
		//Databases will be covered in more detail in CSC1018, 
		//NoSQL databases are often used in professional web app development, for example, MongoDB is used 
		//by major companies for critical projects "https://www.mongodb.com/who-uses-mongodb"
	//MVStores contain MVMaps
	//An MVMap stores a collection of objects (known as values)
		//these objects are accessed using a key
		//the MVMap can be defined to have any type of class as a key and any type as a value
		//provided objects of each class can be converted into an array of bytes
		//Java can convert classes into bytes provided they are defined using "implements Serializable"
	//For example MVMap<String,Int> is a map from a String value and a number
		//both String and Int are defined by that Java standard to be Serializable 
		//if you want to know more about how Serialization works and things that might cause you problems
		//check out "http://web.deu.edu.tr/doc/oreily/java/javanut/ch09_01.htm"
	//One way to use the MVMap is to create your own classes that represent objects in your application
		//in the same way you would for any object oriented program
		//MVMap then gives you a way to store this information to disk
		//they also enable you to store manipulate many more objects than you could fit into memory
		//However each time you change the definition of an object you need to work out how to 
		//transfer any old stored information to the new database 
		//We will briefly be covering this on the course however if you want to know more
		//StackOverflow is your friend "http://stackoverflow.com/questions/3678136/managing-several-versions-of-serialized-java-objects"
	public MVStore s;

	//Open a database file, if the file does not exist then create it
	public DatabaseInterface(String path)
	{
		s = MVStore.open(path);
	}

	//To ensure that all of the data has been saved correctly
	//call this close function before exiting
	public void close()
	{
        s.close();		
	}

	//After making changes to the database
		//call this to save all the values to disk
		//this commit call is used because the computer might crash or lose power while it is being
		//modified and only some of the entries might have been updated
	//for example
		//when updating the marriage status of two people in a social network application
		//if each person was represented by a single class with a "partner" variable storing the name of their partner
		//the computer might crash after only one of the two married people had had their partner values set
		//this would put the database in an inconsistent state which might cause other
		//later processing to fail
		//by calling commit after both entries have been changed the database
		//will either store the complete change or no change
	public void commit()
	{
		s.commit();
	}
	//
	
}
