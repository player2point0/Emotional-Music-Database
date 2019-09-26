package views;

import java.util.ArrayList;
import java.util.List;
import model.SongInfo;
import org.h2.mvstore.MVMap;

import org.json.JSONException;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

//youtube api
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Searchview extends DynamicWebPage
{
	private final int RESULT_NUM = 200;
	private ArrayList<String> videoIds = new ArrayList<>();

	public Searchview(DatabaseInterface db, FileStoreInterface fs) {
		super(db, fs);
		// TODO Auto-generated constructor stub
	}
	
	public boolean process(WebRequest toProcess)
	{
		//100 commits
		if(toProcess.path.toLowerCase().startsWith("search"))
		{
			videoIds.clear();
			String searchResults = "";
			//search based on emotions
			searchResults = searchByEmotions(toProcess);
			if(searchResults.isEmpty()) {
				//search database for song
				searchResults = getSavedSearchResults(toProcess);
				//if doesn't exist create it
				if (searchResults.isEmpty())
				{
					searchResults = getYoutubeResults(toProcess, RESULT_NUM);

					if(searchResults.isEmpty())
					{
						//no songs in database or on youtube
						//show error page
						searchResults = "<h1>nothing found</h1>";
					}
				}
			}
			String stringToSendToWebBrowser = "<!DOCTYPE html>\n" +
					"<html lang=\"en\">\n" +
					"  <head>\n" +
					"    <meta charset=\"utf-8\">\n" +
					"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
					"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
					"    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
					"    <title>Search</title>\n" +

					"\t\t\n" +
					"\t\t<link href=\"https://afeld.github.io/emoji-css/emoji.css\" rel=\"stylesheet\">\n" +
					DiscoverView.getNavBarCss() +
					"\t\t<link href=\"css/search.css\" rel=\"stylesheet\" type=\"text/css\">\t\t\n" +
					"\t\t<script src=\"js/deleteSearchResult.js\"></script>\n"+
					"  </head>\n" +
					"  <body onLoad=\"setAutoPlayVideoIds(); setCookie('searchQuery', window.location.href, 1);\">\n" +
					DiscoverView.getNavBar() +
					searchResults+
					"\t\t\n" +
					"<script> function setAutoPlayVideoIds()\n" +
					"{\n" +
					"\tvar arr = "+arrayListToString(videoIds)+";\n" +
					"\t\n" +
					"\tlocalStorage.removeItem(\"autoPlayVideoIds\");\n"+
					"\tlocalStorage.setItem(\"autoPlayVideoIds\", JSON.stringify(arr));\n" +
					"} " +
					"function setCookie(cname, cvalue, exdays) {\n" +
					"    var d = new Date();\n" +
					"    d.setTime(d.getTime() + (exdays*24*60*60*1000));\n" +
					"    var expires = \"expires=\"+ d.toUTCString();\n" +
					"    document.cookie = cname + \"=\" + cvalue + \";\" + expires + \";path=/\";\n" +
					"}" +
					"</script>"+
					"  </body>\n" +
					"</html>\n";
			toProcess.r = new WebResponse( WebResponse.HTTP_OK,
			WebResponse.MIME_HTML, stringToSendToWebBrowser );
			return true;
		}

		else if(toProcess.path.toLowerCase().startsWith("removesong"))
		{
			String songId = toProcess.params.get("id");
			int votesNeeded = -1;

			boolean deletedSong = deleteSong(songId, toProcess);
			if(!deletedSong) votesNeeded = deleteVotesNeeded(songId);

			JSONObject data = new JSONObject();
			data.put("deleted", deletedSong);
			data.put("votesNeeded", votesNeeded);

			toProcess.r = new WebResponse( WebResponse.HTTP_OK,
					WebResponse.MIME_HTML, data.toString() );
			return true;
		}

		return false;
	}

	private int deleteVotesNeeded(String songId)
	{
		MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");

		if(SongRatings.containsKey((songId)))
		{
			SongInfo tempSong = SongRatings.get(songId);
			return tempSong.getVotesNeeded();
		}
		return -1;
	}

	private boolean deleteSong(String songId, WebRequest toProcess)
	{
		MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");
		String clientIp = toProcess.getClientIpAddress();

		if(SongRatings.containsKey((songId)))
		{
			SongInfo tempSong = SongRatings.get(songId);
			boolean votedToDelete = tempSong.addDeleteVote(clientIp);

			if(votedToDelete)
			{
				SongRatings.remove(songId);
				return true;
			}
			SongRatings.replace(songId, tempSong);
			db.commit();
		}
		return false;
	}

	private String arrayListToString(ArrayList<String> arr)
	{
		String outputString = "[";

		for(int i = 0;i<arr.size();i++)
		{
			outputString += "\""+arr.get(i)+"\"";

			if(i+1 != arr.size()) outputString += ", ";
		}

		outputString += "]";

		return outputString;
	}

	private String sortSongsByRelevance(SongInfo[] songs, double[] relevanceValues, int maxResultNum)
	{
		int numOfSongs = songs.length;
		String outputString = "";

		//get the n largest values
		for(int i = 0;i<numOfSongs;i++)
		{
			//swap the largest value to i
			int j = i + 1;

			if(j < numOfSongs)

				for(;j<numOfSongs;j++)
				{
					if(relevanceValues[j] > relevanceValues[i])
					{
						//swap
						double tempVal = relevanceValues[i];
						relevanceValues[i] = relevanceValues[j];
						relevanceValues[j] = tempVal;

						SongInfo tempSong = songs[i];
						songs[i] = songs[j];
						songs[j] = tempSong;
					}
				}

			if (i > maxResultNum) break;
		}

		//Add search results to output string
		for(int i = 0;i<numOfSongs;i++)
		{
			if (songs[i] == null) continue;

			outputString +=addSearchResult(songs[i]);

			if (i > maxResultNum) break;
		}

		return outputString;
	}

	public String searchByEmotions(WebRequest toProcess)
	{
		//limit number of search results
		String outputString = "";
		//get search query
		String happy = toProcess.params.get("happy");
		String angry = toProcess.params.get("angry");
		String excited = toProcess.params.get("excited");
		String sad = toProcess.params.get("sad");
		String relaxed = toProcess.params.get("relaxed");

		if(happy == null || angry == null || excited == null || sad == null || relaxed == null) return outputString;

		//create a temp song with the search paramaters
		SongInfo searchSong = new SongInfo();
		searchSong.happy = Integer.parseInt(happy);
		searchSong.angry = Integer.parseInt(angry);
		searchSong.excited = Integer.parseInt(excited);
		searchSong.sad = Integer.parseInt(sad);
		searchSong.relaxed = Integer.parseInt(relaxed);

		//open database
		MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");
		List<String> songKeys = SongRatings.keyList();
		int numOfSongs = songKeys.size();
		//store the corresponding relevance values
		double[] relevanceValues = new double[numOfSongs];
		SongInfo[] songs = new SongInfo[numOfSongs];

		//compare the temp song with each song in the database and assign it a relevance value
		for(int i = 0;i<numOfSongs;i++)
		{
			String Id = songKeys.get(i);
			SongInfo tempResult = SongRatings.get(Id);

			relevanceValues[i] = searchSong.compareEmotions(tempResult);
			songs[i] = tempResult;
		}

		//sort the relevanceValues
		outputString = sortSongsByRelevance(songs, relevanceValues, RESULT_NUM);

		return outputString;
	}

	public String getSavedSearchResults(WebRequest toProcess)
	{
		//open database
		MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");
		List<String> songKeys = SongRatings.keyList();
		int numOfSongs = songKeys.size();
		//store the corresponding relevance values
		double[] relevanceValues = new double[numOfSongs];
		SongInfo[] songs = new SongInfo[numOfSongs];

		//get search query
		String searchQuery = toProcess.params.get("searchQuery");
		if(searchQuery == null) searchQuery = "devtest";

		searchQuery =  searchQuery.toLowerCase();
		String[] searchWords = searchQuery.split(" ");
		String outputString = "";

		if(numOfSongs == 0) return "";//no songs so search youtube instead

		int minMatchPercentage = 75;

		for(int i = 0;i<songKeys.size();i++)
		{
			String Id = songKeys.get(i);
			SongInfo tempResult = SongRatings.get(Id);

			double percentage = tempResult.wordPercenatge(searchWords);

			if(percentage >= minMatchPercentage)
			{
				relevanceValues[i] = percentage;
				songs[i] = tempResult;
			}
		}

		outputString = sortSongsByRelevance(songs, relevanceValues, RESULT_NUM);

		return outputString;
	}
	
	public String addSearchResult(SongInfo data)
	{
		videoIds.add(data.youtubeVideoId);
		//defaults to zero
		double happyOpacity = data.happy / 100.0;
		double angryOpacity = data.angry / 100.0;
		double excitedOpacity = data.excited / 100.0;
		double sadOpacity = data.sad / 100.0;
		double relaxedOpacity = data.relaxed / 100.0;

		//set opacity based on emotion value
		return
			"<div id=\""+data.youtubeVideoId+"\" class=\"search-result\">\n" +
				//"<a href=\"removesong?id="+data.youtubeVideoId+"\"  title=\"remove song\" class=\"search-result-delete\" ><i class=\"fas fa-times-circle\"></i></a>"+
				"\t\t\t\t\t<button class=\"search-result-delete\" onclick=\"deleteSong(\'"+data.youtubeVideoId+"\');\"><i class=\"far fa-times-circle\"></i></button>\n"+
				"<a href=\"music?id="+data.youtubeVideoId+"\">\r\n" +
				"\t\t\t\t<img src=\""+data.thumbnailImg+"\">\n" +
				"\t\t\t\t<div class=\"search-result-title\">\n" +
				"\t\t\t\t\t<h2>"+data.songName+"</h2>\n" +
				"\t\t\t\t</div>\n" +
				"\t\t\t\t<div id=\"search-result-emotions\">\n" +
				"\t\t\t\t\t\t<i style=\"opacity:"+happyOpacity+"\" class=\"em-svg em-grin\"></i>\n" +
				"\t\t\t\t\t\t<i style=\"opacity:"+angryOpacity+"\" class=\"em-svg em-angry\" ></i>\n" +
				"\t\t\t\t\t\t<i style=\"opacity:"+excitedOpacity+"\" class=\"em-svg em-heart_eyes\"></i>\n" +
				"\t\t\t\t\t\t<i style=\"opacity:"+sadOpacity+"\" class=\"em-svg em-cry\"></i>\n" +
				"\t\t\t\t\t\t<i style=\"opacity: "+relaxedOpacity+"\" class=\"em-svg em-sleeping\"></i>\n" +
				"\t\t\t\t</div>\n" +
				"</a>"+
			"\t\t\t</div>";
	}

	public String getYoutubeResults(WebRequest toProcess, int resultNum)
	{
		String outputString = "";

		//hard cap of 50
		if(resultNum > 50) resultNum = 50;

		resultNum = 4;//hacky way to fix the non music problem

		//search youtube for song
		String searchQuery = toProcess.params.get("searchQuery");
		if(searchQuery != null)searchQuery = searchQuery.toLowerCase();
		else searchQuery = "r u mine";
		JSONObject searchResult = searchYoutube(searchQuery,resultNum);

		//add better error handling
		if(searchResult == null) return "";

		//create and display the top n results
		for(int i = 0;i<resultNum;i++)
		{
			String result = addNewSong(searchResult, i);

			if(result.isEmpty()) continue;

			outputString += result;
		}

		return outputString;
	}

	public String addNewSong(JSONObject searchResult, int index)
	{
		String videoId = getVideoId(searchResult, index);
		String title = getTitle(searchResult, index);
		String thumbnail = getThumbnail(searchResult, index);

		//prevents null errors
		if(videoId.isEmpty() || title.isEmpty() || thumbnail.isEmpty()) return "";

		//create new song and add to database
		MVMap<String, SongInfo> SongRatings = db.s.openMap("SongRatings");

		//prevents duplicates
		if(SongRatings.containsKey(videoId)) return "";

		SongInfo newSong = new SongInfo();
		//assign data
		newSong.songName = SongInfo.simplifyTitle(title);
		newSong.thumbnailImg = thumbnail;
		newSong.youtubeVideoId = videoId;

		SongRatings.put(newSong.youtubeVideoId, newSong);

		db.commit();

		return addSearchResult(newSong);
	}

	public JSONObject searchYoutube(String query, int resultNum)
	{
		//searching r breaks it

		query = query.replace(" ", "+");
		String url = "https://www.googleapis.com/youtube/v3/search?part=snippet";
		url += "&maxResults="+resultNum;
		url += "&type=video";
		url += "&vvideoEmbeddable=true";
		url += "&videoSyndicated=true";//play outside youtube
		//url += "&channelId=UC-9-kyTW8ZkZNDHQJ6FgpwQ";//music channel
		url += "&q="+query;
		url += "&key=AIzaSyAWZnZXt1NcQq_FXSfHj42ZU_NSoA0YCeM";

		System.out.println(url);

		try
		{
			Document doc = Jsoup.connect(url).timeout(10 * 1000).ignoreContentType(true).get();

			String getJson = doc.text();

			String cleanedJson = "";
			//having quotes in the response json breaks it
			//go through each attribute in the object
			String[] attributes = getJson.split(",");
			//for the value
			for (int i = 0;i<attributes.length;i++)
			{
				String[] lineParts = attributes[i].split(":");

				if(lineParts.length != 2)
				{
					cleanedJson += attributes[i]+",";
					continue;
				}

				String leftSide = lineParts[0];
				String rightSide = lineParts[1];

				if(leftSide.contains("title"))
				{
					StringBuilder value = new StringBuilder(rightSide);
					//start at the first quote and continue till the quote before the comma
					//reomve any quotes inbetween
					for(int j = 2;j<value.length()-1;j++)
					{
						if (value.charAt(j) == '"')
						{
							value.deleteCharAt(j);
							j--;
						}
					}
					cleanedJson += leftSide+":"+value.toString()+",";
				}

				else cleanedJson += attributes[i]+",";
			}


			JSONTokener jsonTokener = new JSONTokener(cleanedJson);
			JSONObject jsonObject = new JSONObject(jsonTokener);//(JSONObject) jsonTokener.nextValue();

			return jsonObject;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getVideoId(JSONObject jsonObject, int index)
	{
		if(jsonObject.has("items"))
		{
			JSONArray arr = jsonObject.getJSONArray("items");

			if(arr.length() > 0)
			{
				if(index >= arr.length()) return "";

				JSONObject firstElement = (JSONObject) arr.get(index);

				if(firstElement.has("id"))
				{
					JSONObject id = firstElement.getJSONObject("id");

					if(id.has("videoId"))
					{
						String videoId = id.getString("videoId");

						return videoId;
					}
				}
			}
		}

		return "";
	}
	public String getTitle(JSONObject jsonObject, int index)
	{
		if(jsonObject.has("items"))
		{
			JSONArray arr = jsonObject.getJSONArray("items");

			if(arr.length() > 0)
			{
				if(index >= arr.length()) return "";

				JSONObject firstElement = (JSONObject) arr.get(index);

				if(firstElement.has("snippet"))
				{
					JSONObject snippet = firstElement.getJSONObject("snippet");

					if(snippet.has("title"))
					{
						String title = snippet.getString("title");

						return title;
					}
				}
			}
		}

		return "";
	}
	public String getThumbnail(JSONObject jsonObject, int index)
	{
		if(jsonObject.has("items"))
		{
			JSONArray arr = jsonObject.getJSONArray("items");

			if(arr.length() > 0)
			{
				if(index >= arr.length()) return "";

				JSONObject firstElement = (JSONObject) arr.get(index);

				if(firstElement.has("snippet"))
				{
					JSONObject snippet = firstElement.getJSONObject("snippet");

					if(snippet.has("thumbnails"))
					{
						JSONObject thumbnails = snippet.getJSONObject("thumbnails");

						if(thumbnails.has("high"))
						{
							JSONObject high = thumbnails.getJSONObject("high");

							if(high.has("url"))
							{
								String url = high.getString("url");

								return url;
							}
						}
					}
				}
			}
		}

		return "";
	}
}
