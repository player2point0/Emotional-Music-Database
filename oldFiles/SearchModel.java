package model;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchModel implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	public String searchQuery;
	public String uniqueId;
	public ArrayList<String> searchResults = new ArrayList<String>();
	
}
