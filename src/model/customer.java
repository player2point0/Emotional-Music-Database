package model;
import java.io.Serializable;
import java.util.ArrayList;
public class customer implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public String name;
	public String email;//unique key
	public String password;
	public String songHistoryId;
	public ArrayList<String> friendsList;

	public void addFriend(String id) {
		friendsList.add(id);
	}
}

